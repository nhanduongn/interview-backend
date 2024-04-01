package com.nhandn.backend.repository;


import com.nhandn.backend.entity.AnswerStudent;
import com.nhandn.backend.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    Student findByUser_Id(Integer userId);

    @Query(value = "select count(*)\n" +
            "from (select st.id\n" +
            "      from student st\n" +
            "               join exam_student es on st.id = es.student_id\n" +
            "               join question q on es.exam_id = q.exam_id\n" +
            "               join answer a on q.id = a.question_id\n" +
            "               join answer_student ast on st.id = ast.student_id\n" +
            "      where st.user_id = :userId\n" +
            "        and q.exam_id = :examId and a.is_correct\n" +
            "        and ast.answer_id = a.id)", nativeQuery = true)
    Long amountCorrectOfStudent(Integer userId, Integer examId);

    @Query(value = "select count(*)\n" +
            "from (select st.id\n" +
            "      from student st\n" +
            "               join exam_student es on st.id = es.student_id\n" +
            "               join question q on es.exam_id = q.exam_id\n" +
            "      where st.user_id = :userId and q.exam_id = :examId)", nativeQuery = true)
    Long amountOfQuestion(Integer userId, Integer examId);

}
