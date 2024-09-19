package com.gestionacademie.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MilitaireFamilySituationCountDTO {

    private String situationFamiliale;
    private String grade;
    private Long count;

    public MilitaireFamilySituationCountDTO(String situationFamiliale, String gradeCategorie, Long count) {
        this.situationFamiliale = situationFamiliale;
        this.grade = gradeCategorie;
        this.count = count;
    }
}
