package com.gestionacademie.dto;

import lombok.Data;

@Data
public class AgeGroupGradeDTO2 {
    private String bataillon;
    private String ageGroup;
    private String gradeCategorie;
    private Long nombreMilitaires;

    public AgeGroupGradeDTO2(String bataillon, String ageGroup, String gradeCategorie, Long nombreMilitaires) {
        this.bataillon = bataillon;
        this.ageGroup = ageGroup;
        this.gradeCategorie = gradeCategorie;
        this.nombreMilitaires = nombreMilitaires;
    }
}

