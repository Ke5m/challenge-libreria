package com.km.libreria.service;

import org.springframework.stereotype.Service;

@Service
public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
