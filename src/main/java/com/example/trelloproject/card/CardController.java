package com.example.trelloproject.card;

import com.example.trelloproject.card.dto.CardRequestDto;
import com.example.trelloproject.card.dto.CardResponseDto;
import com.example.trelloproject.card.dto.CardSearchRequestDto;
import com.example.trelloproject.card.dto.GetCardResponseDto;
import com.example.trelloproject.user.User;
import com.example.trelloproject.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/lists/{listId}/cards")
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private UserRepository userRepository;

    /**
     * 카드 생성
     *
     * @param requestDto 카드 생성에 필요한 데이터 (제목, 설명, 마감일, 담당자 정보)
     * @param file       첨부할 파일 (선택사항, 최대 5MB, 지원형식: jpg, png, pdf, csv)
     * @return 생성된 카드의 정보
     */

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CardResponseDto> createCard(@RequestPart("cardData") CardRequestDto requestDto,
                                                      @RequestPart(value = "file", required = false) MultipartFile file,
                                                      @PathVariable Long listId,
                                                      @RequestAttribute Long userId,
                                                      @RequestAttribute Long workspaceId) throws AccessDeniedException {

        CardResponseDto response = cardService.createCard(requestDto, file, userId, listId, workspaceId);
        return ResponseEntity.ok(response);
    }

    /**
     * 카드 조회(단건)
     *
     * @param cardId
     * @param listId
     * @return
     */
    @GetMapping("/{cardId}")
    public ResponseEntity<GetCardResponseDto> getCard(@PathVariable Long cardId,
                                                      @PathVariable Long listId) {
        // SecurityContext에서 현재 인증된 사용자의 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        try {
            GetCardResponseDto card = cardService.getCard(cardId, listId, user.getId());
            return ResponseEntity.ok(card);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    /**
     * 리스트Id에 따른 카드 전체 조회
     *
     * @param listId
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<GetCardResponseDto>> getListAllCard(@PathVariable Long listId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(defaultValue = "id") String sortBy) {

        Page<GetCardResponseDto> cards = cardService.getCardsByListId(listId, page, size, sortBy);

        return ResponseEntity.ok(cards);
    }

    /**
     * 카드 수정
     *
     * @param cardId
     * @param listId
     * @param requestDto
     * @return
     */
    @PutMapping("/{cardId}")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long cardId,
                                                      @RequestBody CardRequestDto requestDto,
                                                      @RequestAttribute Long userId,
                                                      @RequestAttribute Long listId,
                                                      @RequestAttribute Long workspaceId) throws AccessDeniedException {
        CardResponseDto response = cardService.updateCard(cardId, listId, requestDto, userId, workspaceId);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable Long cardId,
                                        @RequestBody CardRequestDto requestDto,
                                        @RequestAttribute Long userId,
                                        @RequestAttribute Long workspaceId) {
        try {
            cardService.deleteCard(cardId, requestDto, userId, workspaceId);
            return ResponseEntity.ok("카드가 성공적으로 삭제되었습니다.");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    /**
     * 카드 검색 API
     *
     * @param searchDto 검색 조건
     * @param pageable  페이징 정보
     * @return 검색된 카드 목록
     */
    @GetMapping("/search")
    public ResponseEntity<Page<CardResponseDto>> searchCards(
            @ModelAttribute CardSearchRequestDto searchDto,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(cardService.searchCards(searchDto, pageable));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("접근이 거부되었습니다.");
    }

}

