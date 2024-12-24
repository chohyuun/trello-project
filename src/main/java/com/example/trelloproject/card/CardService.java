package com.example.trelloproject.card;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class CardService {

    @Autowired
    private CardRepository cardRepository;


    @Transactional
    public CardResponseDto createCard(CardRequestDto requestDto) {

        Card card = Card.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .dueDate(requestDto.getDueDate())
                .member(requestDto.getMember())
                .list(requestDto.getList())
                .cardFile(requestDto.getCardFile())
                .build();

        Card saveCard = cardRepository.save(card);
        return new CardResponseDto(saveCard);
    }


    @Transactional(readOnly = true)
    public CardResponseDto getCard(Long cardId, Long listId) {
        Card card = cardRepository.findByListIdAndId(cardId, listId).orElseThrow(() -> new EntityNotFoundException("카드가 존재하지 않습니다."));
        return new CardResponseDto(card);
    }

    @Transactional(readOnly = true)
    public java.util.List<CardResponseDto> getListAllCard(Long listId) {
        List<Card> cards = cardRepository.findAllByListId(listId);
        return cards.stream()
                .map(CardResponseDto::new)
                .collect(Collectors.toList());

    }


    public CardResponseDto updateCard(Long cardId,Long listId, CardRequestDto requestDto){
        Card card = cardRepository.findByListIdAndId(cardId,listId).orElseThrow(() -> new EntityNotFoundException("카드를 찾을 수 없습니다."));;

        card.update(
                requestDto.getTitle(),
                requestDto.getDescription(),
                requestDto.getDueDate()
        );
        return new CardResponseDto(card);
    }
}
