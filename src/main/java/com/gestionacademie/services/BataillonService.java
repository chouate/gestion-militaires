package com.gestionacademie.services;


import com.gestionacademie.entities.Bataillon;
import com.gestionacademie.repositories.BataillonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BataillonService {
    @Autowired
    private BataillonRepository bataillonRepository;

    public List<Bataillon> getAllBataillons() {
        return bataillonRepository.findAll();
    }
}
