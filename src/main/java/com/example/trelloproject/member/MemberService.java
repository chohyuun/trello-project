package com.example.trelloproject.member;

import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.global.exception.BusinessException;
import com.example.trelloproject.global.exception.ExceptionType;
import com.example.trelloproject.member.dto.MemberResponseDto;
import com.example.trelloproject.member.dto.UpdateMemberRoleResponseDto;
import com.example.trelloproject.user.User;
import com.example.trelloproject.user.UserRepository;
import com.example.trelloproject.workspace.Workspace;
import com.example.trelloproject.workspace.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    //멤버 초대
    public MessageDto inviteMember(Long userId, Long workspaceId, String email) {

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        User inviteUser = userRepository.findByIdOrElseThrow(userId);
        User user = userRepository.findByEmailOrElseThrow(email);
        Member member = memberRepository.findByIdOrElseThrow(inviteUser.getId());

        if(!member.getId().equals(inviteUser.getId())) {
            throw new BusinessException(ExceptionType.NOT_MEMBER);
        }

        Member invitemember = new Member(workspace, user, false);
        memberRepository.save(invitemember);

        String message = "초대 완료되었습니다.";

        return new MessageDto(message);
    }

    //멤버 수락
    @Transactional
    public MessageDto acceptMember(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        Member member = memberRepository.findByIdOrElseThrow(user.getId());

        member.setActive(true);
        memberRepository.save(member);

        String message = "수락 완료되었습니다.";

        return new MessageDto(message);
    }

    //멤버 권한수정
    @Transactional
    public UpdateMemberRoleResponseDto updateMemberRole(Long userId, Long workspaceId, Long memberId, String role) {
        User user = userRepository.findByIdOrElseThrow(userId);
        Member member = memberRepository.findByIdOrElseThrow(memberId);
        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);

        if(!workspace.getUser().getId().equals(user.getId())) {
            throw new BusinessException(ExceptionType.NOT_ADMIN);
        }

        member.setRole(MemberRole.valueOf(role));

        return new UpdateMemberRoleResponseDto(member.getId(), member.getRole().toString());
    }

    //멤버 조회
    public List<MemberResponseDto> getMembers(Long workspaceId) {
        List<Member> members = memberRepository.findAllByWorkspaceId(workspaceId);

        return members.stream()
                .map(member -> new MemberResponseDto(
                        member.getId(),
                        member.getRole().toString()))
                .collect(Collectors.toList());
    }
}
