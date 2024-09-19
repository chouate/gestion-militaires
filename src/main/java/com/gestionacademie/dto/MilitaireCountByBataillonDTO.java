package com.gestionacademie.dto;

import lombok.Data;

@Data
public class MilitaireCountByBataillonDTO {

    private String bataillon;
    private String bmca;
    private long count;

    public MilitaireCountByBataillonDTO(String bataillon ,String bmca ,long count) {
        this.bataillon = bataillon;
        this.bmca = bmca;
        this.count = count;
    }

}
