package com.example.trelloproject.card.dto;

import com.example.trelloproject.card.CardFile;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetCardFileDto {
    private Long id;
    private String fileName;

    public GetCardFileDto(CardFile cardFile) {
        this.id = cardFile.getId();
        this.fileName = cardFile.getFile();
    }
}
