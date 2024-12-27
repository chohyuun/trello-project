package com.example.trelloproject.workspace.dto;

import com.example.trelloproject.workspace.Workspace;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SearchWorkspaceResponseDto {

    private Long workspaceId;

    private Long userId;

    private String title;

    private String description;

    private List<BoardDto> boards;

    public SearchWorkspaceResponseDto(Workspace workspace) {
        this.workspaceId = workspace.getId();
        this.userId = workspace.getUser().getId();
        this.title = workspace.getTitle();
        this.description = workspace.getDescription();
        this.boards = workspace.getBoards().stream().map(BoardDto::new).collect(Collectors.toList());
    }
}
