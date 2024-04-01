package com.nhandn.backend.service;

import com.nhandn.backend.dto.TestRequest;
import com.nhandn.backend.dto.ExamDto;
import com.nhandn.backend.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;


public interface ExamService {

    void createExamination(ExamDto request);

    ExamDto readExaminationDetail(Integer id) throws ResourceNotFoundException;

    void updateExamination(ExamDto request);

    Page<ExamDto> readAllExamination(Integer page, Integer size);

    void deleteExam(Integer id) throws Exception;

    Double test(TestRequest request) throws ResourceNotFoundException;

    Double resultOfStudent(Integer userId, Integer examId);

}
