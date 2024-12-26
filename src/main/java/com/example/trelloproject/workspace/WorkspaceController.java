package com.example.trelloproject.workspace;

import com.example.trelloproject.global.constant.Const;
import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.user.User;
import com.example.trelloproject.workspace.dto.SearchWorkspaceResponseDto;
import com.example.trelloproject.workspace.dto.WorkspaceRequestDto;
import com.example.trelloproject.workspace.dto.WorkspaceResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<WorkspaceResponseDto> createWorkspace(@RequestBody WorkspaceRequestDto dto, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        WorkspaceResponseDto workspaceResponseDto = workspaceService.createWorkspace(user.getId(), dto.getTitle(), dto.getDescription());

        return new ResponseEntity<>(workspaceResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WorkspaceResponseDto>> getWorkspaces(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        List<WorkspaceResponseDto> workspaceResponseDtos = workspaceService.getWorkspaces(user.getId());

        return new ResponseEntity<>(workspaceResponseDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SearchWorkspaceResponseDto> getWorkspace(@PathVariable("id") Long workspaceId, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        SearchWorkspaceResponseDto workspaceResponseDto = workspaceService.getWorkspace(user.getId(), workspaceId);

        return new ResponseEntity<>(workspaceResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WorkspaceResponseDto> updateWorkspace(
            @PathVariable("id") Long workspaceId,
            @RequestBody WorkspaceRequestDto dto,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        WorkspaceResponseDto workspaceResponseDto = workspaceService.updateWorkspace(user.getId(), workspaceId, dto.getTitle(), dto.getDescription());

        return new ResponseEntity<>(workspaceResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteWorkspace(@PathVariable("id") Long workspaceId, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        MessageDto messageDto = workspaceService.deleteWorkspace(user.getId(), workspaceId);

        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }
}
