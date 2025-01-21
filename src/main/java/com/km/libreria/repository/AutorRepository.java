package com.km.libreria.repository;

import com.km.libreria.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    // Métodos personalizados si los necesitas
    Optional<Autor> findByNombreAutor(String nombreAutor);
    @Query("SELECT a FROM Autor a WHERE " +
            "(a.fechaMuerte IS NULL OR a.fechaMuerte >= :fechaDe) AND " +
            "(a.fechaNacimiento <= :fechaDe) AND " +
            "(a.fechaMuerte IS NOT NULL OR a.fechaNacimiento IS NOT NULL)")
    List<Autor> findAutoresVivosPorFechaDe(@Param("fechaDe") int fechaDe);
}
