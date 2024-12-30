package com.example.trelloproject.comment;

import com.example.trelloproject.comment.dto.CommentListResponseDto;
import com.example.trelloproject.comment.dto.CommentRequestDto;
import com.example.trelloproject.comment.dto.CommentResponseDto;
import com.example.trelloproject.global.dto.MessageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workspaces/{workspaceId}/boards/{boardId}/lists/{listId}/cards/{cardId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /**
     * 댓글 추가
     *
     * @param cardId            댓글이 들어갈 card의 id
     * @param commentRequestDto contents string - 댓글 입력
     * @param loginUser         로그인 한 유저의 정보
     */
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable("workspaceId") Long workspaceId,
            @PathVariable("cardId") Long cardId,
            @Valid @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetails loginUser
    ) {
        CommentResponseDto commentResponseDto = commentService.createComment(workspaceId, commentRequestDto.getContents(), loginUser.getUsername(), cardId);

        return ResponseEntity.ok(commentResponseDto);
    }

    /**
     * 댓글 수정
     *
     * @param cardId            수정할 댓글이 있는 카드의 id
     * @param id                변경할 댓글 id
     * @param commentRequestDto 댓글 수정 내용
     * @param loginUser         로그인 한 유저의 정보
     * @return 수정된 내용
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable("workspaceId") Long workspaceId,
            @PathVariable("cardId") Long cardId,
            @PathVariable("id") Long id,
            @Valid @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetails loginUser
    ) {
        CommentResponseDto commentResponseDto = commentService.updateComment(workspaceId, commentRequestDto.getContents(), id, cardId, loginUser.getUsername());

        return ResponseEntity.ok(commentResponseDto);
    }

    /**
     * 댓글 삭제
     *
     * @param cardId    수정할 댓글이 있는 카드의 id
     * @param id        변경할 댓글 id
     * @param loginUser 로그인 한 유저의 정보
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteComment(
            @PathVariable("cardId") Long cardId,
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails loginUser
    ) {
        MessageDto messageDto = commentService.deleteComment(cardId, id, loginUser.getUsername());

        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }

    /**
     * 댓글 전체 조회
     *
     * @param cardId 댓긇을 조회할 카드 id
     * @return 댓글 리스트(작성 멤버 명, 내용,
     */
    @GetMapping
    public ResponseEntity<List<CommentListResponseDto>> getComments(
            @PathVariable("cardId") Long cardId
    ) {
        List<CommentListResponseDto> commentListResponseDtoList = commentService.getComments(cardId);

        return ResponseEntity.ok(commentListResponseDtoList);
    }
}
