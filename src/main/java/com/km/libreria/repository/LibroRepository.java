package com.km.libreria.repository;

import com.km.libreria.models.Autor;
import com.km.libreria.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByIdioma(String idioma);
    boolean existsByTituloAndAutor(String titulo, Autor autor);
    List<Libro> findByAutor(Autor autor);
    List<Libro> findByAutorNombreAutor(String nombreAutor);
}

