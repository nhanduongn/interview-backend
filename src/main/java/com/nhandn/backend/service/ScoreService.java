package com.nhandn.backend.service;

import com.nhandn.backend.dto.HistoryResultTestResponse;

import java.util.List;

public interface ScoreService {
    List<HistoryResultTestResponse> listHistoryResultTest();

    List<HistoryResultTestResponse> listHistoryResultTestOfStudent();
}
