package com.example.trelloproject.card;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;


@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;


    @Transactional
    public CardResponseDto createCard(CardRequestDto requestDto) {

        Card card = new Card(
                requestDto.getTitle(),
                requestDto.getDescription(),
                requestDto.getDueDate(),
                requestDto.getMember(),
                requestDto.getList(),
                requestDto.getCardFile()
        );


        Card saveCard = cardRepository.save(card);
        return new CardResponseDto(saveCard);
    }


    @Transactional(readOnly = true)
    public CardResponseDto getCard(Long cardId, Long listId) {
        Card card = cardRepository.findByListIdAndId(cardId, listId).orElseThrow(() -> new EntityNotFoundException("카드가 존재하지 않습니다."));
        return new CardResponseDto(card);
    }

//    @Transactional(readOnly = true)
//    public java.util.List<CardResponseDto> getListAllCard(Long listId) {
//        List<Card> cards = cardRepository.findAllByListId(listId);
//        return cards.stream()
//                .map(CardResponseDto::new)
//                .collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public Page<CardResponseDto> getCardsByListId(Long listId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Card> cardPage = cardRepository.findAllByListId(listId, pageable);
        return cardPage.map(CardResponseDto::new);
    }


    public CardResponseDto updateCard(Long cardId, Long listId, CardRequestDto requestDto) {
        Card card = cardRepository.findByListIdAndId(cardId, listId).orElseThrow(() -> new EntityNotFoundException("카드를 찾을 수 없습니다."));
        ;

        card.update(
                requestDto.getTitle(),
                requestDto.getDescription(),
                requestDto.getDueDate()
        );
        return new CardResponseDto(card);
    }

    public Boolean deleteCard(Long cardId) {
        if (cardRepository.existsById(cardId)) {
            cardRepository.deleteById(cardId);
            return true;
        } else {
            return false;
        }
    }
}
