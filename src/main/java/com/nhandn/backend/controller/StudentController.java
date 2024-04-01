package com.nhandn.backend.controller;

import com.nhandn.backend.dto.ApiResponse;
import com.nhandn.backend.dto.TestRequest;
import com.nhandn.backend.exception.ResourceNotFoundException;
import com.nhandn.backend.service.ExamService;
import com.nhandn.backend.service.ScoreService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final ExamService service;
    private final ScoreService scoreService;

    @GetMapping("/read-exam/{id}")
    public ResponseEntity<?> read(@PathVariable Integer id) throws ResourceNotFoundException {
        return ResponseEntity.ok(ApiResponse.builder()
                .content(service.readExaminationDetail(id))
                .message(String.format("Read all exam by id = %s", id))
                .code(HttpStatus.OK.name()).build());

    }


    @Operation(summary = "API Lấy danh sách các bài kiểm tra cho giáo viên và học sinh.")
    @GetMapping("/read/all/exam")
    public ResponseEntity<?> getAllExam(@RequestParam Integer page, @RequestParam Integer size) {
        return ResponseEntity.ok(ApiResponse.builder()
                .content(service.readAllExamination(page, size))
                .message("Read all exam")
                .code(HttpStatus.OK.name()).build());

    }


    @Operation(summary = "API cho phép học sinh thực hiện bài kiểm tra và trả về kết quả")
    @PostMapping("/test")
    public ResponseEntity<?> test(@RequestBody TestRequest request) throws ResourceNotFoundException {
        Double res = service.test(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .content(res)
                .message("Completed examination and return result")
                .code(HttpStatus.OK.name()).build());
    }


    @Operation(summary = "API danh sách lịch sử các kết quả thực hiện bài kiểm cho học sinh.")
    @GetMapping("/history-test")
    public ResponseEntity<?> historyResultTestOfStudent() {
        return ResponseEntity.ok(ApiResponse.builder()
                .content(scoreService.listHistoryResultTestOfStudent())
                .message("History result test")
                .code(HttpStatus.OK.name()).build());
    }
}
