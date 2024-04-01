package com.nhandn.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryResultTestResponse {

    private String username;
    private String email;
    private String nameExam;
    private String description;
    private Double result;
}
