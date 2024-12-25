package com.example.trelloproject.card;

import com.example.trelloproject.global.exception.*;
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
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.FilenameUtils;


@Service
public class CardFileService {
    @Autowired
    private CardFileRepository cardFileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public CardFile saveFile(MultipartFile file, Card card) throws IOException {
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

        try {
            String uniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            CardFile cardFile = new CardFile(card, uniqueFileName);
            return cardFileRepository.save(cardFile);

        } catch (IOException e) {
            throw new BusinessException(ExceptionType.FILE_UPLOAD_ERROR);
        }
    }
}
