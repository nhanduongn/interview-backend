package com.nhandn.backend.service;

import com.nhandn.backend.dto.HistoryResultTestResponse;
import com.nhandn.backend.entity.User;
import com.nhandn.backend.repository.ScoreRepository;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    private final ScoreRepository repository;
    @Override
    public List<HistoryResultTestResponse> listHistoryResultTest() {
        List<Tuple> historyTuples = repository.listHistoryResultTest();
        List<HistoryResultTestResponse> histories = historyTuples.stream()
                .map(t -> new HistoryResultTestResponse(
                        t.get(0, String.class),
                        t.get(1, String.class),
                        t.get(2, String.class),
                        t.get(3, String.class),
                        t.get(4, Double.class)
                ))
                .collect(Collectors.toList());
        return histories;
    }

    @Override
    public List<HistoryResultTestResponse> listHistoryResultTestOfStudent() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = user.getId();
        List<Tuple> historyTuples = repository.listHistoryResultTestByUser(userId);
        return historyTuples.stream()
                .map(t -> new HistoryResultTestResponse(
                        t.get(0, String.class),
                        t.get(1, String.class),
                        t.get(2, String.class),
                        t.get(3, String.class),
                        t.get(4, Double.class)
                ))
                .collect(Collectors.toList());
    }
}
