package com.gestionacademie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class MilitaireGradeCountDTO {

    private String bataillon;
    private String gradeCategory;
    private Long count;
}
