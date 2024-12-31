package com.example.trelloproject.board.dto;

import com.example.trelloproject.list.ListEntity;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ListDto {

    private Long listId;

    private String title;

    private List<CardDto> cards;

    public ListDto(ListEntity list) {
        this.listId = list.getId();
        this.title = list.getTitle();
        this.cards = list.getCards().stream().map(CardDto::new).collect(Collectors.toList());
    }
}
