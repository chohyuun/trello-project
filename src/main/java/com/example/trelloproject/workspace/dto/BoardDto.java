package com.example.trelloproject.workspace.dto;

import com.example.trelloproject.board.Board;
import lombok.Getter;

@Getter
public class BoardDto {

    private Long boardId;

    private String title;

    private String file;

    public BoardDto(Board board) {
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.file = board.getImagePath();
    }
}
