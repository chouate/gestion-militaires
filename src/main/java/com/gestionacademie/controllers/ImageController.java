//package com.gestionacademie.controllers;
//
//import com.gestionacademie.entities.Militaire;
//import com.gestionacademie.services.MilitaireService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//@Controller
//@RequestMapping("/images")
//public class ImageController {
//
//    @Autowired
//    private MilitaireService militaireService;
//
//    @GetMapping("/{id}")
//    @ResponseBody
//    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
//        Militaire militaire = militaireService.getMilitaireById(id);
//        if (militaire != null && militaire.getImage() != null) {
//            return ResponseEntity.ok()
//                    .contentType(MediaType.IMAGE_JPEG) // Assurez-vous du type d'image
//                    .body(militaire.getImage());
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//}
//
