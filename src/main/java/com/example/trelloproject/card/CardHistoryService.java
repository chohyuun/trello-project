package com.example.trelloproject.card;

import com.example.trelloproject.card.dto.CardHistoryResponseDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardHistoryService {
    private CardHistoryRepository cardHistoryRepository;

    @Transactional(readOnly = true)
    public Page<CardHistoryResponseDto> getCardHistory(Long cardId, Pageable pageable) {
        Page<CardHistory> histories = cardHistoryRepository.findByCardId(cardId, pageable);
        return histories.map(CardHistoryResponseDto::new);
    }
}
