package com.example.trelloproject.card.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class CardFileRequestDto {
    private final Long cardId;
    private final String fileName;
    private final MultipartFile file;

    public CardFileRequestDto(Long cardId, String fileName, MultipartFile file) {
        this.cardId = cardId;
        this.fileName = fileName;
        this.file = file;
    }
}
