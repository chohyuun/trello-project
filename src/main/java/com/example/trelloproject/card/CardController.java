package com.example.trelloproject.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api/lists/{listId}/cards")
//test 용
@RequestMapping("/api/lists/1/cards")


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
    public ResponseEntity<List<CardResponseDto>> getListAllCard(@PathVariable Long listId) {
        return ResponseEntity.ok(cardService.getListAllCard(listId));
    }

    @PutMapping("/{cardId}")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long cardId, @PathVariable Long listId, @RequestBody CardRequestDto requestDto) {

        return ResponseEntity.ok(cardService.updateCard(cardId, listId, requestDto));
    }

}

