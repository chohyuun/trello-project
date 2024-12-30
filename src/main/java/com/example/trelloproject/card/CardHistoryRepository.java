package com.example.trelloproject.card;

import com.example.trelloproject.card.enums.ActionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardHistoryRepository extends JpaRepository<CardHistory,Long> {
    Page<CardHistory> findByCardId(Long cardId, Pageable pageable);

    Page<CardHistory> findByCardIdAndActionType(Long cardId, ActionType actionType, Pageable pageable);
}
