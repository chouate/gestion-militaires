package com.gestionacademie.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public  String home(){
        return "redirect:/home";
    }
    @GetMapping("/home")
    public String getHome(){
        return "home";
    }

    @GetMapping("/etatMajor")
    public String getPageEtatMajor(){
        return "etatMajor";
    }

    @GetMapping("/uploadError")
    public String uploadError() {
        return "uploadError";  // Make sure you have an HTML template for this
    }


}
