package com.gestionacademie.controllers;

import com.gestionacademie.entities.Militaire;


import com.gestionacademie.exceptions.FileTooLargeException;
import com.gestionacademie.exceptions.MatriculeAlreadyExistsException;
import com.gestionacademie.services.BataillonService;
import com.gestionacademie.services.CompanieService;
import com.gestionacademie.services.GradeService;
import com.gestionacademie.services.MilitaireService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/militaires")
public class MilitaireController {
    @Value("${upload.dir}")
    private String uploadDir;
    @Autowired
    private MilitaireService militaireService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private BataillonService bataillonService;
    @Autowired
    private CompanieService companieService;

    private List<String> listBMCA = Arrays.asList(" ","VL","PL","TC","SR","Moto");
    private List<String> listDiplomes = Arrays.asList("DOCTORAT","C.B.M","C.E.M","C.O.S","C.SUP.LOG","C.D.P","C.D.C","C.APP",
            "B.C.M","B.S","B.E","C.I.A","CAT 2","CAT 1","N.DIPLOMES");
    private List<String> listSituationFamiliale = Arrays.asList("Célébataire","Marié","Remarié","Divorcé");

//    @GetMapping
//    public String listMilitaires(Model model,
//                                 @RequestParam(name = "page", defaultValue = "0") int page,
//                                 @RequestParam(name = "size", defaultValue = "10") int size,
//                                 @RequestParam(name = "search", required = false) String search) {
//        Page<Militaire> pagesMilitaire;
//        if (search != null && !search.isEmpty()) {
//            pagesMilitaire = militaireService.searchMilitaires(search, PageRequest.of(page, size, Sort.by("nom").ascending()));
//        } else {
//            pagesMilitaire = militaireService.findAll(PageRequest.of(page, size, Sort.by("nom").ascending()));
//        }
//
//        model.addAttribute("totalPages", pagesMilitaire.getTotalPages());
//        model.addAttribute("pages", new int[pagesMilitaire.getTotalPages()]);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("militaires", pagesMilitaire.getContent());
//        model.addAttribute("totalMilitaires", pagesMilitaire.getTotalElements());
//        return "militaires";
//    }
@GetMapping
public String listMilitaires(Model model,
                             @RequestParam(name = "bataillon" ,required = false ) String bataillon,
                             @RequestParam(name = "page", defaultValue = "1") int page,
                             @RequestParam(name = "size", defaultValue = "20") int size,
                             @RequestParam(name = "search", required = false) String search,
                             @RequestParam(name = "searchMatricule", required = false) String searchMatricule) {
    Page<Militaire> pagesMilitaire;
    // Subtract 1 from the page index to convert it to 0-based for PageRequest
    int pageIndex = page - 1;

    Pageable pageableSearching = PageRequest.of(pageIndex, 1000, Sort.by("grade.id").ascending());
    Pageable pageable = PageRequest.of(pageIndex, size, Sort.by("grade.id").ascending());
    // Check if searching by matricule
    System.out.println("searchMatricule : "+searchMatricule);
    System.out.println("search : "+search);
    System.out.println("bataillon avant la condition : "+bataillon);

    if (search != null && !search.isEmpty()) {
        if (bataillon != null && !bataillon.isEmpty()) {
            System.out.println("bataillon apres la condition dans le controller: "+bataillon);
            System.out.println("la recherche avec  searchMilitaires dans le controller premier condition");
            pagesMilitaire = militaireService.searchMilitaires(search, bataillon, pageableSearching);
        } else {
            System.out.println("la recherche avec  searchMilitaires dans le controller deuxieme condition");
            pagesMilitaire = militaireService.searchMilitaires(search, pageableSearching);
        }
    }
    // Default case: retrieve all military personnel, possibly filtered by bataillon
    else {
        if (bataillon != null && !bataillon.isEmpty()) {
            System.out.println("sans recherche pour une bataillon ");
            pagesMilitaire = militaireService.getMilitairesByBataillon(pageable, bataillon);
        } else {
            System.out.println("sans recherche pour l'etat major ");
            pagesMilitaire = militaireService.findAll(pageable);
            System.out.println("pagesMilitaire : "+pagesMilitaire.getTotalPages());
        }
    }

    // Initialize pages array starting from 1 instead of 0
//    int totalPages = pagesMilitaire.getTotalPages();
//    int[] pages = new int[totalPages];
//    for (int i = 0; i < totalPages; i++) {
//        pages[i] = i + 1;  // Start from 1 instead of 0
//    }

    model.addAttribute("bataillon",bataillon);
    model.addAttribute("totalPages", pagesMilitaire.getTotalPages());
    model.addAttribute("pages", new int[pagesMilitaire.getTotalPages()]);
    model.addAttribute("currentPage", page);
    model.addAttribute("militaires", pagesMilitaire.getContent());
    model.addAttribute("totalMilitaires", pagesMilitaire.getTotalElements());
    return "militaires";
}



    @GetMapping("/new")
    public String showNewMilitaireForm(Model model,@RequestParam(value = "page",defaultValue = "1") int page) {
        model.addAttribute("militaire", new Militaire());
        model.addAttribute("grades",gradeService.getAllGrades());
        model.addAttribute("bataillons",bataillonService.getAllBataillons());
        model.addAttribute("companies",companieService.getAllCompanies());
        model.addAttribute("listBMCA",listBMCA);
        model.addAttribute("listSituationFamiliale",listSituationFamiliale);
        // Ajouter les listes des diplômes par catégorie
        model.addAttribute("diplomesOfficier", Arrays.asList("DOCTORAT", "C.B.M", "C.E.M", "C.O.S", "C.SUP.LOG", "C.D.P", "C.D.C", "C.APP", "B.C.M"));
        model.addAttribute("diplomesODR", Arrays.asList("B.C.M", "B.S", "B.E", "C.I.A"));
        model.addAttribute("diplomesMDR", Arrays.asList("C.I.A", "CAT 2", "CAT 1", "N.DIPLOMES"));
        model.addAttribute("page",page);
        System.out.println("the page is :"+page);
        return "new_militaire";
    }

    @PostMapping
    public String saveMilitaire(@Valid @ModelAttribute Militaire militaire,
                                BindingResult bindingResult,Model model,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam("photo") MultipartFile file) {


        if (!file.isEmpty()) {
            try {

                // Définir le chemin où vous souhaitez enregistrer l'image
                //String uploadDir = new File("src/main/resources/static/uploads/").getAbsolutePath() + File.separator;

                String originalFilename = file.getOriginalFilename();
                String filePath = uploadDir + originalFilename;

                // Créer le dossier s'il n'existe pas
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                // Enregistrer l'image sur le disque
                File destinationFile = new File(filePath);

                file.transferTo(destinationFile);

                System.out.println( "originalFilename"+originalFilename);
                System.out.println("filePath"+filePath);
                // Enregistrer le chemin dans l'entité
                militaire.setImagePath(originalFilename);
            } catch (IOException e) {
                e.printStackTrace();
//                bindingResult.rejectValue("photo", "error.photo", "Failed to upload photo.");
                System.out.println("Failed to upload photo.");
            }
        }
        if(bindingResult.hasErrors()){
            model.addAttribute("grades",gradeService.getAllGrades());
            model.addAttribute("bataillons",bataillonService.getAllBataillons());
            model.addAttribute("companies",companieService.getAllCompanies());
            model.addAttribute("listBMCA",listBMCA);
            model.addAttribute("listSituationFamiliale",listSituationFamiliale);
            model.addAttribute("listDiplomes",listDiplomes);
            model.addAttribute("page",page);
            return "new_militaire";
        }
        try {
            militaireService.saveMilitaire(militaire);
        } catch (MatriculeAlreadyExistsException e) {
            bindingResult.rejectValue("matricule", "error.matricule", e.getMessage());
            model.addAttribute("grades",gradeService.getAllGrades());
            model.addAttribute("bataillons",bataillonService.getAllBataillons());
            model.addAttribute("companies",companieService.getAllCompanies());
            model.addAttribute("listBMCA",listBMCA);
            model.addAttribute("listSituationFamiliale",listSituationFamiliale);
            model.addAttribute("listDiplomes",listDiplomes);
            model.addAttribute("page",page);
            return "new_militaire";
        }
        return "redirect:/militaires";
    }


    @GetMapping("/edit/{id}")
    public String editMilitaireForm(@PathVariable Long id, Model model,int page) {
        Militaire militaire = militaireService.getMilitaireById(id);
        model.addAttribute("currentPage",page);
        model.addAttribute("militaire", militaire);
        model.addAttribute("grades",gradeService.getAllGrades());
        model.addAttribute("bataillons",bataillonService.getAllBataillons());
        model.addAttribute("companies",companieService.getAllCompanies());
        model.addAttribute("listBMCA",listBMCA);
        model.addAttribute("listSituationFamiliale",listSituationFamiliale);
        model.addAttribute("listDiplomes",listDiplomes);
        return "edit_militaire_view";
    }
    @PostMapping("/update/{id}")
    public String updateMilitaire(@PathVariable Long id, @Valid @ModelAttribute Militaire militaire,
                                  BindingResult bindingResult,
                                  Model model,
                                  @RequestParam(defaultValue = "1") int page,
                                  @RequestParam("photo") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                // Définir le chemin où vous souhaitez enregistrer l'image
                //String uploadDir = new File("src/main/resources/static/uploads/").getAbsolutePath() + File.separator;

                String originalFilename = file.getOriginalFilename();
                String filePath = uploadDir + originalFilename;

                // Créer le dossier s'il n'existe pas
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Enregistrer l'image sur le dossier
                File destinationFile = new File(filePath);
                file.transferTo(destinationFile);
                // Check if the file exists before redirection
                while (!destinationFile.exists()) {
                    Thread.sleep(10000); // Pause for a moment to allow the file to be written
                }
                // Enregistrer le chemin dans l'entité
                militaire.setImagePath(originalFilename);
            } catch (IOException e) {
                e.printStackTrace();
                bindingResult.rejectValue("photo", "error.photo", "Failed to upload photo.");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("currentPage",page);
            model.addAttribute("grades",gradeService.getAllGrades());
            model.addAttribute("bataillons",bataillonService.getAllBataillons());
            model.addAttribute("companies",companieService.getAllCompanies());
            model.addAttribute("listBMCA",listBMCA);
            model.addAttribute("listSituationFamiliale",listSituationFamiliale);
            model.addAttribute("listDiplomes",listDiplomes);
            return "edit_militaire_view";
        }

        try {
            militaireService.updateMilitaire(id, militaire);
        } catch (MatriculeAlreadyExistsException e) {
            model.addAttribute("currentPage",page);
            model.addAttribute("grades",gradeService.getAllGrades());
            model.addAttribute("bataillons",bataillonService.getAllBataillons());
            model.addAttribute("companies",companieService.getAllCompanies());
            model.addAttribute("listBMCA",listBMCA);
            model.addAttribute("listSituationFamiliale",listSituationFamiliale);
            model.addAttribute("listDiplomes",listDiplomes);

            bindingResult.rejectValue("matricule", "error.matricule", e.getMessage());
            return "edit_militaire_view";
        }
        model.addAttribute("currentPage",page);
        return "redirect:/militaires?page="+page;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteMilitaire(@PathVariable Long id) {
        militaireService.deleteMilitaire(id);
        return "redirect:/militaires";
    }
    @GetMapping("/print/{id}")
    public String printMilitaire(@PathVariable Long id, Model model) {
        Militaire militaire = militaireService.getMilitaireById(id);
        model.addAttribute("militaire", militaire);
        return "print_militaire_view";
    }

    @GetMapping("/{id}/fiche-conge/form")
    public String showLeaveForm(@PathVariable Long id, Model model) {
        Militaire militaire = militaireService.findById(id);
        if (militaire == null) {
            return "redirect:/error";
        }
        model.addAttribute("militaire", militaire);
        return "leave_form";
    }
    @PostMapping("/{id}/fiche-conge")
    public String submitLeaveForm(
            @PathVariable Long id,
            @RequestParam("valableDe") String valableDe,
            @RequestParam("valableA") String valableA,
            @RequestParam("seRendreDe") String seRendreDe,
            @RequestParam("seRendreA") String seRendreA,
            Model model) {

        Militaire militaire = militaireService.findById(id);
        if (militaire == null) {
            return "redirect:/error";
        }

        // Calculate leave duration
        LocalDate startDate = LocalDate.parse(valableDe);
        LocalDate endDate = LocalDate.parse(valableA);
        long duration = ChronoUnit.DAYS.between(startDate, endDate);

        model.addAttribute("militaire", militaire);
        model.addAttribute("valableDe", valableDe);
        model.addAttribute("valableA", valableA);
        model.addAttribute("seRendreDe", seRendreDe);
        model.addAttribute("seRendreA", seRendreA);
        model.addAttribute("duree", duration);
        model.addAttribute("localDate",LocalDate.now());

        return "fiche_conge_view";
    }

    @GetMapping("/{id}")
    public String viewMilitaire(@PathVariable Long id, Model model) {
        Militaire militaire = militaireService.getMilitaireById(id);
        model.addAttribute("militaire", militaire);
        return "view_militaire";
    }

    //    public String saveMilitaire(@Valid @ModelAttribute Militaire militaire, BindingResult bindingResult,Model model,@RequestParam(defaultValue = "0") int page,@RequestParam("photo") MultipartFile file) {
//        if (!file.isEmpty()) {
//            try {
//                militaire.setImage(file.getBytes());
//            } catch (IOException e) {
//                e.printStackTrace();
//                // Handle file processing exception
//                bindingResult.rejectValue("photo", "error.photo", "Failed to upload photo.");
//            }
//        }
//        if(bindingResult.hasErrors()){
//            model.addAttribute("grades",gradeService.getAllGrades());
//            model.addAttribute("bataillons",bataillonService.getAllBataillons());
//            model.addAttribute("companies",companieService.getAllCompanies());
//            model.addAttribute("listBMCA",listBMCA);
//            model.addAttribute("listSituationFamiliale",listSituationFamiliale);
//            model.addAttribute("listDiplomes",listDiplomes);
//            model.addAttribute("page",page);
//            return "new_militaire";
//        }
//        try {
//            militaireService.saveMilitaire(militaire);
//        } catch (MatriculeAlreadyExistsException e) {
//            bindingResult.rejectValue("matricule", "error.matricule", e.getMessage());
//            model.addAttribute("grades",gradeService.getAllGrades());
//            model.addAttribute("bataillons",bataillonService.getAllBataillons());
//            model.addAttribute("companies",companieService.getAllCompanies());
//            model.addAttribute("listBMCA",listBMCA);
//            model.addAttribute("listSituationFamiliale",listSituationFamiliale);
//            model.addAttribute("listDiplomes",listDiplomes);
//            model.addAttribute("page",page);
//            return "new_militaire";
//        }
//        return "redirect:/militaires";
//    }

    //    public String saveMilitaire(@Valid @ModelAttribute Militaire militaire, BindingResult bindingResult,Model model,@RequestParam(defaultValue = "0") int page,@RequestParam("photo") MultipartFile file) {
//        if (!file.isEmpty()) {
//            try {
//                militaire.setImage(file.getBytes());
//            } catch (IOException e) {
//                e.printStackTrace();
//                // Handle file processing exception
//                bindingResult.rejectValue("photo", "error.photo", "Failed to upload photo.");
//            }
//        }
//        if(bindingResult.hasErrors()){
//            model.addAttribute("grades",gradeService.getAllGrades());
//            model.addAttribute("bataillons",bataillonService.getAllBataillons());
//            model.addAttribute("companies",companieService.getAllCompanies());
//            model.addAttribute("listBMCA",listBMCA);
//            model.addAttribute("listSituationFamiliale",listSituationFamiliale);
//            model.addAttribute("listDiplomes",listDiplomes);
//            model.addAttribute("page",page);
//            return "new_militaire";
//        }
//        try {
//            militaireService.saveMilitaire(militaire);
//        } catch (MatriculeAlreadyExistsException e) {
//            bindingResult.rejectValue("matricule", "error.matricule", e.getMessage());
//            model.addAttribute("grades",gradeService.getAllGrades());
//            model.addAttribute("bataillons",bataillonService.getAllBataillons());
//            model.addAttribute("companies",companieService.getAllCompanies());
//            model.addAttribute("listBMCA",listBMCA);
//            model.addAttribute("listSituationFamiliale",listSituationFamiliale);
//            model.addAttribute("listDiplomes",listDiplomes);
//            model.addAttribute("page",page);
//            return "new_militaire";
//        }
//        return "redirect:/militaires";
//    }

}
