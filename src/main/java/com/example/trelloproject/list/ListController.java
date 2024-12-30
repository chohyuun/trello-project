package com.example.trelloproject.list;

import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.global.jwt.JwtUtil;
import com.example.trelloproject.list.dto.ChangeIndexRequestDto;
import com.example.trelloproject.list.dto.ChangeTitleRequestDto;
import com.example.trelloproject.list.dto.ListRequestDto;
import com.example.trelloproject.list.dto.ListResponseDto;
import com.example.trelloproject.member.MemberRole;
import com.example.trelloproject.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("workspaces/{workspace_id}/boards/{board_id}/lists")
public class ListController {

    private final ListService listService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ListResponseDto> createList(
            @PathVariable("board_id") Long boardId,
            @PathVariable("workspace_id") Long workspaceId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody ListRequestDto dto) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        MemberRole memberRole = memberService.accessMember(userId, workspaceId);

        ListResponseDto listResponseDto = listService.createList(memberRole, boardId, dto.getTitle());

        return new ResponseEntity<>(listResponseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ListResponseDto> updateTitle(
            @PathVariable("id") Long listId,
            @PathVariable("workspace_id") Long workspaceId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody ChangeTitleRequestDto dto){

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        MemberRole memberRole = memberService.accessMember(userId, workspaceId);

        ListResponseDto listResponseDto = listService.changeTitle(memberRole, listId, dto.getTitle());

        return new ResponseEntity<>(listResponseDto, HttpStatus.OK);
    }


    @PatchMapping("/{id}/index")
    public ResponseEntity<ListResponseDto> changeIndex(
            @PathVariable("id") Long listId,
            @PathVariable("workspace_id") Long workspaceId,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody ChangeIndexRequestDto dto) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        MemberRole memberRole = memberService.accessMember(userId, workspaceId);

        ListResponseDto listResponseDto = listService.changeIndex(memberRole, listId, dto.getIndex());

        return new ResponseEntity<>(listResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteList(
            @PathVariable("id") Long listId,
            @PathVariable("workspace_id") Long workspaceId,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.replace("Bearer ", "");
        Long userId = jwtUtil.extractUserId(token);

        MemberRole memberRole = memberService.accessMember(userId, workspaceId);

        MessageDto messageDto = listService.deleteList(memberRole, listId);

        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }
}
