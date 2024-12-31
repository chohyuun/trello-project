package com.example.trelloproject.list;

import com.example.trelloproject.board.Board;
import com.example.trelloproject.board.BoardRepository;
import com.example.trelloproject.global.dto.MessageDto;
import com.example.trelloproject.global.exception.BusinessException;
import com.example.trelloproject.global.exception.ExceptionType;
import com.example.trelloproject.list.dto.ListResponseDto;
import com.example.trelloproject.member.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListService {

    private final ListRepository listRepository;
    private final BoardRepository boardRepository;

    /**
     * 리스트 생성
     *
     * @param memberRole   멤버 권한
     * @param boardId   보드 id
     * @param title   리스트 제목
     * @return ListResponseDto
     */
    @Transactional
    public ListResponseDto createList(MemberRole memberRole, Long boardId, String title) {
        if(memberRole.equals(MemberRole.MEMBER)){
            throw new BusinessException(ExceptionType.UNAUTHORIZED);
        }

        Board board = boardRepository.findByIdOrElseThrow(boardId);

        ListEntity list = new ListEntity(board, title);
        int maxIndex = listRepository.findMaxIndex();
        list.setIndex(maxIndex + 1);

        ListEntity savedList = listRepository.save(list);

        return new ListResponseDto(savedList.getId(), savedList.getBoard().getId(), savedList.getTitle(), savedList.getIndex());
    }

    /**
     * 리스트 제목 수정
     *
     * @param memberRole   멤버 권한
     * @param listId   리스트 id
     * @param title   리스트 제목
     * @return ListResponseDto
     */
    @Transactional
    public ListResponseDto changeTitle(MemberRole memberRole, Long listId, String title) {
        if(memberRole.equals(MemberRole.MEMBER)){
            throw new BusinessException(ExceptionType.UNAUTHORIZED);
        }

        ListEntity list = listRepository.findByIdOrElseThrow(listId);
        list.updateTitle(title);

        return new ListResponseDto(list.getId(), list.getBoard().getId(), list.getTitle(), list.getIndex());
    }

    /**
     * 리스트 순서 변경
     *
     * @param memberRole   멤버 권한
     * @param listId   리스트 id
     * @param index   리스트 순서
     * @return ListResponseDto
     */
    @Transactional
    public ListResponseDto changeIndex(MemberRole memberRole,Long listId, Integer index) {
        if(memberRole.equals(MemberRole.MEMBER)){
            throw new BusinessException(ExceptionType.UNAUTHORIZED);
        }

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

    /**
     * 리스트 삭제
     *
     * @param memberRole   멤버 권한
     * @param listId   리스트 id
     * @return MessageDto
     */
    public MessageDto deleteList(MemberRole memberRole, Long listId) {
        if(memberRole.equals(MemberRole.MEMBER)){
            throw new BusinessException(ExceptionType.UNAUTHORIZED);
        }

        ListEntity list = listRepository.findByIdOrElseThrow(listId);
        listRepository.delete(list);

        String message = "삭제 완료되었습니다.";
        return new MessageDto(message);
    }
}
