package com.gestionacademie.entities;

import jakarta.persistence.*;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Militaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

//    @Lob
//    @Column(columnDefinition = "MEDIUMBLOB")
//    private byte[] image;

    private String imagePath; // chemin de l'image

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "La date de naissance est obligatoire")
    private Date dateNaissance;

    @Pattern(regexp = "^[0-9]{10}$", message = "Le téléphone doit contenir 10 chiffres")
    private String telephone;

    @NotBlank(message = "La fonction est obligatoire")
    private String fonction;

    @NotBlank(message = "Le diplôme est obligatoire")
    private String diplome;

    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    private String BMCA;

    @NotBlank(message = "champ est obligatoire")
    private String situationFamiliale;


    //@Pattern(regexp = "^[1-9][0-9]*$", message = "Le matricule ne doit pas commencer par zéro et doit être numérique")
    @Column(unique = true, nullable = false) // Ensures uniqueness
    private String matricule;

    @Column(name = "LD", nullable = true)  // Le champ est autorisé à être NULL
    @Positive(message = "Le LD doit être positif")
    private Integer LD;

    @Column(name = "PTC", nullable = true)  // Le champ est autorisé à être NULL
    @Positive ( message = "Le PTC doit être positif")
    private Integer PTC;

    @ManyToMany
    @JoinTable(
            name = "militaire_stage",
            joinColumns = @JoinColumn(name = "militaire_id"),
            inverseJoinColumns = @JoinColumn(name = "stage_id")
    )
    private List<Stage> stages;

    @ManyToOne
    private Grade grade;

    @ManyToOne
    private Companie companie;

    @ManyToOne
    private Bataillon bataillon;

    public int getAge(){
        if(dateNaissance == null)
            return 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateNaissance);
        LocalDate birthDate = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}
