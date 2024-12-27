package com.example.trelloproject.board.dto;

import com.example.trelloproject.card.Card;
import lombok.Getter;

@Getter
public class CardDto {

    private Long cardId;
    private String title;

    public CardDto(Card card) {
        this.cardId = card.getId();
        this.title = card.getTitle();
    }
}
