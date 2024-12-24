package com.example.trelloproject.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    //단건 조회
    Optional<Card> findByListIdAndId(Long cardId, Long listId);
    //리스트 카드 전체 조희
    List<Card> findAllByListId(Long listId);


}
