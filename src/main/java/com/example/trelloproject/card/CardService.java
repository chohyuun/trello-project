package com.example.trelloproject.card;

import com.example.trelloproject.card.dto.CardRequestDto;
import com.example.trelloproject.card.dto.CardResponseDto;
import com.example.trelloproject.card.dto.CardSearchRequestDto;
import com.example.trelloproject.global.exception.BusinessException;
import com.example.trelloproject.global.exception.ExceptionType;
import com.example.trelloproject.member.Member;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;
    //    @Autowired
//    private MemberRepository  memberRepository;
    @Autowired
    private CardFileService cardFileService;

    @Transactional
    public CardResponseDto createCard(CardRequestDto requestDto, MultipartFile file) {
        //맴버 조회
//        Member member = memberRepository.findById(requestDto.getMemberId())
//                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다."));
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

        return new CardResponseDto(saveCard);
    }

    private String saveFile(MultipartFile file) throws IOException {
        // 파일 저장 로직 구현
        // 파일 이름 생성, 저장 경로 설정 등
        // 저장된 파일의 이름 또는 경로를 반환

        return "saveFile";
    }


    @Transactional(readOnly = true)
    public CardResponseDto getCard(Long cardId, Long listId) {
        Card card = cardRepository.findByListIdAndId(cardId, listId).orElseThrow(() -> new EntityNotFoundException("카드가 존재하지 않습니다."));
        return new CardResponseDto(card);
    }

//    @Transactional(readOnly = true)
//    public java.util.List<CardResponseDto> getListAllCard(Long listId) {
//        List<Card> cards = cardRepository.findAllByListId(listId);
//        return cards.stream()
//                .map(CardResponseDto::new)
//                .collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public Page<CardResponseDto> getCardsByListId(Long listId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Card> cardPage = cardRepository.findAllByListId(listId, pageable);
        return cardPage.map(CardResponseDto::new);
    }

    @Transactional(readOnly = true)
    public Page<CardResponseDto> searchCards(CardSearchRequestDto searchDto, Pageable pageable) {
        Page<Card> cards;

        // 마감일 범위 검색
        if (searchDto.getStartDate() != null && searchDto.getEndDate() != null) {
            cards = cardRepository.findByDueDateBetween(
                    searchDto.getStartDate(),
                    searchDto.getEndDate(),
                    pageable
            );
        }
        // 보드 ID로 검색
        else if (searchDto.getBoardId() != null) {
            if (searchDto.getKeyword() != null && !searchDto.getKeyword().isEmpty()) {
                cards = cardRepository.findByList_Board_IdAndTitleContainingOrDescriptionContaining(
                        searchDto.getBoardId(),
                        searchDto.getKeyword(),
                        searchDto.getKeyword(),
                        pageable
                );
            } else {
                cards = cardRepository.findByList_Board_Id(searchDto.getBoardId(), pageable);
            }
        }
        // 리스트 ID로 검색
        else if (searchDto.getListId() != null) {
            cards = cardRepository.findByListIdAndTitleContainingOrDescriptionContainingOrMember_User_UserNameContaining(
                    searchDto.getListId(),
                    searchDto.getKeyword(),
                    searchDto.getKeyword(),
                    searchDto.getKeyword(),
                    pageable
            );
        }
        // 전체 검색
        else {
            cards = cardRepository.findByTitleContainingOrDescriptionContainingOrMember_User_UserNameContaining(
                    searchDto.getKeyword(),
                    searchDto.getKeyword(),
                    searchDto.getKeyword(),
                    pageable
            );
        }

        return cards.map(CardResponseDto::new);
    }


    @Transactional
    public CardResponseDto updateCard(Long cardId, Long listId, CardRequestDto requestDto) {
        Card card = cardRepository.findByListIdAndId(cardId, listId)
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

        card.update(
                requestDto.getTitle(),
                requestDto.getDescription(),
                requestDto.getDueDate()
        );

        return new CardResponseDto(card);
    }


    public Boolean deleteCard(Long cardId) {
        if (cardRepository.existsById(cardId)) {
            cardRepository.deleteById(cardId);
            return true;
        } else {
            return false;
        }
    }
}