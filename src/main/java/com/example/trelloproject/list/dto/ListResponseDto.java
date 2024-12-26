package com.example.trelloproject.list.dto;

public class ListResponseDto {

    private final Long listId;

    private final Long boardId;

    private final String title;

    private final Integer index;

    public ListResponseDto(Long listId, Long boardId, String title, Integer index) {
        this.listId = listId;
        this.boardId = boardId;
        this.title = title;
        this.index = index;
    }
}
