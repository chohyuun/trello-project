package com.example.trelloproject.board;

import com.example.trelloproject.board.dto.BoardRequestDto;
import com.example.trelloproject.board.dto.BoardResponseDto;
import com.example.trelloproject.board.dto.SearchBoardResponseDto;
import com.example.trelloproject.global.constant.Const;
import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces/{workspace_id}/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(
            @PathVariable("workspace_id") Long workspaceId,
            @RequestBody BoardRequestDto dto,
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        BoardResponseDto boardResponseDto = boardService.createBoard(user, workspaceId, dto.getTitle(), dto.getFile());

        return new ResponseEntity<>(boardResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SearchBoardResponseDto> getBoard(@PathVariable("id") Long boardId){

        SearchBoardResponseDto searchBoardResponseDto = boardService.getBoard(boardId);

        return new ResponseEntity<>(searchBoardResponseDto, HttpStatus.OK);
    }

    //보드 수정 추가 예정

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteBoard(@PathVariable("id") Long boardId){

        MessageDto messageDto = boardService.deleteBoard(boardId);

        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }
}
