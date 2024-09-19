package com.gestionacademie.dto;

import lombok.Data;

@Data
public class AgeGroupGradeDTO {
    private String ageGroup;
    private String gradeCategorie;
    private Long nombreMilitaires;

    public AgeGroupGradeDTO(String ageGroup, String gradeCategorie, Long nombreMilitaires) {
        this.ageGroup = ageGroup;
        this.gradeCategorie = gradeCategorie;
        this.nombreMilitaires = nombreMilitaires;
    }
}

