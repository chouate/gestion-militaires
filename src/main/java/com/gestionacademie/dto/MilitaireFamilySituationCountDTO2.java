package com.gestionacademie.dto;

import lombok.Data;

@Data
public class MilitaireFamilySituationCountDTO2 {

    private String situationFamiliale;
    private String grade;
    private String bataillon;
    private Long count;

    public MilitaireFamilySituationCountDTO2(String situationFamiliale, String gradeCategorie, String bataillon, Long count) {
        this.situationFamiliale = situationFamiliale;
        this.grade = gradeCategorie;
        this.bataillon = bataillon;
        this.count = count;
    }
}
