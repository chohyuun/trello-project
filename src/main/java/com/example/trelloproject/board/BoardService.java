package com.example.trelloproject.board;

import com.example.trelloproject.board.dto.BoardResponseDto;
import com.example.trelloproject.board.dto.SearchBoardResponseDto;
import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.user.User;
import com.example.trelloproject.workspace.Workspace;
import com.example.trelloproject.workspace.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;

    //TODO : 권한 및 예외처리, 이미지 저장 수정

    //보드 생성
    public BoardResponseDto createBoard(User user, Long workspaceId, String title, String file) {
        Workspace workspace = workspaceRepository.findByIdOrElseThrow(workspaceId);


        Board board = new Board(workspace, title);
        board.setImagePath(file);

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
}
