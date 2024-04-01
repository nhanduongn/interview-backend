package com.nhandn.backend.repository;


import com.nhandn.backend.dto.HistoryResultTestResponse;
import com.nhandn.backend.entity.Score;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ScoreRepository extends JpaRepository<Score, Integer> {

    @Query(value = "select * from score sc where sc.exam_id = :examId and sc.student_id = :studentId",
            nativeQuery = true)
    Score findByExamIdAndStudentId(Integer examId, Integer studentId);

    @Query(value = "select u.username, u.email, e.name as nameExam, e.description, sc.result from score sc\n" +
            "join exams e on e.id = sc.exam_id\n" +
            "join student s on s.id = sc.student_id\n" +
            "join users u on u.id = s.user_id", nativeQuery = true)
    List<Tuple> listHistoryResultTest();

    @Query(value = "select u.username, u.email, e.name as nameExam, e.description, sc.result from score sc\n" +
            "join exams e on e.id = sc.exam_id\n" +
            "join student s on s.id = sc.student_id\n" +
            "join users u on u.id = s.user_id\n" +
            "where u.id = :userId", nativeQuery = true)
    List<Tuple> listHistoryResultTestByUser(Integer userId);

    @Query(value = "select count(sc.id) > 0 from score sc\n" +
            "join student s on s.id = sc.student_id\n" +
            "where s.user_id = :userId", nativeQuery = true)
    boolean isUserTested(Integer userId);

    @Query(value = "select count(sc.id) > 0 from score sc\n" +
            "join exams e on e.id = sc.exam_id\n" +
            "where e.id = :examId", nativeQuery = true)
    boolean isExamTested(Integer examId);
}
