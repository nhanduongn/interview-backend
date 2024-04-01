package com.nhandn.backend.service;

import com.nhandn.backend.dto.AnswerDto;
import com.nhandn.backend.dto.TestRequest;
import com.nhandn.backend.dto.ExamDto;
import com.nhandn.backend.dto.QuestionDto;
import com.nhandn.backend.entity.*;
import com.nhandn.backend.enums.Role;
import com.nhandn.backend.exception.ResourceNotFoundException;
import com.nhandn.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;


@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {

    private final ExaminationRepository examRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final AnswerStudentRepository answerStudentRepository;
    private final StudentRepository studentRepo;
    private final ScoreRepository scoreRepo;
    private final StudentRepository studentRepository;
    private final ExamStudentRepository esRepo;

    @Transactional
    @Override
    public void createExamination(ExamDto request) {
        Exam exam = Exam.builder().name(request.getName()).description(request.getDescription()).build();

        var savedExamination = examRepository.save(exam);

        List<Question> questions = request.getQuestions().stream()
                .map(q -> Question.builder()
                        .ques(q.getQues()).exam(savedExamination)
                        .answers(q.getAnswers().stream()
                                .map(a -> Answer.builder()
                                        .ans(a.getAns())
                                        .isCorrect(a.isAnswer())
                                        .build())
                                .toList()).build()).toList();
        var savedQuestion = questionRepository.saveAll(questions);
        List<Answer> answers = new ArrayList<>();
        Map<List<Answer>, List<Question>> questionListMap = savedQuestion.stream()
                .collect(groupingBy(Question::getAnswers));

        for (Map.Entry<List<Answer>, List<Question>> map : questionListMap.entrySet()) {
            map.getKey().forEach(ans -> ans.setQuestion(map.getValue().get(0)));
            answers.addAll(map.getKey());
        }
        answerRepository.saveAll(answers);

    }

    @Override
    public ExamDto readExaminationDetail(Integer id) throws ResourceNotFoundException {
        Exam exam = examRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Exam not found for this id :: " + id));
        List<Question> questions = questionRepository.findAllByExam_Id(exam.getId());
        List<Answer> answers = answerRepository.findAllByQuestion_Id(questions.stream()
                .map(Question::getId).toList());
        List<QuestionDto> questionDtoList = questions.stream()
                .map(q -> QuestionDto.builder()
                        .id(q.getId()).ques(q.getQues())
                        .answers(answers.stream()
                                .filter(a -> Objects.equals(q.getId(), a.getQuestion().getId()))
                                .map(a -> AnswerDto.builder()
                                        .id(a.getId()).ans(a.getAns())
                                        .answer(a.isCorrect()).build())
                                .toList()).build())
                .collect(Collectors.toList());
        return ExamDto.builder().id(exam.getId()).name(exam.getName())
                .description(exam.getDescription()).questions(questionDtoList).build();
    }

    @Override
    public void updateExamination(ExamDto request) {
        Exam exam = examRepository.findById(request.getId()).orElse(null);
        if (Objects.nonNull(exam)) {
            exam.setName(request.getName());
            exam.setDescription(request.getDescription());
            examRepository.save(exam);
        }
        request.getQuestions().forEach(q -> {
                    var question = questionRepository.findById(q.getId()).orElse(null);
                    if (Objects.nonNull(question)) {
                        question.setQues(q.getQues());
                        questionRepository.save(question);
                    }});
        List<AnswerDto> ansUpdate = request.getQuestions()
                .stream().map(QuestionDto::getAnswers)
                .flatMap(Collection::stream).toList();
        ansUpdate.forEach(re -> {
            var answer = answerRepository.findById(re.getId()).orElse(null);
            if (Objects.nonNull(answer)) {
                answer.setAns(re.getAns());
                answer.setCorrect(re.isAnswer());
                answerRepository.save(answer);
            }
        });

    }

    @Override
    public Page<ExamDto> readAllExamination(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<ExamDto> examination = examRepository.findAll(pageable).map(e ->
                ExamDto.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .description(e.getDescription())
                        .build());
        return examination;
    }

    @Transactional
    @Override
    public void deleteExam(Integer id) throws Exception {
        Exam exam = examRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Exam not found for this id :: " + id));
        boolean userTested = scoreRepo.isExamTested(id);
        if (userTested) {
            throw new Exception("This exam made test by other");
        }
        exam.getQuestions().forEach(q -> {
            answerRepository.deleteAll(q.getAnswers());
            questionRepository.delete(q);
        });
        examRepository.delete(exam);
    }

    @Transactional
    @Override
    public Double test(TestRequest request) throws ResourceNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = user.getId();
        if (Role.STUDENT == user.getRole()) {
            Student student = studentRepository.findByUser_Id(userId);
            if (Objects.isNull(student)) {
                student = Student.builder().user(user).build();
            }
            student = studentRepository.save(student);
            Exam exam = examRepository.findById(request.getExamId()).orElseThrow(() -> new ResourceNotFoundException("Exam not found for this id :: " + request.getExamId()));
            boolean isTested = examRepository.findExamTested(student.getId(), request.getExamId());
            if (!isTested) {
                ExamStudent es = ExamStudent.builder()
                        .student(student)
                        .exam(exam)
                        .build();
                esRepo.save(es);
            }
            Integer studentId = student.getId();
            Integer examId = exam.getId();
            if (Objects.nonNull(request.getAnswers())) {
                answerStudentRepository.deleteAllById(answerStudentRepository
                        .getIds(request.getAnswers(), studentId, examId));
                List<AnswerStudent> answerStudents = new ArrayList<>();
                for (Integer ansId : request.getAnswers()) {
                    var ansStudent = answerStudentRepository.findByAnswerIdAndStudentId(ansId, studentId);
                    if (Objects.isNull(ansStudent)) {
                        var answerStudent = AnswerStudent.builder()
                                .answer(answerRepository.findById(ansId).orElseThrow(
                                        () -> new ResourceNotFoundException("Answer not found for this id :: " + ansId)))
                                .exam(exam)
                                .student(student)
                                .build();
                        answerStudents.add(answerStudent);
                    }
                }
                answerStudentRepository.saveAll(answerStudents);
            }

            Double res = resultOfStudent(userId, exam.getId());
            Score score = scoreRepo.findByExamIdAndStudentId(examId, studentId);
            if (Objects.isNull(score)) {
                score = Score.builder()
                        .exam(exam)
                        .student(student)
                        .build();
            }
            score.setResult(res);
            scoreRepo.save(score);
            return res;

        }
        return 0.0;
    }

    @Override
    public Double resultOfStudent(Integer userId, Integer examId) {
        Long correct = studentRepo.amountCorrectOfStudent(userId, examId);
        Long question = studentRepo.amountOfQuestion(userId, examId);
        Double res = (double) ((correct * 100)/question);
        return res;
    }
}
