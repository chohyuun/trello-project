package com.example.trelloproject.notice;

import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostEphemeralRequest;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.users.UsersListRequest;
import com.slack.api.methods.response.chat.ChatPostEphemeralResponse;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Service
public class NoticeService {
    private final Slack slack = Slack.getInstance();

    @Value("${slack.bot.url}")
    private String botToken;

    /**
     * 채널 메시지 전송
     *
     * @param message   메시지 내용
     * @param channel   올릴 채널
     * @param userEmail 작성자 메일 주소
     */
    public void sendSlackBotMessage(String message, NoticeChannel channel, String userEmail) {
        try {
            String postMessage = messageFormat(message, channel, userEmail);

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channel.getChannel())
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
     * @param channel        채널 종류 enum
     * @param createUser     작성자 메일 주소
     * @param getMessageUser 메시지 받을 유저 메일 주소
     */
    public void sendSlackBotUserMessage(String message, NoticeChannel channel, String createUser, String getMessageUser) {
        try {
            String userId = getUsers(getMessageUser);

            String postMessage = messageFormat(message, channel, createUser);

            ChatPostEphemeralRequest request = ChatPostEphemeralRequest.builder()
                    .channel(channel.getChannel())
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

    /**
     * 메시지 생성 메소드
     *
     * @param message  보낼 메시지
     * @param channel  올라갈 채널
     * @param sendUser 작성, 생성자
     * @return message
     */
    public String messageFormat(String message, NoticeChannel channel, String sendUser) {
        String returnMessage = "";

        if (channel.equals(NoticeChannel.NOTICE_TEST)) {
            returnMessage = "[NOTICE-TEST] 테스트 페이지에 메시지 보내기 성공!! \n" + "작성자: " + sendUser + "\n내용: " + message;
        } else if (channel.equals(NoticeChannel.WORKSPACE)) {
            returnMessage = "[WORKSPACE] 워크스페이스가 생성되었습니다!! \n" + "생성자: " + sendUser + "\n내용: " + message;
        } else if (channel.equals(NoticeChannel.COMMENT)) {
            returnMessage = "[COMMENT] 댓글이 작성되었습니다!! \n" + "작성자: " + sendUser + "\n내용: " + message;
        } else if (channel.equals(NoticeChannel.BOARD)) {
            returnMessage = "[BOARD] 보드가 생성되었습니다!! \n" + "생성자: " + sendUser + "\n내용: " + message;
        } else if (channel.equals(NoticeChannel.CARD)) {
            returnMessage = "[CARD] 카드가 생성되었습니다!! \n" + "생성자: " + sendUser + "\n내용: " + message;
        }

        return returnMessage;
    }
}
