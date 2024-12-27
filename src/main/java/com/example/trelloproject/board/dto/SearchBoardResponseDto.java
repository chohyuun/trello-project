package com.example.trelloproject.board.dto;

import com.example.trelloproject.board.Board;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SearchBoardResponseDto {

    private Long boardId;

    private Long workspaceId;

    private String title;

    private String file;

    private List<ListDto> lists;

    public SearchBoardResponseDto(Board board) {
        this.boardId = board.getId();
        this.workspaceId = board.getWorkspace().getId();
        this.title = board.getTitle();
        this.file = board.getImagePath();
        this.lists = board.getLists().stream().map(ListDto::new).collect(Collectors.toList());
    }
}
