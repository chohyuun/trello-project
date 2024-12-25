package com.example.trelloproject.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardFileRepository extends JpaRepository<CardFile,Long> {
    // 기본적인 CRUD 작업은 JpaRepository에서 제공
    // 카드 ID로 첨부파일 찾기
    Optional<CardFile> findByCardId(Long cardId);
    // 카드 ID로 첨부파일 삭제
    void deleteByCardId(Long cardId);


}
