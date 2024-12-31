package com.example.trelloproject.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface CardRepository extends JpaRepository<Card, Long> , JpaSpecificationExecutor<Card> {

    //단건 조회
    Optional<Card> findByListIdAndId(Long listId , Long cardId);
    //리스트 카드 전체 조희
    Page<Card> findAllByListId(Long listId, Pageable pageable);



    //최적화 전
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
    Page<Card> findByList_Board_IdAndTitleContainingOrDescriptionContaining(
            Long boardId, String title, String description, Pageable pageable
    );

    // 특정 보드에 속한 모든 카드 검색
    Page<Card> findByList_Board_Id(
            Long boardId,
            Pageable pageable
    );

    //최적화 후 쿼리
    @Query(value = "SELECT * FROM card c " +
            "WHERE MATCH(c.title, c.description) AGAINST (?1 IN BOOLEAN MODE) " +
            "AND c.list_id IN (SELECT l.id FROM list l WHERE l.board_id = ?2) " +
            "AND c.due_date BETWEEN ?3 AND ?4",
            countQuery = "SELECT COUNT(*) FROM card c " +
                    "WHERE MATCH(c.title, c.description) AGAINST (?1 IN BOOLEAN MODE) " +
                    "AND c.list_id IN (SELECT l.id FROM list l WHERE l.board_id = ?2) " +
                    "AND c.due_date BETWEEN ?3 AND ?4",
            nativeQuery = true)
    Page<Card> searchCards(@Param("keyword") String keyword,
                           @Param("boardId") Long boardId,
                           @Param("startDate") Date startDate,
                           @Param("endDate") Date endDate,
                           Pageable pageable);

}