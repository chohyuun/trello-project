package com.example.trelloproject.member;

import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.global.jwt.JwtUtil;
import com.example.trelloproject.member.dto.InviteMemberRequestDto;
import com.example.trelloproject.member.dto.MemberResponseDto;
import com.example.trelloproject.member.dto.UpdateMemberRoleRequestDto;
import com.example.trelloproject.member.dto.UpdateMemberRoleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/workspaces/{workspace_id}/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    //멤버 초대
    @PostMapping
    public ResponseEntity<MessageDto> inviteMember(
            @PathVariable("workspace_id") Long workspaceId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody InviteMemberRequestDto dto) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        MemberRole memberRole = memberService.accessMember(userId, workspaceId);

        MessageDto messageDto = memberService.inviteMember(memberRole, workspaceId, dto.getEmail());

        return new ResponseEntity<>(messageDto, HttpStatus.CREATED);
    }

    //멤버 수락
    @PatchMapping
    public ResponseEntity<MessageDto> acceptMember(
            @PathVariable("workspace_id") Long workspaceId,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        MessageDto messageDto = memberService.acceptMember(userId, workspaceId);

        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }

    //멤버 권한수정
    @PatchMapping("/{id}")
    public ResponseEntity<UpdateMemberRoleResponseDto> updateRoleMember(
            @PathVariable("id") Long memberId,
            @PathVariable("workspace_id") Long workspaceId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UpdateMemberRoleRequestDto dto) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        MemberRole memberRole = memberService.accessMember(userId, workspaceId);

        UpdateMemberRoleResponseDto updateMemberRoleResponseDto = memberService.updateMemberRole(memberRole, memberId, dto.getRole());

        return new ResponseEntity<>(updateMemberRoleResponseDto, HttpStatus.OK);
    }

    //멤버 조회
    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getMembers(
            @PathVariable("workspace_id") Long workspaceId,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        memberService.accessMember(userId, workspaceId);

        List<MemberResponseDto> members = memberService.getMembers(workspaceId);

        return new ResponseEntity<>(members, HttpStatus.OK);
    }
}
