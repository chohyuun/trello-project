package com.example.trelloproject.list;

import com.example.trelloproject.board.Board;
import com.example.trelloproject.board.BoardRepository;
import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.list.dto.ListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListService {

    private final ListRepository listRepository;
    private final BoardRepository boardRepository;

    //TODO : 권한 및 예외처리

    //리스트 생성
    public ListResponseDto createList(Long boardId, String title) {

        Board board = boardRepository.findByIdOrElseThrow(boardId);

        ListEntity list = new ListEntity(board, title);
        int maxIndex = listRepository.findMaxIndex();
        list.setIndex(maxIndex);

        ListEntity savedList = listRepository.save(list);

        return new ListResponseDto(savedList.getId(), savedList.getBoard().getId(), savedList.getTitle(), savedList.getIndex());
    }

    //리스트 제목 변경
    @Transactional
    public ListResponseDto changeTitle(Long listId, String title) {
        ListEntity list = listRepository.findByIdOrElseThrow(listId);

        list.updateTitle(title);

        return new ListResponseDto(list.getId(), list.getBoard().getId(), list.getTitle(), list.getIndex());
    }

    //리스트 순서 변경
    @Transactional
    public ListResponseDto changeIndex(Long listId, Integer index) {
        ListEntity list = listRepository.findByIdOrElseThrow(listId);

        int oldIndex = list.getIndex();

        if(oldIndex == index){
            return new ListResponseDto(list.getId(), list.getBoard().getId(), list.getTitle(), list.getIndex());
        }

        if(index > oldIndex){
            listRepository.decrementIndexRange(oldIndex + 1, index);
        }else{
            listRepository.incrementIndexRange(index, oldIndex - 1);
        }
        list.setIndex(index);

        return new ListResponseDto(list.getId(), list.getBoard().getId(), list.getTitle(), list.getIndex());
    }

    //리스트 삭제
    public MessageDto deleteList(Long listId) {
        ListEntity list = listRepository.findByIdOrElseThrow(listId);

        listRepository.delete(list);

        String message = "삭제 완료되었습니다.";

        return new MessageDto(message);
    }

    //접근 권함
    public void accessList(Long listId) {

    }
}
