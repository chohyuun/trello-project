package com.example.trelloproject.card;

import com.example.trelloproject.card.dto.CardRequestDto;
import com.example.trelloproject.card.dto.CardResponseDto;
import com.example.trelloproject.card.dto.CardSearchRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/lists/{listId}/cards")


public class CardController {
    @Autowired
    private CardService cardService;

    /**
     * 카드 생성
     *
     * @param requestDto 카드 생성에 필요한 데이터 (제목, 설명, 마감일, 담당자 정보)
     * @param file 첨부할 파일 (선택사항, 최대 5MB, 지원형식: jpg, png, pdf, csv)
     * @return 생성된 카드의 정보
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CardResponseDto> createCard(
            @RequestPart("cardData") CardRequestDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(cardService.createCard(requestDto, file));
    }

    /**
     * 카드 조회(단건)
     *
     * @param cardId
     * @param listId
     * @return
     */
    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponseDto> getcard(@PathVariable Long cardId, @PathVariable Long listId) {
        return ResponseEntity.ok(cardService.getCard(cardId, listId));
    }

    /**
     * 리스트Id에 따른 카드 전체 조회
     *
     * @param listId
     * @return
     */
    @GetMapping
    public ResponseEntity<Page<CardResponseDto>> getListAllCard(@PathVariable Long listId,
                                                                @RequestParam(defaultValue = "0") int page,
                                                                @RequestParam(defaultValue = "10") int size,
                                                                @RequestParam(defaultValue = "id") String sortBy) {

        Page<CardResponseDto> cards = cardService.getCardsByListId(listId, page, size, sortBy);

        return ResponseEntity.ok(cards);
    }

    /**
     * 카드 수정
     * @param cardId
     * @param listId
     * @param requestDto
     * @return
     */
    @PutMapping("/{cardId}")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long cardId, @PathVariable Long listId, @RequestBody CardRequestDto requestDto) {

        return ResponseEntity.ok(cardService.updateCard(cardId, listId, requestDto));
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<String> deleteCard(@PathVariable Long cardId){
        boolean isDeleted = cardService.deleteCard(cardId);
        if(isDeleted){
            return ResponseEntity.ok("성공적으로 삭제 되었습니다.");
        }else {
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * 카드 검색 API
     *
     * @param searchDto 검색 조건
     * @param pageable 페이징 정보
     * @return 검색된 카드 목록
     */
    @GetMapping("/search")
    public ResponseEntity<Page<CardResponseDto>> searchCards(
            @ModelAttribute CardSearchRequestDto searchDto,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(cardService.searchCards(searchDto, pageable));
    }

}

