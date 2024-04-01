package com.nhandn.backend.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExamDto {

    private Integer id;
    private String name;
    private String description;
    private List<QuestionDto> questions;

}
