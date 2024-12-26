package com.example.trelloproject.list;

import com.example.trelloproject.global.exception.BusinessException;
import com.example.trelloproject.global.exception.ExceptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ListRepository extends JpaRepository<ListEntity, Long> {

    default ListEntity findByIdOrElseThrow(Long listId) {
        return findById(listId).orElseThrow(() -> new BusinessException(ExceptionType.NOT_FIND_LIST));
    }

    //현재 순서의 끝번호를 가져오는 Query(null일시 1 반환)
    @Query("SELECT COALESCE(MAX(l.index), 1) FROM ListEntity l")
    int findMaxIndex();

    //순서변경 Query
    @Modifying
    @Query("UPDATE ListEntity l SET l.index = l.index + 1 WHERE l.index >= :start AND l.index <= :end")
    void incrementIndexRange(@Param("start") int start, @Param("end") int end);

    @Modifying
    @Query("UPDATE ListEntity l SET l.index = l.index - 1 WHERE l.index >= :start AND l.index <= :end")
    void decrementIndexRange(@Param("start") int start, @Param("end") int end);
}
