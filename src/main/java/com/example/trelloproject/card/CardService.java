package com.example.trelloproject.card;

import com.example.trelloproject.card.dto.CardRequestDto;
import com.example.trelloproject.card.dto.CardResponseDto;
import com.example.trelloproject.card.dto.CardSearchRequestDto;
import com.example.trelloproject.card.dto.GetCardResponseDto;
import com.example.trelloproject.card.enums.ActionType;
import com.example.trelloproject.global.exception.BusinessException;
import com.example.trelloproject.global.exception.ExceptionType;
import com.example.trelloproject.member.Member;
import com.example.trelloproject.member.MemberRepository;
import com.example.trelloproject.member.MemberRole;
import com.example.trelloproject.member.MemberService;
import com.example.trelloproject.notice.NoticeChannel;
import com.example.trelloproject.notice.NoticeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;


@Service
public class CardService {

    private final NoticeService noticeService;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CardFileService cardFileService;
    @Autowired
    private CardHistoryRepository cardHistoryRepository;
    @Autowired
    private MemberService memberService;

    @Autowired
    public CardService(MemberService memberService, CardRepository cardRepository, NoticeService noticeService) {
        this.memberService = memberService;
        this.cardRepository = cardRepository;
        this.noticeService = noticeService;
    }


    @Transactional
    public CardResponseDto createCard(CardRequestDto requestDto, MultipartFile file, long userId, Long workspaceId, Long listId, String userEmail) throws AccessDeniedException {
        if (!memberService.hasCardEditPermission(userId, workspaceId)) {
            throw new BusinessException(ExceptionType.UNAUTHORIZED);
        }

        Card card = new Card(
                requestDto.getTitle(),
                requestDto.getDescription(),
                requestDto.getDueDate(),
                requestDto.getMember(),
                //member, 나중에 변경
                requestDto.getList(),
                null //나중에 설정
        );

        Card saveCard = cardRepository.save(card);

        if (file != null && !file.isEmpty()) {
            try {
                CardFile cardFile = cardFileService.uploadFile(file, saveCard);
                saveCard = saveCard.withCardFile(cardFile);
                saveCard = cardRepository.save(saveCard);
            } catch (IOException e) {
                throw new RuntimeException("파일 저장 중 오류가 발생했습니다.", e);
            }
        }

        noticeService.sendSlackBotMessage(requestDto.getTitle(), workspaceId, NoticeChannel.CARD, userEmail);

        return new CardResponseDto(saveCard);
    }

    private String saveFile(MultipartFile file) throws IOException {
        // 파일 저장 로직 구현
        // 파일 이름 생성, 저장 경로 설정 등
        // 저장된 파일의 이름 또는 경로를 반환

        return "saveFile";
    }


    @Transactional(readOnly = true)
    public GetCardResponseDto getCard(Long cardId, Long listId, Long userId)throws AccessDeniedException {
        Card card = cardRepository.findByListIdAndId(listId, cardId)
                .orElseThrow(() -> new EntityNotFoundException("카드가 존재하지 않습니다."));

        Member member = memberRepository.findByUserIdAndWorkspaceId(userId, card.getList().getBoard().getWorkspace().getId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        if (!hasReadPermission(member)) {
            throw new AccessDeniedException("카드 조회 권한이 없습니다.");
        }

        return new GetCardResponseDto(card);
    }

    private boolean hasReadPermission(Member member) {
        return member.getRole() != MemberRole.READONLY;
    }




    @Transactional(readOnly = true)
    public Page<GetCardResponseDto> getCardsByListId(Long listId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Card> cardPage = cardRepository.findAllByListId(listId, pageable);
        return cardPage.map(GetCardResponseDto::new);
    }


    @Transactional
    public GetCardResponseDto updateCard(Long cardId, Long listId, MultipartFile file, CardRequestDto requestDto, Long userId, Long workspaceId, String userEmail) throws AccessDeniedException {

        if (!memberService.hasCardEditPermission(userId, workspaceId)) {
            throw new BusinessException(ExceptionType.UNAUTHORIZED);
        }

        Card card = cardRepository.findByListIdAndId(listId, cardId)
                .orElseThrow(() -> new EntityNotFoundException("카드를 찾을 수 없습니다."));

        // 기존 파일이 있고 새 파일이 들어오면 기존 파일 삭제
        if (card.getCardFile() != null && requestDto.getCardFile() != null) {
            cardFileService.deleteCardFile(card.getCardFile().getId());
        }

        // 새 파일이 있으면 업로드
        CardFile newFile = null;
        if (requestDto.getCardFile() != null && !requestDto.getCardFile().isEmpty()) {
            try {
                newFile = cardFileService.uploadFile(requestDto.getCardFile(), card);
            } catch (IOException e) {
                throw new BusinessException(ExceptionType.FILE_UPLOAD_ERROR);
            }
        }
        // 카드 업데이트
        card.update(
                requestDto.getTitle(),
                requestDto.getDescription(),
                requestDto.getDueDate()
        );

        // 카드 변경 이력 저장
        CardHistory history = new CardHistory(
                card,
                card.getTitle(),
                card.getDescription(),
                card.getDueDate(),
                requestDto.getMember(),
                ActionType.UPDATE
        );
        cardHistoryRepository.save(history);

        noticeService.sendSlackBotMessage("카드 내용 변경!", workspaceId, NoticeChannel.CARD_CHANGE, userEmail);

        return new GetCardResponseDto(card);
    }


    public Boolean deleteCard(Long cardId, Long userId, Long workspaceId) throws AccessDeniedException {

        if (!memberService.hasCardEditPermission(userId, workspaceId)) {
            throw new BusinessException(ExceptionType.UNAUTHORIZED);
        }

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));

        cardRepository.delete(card);

        return true;
    }

    @Transactional(readOnly = true)
    public Page<CardResponseDto> searchCards(CardSearchRequestDto searchDto, Pageable pageable) {
        Specification<Card> spec = Specification.where(null);

        if (searchDto.getKeyword() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(root.get("title"), "%" + searchDto.getKeyword() + "%"),
                            cb.like(root.get("description"), "%" + searchDto.getKeyword() + "%")
                    )
            );
        }

        if (searchDto.getBoardId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("list").get("board").get("id"), searchDto.getBoardId())
            );
        }

        if (searchDto.getListId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("list").get("id"), searchDto.getListId())
            );
        }

        if (searchDto.getStartDate() != null && searchDto.getEndDate() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.between(root.get("dueDate"), searchDto.getStartDate(), searchDto.getEndDate())
            );
        }

        Page<Card> cards = cardRepository.findAll(spec, pageable);
        return cards.map(CardResponseDto::new);
    }

}
