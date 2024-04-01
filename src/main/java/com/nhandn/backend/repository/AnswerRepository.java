package com.nhandn.backend.repository;

import com.nhandn.backend.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    @Query(value = "select * from answer a where a.question_id in (:quesId)", nativeQuery = true)
    List<Answer> findAllByQuestion_Id(List<Integer> quesId);
}
