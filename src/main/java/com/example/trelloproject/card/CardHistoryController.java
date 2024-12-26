package com.example.trelloproject.card;

import com.example.trelloproject.card.dto.CardHistoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/cards/{cardId}/history")
public class CardHistoryController {
    private CardHistoryService cardHistoryService;

    @GetMapping
    public ResponseEntity<Page<CardHistoryResponseDto>> getCardHistory(
            @PathVariable Long cardId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(cardHistoryService.getCardHistory(cardId, pageable));
    }
}
