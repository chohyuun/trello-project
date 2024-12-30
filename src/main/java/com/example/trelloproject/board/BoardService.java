package com.example.trelloproject.board;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.trelloproject.board.dto.BoardResponseDto;
import com.example.trelloproject.board.dto.SearchBoardResponseDto;
import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.global.exception.BusinessException;
import com.example.trelloproject.global.exception.ExceptionType;
import com.example.trelloproject.member.MemberRole;
import com.example.trelloproject.workspace.Workspace;
import com.example.trelloproject.workspace.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final AmazonS3 amazonS3;
    private final String bucketName = "mytrellobucket2";
    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;

    /**
     * 보드 생성
     *
     * @param memberRole   멤버 권한
     * @param workspaceId   워크스페이스 id
     * @param title   보드 제목
     * @param file   이미지 파일
     * @return BoardResponseDto
     */
    @Transactional
    public BoardResponseDto createBoard(MemberRole memberRole, Long workspaceId, String title, MultipartFile file) {
        if(memberRole.equals(MemberRole.READONLY)) {
            throw new BusinessException(ExceptionType.UNAUTHORIZED);
        }

        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);

        String fileUrl = uploadFileToS3(file);

        Board board = new Board(workspace, title);
        board.setImagePath(fileUrl);

        Board savedBoard = boardRepository.save(board);

        return new BoardResponseDto(savedBoard.getId(), savedBoard.getWorkspace().getId(), savedBoard.getTitle(), savedBoard.getImagePath());
    }

    /**
     * 보드 단건 조회
     *
     * @param boardId   보드 id
     * @return SearchBoardResponseDto
     */
    public SearchBoardResponseDto getBoard(Long boardId){

        Board board = boardRepository.findByIdOrElseThrow(boardId);

        return new SearchBoardResponseDto(board);
    }

    /**
     * 보드 수정
     *
     * @param memberRole   멤버 권한
     * @param boardId   보드 id
     * @param title   보드 제목
     * @param file   이미지 파일
     * @return BoardResponseDto
     */
    @Transactional
    public BoardResponseDto updateBoard(MemberRole memberRole, Long boardId, String title, MultipartFile file) {
        if(memberRole.equals(MemberRole.READONLY)) {
            throw new BusinessException(ExceptionType.UNAUTHORIZED);
        }

        Board board = boardRepository.findByIdOrElseThrow(boardId);
        String fileUrl = uploadFileToS3(file);
        board.updateBoard(title, fileUrl);

        return new BoardResponseDto(board.getId(), board.getWorkspace().getId(), board.getTitle(), board.getImagePath());
    }

    /**
     * 보드 삭제
     *
     * @param memberRole   멤버 권한
     * @param boardId   보드 id
     * @return MessageDto
     */
    @Transactional
    public MessageDto deleteBoard(MemberRole memberRole, Long boardId){
        if(memberRole.equals(MemberRole.READONLY)) {
            throw new BusinessException(ExceptionType.UNAUTHORIZED);
        }

        Board board = boardRepository.findByIdOrElseThrow(boardId);
        boardRepository.delete(board);

        String message = "삭제 완료되었습니다.";
        return new MessageDto(message);
    }

    /**
     * 파일 업로드
     *
     * @param file   이미지 파일
     * @return String(이미지 경로)
     */
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
