package com.nhandn.backend.repository;

import com.nhandn.backend.entity.Question;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface QuestionRepository extends JpaRepository<Question, Integer> {

    @EntityGraph(
            attributePaths = { "answers" })
    List<Question> findAllByExam_Id(Integer examId);
}
