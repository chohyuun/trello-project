package com.example.trelloproject.card;

import com.example.trelloproject.card.dto.CardRequestDto;
import com.example.trelloproject.card.dto.CardResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/lists/{listId}/cards")


public class CardController {
    @Autowired
    private CardService cardService;

    /**
     * 카드 생성
     *
     * @param requestDto
     * @return resposeDto
     */
    @PostMapping
    public CardResponseDto creatCard(@RequestBody CardRequestDto requestDto) {

        return cardService.createCard(requestDto);
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

}

