package com.km.libreria.service;

import com.km.libreria.models.Autor;
import com.km.libreria.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> obtenerAutoresRegistrados() {
        return autorRepository.findAll();
    }

    // Otros métodos según necesidad
}
