package com.km.libreria.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.km.libreria.dto.AutorDTO;
import com.km.libreria.dto.LibroDTO;
import com.km.libreria.models.Autor;
import com.km.libreria.models.Libro;
import com.km.libreria.repository.AutorRepository;
import com.km.libreria.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class LibroService {
    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private ConsumoAPI consumoAPI;

    @Autowired
    private IConvierteDatos convierteDatos;

    // Método para buscar libros por título
    public List<LibroDTO> buscarLibrosPorTitulo(String titulo) {
        try {
            String tituloCodificado = URLEncoder.encode(titulo, StandardCharsets.UTF_8.toString());
            String jsonResponse = consumoAPI.obtenerDatos("https://gutendex.com/books/?search=" + tituloCodificado);

            // Procesamiento del JSON
            JsonNode rootNode = convierteDatos.obtenerDatos(jsonResponse, JsonNode.class);
            JsonNode resultsNode = rootNode.get("results");

            List<LibroDTO> libros = new ArrayList<>();

            if (resultsNode.isArray()) {
                for (JsonNode libroNode : resultsNode) {
                    String tituloLibro = libroNode.get("title").asText();
                    String idioma = libroNode.get("languages").get(0).asText();
                    Double numeroDescargas = libroNode.get("download_count").asDouble();

                    JsonNode autorNode = libroNode.get("authors").get(0);
                    String nombreAutor = autorNode.get("name").asText();
                    Integer fechaNacimiento = autorNode.get("birth_year").isNull() ? null : autorNode.get("birth_year").asInt();
                    Integer fechaMuerte = autorNode.get("death_year").isNull() ? null : autorNode.get("death_year").asInt();

                    AutorDTO autorDTO = new AutorDTO(nombreAutor, fechaNacimiento, fechaMuerte);
                    libros.add(new LibroDTO(tituloLibro, autorDTO,idioma ,numeroDescargas ));
                }
            }

            return libros;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar libros en la API: " + e.getMessage());
        }
    }
    // Convertir Libro a LibroDTO
    public List<LibroDTO> obtenerLibrosDTO(List<Libro> libros) {
        List<LibroDTO> libroDTOList = new ArrayList<>();

        for (Libro libro : libros) {
            // Convertir Autor a AutorDTO
            AutorDTO autorDTO = new AutorDTO(libro.getAutor().getNombreAutor(),
                    libro.getAutor().getFechaNacimiento(),
                    libro.getAutor().getFechaMuerte());

            // Crear el LibroDTO
            LibroDTO libroDTO = new LibroDTO(libro.getTitulo(), autorDTO, libro.getIdioma(), libro.getNumeroDescargas());

            // Agregarlo a la lista
            libroDTOList.add(libroDTO);
        }

        return libroDTOList;
    }

    //metodo para obtener todos los libros registrados
    public List<Libro> obtenerLibrosRegistrados() {
        return libroRepository.findAll();
    }

    //Método para obtener todos los autores registrados
    public List<AutorDTO> obtenerAutoresRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        List<AutorDTO> autoresDTO = new ArrayList<>();

        for (Libro libro : libros) {
            AutorDTO autorDTO = new AutorDTO(libro.getAutor().getNombreAutor(), libro.getAutor().getFechaNacimiento(), libro.getAutor().getFechaMuerte());
            autoresDTO.add(autorDTO);
        }
        return autoresDTO;
    }
    public List<Libro> obtenerLibrosPorAutor(String nombreAutor) {
        return libroRepository.findByAutorNombreAutor(nombreAutor);
    }

    // Método para obtener los autores vivos en un año específico
    public List<AutorDTO> obtenerAutoresVivosPorFechaDe(int fechaDe) {
        List<Autor> autores = autorRepository.findAutoresVivosPorFechaDe(fechaDe);
        List<AutorDTO> autoresVivos = new ArrayList<>();

        for (Autor autor : autores) {
            // Convertir Autor a AutorDTO
            AutorDTO autorDTO = new AutorDTO(autor.getNombreAutor(), autor.getFechaNacimiento(), autor.getFechaMuerte());
            autoresVivos.add(autorDTO);
        }

        return autoresVivos;
    }

    // Método para obtener libros por idioma
    public List<Libro> obtenerLibrosPorIdioma(String idioma) {
        return libroRepository.findByIdioma(idioma);
    }



    // Método para guardar libro en la base de datos
    public void guardarLibros(List<LibroDTO> librosDTO) {
        for (LibroDTO libroDTO : librosDTO) {
            // Convertir LibroDTO a Libro
            Libro libro = new Libro();
            libro.setTitulo(libroDTO.getTitulo());
            libro.setIdioma(libroDTO.getIdioma());
            libro.setNumeroDescargas(libroDTO.getNumeroDescargas());

            // Buscar o crear Autor
            Autor autor = autorRepository.findByNombreAutor(libroDTO.getAutor().getNombreAutor())
                    .orElseGet(() -> {
                        Autor nuevoAutor = new Autor();
                        nuevoAutor.setNombreAutor(libroDTO.getAutor().getNombreAutor());
                        nuevoAutor.setFechaNacimiento(libroDTO.getAutor().getFechaNacimiento());
                        nuevoAutor.setFechaMuerte(libroDTO.getAutor().getFechaMuerte() != null ? libroDTO.getAutor().getFechaMuerte() : null);
                        return autorRepository.save(nuevoAutor);
                    });

            libro.setAutor(autor);

            // Verificar si el libro ya existe para evitar duplicados
            if (!libroRepository.existsByTituloAndAutor(libro.getTitulo(), libro.getAutor())) {
                libroRepository.save(libro);
            }
        }
    }
}
