package com.gestionacademie.services;


import com.gestionacademie.dto.*;
import com.gestionacademie.entities.Militaire;
import com.gestionacademie.exceptions.FileTooLargeException;
import com.gestionacademie.exceptions.MatriculeAlreadyExistsException;
import com.gestionacademie.repositories.MilitaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MilitaireService {
    private static final Long MAX_ALLOWED_SIZE = (long) (1024 * 1024);
    @Autowired
    private MilitaireRepository militaireRepository;


    public Militaire findById(Long id) {
        return militaireRepository.findById(id).orElse(null);
    }

    public Militaire saveMilitaire(Militaire militaire) {
        if (militaireRepository.existsByMatricule(militaire.getMatricule())) {
            throw new MatriculeAlreadyExistsException("Un militaire avec le matricule " + militaire.getMatricule() + " existe déjà.");
        }
        return militaireRepository.save(militaire);
    }

    public void updateMilitaire(Long id, Militaire updatedMilitaire) {

        Militaire existingMilitaire = militaireRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid militaire Id:" + id));

        // Vérifier si le matricule a été modifié
        if (!existingMilitaire.getMatricule().equals(updatedMilitaire.getMatricule())) {
            // Si le matricule a changé, vérifier s'il existe déjà
            if (militaireRepository.existsByMatricule(updatedMilitaire.getMatricule())) {
                throw new MatriculeAlreadyExistsException("Un militaire avec le matricule " + updatedMilitaire.getMatricule() + " existe déjà.");
            }
        }
        existingMilitaire.setNom(updatedMilitaire.getNom());
        existingMilitaire.setPrenom(updatedMilitaire.getPrenom());
        existingMilitaire.setDateNaissance(updatedMilitaire.getDateNaissance());
        existingMilitaire.setTelephone(updatedMilitaire.getTelephone());
        existingMilitaire.setFonction(updatedMilitaire.getFonction());
        existingMilitaire.setDiplome(updatedMilitaire.getDiplome());
        existingMilitaire.setAdresse(updatedMilitaire.getAdresse());
        existingMilitaire.setBMCA(updatedMilitaire.getBMCA());
        existingMilitaire.setSituationFamiliale(updatedMilitaire.getSituationFamiliale());
        existingMilitaire.setMatricule(updatedMilitaire.getMatricule());
        existingMilitaire.setLD(updatedMilitaire.getLD());
        existingMilitaire.setPTC(updatedMilitaire.getPTC());
        existingMilitaire.setStages(updatedMilitaire.getStages());
        existingMilitaire.setGrade(updatedMilitaire.getGrade());
        existingMilitaire.setCompanie(updatedMilitaire.getCompanie());
        existingMilitaire.setBataillon(updatedMilitaire.getBataillon());
        existingMilitaire.setImagePath(updatedMilitaire.getImagePath());
        militaireRepository.save(existingMilitaire);
    }

    public Militaire getMilitaireById(Long id) {
        return militaireRepository.findById(id).orElse(null);
    }


    public void deleteMilitaire(Long id) {
        militaireRepository.deleteById(id);
    }
    public Page<Militaire> findAll(Pageable pageable){return militaireRepository.findAll(pageable);}

    public Page<Militaire> getMilitairesByBataillon(Pageable pageable,String bataillon){
        return militaireRepository.getMilitairesByBataillon(pageable,bataillon);
    }


    public List<MilitaireCountByBataillonDTO> getMilitaireCountByBMCAAndBataillon() {
        return militaireRepository.countByBMCAAndBataillon();
    }
    public List<MilitaireCountByBataillonDTO> getMilitaireCountByBMCAAndBataillon(String bmca) {
        return militaireRepository.countByBMCAAndBataillon(bmca);
    }

    // Méthode pour récupérer les comptes par catégorie de grade et bataillon
    public List<MilitaireGradeCountDTO> getMilitaireCountByGradeCategoryAndBataillon() {
        return militaireRepository.countByGradeCategoryAndBataillon();
    }
    public List<MilitaireGradeCountDTO> getMilitaireCountByGradeCategoryAndBataillon(String bataillon) {
        return militaireRepository.countByGradeCategoryAndBataillon(bataillon);
    }

    public List<Object[]> getTotalMilitaireByBataillon() {
        return militaireRepository.countTotalByBataillon();
    }
    public List<Object[]> getTotalMilitaireByBataillon(String bataillon) {
        return militaireRepository.countTotalByBataillon(bataillon);
    }

    public List<MilitaireFamilySituationCountDTO> getMilitaireCountByFamilySituation() {
        return militaireRepository.countByFamilySituation();
    }
    public List<MilitaireFamilySituationCountDTO2> getMilitaireCountByFamilySituation(String bataillon) {
        return militaireRepository.countByFamilySituation(bataillon);
    }

    public List<AgeGroupGradeDTO> getMilitaireCountByAgeGroupAndGrade() {
        List<Object[]> results = militaireRepository.findMilitairesByAgeGroupAndGrade();
        List<AgeGroupGradeDTO> dtos = new ArrayList<>();
        for (Object[] result : results) {
            String ageGroup = (String) result[0];
            String gradeCategorie = (String) result[1];
            Long nombreMilitaires = ((Number) result[2]).longValue();
            dtos.add(new AgeGroupGradeDTO(ageGroup, gradeCategorie, nombreMilitaires));
        }
        return dtos;
    }

    public List<AgeGroupGradeDTO2> getMilitaireCountByAgeGroupAndGrade(String bataillon) {
        List<Object[]> results = militaireRepository.findMilitairesByAgeGroupAndGrade(bataillon);
        List<AgeGroupGradeDTO2> dtos = new ArrayList<>();
        for (Object[] result : results) {
            String bataillonName = (String) result[0];
            String ageGroup = (String) result[1];
            String gradeCategorie = (String) result[2];
            Long nombreMilitaires = ((Number) result[3]).longValue();
            dtos.add(new AgeGroupGradeDTO2(bataillonName,ageGroup, gradeCategorie, nombreMilitaires));
        }
        return dtos;
    }

    public Map<String, Map<String, Integer>> getDiplomesCountByGradeCategory() {
        List<Object[]> results = militaireRepository.countDiplomesByGradeCategoryAndDiplome();
        Map<String, Map<String, Integer>> diplomeCountByCategory = new HashMap<>();

        for (Object[] result : results) {
            String categorie = (String) result[0];
            String diplome = (String) result[1];
            Long count = (Long) result[2];

            diplomeCountByCategory.putIfAbsent(categorie, new HashMap<>());
            diplomeCountByCategory.get(categorie).put(diplome, count.intValue());
        }

        return diplomeCountByCategory;
    }
//    public Map<String, Map<String, Integer>> getDiplomesCountByGradeCategory(String bataillon) {
//        List<Object[]> results = militaireRepository.countDiplomesByGradeCategoryAndDiplome(bataillon);
//        Map< String, Map<String, Integer>> diplomeCountByCategory = new HashMap<>();
//
//        for (Object[] result : results) {
//            String bataillonName = bataillon;
//            String categorie = (String) result[0];
//            String diplome = (String) result[1];
//            Long count = (Long) result[2];
//
//            diplomeCountByCategory.putIfAbsent( categorie, new HashMap<>());
//            diplomeCountByCategory.get(categorie).put(diplome, count.intValue());
//        }
//
//        return diplomeCountByCategory;
//    }
    public Map<String, Map<String, Map<String, Integer>>> getDiplomesCountByGradeCategory(String bataillon) {
        List<Object[]> results = militaireRepository.countDiplomesByGradeCategoryAndDiplome(bataillon);
        Map<String, Map<String, Map<String, Integer>>> diplomeCountByCategory = new HashMap<>();

        for (Object[] result : results) {
            String bataillonName = bataillon;  // Vous pouvez obtenir le nom du bataillon si nécessaire
            String categorie = (String) result[0];
            String diplome = (String) result[1];
            Long count = (Long) result[2];

            // Vérifier si le bataillon existe déjà
            diplomeCountByCategory.putIfAbsent(bataillonName, new HashMap<>());

            // Accéder au map interne (catégorie -> diplome)
            Map<String, Map<String, Integer>> categoryMap = diplomeCountByCategory.get(bataillonName);
            categoryMap.putIfAbsent(categorie, new HashMap<>());

            // Ajouter le diplome et son count
            categoryMap.get(categorie).put(diplome, count.intValue());
        }

        return diplomeCountByCategory;
    }

    public Page<Militaire> searchMilitaires(String search,String bataillon, Pageable pageable) {
        // Supprimer les espaces inutiles au début et à la fin de la chaîne de recherche
        search = search.trim();
        // Allow matricule with digits and '/' (forward slash)
        if (search.matches("[\\d/]+")) { // Matching digits and forward slashes
            System.out.println("la fonction de 2 approches de recherche POUR le matricule  ");
            return militaireRepository.findByMatriculeContainingAndBataillon_Nom(search, bataillon, pageable);
        } else {
            System.out.println("la fonction searchMilitaires de 2 approches dans le service POUR le NOM ");
            return militaireRepository.findByNomAndPrenomContainingAndBataillon_Nom(search, bataillon, pageable);
        }
    }
    public Page<Militaire> searchMilitaires(String search, Pageable pageable) {
        // Check if the search string is a valid matricule number pattern
        if (search.matches("\\d*/?\\d*")) { // Assuming matricule numbers are all digits
            System.out.println("la fonction de 2 approches de recherche POUR le matricule  ");
            return militaireRepository.findByMatriculeContaining(search, pageable);
        } else {
            System.out.println("la fonction searchMilitaires de 2 approches dans le service POUR le NOM ");
            return militaireRepository.findByNomAndPrenomContaining(search, pageable);
        }
    }



    public Page<Militaire> searchMilitairesByMatricule(String matricule, Pageable pageable) {
        return militaireRepository.findByMatriculeContainingIgnoreCase(matricule, pageable);
    }
    public Page<Militaire> searchMilitairesByMatriculeAndBataillon(String matricule, String bataillon, Pageable pageable) {
        return militaireRepository.findByMatriculeContainingAndBataillon_Nom(matricule, bataillon, pageable);
    }

    public Page<Militaire> searchMilitairesByNameAndBataillon(String search, String bataillon, Pageable pageable) {
        return militaireRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseAndBataillon_Nom(search, search, bataillon, pageable);
    }

    public Page<Militaire> findAllByBataillon(String bataillon, Pageable pageable) {
        return militaireRepository.findByBataillon_Nom(bataillon, pageable);
    }

    //**********************************************************************
    public List<MilitaireCountDTO> getMilitaireCountByBMCA() {
        return militaireRepository.countByBMCA();
    }
}
