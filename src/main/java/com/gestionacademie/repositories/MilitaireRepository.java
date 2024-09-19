package com.gestionacademie.repositories;


import com.gestionacademie.dto.*;
import com.gestionacademie.entities.Militaire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public interface MilitaireRepository extends JpaRepository<Militaire, Long> {

    @Override
    Page<Militaire> findAll(Pageable pageable);

    @Query("SELECT m FROM Militaire m WHERE m.bataillon.nom =:bataillon ")
    Page<Militaire> getMilitairesByBataillon(Pageable pageable,@Param("bataillon") String bataillon);


    @Query("SELECT new com.gestionacademie.dto.MilitaireCountDTO(m.BMCA, COUNT(m)) FROM Militaire m GROUP BY m.BMCA")
    List<MilitaireCountDTO> countByBMCA();

    @Query("SELECT new com.gestionacademie.dto.MilitaireCountByBataillonDTO(m.bataillon.nom, m.BMCA, COUNT(m)) " + "FROM Militaire m GROUP BY m.bataillon.nom, m.BMCA")
    List<MilitaireCountByBataillonDTO> countByBMCAAndBataillon();
    @Query("SELECT new com.gestionacademie.dto.MilitaireCountByBataillonDTO(m.bataillon.nom, m.BMCA, COUNT(m)) " + "FROM Militaire m where m.bataillon.nom =:bmca GROUP BY m.BMCA")
    List<MilitaireCountByBataillonDTO> countByBMCAAndBataillon(@Param("bmca") String bmca);

    // Requête pour obtenir le nombre de chaque catégorie de grade par bataillon
    @Query("SELECT m.bataillon, m.grade, COUNT(m) FROM Militaire m GROUP BY m.bataillon, m.grade")
    List<Object[]> countByBataillonAndGrade();

    @Query("SELECT new com.gestionacademie.dto.MilitaireGradeCountDTO(m.bataillon.nom, m.grade.categorie, COUNT(m)) " +
            "FROM Militaire m GROUP BY m.bataillon.nom, m.grade.categorie")
    List<MilitaireGradeCountDTO> countByGradeCategoryAndBataillon();

    @Query("SELECT new com.gestionacademie.dto.MilitaireGradeCountDTO(m.bataillon.nom, m.grade.categorie, COUNT(m)) " +
            "FROM Militaire m WHERE m.bataillon.nom = :bataillon GROUP BY m.bataillon.nom, m.grade.categorie")
    List<MilitaireGradeCountDTO> countByGradeCategoryAndBataillon(@Param("bataillon") String bataillon);
    @Query("SELECT m.bataillon.nom, COUNT(m) FROM Militaire m GROUP BY m.bataillon.nom")
    List<Object[]> countTotalByBataillon();

    @Query("SELECT m.bataillon.nom, COUNT(m) FROM Militaire m WHERE m.bataillon.nom = :bataillon GROUP BY m.bataillon.nom")
    List<Object[]> countTotalByBataillon(@Param("bataillon") String bataillon);
    @Query("SELECT new com.gestionacademie.dto.MilitaireFamilySituationCountDTO(m.situationFamiliale, m.grade.categorie, COUNT(m)) " +
            "FROM Militaire m GROUP BY m.situationFamiliale, m.grade.categorie")
    List<MilitaireFamilySituationCountDTO> countByFamilySituation();

    @Query("SELECT new com.gestionacademie.dto.MilitaireFamilySituationCountDTO2(m.situationFamiliale, m.grade.categorie, m.bataillon.nom, COUNT(m)) " +
            "FROM Militaire m WHERE m.bataillon.nom = :bataillon GROUP BY m.situationFamiliale, m.grade.categorie")
    List<MilitaireFamilySituationCountDTO2> countByFamilySituation(@Param("bataillon") String bataillon);

    @Query(value = "SELECT " +
            "CASE " +
            "WHEN TIMESTAMPDIFF(YEAR, m.date_naissance, CURRENT_DATE) < 19 THEN '< 19 ' " +
            "WHEN TIMESTAMPDIFF(YEAR, m.date_naissance, CURRENT_DATE) BETWEEN 19 AND 29 THEN '19-29' " +
            "WHEN TIMESTAMPDIFF(YEAR, m.date_naissance, CURRENT_DATE) BETWEEN 30 AND 39 THEN '30-39' " +
            "WHEN TIMESTAMPDIFF(YEAR, m.date_naissance, CURRENT_DATE) BETWEEN 40 AND 49 THEN '40-49' " +
            "WHEN TIMESTAMPDIFF(YEAR, m.date_naissance, CURRENT_DATE) BETWEEN 50 AND 59 THEN '50-59' " +
            "ELSE '60+' " +
            "END AS ageGroup, " +
            "g.categorie AS gradeCategorie, " +
            "COUNT(m.id) AS nombreMilitaires " +
            "FROM Militaire m " +
            "JOIN Grade g ON m.grade_id = g.id " +
            "GROUP BY ageGroup, g.categorie", nativeQuery = true)
    List<Object[]> findMilitairesByAgeGroupAndGrade();

    @Query(value = "SELECT " +
            "b.nom AS bataillonName, " +  // Sélectionner le nom du bataillon
            "CASE " +
            "WHEN TIMESTAMPDIFF(YEAR, m.date_naissance, CURRENT_DATE) < 19 THEN '< 19 ' " +
            "WHEN TIMESTAMPDIFF(YEAR, m.date_naissance, CURRENT_DATE) BETWEEN 19 AND 29 THEN '19-29' " +
            "WHEN TIMESTAMPDIFF(YEAR, m.date_naissance, CURRENT_DATE) BETWEEN 30 AND 39 THEN '30-39' " +
            "WHEN TIMESTAMPDIFF(YEAR, m.date_naissance, CURRENT_DATE) BETWEEN 40 AND 49 THEN '40-49' " +
            "WHEN TIMESTAMPDIFF(YEAR, m.date_naissance, CURRENT_DATE) BETWEEN 50 AND 59 THEN '50-59' " +
            "ELSE '60+' " +
            "END AS ageGroup, " +
            "g.categorie AS gradeCategorie, " +
            "COUNT(m.id) AS nombreMilitaires " +
            "FROM Militaire m " +
            "JOIN Grade g ON m.grade_id = g.id " +
            "JOIN Bataillon b ON m.bataillon_id = b.id " + // Ajouter la jointure pour Bataillon
            "WHERE b.nom = :bataillon " + // Ajouter la condition pour Bataillon
            "GROUP BY ageGroup, g.categorie", nativeQuery = true)
    List<Object[]> findMilitairesByAgeGroupAndGrade(@Param("bataillon") String bataillon);
    boolean existsByMatricule(String matricule);



    // Method to search by matricule
    Page<Militaire> findByMatriculeContaining(String matricule, Pageable pageable);

    Page<Militaire> findByMatriculeContainingIgnoreCase(String matricule, Pageable pageable);

    @Query("SELECT g.categorie, m.diplome, COUNT(m) FROM Militaire m  JOIN m.grade g  GROUP BY g.categorie, m.diplome")
    List<Object[]> countDiplomesByGradeCategoryAndDiplome();

    @Query("SELECT g.categorie, m.diplome, COUNT(m) " +
            "FROM Militaire m " +
            "JOIN m.grade g " +
            "JOIN m.bataillon b " +  // Ajout de la jointure avec Bataillon
            "WHERE b.nom = :bataillon " +  // Condition sur le nom du bataillon
            "GROUP BY g.categorie, m.diplome")
    List<Object[]> countDiplomesByGradeCategoryAndDiplome(@Param("bataillon") String bataillon);
    //@Query("SELECT m FROM Militaire m WHERE m.bataillon.nom = :bataillon AND (LOWER(m.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(m.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    @Query("SELECT m FROM Militaire m WHERE m.bataillon.nom = :bataillon " +
            "AND (" +
            "(LOWER(m.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(m.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "OR (CONCAT(LOWER(m.nom), ' ', LOWER(m.prenom)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "OR (CONCAT(LOWER(m.prenom), ' ', LOWER(m.nom)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +  // Check for "prenom nom" order
            ")")
    Page<Militaire> findByNomAndPrenomContainingAndBataillon_Nom(@Param("searchTerm") String searchTerm, @Param("bataillon") String bataillon, Pageable pageable);



    @Query("SELECT m FROM Militaire m WHERE " +
            " (LOWER(m.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(m.prenom) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "OR (CONCAT(LOWER(m.nom), ' ', LOWER(m.prenom)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
            "OR (CONCAT(LOWER(m.prenom), ' ', LOWER(m.nom)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) ")
    Page<Militaire> findByNomAndPrenomContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    @Query("SELECT m FROM Militaire m WHERE m.bataillon.nom = :bataillon AND LOWER(m.matricule) LIKE LOWER(CONCAT('%', :matricule, '%'))")
    Page<Militaire> findByMatriculeContainingAndBataillon_Nom(@Param("matricule") String matricule,@Param("bataillon") String bataillon ,Pageable pageable);
    Page<Militaire> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseAndBataillon_Nom(String nom, String prenom, String bataillon, Pageable pageable);

    Page<Militaire> findByBataillon_Nom(String bataillon, Pageable pageable);

    //Page<Militaire> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom, Pageable pageable);
    //Page<Militaire> findByMatriculeContainingAndBataillon_Nom(String matricule, String bataillon, Pageable pageable);
}
