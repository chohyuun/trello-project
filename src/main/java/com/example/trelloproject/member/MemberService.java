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

    /**
     * 멤버 초대
     *
     * @param memberRole   멤버 권한
     * @param workspaceId   워크스페이스 id
     * @param email   초대 유저 email
     * @return MessageDto
     */
    @Transactional
    public MessageDto inviteMember(MemberRole memberRole, Long workspaceId, String email) {
        if(memberRole == MemberRole.READONLY) {
            throw new BusinessException(ExceptionType.UNAUTHORIZED);
        }

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        User user = userRepository.findByEmailOrElseThrow(email);
        Member invitemember = new Member(workspace, user, false);

        memberRepository.save(invitemember);

        String message = "초대 완료되었습니다.";
        return new MessageDto(message);
    }

    /**
     * 멤버 수락
     *
     * @param userId   유저 id
     * @param workspaceId   워크스페이스 id
     * @return MessageDto
     */
    @Transactional
    public MessageDto acceptMember(Long userId, Long workspaceId) {
        Member member = memberRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new BusinessException(ExceptionType.NOT_FIND_MEMBER));

        member.setActive(true);
        memberRepository.save(member);

        String message = "수락 완료되었습니다.";
        return new MessageDto(message);
    }

    /**
     * 멤버 권한 수정
     *
     * @param memberRole   멤버 권한
     * @param memberId   멤버 id
     * @param role   수정 후 멤버 권한
     * @return UpdateMemberRoleResponseDto
     */
    @Transactional
    public UpdateMemberRoleResponseDto updateMemberRole(MemberRole memberRole, Long memberId, String role) {
        if(memberRole != MemberRole.ADMIN) {
            throw new BusinessException(ExceptionType.NOT_ADMIN);
        }

        Member member = memberRepository.findByIdOrElseThrow(memberId);
        member.setRole(MemberRole.valueOf(role));

        return new UpdateMemberRoleResponseDto(member.getId(), member.getRole().toString());
    }

    /**
     * 멤버 조회
     *
     * @param workspaceId   워크스페이스 id
     * @return List<MemberResponseDto>
     */
    public List<MemberResponseDto> getMembers(Long workspaceId) {
        List<Member> members = memberRepository.findAllByWorkspaceId(workspaceId);

        return members.stream()
                .map(member -> new MemberResponseDto(
                        member.getId(),
                        member.getRole().toString()))
                .collect(Collectors.toList());
    }

    /**
     * 해당 워크스페이스의 멤버인지 확인 후 멤버권한 반환
     *
     * @param userId   유저 id
     * @param workspaceId   워크스페이스 id
     * @return MemberRole
     */
    public MemberRole accessMember(Long userId, Long workspaceId) {
        Member member = memberRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new BusinessException(ExceptionType.NOT_FIND_MEMBER));

        return member.getRole();
    }

    //멤버 권한 확인(카드관련)
    public boolean hasCardEditPermission(Long userId, Long workspaceId) {
        Member member = memberRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new BusinessException(ExceptionType.NOT_FIND_MEMBER));
        return member.getRole() != MemberRole.READONLY;
    }
}
