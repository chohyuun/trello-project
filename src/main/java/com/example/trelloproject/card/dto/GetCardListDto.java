package com.example.trelloproject.card.dto;

import com.example.trelloproject.list.ListEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetCardListDto {
    private Long id;
    private String title;

    public GetCardListDto(ListEntity list) {
        this.id = list.getId();
        this.title = list.getTitle();
    }
}
