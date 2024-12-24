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
     * @param requestDto
     * @return resposeDto
     */

    @PostMapping
    public CardResponseDto creatCard(@RequestBody CardRequestDto requestDto) {

        return cardService.createCard(requestDto);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponseDto> getcard(@PathVariable Long cardId, @PathVariable Long listId ) {
        return ResponseEntity.ok(cardService.getCard(cardId, listId));
    }

    @GetMapping
    public ResponseEntity<List<CardResponseDto>> getListAllCard(@PathVariable Long listId){
        return ResponseEntity.ok(cardService.getListAllCard(listId));
    }
}

