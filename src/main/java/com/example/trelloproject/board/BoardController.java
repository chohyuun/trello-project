package com.example.trelloproject.board;

import com.example.trelloproject.board.dto.BoardResponseDto;
import com.example.trelloproject.board.dto.SearchBoardResponseDto;
import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.global.jwt.JwtUtil;
import com.example.trelloproject.member.MemberRole;
import com.example.trelloproject.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces/{workspace_id}/boards")
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(
            @PathVariable("workspace_id") Long workspaceId,
            @RequestPart("title") String title,
            @RequestPart("file") MultipartFile file,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        MemberRole memberRole = memberService.accessMember(userId, workspaceId);

        BoardResponseDto boardResponseDto = boardService.createBoard(memberRole, workspaceId, title, file);

        return new ResponseEntity<>(boardResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SearchBoardResponseDto> getBoard(
            @PathVariable("id") Long boardId,
            @PathVariable("workspace_id") Long workspaceId,
            @RequestHeader("Authorization") String authorizationHeader){

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        memberService.accessMember(userId, workspaceId);

        SearchBoardResponseDto searchBoardResponseDto = boardService.getBoard(boardId);

        return new ResponseEntity<>(searchBoardResponseDto, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BoardResponseDto> updateBoard(
            @PathVariable("id") Long boardId,
            @PathVariable("workspace_id") Long workspaceId,
            @RequestPart("title") String title,
            @RequestPart("file") MultipartFile file,
            @RequestHeader("Authorization") String authorizationHeader){

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        MemberRole memberRole = memberService.accessMember(userId, workspaceId);

        BoardResponseDto boardResponseDto = boardService.updateBoard(memberRole, boardId, title, file);

        return new ResponseEntity<>(boardResponseDto, HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteBoard(
            @PathVariable("id") Long boardId,
            @PathVariable("workspace_id") Long workspaceId,
            @RequestHeader("Authorization") String authorizationHeader){

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        MemberRole memberRole = memberService.accessMember(userId, workspaceId);
        MessageDto messageDto = boardService.deleteBoard(memberRole, boardId);

        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }
}
