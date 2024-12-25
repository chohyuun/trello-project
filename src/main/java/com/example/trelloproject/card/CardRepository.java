package com.example.trelloproject.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    //단건 조회
    Optional<Card> findByListIdAndId(Long cardId, Long listId);
    //리스트 카드 전체 조희
    Page<Card> findAllByListId(Long listId, Pageable pageable);

    // 기본 검색
    Page<Card> findByTitleContainingOrDescriptionContainingOrMember_User_UserNameContaining(
            String title,
            String description,
            String userName,
            Pageable pageable
    );

    // 마감일 범위로 검색
    Page<Card> findByDueDateBetween(
            Date startDate,
            Date endDate,
            Pageable pageable
    );

    // 특정 리스트에 속한 카드 검색
    Page<Card> findByListIdAndTitleContainingOrDescriptionContainingOrMember_User_UserNameContaining(
            Long listId,
            String title,
            String description,
            String userName,
            Pageable pageable
    );

    // 특정 보드에 속한 모든 카드 검색
    Page<Card> findByList_Board_Id(
            Long boardId,
            Pageable pageable
    );

    // 보드 내 카드 검색 (키워드로)
    Page<Card> findByList_Board_IdAndTitleContainingOrDescriptionContaining(
            Long boardId,
            String title,
            String description,
            Pageable pageable
    );

}
