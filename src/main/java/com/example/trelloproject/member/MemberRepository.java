package com.example.trelloproject.member;

import com.example.trelloproject.global.exception.BusinessException;
import com.example.trelloproject.global.exception.ExceptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m FROM Member m JOIN FETCH m.user WHERE m.workspace.id = :workspaceId")
    List<Member> findAllByWorkspaceId(@Param("workspaceId") Long workspaceId);

    //해당 워크스페이스의 멤버인지 확인
    @Query("SELECT m FROM Member m JOIN FETCH m.user jOIN FETCH m.workspace WHERE m.user.id = :userId AND m.workspace.id = :workspaceId")
    Optional<Member> findByUserIdAndWorkspaceId(@Param("userId") Long userId, @Param("workspaceId") Long workspaceId);

    @Query("SELECT m FROM Member m JOIN FETCH m.user u JOIN FETCH m.workspace w WHERE u.email = :userEmail AND w.id = :workspaceId")
    Optional<Member> findByUserEmailAndWorkspaceId(@Param("userEmail") String userEmail, @Param("workspaceId") Long workspaceId);

    default Member findByIdOrElseThrow(Long MemberId) {
        return findById(MemberId).orElseThrow(() -> new BusinessException(ExceptionType.NOT_FIND_MEMBER));
    }
}
