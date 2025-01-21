package com.km.libreria.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ConvierteDatos implements IConvierteDatos {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            objectMapper.findAndRegisterModules(); // Asegura que todos los módulos de Jackson estén registrados
            return objectMapper.readValue(json, clase); // Convierte el JSON a la clase proporcionada
        } catch (IOException e) {
            throw new RuntimeException("Error al convertir JSON a " + clase.getSimpleName(), e);
        }
    }
}
