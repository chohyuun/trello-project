package com.example.trelloproject.comment;

import com.example.trelloproject.card.Card;
import com.example.trelloproject.card.CardRepository;
import com.example.trelloproject.comment.dto.CommentListResponseDto;
import com.example.trelloproject.comment.dto.CommentResponseDto;
import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.global.exception.BusinessException;
import com.example.trelloproject.global.exception.ExceptionType;
import com.example.trelloproject.member.Member;
import com.example.trelloproject.member.MemberRepository;
import com.example.trelloproject.user.User;
import com.example.trelloproject.user.UserRepository;
import com.example.trelloproject.workspace.Workspace;
import com.example.trelloproject.workspace.WorkspaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final WorkspaceRepository workspaceRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    //    private final NoticeService noticeService;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDto createComment(Long workspaceId, String comments, String memberEmail, Long cardId) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new BusinessException(ExceptionType.NOT_FIND_WORKSPACE));
        Member member = memberRepository.findByUserEmailAndWorkspaceId(memberEmail, workspaceId)
                .orElseThrow(() -> new BusinessException(ExceptionType.NOT_FIND_MEMBER));

        member.authCheck();
        member.notWorkspaceMember(workspaceId);

        Card card = cardRepository.findById(cardId).orElseThrow();
        Member cardOwner = card.getMember();

        Comment comment = new Comment(comments, member, card);

        commentRepository.save(comment);
//        noticeService.sendSlackBotUserMessage(comments, workspace.getSlackCode(), NoticeChannel.COMMENT, member.getUser().getEmail(), cardOwner.getUser().getEmail());

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(
            Long workspaceId, String comments, Long commentId, Long cardId, String memberEmail
    ) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new BusinessException(ExceptionType.NOT_FIND_WORKSPACE));
        User user = userRepository.findById(workspace.getUser().getId()).orElseThrow(() -> new BusinessException(ExceptionType.NOT_FIND_MEMBER));

        comment.isUserComment(memberEmail);
        comment.isCardToComment(cardId);

        comment.update(comments);
//        noticeService.sendSlackBotUserMessage(comments, workspace.getSlackCode(), NoticeChannel.COMMENT_CHANGE, memberEmail, user.getEmail());

        return new CommentResponseDto(comment);
    }

    @Transactional
    public MessageDto deleteComment(Long cardId, Long commentId, String memberEmail) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();

        comment.isUserComment(memberEmail);
        comment.isCardToComment(cardId);

        commentRepository.delete(comment);

        return new MessageDto("댓글 삭제가 성공했습니다.");
    }

    public List<CommentListResponseDto> getComments(Long cardId) {
        List<Comment> comments = commentRepository.findByCardId(cardId);

        List<CommentListResponseDto> commentListResponseDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentListResponseDtos.add(new CommentListResponseDto(
                    comment.getId(),
                    comment.getMember().getId(),
                    comment.getMember().getUser().getUserName(),
                    comment.getContents()
            ));
        }

        return commentListResponseDtos;
    }
}
