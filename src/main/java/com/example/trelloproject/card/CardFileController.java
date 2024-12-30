package com.example.trelloproject.card;

import com.example.trelloproject.card.dto.CardFileResponseDto;
import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.global.exception.BusinessException;
import com.example.trelloproject.global.exception.ExceptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/cards/{cardId}/files")
public class CardFileController {

    private final CardFileService cardFileService;
    private final CardRepository cardRepository;

    @Autowired
    public CardFileController(CardFileService cardFileService, CardRepository cardRepository) {
        this.cardFileService = cardFileService;
        this.cardRepository = cardRepository;
    }

    /**
     * 카드에 파일 첨부
     *
     * @param cardId 카드 ID
     * @param file   첨부할 파일 (최대 5MB, 지원형식: jpg, png, pdf, csv)
     * @return 첨부된 파일 정보
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CardFileResponseDto> uploadFile(
            @PathVariable Long cardId,
            @RequestPart(value = "file") MultipartFile file) {
        try {
            Card card = cardRepository.findById(cardId)
                    .orElseThrow(() -> new BusinessException(ExceptionType.CARD_NOT_FOUND));

            CardFile savedFile = cardFileService.uploadFile(file, card);
            return ResponseEntity.ok(new CardFileResponseDto(savedFile));
        } catch (IOException e) {
            throw new BusinessException(ExceptionType.FILE_UPLOAD_ERROR);
        }
    }

    /**
     * 카드의 첨부파일 조회
     *
     * @param cardId 카드 ID
     * @return 첨부파일 정보
     */
    @GetMapping("/{cardfileId}")
    public ResponseEntity<CardFileResponseDto> getFile(@PathVariable Long cardId) {
        CardFile cardFile = cardFileService.getCardFile(cardId);
        return ResponseEntity.ok(new CardFileResponseDto(cardFile));
    }

    /**
     * 카드의 첨부파일 삭제
     *
     * @param cardId 카드 ID
     * @return 삭제 결과 메시지
     */
    @DeleteMapping("/{cardfileId}")
    public ResponseEntity<MessageDto> deleteFile(@PathVariable Long cardId) {
        cardFileService.deleteCardFile(cardId);
        return ResponseEntity.ok(new MessageDto("첨부파일이 삭제되었습니다."));
    }
}
