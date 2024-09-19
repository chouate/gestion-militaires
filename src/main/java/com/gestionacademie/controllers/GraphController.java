package com.gestionacademie.controllers;

import com.gestionacademie.dto.*;
import com.gestionacademie.services.MilitaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/graphs")
public class GraphController {

    @Autowired
    private MilitaireService militaireService;

    @GetMapping
    public String showGraphs(Model model,
                             @RequestParam(name = "bataillon" ,required = false ) String bataillon) {
        System.out.println("bataillon : "+bataillon);

        if(bataillon.isEmpty()){
            List<MilitaireCountByBataillonDTO> militaireCounts = militaireService.getMilitaireCountByBMCAAndBataillon();
            List<MilitaireCountByBataillonDTO> militaireCounts2 = militaireService.getMilitaireCountByBMCAAndBataillon("B.H.M");
            List<MilitaireGradeCountDTO> gradeCategoryData = militaireService.getMilitaireCountByGradeCategoryAndBataillon();
            List<Object[]> totalMilitairesByBataillon = militaireService.getTotalMilitaireByBataillon();
            List<MilitaireFamilySituationCountDTO> familySituationData = militaireService.getMilitaireCountByFamilySituation();
            List<AgeGroupGradeDTO> ageGroupGradeData = militaireService.getMilitaireCountByAgeGroupAndGrade();
            Map<String, Map<String, Integer>> diplomeCountByCategory = militaireService.getDiplomesCountByGradeCategory();

            model.addAttribute("militaireCounts", militaireCounts);
            model.addAttribute("militaireCounts2", militaireCounts2);
            model.addAttribute("diplomeCountByCategory", diplomeCountByCategory);
            model.addAttribute("ageGroupGradeData", ageGroupGradeData);
            model.addAttribute("militaireGradeCategoryCounts", gradeCategoryData);
            model.addAttribute("totalMilitairesByBataillon", totalMilitairesByBataillon);
            model.addAttribute("familySituationData", familySituationData);

            return "graphsEtatMajor";
        }else {
            System.out.println("bataillon dans else : "+bataillon);
            List<MilitaireCountByBataillonDTO> militaireCounts2 = militaireService.getMilitaireCountByBMCAAndBataillon(bataillon);
            model.addAttribute("militaireCounts2", militaireCounts2);
            List<MilitaireGradeCountDTO> gradeCategoryData = militaireService.getMilitaireCountByGradeCategoryAndBataillon(bataillon);
            model.addAttribute("militaireGradeCategoryCounts", gradeCategoryData);
            List<Object[]> totalMilitairesByBataillon = militaireService.getTotalMilitaireByBataillon(bataillon);
            model.addAttribute("totalMilitairesByBataillon", totalMilitairesByBataillon);
            List<MilitaireFamilySituationCountDTO2> familySituationData = militaireService.getMilitaireCountByFamilySituation(bataillon);
            model.addAttribute("familySituationData", familySituationData);
            List<AgeGroupGradeDTO2> ageGroupGradeData = militaireService.getMilitaireCountByAgeGroupAndGrade(bataillon);
            model.addAttribute("ageGroupGradeData", ageGroupGradeData);
            Map<String,Map<String, Map<String, Integer>>> diplomeCountByCategory = militaireService.getDiplomesCountByGradeCategory(bataillon);
            model.addAttribute("diplomeCountByCategory", diplomeCountByCategory);

            return "graphsBataillon";
        }

    }
}
