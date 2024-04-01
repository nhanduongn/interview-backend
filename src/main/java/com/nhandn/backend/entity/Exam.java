package com.nhandn.backend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exams", schema = "public")
public class Exam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exams_generator")
    @SequenceGenerator(name = "exams_generator", sequenceName = "exams_seq", allocationSize = 1)
    private Integer id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "exam")
    private List<Question> questions;

    @OneToMany(mappedBy = "exam")
    private List<Score> scores;

    @OneToMany(mappedBy = "exam")
    private List<AnswerStudent> students;

}
