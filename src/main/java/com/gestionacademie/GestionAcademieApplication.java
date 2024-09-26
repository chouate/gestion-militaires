package com.gestionacademie;

import com.gestionacademie.repositories.BataillonRepository;
import com.gestionacademie.repositories.CompanieRepository;
import com.gestionacademie.repositories.GradeRepository;
import com.gestionacademie.repositories.MilitaireRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GestionAcademieApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionAcademieApplication.class, args);
	}


	@Bean
	CommandLineRunner commandLineRunner(CompanieRepository companieRepository,
										BataillonRepository bataillonRepository,
										GradeRepository gradeRepository,
										MilitaireRepository militaireRepository
	){
		return  args -> {


//			List<String > listBataillon = Arrays.asList("B.C.S","1B.L.I.R","2B.L.I.R","B.H.M");
//			for (String nom : listBataillon){
//				Bataillon bataillon = new Bataillon();
//				bataillon.setNom(nom);
//				bataillonRepository.save(bataillon);
//			}
//			List<String > listNomCompanie = Arrays.asList("CCS","1CIR","2CIR","3CIR");
//			for (String nom : listNomCompanie){
//				Companie companie = new Companie();
//				companie.setNom(nom);
//				companieRepository.save(companie);
//			}
//			List<String> listOfficier = Arrays.asList("CM","col","Ltcol","cmdt","CNE","LT","S/LT");
//			List<String> listODR = Arrays.asList("A/C","Adj","S/C","SGT" );
//			List<String> listMDR = Arrays.asList("C/c","Cal","2cl","1cl");
//
//			for(String nom : listOfficier){
//				Grade grade = new Grade();
//				grade.setNom(nom);
//				grade.setCategorie("Officier");
//				gradeRepository.save(grade);
//			}
//			for(String nom : listODR){
//				Grade grade = new Grade();
//				grade.setNom(nom);
//				grade.setCategorie("ODR");
//				gradeRepository.save(grade);
//			}
//			for(String nom : listMDR){
//				Grade grade = new Grade();
//				grade.setNom(nom);
//				grade.setCategorie("MDR");
//				gradeRepository.save(grade);
//			}


		};
	}

}
