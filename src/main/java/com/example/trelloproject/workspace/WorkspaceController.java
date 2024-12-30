package com.example.trelloproject.workspace;

import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.global.jwt.JwtUtil;
import com.example.trelloproject.member.MemberRole;
import com.example.trelloproject.member.MemberService;
import com.example.trelloproject.workspace.dto.SearchWorkspaceResponseDto;
import com.example.trelloproject.workspace.dto.WorkspaceCreateRequestDto;
import com.example.trelloproject.workspace.dto.WorkspaceRequestDto;
import com.example.trelloproject.workspace.dto.WorkspaceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<WorkspaceResponseDto> createWorkspace(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody WorkspaceCreateRequestDto dto) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        WorkspaceResponseDto workspaceResponseDto = workspaceService.createWorkspace(userId, dto.getTitle(), dto.getDescription(), dto.getSlackCode());

        return new ResponseEntity<>(workspaceResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WorkspaceResponseDto>> getWorkspaces(
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        List<WorkspaceResponseDto> workspaceResponseDtos = workspaceService.getWorkspaces(userId);

        return new ResponseEntity<>(workspaceResponseDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SearchWorkspaceResponseDto> getWorkspace(
            @PathVariable("id") Long workspaceId,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        memberService.accessMember(userId, workspaceId);

        SearchWorkspaceResponseDto workspaceResponseDto = workspaceService.getWorkspace(workspaceId);

        return new ResponseEntity<>(workspaceResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WorkspaceResponseDto> updateWorkspace(
            @PathVariable("id") Long workspaceId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody WorkspaceRequestDto dto) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);
        String userEmail = jwtUtil.extractEmail(token);

        MemberRole memberRole = memberService.accessMember(userId, workspaceId);

        WorkspaceResponseDto workspaceResponseDto = workspaceService.updateWorkspace(memberRole, workspaceId, dto.getTitle(), dto.getDescription(), userEmail);

        return new ResponseEntity<>(workspaceResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteWorkspace(
            @PathVariable("id") Long workspaceId,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        MemberRole memberRole = memberService.accessMember(userId, workspaceId);

        MessageDto messageDto = workspaceService.deleteWorkspace(memberRole, workspaceId);

        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }
}
