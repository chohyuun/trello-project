package com.example.trelloproject.list;

import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.list.dto.ChangeIndexRequestDto;
import com.example.trelloproject.list.dto.ChangeTitleRequestDto;
import com.example.trelloproject.list.dto.ListRequestDto;
import com.example.trelloproject.list.dto.ListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/{board_id}/list")
public class ListController {

    private final ListService listService;

    @PostMapping
    public ResponseEntity<ListResponseDto> createList(@PathVariable("board_id") Long boardId, @RequestBody ListRequestDto dto) {

        ListResponseDto listResponseDto = listService.createList(boardId, dto.getTitle());

        return new ResponseEntity<>(listResponseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ListResponseDto> updateTitle(@PathVariable("id") Long listId, @RequestBody ChangeTitleRequestDto dto){

        ListResponseDto listResponseDto = listService.changeTitle(listId, dto.getTitle());

        return new ResponseEntity<>(listResponseDto, HttpStatus.OK);
    }


    @PatchMapping("/{id}/index")
    public ResponseEntity<ListResponseDto> changeIndex(
            @PathVariable("id") Long listId,
            @RequestBody ChangeIndexRequestDto dto) {

        ListResponseDto listResponseDto = listService.changeIndex(listId, dto.getIndex());

        return new ResponseEntity<>(listResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> deleteList(@PathVariable("id") Long listId) {

        MessageDto messageDto = listService.deleteList(listId);

        return new ResponseEntity<>(messageDto, HttpStatus.OK);
    }
}
