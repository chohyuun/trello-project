package com.example.trelloproject.workspace.dto;

import lombok.Getter;

@Getter
public class WorkspaceResponseDto {

    private final Long workspaceId;

    private final Long userId;

    private final String title;

    private final String description;

    public WorkspaceResponseDto(Long workspaceId, Long userId, String title, String description) {
        this.workspaceId = workspaceId;
        this.userId = userId;
        this.title = title;
        this.description = description;
    }
}
