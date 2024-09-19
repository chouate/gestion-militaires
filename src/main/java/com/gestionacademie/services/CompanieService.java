package com.gestionacademie.services;

import com.gestionacademie.entities.Companie;
import com.gestionacademie.repositories.CompanieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanieService {
    @Autowired
    private CompanieRepository companieRepository;

    public List<Companie> getAllCompanies() {
        return companieRepository.findAll();
    }
}
