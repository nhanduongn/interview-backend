package com.nhandn.backend.repository;


import com.nhandn.backend.entity.Exam;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface ExaminationRepository extends JpaRepository<Exam, Integer> {

    Optional<Exam> findById(Integer id);
    @Query(value = "select count(es.student_id) > 0\n" +
            "from exam_student es\n" +
            "where es.student_id = :studentId and es.exam_id = :examId", nativeQuery = true)
    boolean findExamTested(Integer studentId, Integer examId);

    @Query(value = "select * from examination ex where ex.tested = true", nativeQuery = true)
    List<Exam> historicalTest();



}
