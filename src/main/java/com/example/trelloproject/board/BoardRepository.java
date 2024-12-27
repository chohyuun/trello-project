package com.example.trelloproject.board;

import com.example.trelloproject.global.exception.BusinessException;
import com.example.trelloproject.global.exception.ExceptionType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    default Board findByIdOrElseThrow(Long boardId) {
        return findById(boardId).orElseThrow(() -> new BusinessException(ExceptionType.NOT_FIND_BOARD));
    }
}
