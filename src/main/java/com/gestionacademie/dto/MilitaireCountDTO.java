package com.gestionacademie.dto;

import lombok.Data;

@Data
public class MilitaireCountDTO {
    private String category;
    private long count;

    public MilitaireCountDTO(String category, long count) {
        this.category = category;
        this.count = count;
    }
}
