package com.example.trelloproject.workspace;

import com.example.trelloproject.global.exception.BusinessException;
import com.example.trelloproject.global.exception.ExceptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    @Query("SELECT DISTINCT m.workspace FROM Member m jOIN FETCH m.workspace w jOIN FETCH w.user WHERE m.user.id = :userId")
    List<Workspace> findWorkspacesByUserId(@Param("userId") Long userId);

    default Workspace findByIdOrElseThrow(Long workspaceId) {
        return findById(workspaceId).orElseThrow(() -> new BusinessException(ExceptionType.NOT_FIND_WORKSPACE));
    }
}
