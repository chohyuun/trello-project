package com.example.trelloproject.board;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.trelloproject.board.dto.BoardResponseDto;
import com.example.trelloproject.board.dto.SearchBoardResponseDto;
import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.user.User;
import com.example.trelloproject.workspace.Workspace;
import com.example.trelloproject.workspace.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final AmazonS3 amazonS3;
    private final String bucketName = "mytrellobucket2";
    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;

    //TODO : 권한 및 예외처리, 이미지 저장 수정

    //보드 생성
    public BoardResponseDto createBoard(User user, Long workspaceId, String title, MultipartFile file) {

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);

        String fileUrl = uploadFileToS3(file);

        Board board = new Board(workspace, title);
        board.setImagePath(fileUrl);

        Board savedBoard = boardRepository.save(board);

        return new BoardResponseDto(savedBoard.getId(), savedBoard.getWorkspace().getId(), savedBoard.getTitle(), savedBoard.getImagePath());
    }

    //보드 단건 조회
    public SearchBoardResponseDto getBoard(Long boardId){

        Board board = boardRepository.findByIdOrElseThrow(boardId);

        return new SearchBoardResponseDto(board);
    }

    //보드 수정

    //보드 삭제
    public MessageDto deleteBoard(Long boardId){

        Board board = boardRepository.findByIdOrElseThrow(boardId);

        boardRepository.delete(board);

        String message = "삭제 완료되었습니다.";

        return new MessageDto(message);
    }

    private String uploadFileToS3(MultipartFile file){
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        try{
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);

            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (IOException e){
            throw new RuntimeException("S3 업로드 실패", e);
        }
    }
}
