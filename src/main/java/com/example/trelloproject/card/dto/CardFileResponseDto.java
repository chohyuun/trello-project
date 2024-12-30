package com.example.trelloproject.card.dto;

import com.example.trelloproject.card.CardFile;
import lombok.Getter;

@Getter
public class CardFileResponseDto {
    private final Long id;
    private final Long cardId;
    private final String fileName;

    public CardFileResponseDto(CardFile cardFile) {
        this.id = cardFile.getId();
        this.cardId = cardFile.getCard().getId();
        this.fileName = cardFile.getFile();
    }
}
