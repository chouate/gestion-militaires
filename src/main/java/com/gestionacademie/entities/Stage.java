package com.gestionacademie.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Stage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;

    @ManyToMany(mappedBy = "stages")
    private List<Militaire> militaires;
}
