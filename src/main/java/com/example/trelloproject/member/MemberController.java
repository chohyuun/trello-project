package com.example.trelloproject.member;

import com.example.trelloproject.global.constant.Const;
import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.member.dto.InviteMemberRequestDto;
import com.example.trelloproject.member.dto.MemberResponseDto;
import com.example.trelloproject.member.dto.UpdateMemberRoleRequestDto;
import com.example.trelloproject.member.dto.UpdateMemberRoleResponseDto;
import com.example.trelloproject.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/workspaces/{workspace_id}/members")
public class MemberController {

    private final MemberService memberService;

    //멤버 초대
    @PostMapping
    public ResponseEntity<MessageDto> inviteMember(
            @PathVariable("workspace_id") Long workspaceId,
            @RequestBody InviteMemberRequestDto dto,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        MessageDto messageDto = memberService.inviteMember(user.getId(), workspaceId, dto.getEmail());

        return new ResponseEntity<>(messageDto, HttpStatus.CREATED);
    }

    //멤버 수락
    @PatchMapping
    public ResponseEntity<MessageDto> acceptMember(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        MessageDto messageDto = memberService.acceptMember(user.getId());

        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }

    //멤버 권한수정
    @PatchMapping("/{id}")
    public ResponseEntity<UpdateMemberRoleResponseDto> updateRoleMember(
            @PathVariable("id") Long memberId,
            @PathVariable("workspace_id") Long workspaceId,
            @RequestBody UpdateMemberRoleRequestDto dto,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        UpdateMemberRoleResponseDto updateMemberRoleResponseDto = memberService.updateMemberRole(user.getId(), workspaceId, memberId, dto.getRole());

        return new ResponseEntity<>(updateMemberRoleResponseDto, HttpStatus.OK);
    }

    //멤버 조회
    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getMembers(@PathVariable("workspace_id") Long workspaceId) {

        List<MemberResponseDto> members = memberService.getMembers(workspaceId);

        return new ResponseEntity<>(members, HttpStatus.OK);
    }
}
