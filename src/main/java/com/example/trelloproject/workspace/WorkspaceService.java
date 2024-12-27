package com.example.trelloproject.workspace;

import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.global.exception.BusinessException;
import com.example.trelloproject.global.exception.ExceptionType;
import com.example.trelloproject.member.Member;
import com.example.trelloproject.member.MemberRepository;
import com.example.trelloproject.member.MemberRole;
import com.example.trelloproject.user.User;
import com.example.trelloproject.user.UserRepository;
import com.example.trelloproject.user.enums.UserRole;
import com.example.trelloproject.workspace.dto.SearchWorkspaceResponseDto;
import com.example.trelloproject.workspace.dto.WorkspaceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    /**
     * 워크스페이스 생성
     *
     * @param userId   유저 id
     * @param title   워크스페이스 제목
     * @param description   워크스페이스 설명
     */

    @Transactional
    public WorkspaceResponseDto createWorkspace(Long userId, String title, String description) {
        User user = userRepository.findByIdOrElseThrow(userId);

        if(!user.getRole().equals(UserRole.ADMIN)){
            throw new BusinessException(ExceptionType.NOT_ADMIN);
        }

        Workspace workspace = new Workspace(user, title, description);
        Member member = new Member(workspace, user, true);
        member.setRole(MemberRole.ADMIN);

        Workspace savedWorkspace = workspaceRepository.save(workspace);
        memberRepository.save(member);

        return new WorkspaceResponseDto(
                savedWorkspace.getId(),
                savedWorkspace.getUser().getId(),
                savedWorkspace.getTitle(),
                savedWorkspace.getDescription());
    }

    /**
     * 워크스페이스 전체 조회
     *
     * @param userId   유저 id
     */
    public List<WorkspaceResponseDto> getWorkspaces(Long userId) {
        List<Workspace> workspaces = workspaceRepository.findWorkspacesByUserId(userId);

        return workspaces.stream()
                .map(workspace -> new WorkspaceResponseDto(
                        workspace.getId(),
                        workspace.getUser().getId(),
                        workspace.getTitle(),
                        workspace.getDescription()))
                .collect(Collectors.toList());
    }

    /**
     * 워크스페이스 단건 조회
     *
     * @param userId   유저 id
     * @param workspaceId   워크스페이스 id
     */
    public SearchWorkspaceResponseDto getWorkspace(Long userId, Long workspaceId) {

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        accessWorkspace(userId, workspace.getId());


        return new SearchWorkspaceResponseDto(workspace);
    }


    /**
     * 워크스페이스 수정
     *
     * @param userId   유저 id
     * @param workspaceId   워크스페이스 id
     * @param title   워크스페이스 제목
     * @param description   워크스페이스 설명
     */
    @Transactional
    public WorkspaceResponseDto updateWorkspace(Long userId, Long workspaceId, String title, String description) {

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        accessWorkspace(userId, workspace.getId());

        workspace.updateWorkspace(title, description);

        return new WorkspaceResponseDto(workspace.getId(), workspace.getUser().getId(), workspace.getTitle(), workspace.getDescription());
    }

    /**
     * 워크스페이스 삭제
     *
     * @param userId   유저 id
     * @param workspaceId   워크스페이스 id
     */
    public MessageDto deleteWorkspace(Long userId, Long workspaceId) {

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);
        accessWorkspace(userId, workspace.getId());

        workspaceRepository.delete(workspace);

        String message = "삭제 완료되었습니다.";

        return new MessageDto(message);
    }

    //접근 권한
    public void accessWorkspace(Long userId, Long workspaceId) {
        Member member = memberRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new BusinessException(ExceptionType.UNAUTHORIZED));

        if(!member.getRole().equals(MemberRole.ADMIN) && !member.getRole().equals(MemberRole.MEMBER)){
            throw new BusinessException(ExceptionType.UNAUTHORIZED);
        }
    }
}