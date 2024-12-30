package com.example.trelloproject.notice;

import com.example.trelloproject.global.exception.BusinessException;
import com.example.trelloproject.global.exception.ExceptionType;
import com.example.trelloproject.workspace.Workspace;
import com.example.trelloproject.workspace.WorkspaceRepository;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostEphemeralRequest;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.conversations.ConversationsInviteRequest;
import com.slack.api.methods.request.users.UsersListRequest;
import com.slack.api.methods.response.chat.ChatPostEphemeralResponse;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final Slack slack = Slack.getInstance();
    private final WorkspaceRepository workspaceRepository;

    @Value("${slack.bot.url}")
    private String botToken;

    /**
     * 채널 메시지 전송
     *
     * @param message     메시지 내용
     * @param workspaceId 올릴 채널
     * @param noticeType  채널 변경 타입
     * @param userEmail   작성자 메일 주소
     */
    public void sendSlackBotMessage(String message, Long workspaceId, NoticeChannel noticeType, String userEmail) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new BusinessException(ExceptionType.NOT_FIND_WORKSPACE));

        try {
            String postMessage = "";
            if (userEmail == null) {
                postMessage = "[" + noticeType.name() + "]" + noticeType.getChannel() + " 성공!!!\n" + "내용: " + message;
            } else {
                postMessage = "[" + noticeType.name() + "]" + noticeType.getChannel() + " 성공!!!\n" + "작성자: " + userEmail + "\n내용: " + message;
            }


            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(workspace.getSlackCode())
                    .text(postMessage).build();

            ChatPostMessageResponse response = slack.methods(botToken).chatPostMessage(request);

            if (!response.isOk()) {
                throw new IllegalArgumentException(response.getError());
            }
        } catch (SlackApiException | IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * 유저 지정 메시지 전송
     *
     * @param message        슬랙에 띄울 메시지 내용
     * @param workspaceId    올릴 채널
     * @param noticeType     채널 변경 타입
     * @param userEmail      작성자 메일 주소
     * @param getMessageUser 메시지 받을 유저 메일 주소
     */
    public void sendSlackBotUserMessage(String message, Long workspaceId, NoticeChannel noticeType, String userEmail, String getMessageUser) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new BusinessException(ExceptionType.NOT_FIND_WORKSPACE));

        try {
            String userId = getUsers(getMessageUser);

            String postMessage = "[" + noticeType.name() + "]" + noticeType.getChannel() + " 성공!!!\n" + "작성자: " + userEmail + "\n내용: " + message;

            ChatPostEphemeralRequest request = ChatPostEphemeralRequest.builder()
                    .channel(workspace.getSlackCode())
                    .user(userId)
                    .text(postMessage).build();

            ChatPostEphemeralResponse response = slack.methods(botToken).chatPostEphemeral(request);

            if (!response.isOk()) {
                throw new IllegalArgumentException(response.getError());
            }
        } catch (SlackApiException | IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * 채널 유저 초대
     *
     * @param workspaceId 초대할 채널
     * @param userEmail   초대할 유저 정보
     */
    @Transactional
    public void inviteUser(Long workspaceId, String userEmail) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new BusinessException(ExceptionType.NOT_FIND_WORKSPACE));

        try {
            ConversationsInviteRequest request = ConversationsInviteRequest.builder()
                    .channel(workspace.getSlackCode())
                    .users(Collections.singletonList(userEmail))
                    .build();

            slack.methods(botToken).conversationsInvite(request);

            sendSlackBotMessage(userEmail + "가 추가 되었습니다.", workspaceId, NoticeChannel.MEMBER, userEmail);
        } catch (SlackApiException | IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * 슬랙 체널 유저 확인
     *
     * @param userEmail 유저 이메일을 통해 id 반환
     * @return userId
     * @throws SlackApiException|IOException 슬랙 워크스페이스에 유저 존재하지 않을경우 에러
     */
    public String getUsers(String userEmail) throws SlackApiException, IOException {
        UsersListResponse users = slack.methods(botToken).usersList(UsersListRequest.builder().build());

        for (User listUser : users.getMembers()) {
            if (userEmail.equals(listUser.getProfile().getEmail())) {
                return listUser.getId();
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하는 유저가 아닙니다: " + userEmail);
    }

}
