package com.example.trelloproject.card;

import com.example.trelloproject.global.exception.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;


@Service
public class CardFileService {
    @Autowired
    private CardFileRepository cardFileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public CardFile uploadFile(MultipartFile file, Card card) throws IOException {
        // 파일 크기 검증
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new BusinessException(ExceptionType.FILE_SIZE_EXCEEDED);
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = FilenameUtils.getExtension(fileName).toLowerCase();

        // 파일 형식 검증
        if (!Arrays.asList("jpg", "png", "pdf", "csv").contains(fileExtension)) {
            throw new BusinessException(ExceptionType.INVALID_FILE_FORMAT);
        }

        String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        CardFile cardFile = new CardFile(card, uniqueFileName);
        return cardFileRepository.save(cardFile);
    }


    //파일조회
    public CardFile getCardFile(Long cardId) {
        return cardFileRepository.findByCardId(cardId)
                .orElseThrow(() -> new BusinessException(ExceptionType.FILE_NOT_FOUND));
    }

    //파일삭제
    @Transactional
    public void deleteCardFile(Long cardId) {
        CardFile cardFile = getCardFile(cardId);

        try {
            // 실제 파일 삭제
            Path filePath = Paths.get(uploadDir).resolve(cardFile.getFile());
            Files.deleteIfExists(filePath);

            // DB에서 파일 정보 삭제
            cardFileRepository.delete(cardFile);
        } catch (IOException e) {
            throw new BusinessException(ExceptionType.FILE_DELETE_ERROR);
        }
    }

}
