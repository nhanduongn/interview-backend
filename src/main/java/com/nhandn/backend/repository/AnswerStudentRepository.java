package com.nhandn.backend.repository;


import com.nhandn.backend.entity.AnswerStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerStudentRepository extends JpaRepository<AnswerStudent, Integer> {

    @Query(value = "select * from answer_student ast\n" +
            "where ast.answer_id not in (:ansId) and ast.student_id = :studentId and ast.exam_id = :examId",
            nativeQuery = true)
    List<Integer> getIds(List<Integer> ansId, Integer studentId, Integer examId);

    @Query(value = "select * from answer_student ast\n" +
            "where ast.answer_id = :ansId and ast.student_id = :studentId", nativeQuery = true)
    AnswerStudent findByAnswerIdAndStudentId(Integer ansId, Integer studentId);
}
