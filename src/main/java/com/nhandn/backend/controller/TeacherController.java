package com.nhandn.backend.controller;


import com.nhandn.backend.dto.ApiResponse;
import com.nhandn.backend.dto.ExamDto;
import com.nhandn.backend.dto.UserRequest;
import com.nhandn.backend.exception.ResourceNotFoundException;
import com.nhandn.backend.service.ExamService;
import com.nhandn.backend.service.ScoreService;
import com.nhandn.backend.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final UserService service;
    private final ExamService examService;
    private final ScoreService scoreService;

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(
            @RequestBody @Valid UserRequest request) throws Exception {
        return ResponseEntity.ok(ApiResponse.builder()
                .message("User created successfully")
                .content(service.create(request))
                .code(HttpStatus.OK.name()).build());

    }

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody UserRequest request) {
        service.updateUser(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("User updated successfully")
                .code(HttpStatus.OK.name()).build());
    }

    @GetMapping("/read/all-user")
    public ResponseEntity<?> getAllUser(@RequestParam Integer page, @RequestParam Integer size) {
        return ResponseEntity.ok(ApiResponse.builder()
                .content(service.getAllUser(page, size))
                .message("Read all user successfully")
                .code(HttpStatus.OK.name()).build());
    }

    @GetMapping("/read/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .content(service.getUser(id))
                .message(String.format("Read all user by id = %s", id))
                .code(HttpStatus.OK.name()).build());
    }

    @DeleteMapping("/delete/user")
    public ResponseEntity<?> deleteUser(@RequestParam String email) throws Exception {
        service.deleteUser(email);
        return ResponseEntity.ok(ApiResponse.builder()
                        .message(String.format("Deleted user by email = %s", email))
                        .code(HttpStatus.OK.name()).build());
    }

    @PostMapping("/create-exam")
    public ResponseEntity<?> createExamination(@RequestBody ExamDto request) {
        examService.createExamination(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Exam created successfully")
                .code(HttpStatus.OK.name()).build());
    }

    @PutMapping("/update-exam")
    public ResponseEntity<?> updateExamination(@RequestBody ExamDto request) {
        examService.updateExamination(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Exam updated successfully")
                .code(HttpStatus.OK.name()).build());
    }


    @DeleteMapping("/delete-exam/{id}")
    public ResponseEntity<?> deleteExamination(@PathVariable Integer id) throws Exception {
        examService.deleteExam(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Exam deleted successfully")
                .code(HttpStatus.OK.name()).build());
    }

    @Operation(summary = "API danh sách lịch sử các kết quả thực hiện bài kiểm cho giáo viên.")
    @GetMapping("/history-test/all")
    public ResponseEntity<?> historyTest() {
        return ResponseEntity.ok(ApiResponse.builder()
                .content(scoreService.listHistoryResultTest())
                .message("History result test")
                .code(HttpStatus.OK.name()).build());
    }

}
