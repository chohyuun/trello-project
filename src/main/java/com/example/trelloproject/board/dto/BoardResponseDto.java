package com.example.trelloproject.board.dto;

import lombok.Getter;

@Getter
public class BoardResponseDto {

    private Long boardId;

    private Long workspaceId;

    private String title;

    private String file;

    public BoardResponseDto(Long boardId, Long workspaceId, String title, String file) {
        this.boardId = boardId;
        this.workspaceId = workspaceId;
        this.title = title;
        this.file = file;
    }
}
