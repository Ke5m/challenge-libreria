package com.km.libreria.principal;

import com.km.libreria.dto.AutorDTO;
import com.km.libreria.dto.LibroDTO;
import com.km.libreria.models.Libro;
import com.km.libreria.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

@Component
public class Principal {

    @Autowired
    private LibroService libroService;

    private final Scanner teclado = new Scanner(System.in);

    public void muestraMenu() {
        while (true) {
            System.out.println("\n--- Menú ---");
            System.out.println("1. Buscar libros por título");
            System.out.println("2. Mostrar libros registrados");
            System.out.println("3. Mostrar autores registrados");
            System.out.println("4. Buscar autores vivos en un año");
            System.out.println("5. Filtrar libros por idioma");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opción: ");

            int opcion = teclado.nextInt();
            teclado.nextLine(); // Limpiar buffer
            if (opcion == 0) {
                System.out.println("Cerrando programa");
                break;
            }
            ejecutarOpcion(opcion);
        }
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                buscarLibrosPorTitulo();
                break;
            case 2:
                mostrarLibrosRegistrados();
                break;
            case 3:
                mostrarAutoresRegistrados();
                break;
            case 4:
                buscarAutoresVivos();
                break;
            case 5:
                filtrarLibrosPorIdioma();
                break;
            default:
                System.out.println("Opción no válida");
        }
    }

    private void buscarLibrosPorTitulo() {
        System.out.print("Ingresa el título del libro: ");
        String titulo = teclado.nextLine();
        try {
            List<LibroDTO> libros = libroService.buscarLibrosPorTitulo(titulo);
            if (!libros.isEmpty()) {
                for (LibroDTO libro : libros) {
                    System.out.println("\nTítulo: " + libro.getTitulo());
                    System.out.println("Autor: " + libro.getAutor().getNombreAutor());
                    System.out.println("Idioma: " + libro.getIdioma());
                    System.out.println("Número de descargas: " + libro.getNumeroDescargas());
                }
                libroService.guardarLibros(libros);
            } else {
                System.out.println("No se encontraron libros con ese título.");
            }
        } catch (Exception e) {
            System.out.println("Error al buscar libros: " + e.getMessage());
        }
    }

    private void mostrarLibrosRegistrados() {
        List<Libro> libros = libroService.obtenerLibrosRegistrados();
        if (!libros.isEmpty()) {
            for (Libro libro : libros) {
                System.out.println("\nTítulo: " + libro.getTitulo());
                System.out.println("Autor: " + libro.getAutor().getNombreAutor());
                System.out.println("Idioma: " + libro.getIdioma());
                System.out.println("Número de descargas: " + libro.getNumeroDescargas());
            }
        } else {
            System.out.println("No se encontraron libros registrados.");
        }
    }

    private void mostrarAutoresRegistrados() {
        List<AutorDTO> autores = libroService.obtenerAutoresRegistrados();

        // Usar un Set para evitar duplicados al mostrar los autores
        Set<String> autoresVistos = new HashSet<>();

        if (!autores.isEmpty()) {
            for (AutorDTO autorDTO : autores) {
                // Si el autor no ha sido mostrado antes, se muestra
                if (autoresVistos.add(autorDTO.getNombreAutor())) {
                    System.out.println("\nAutor: " + autorDTO.getNombreAutor());
                    System.out.println("Fecha de nacimiento: " + autorDTO.getFechaNacimiento());
                    System.out.println("Fecha de fallecimiento: " +
                            (autorDTO.getFechaMuerte() != null ? autorDTO.getFechaMuerte() : "Aún vivo"));

                    // Obtener los libros asociados al autor
                    List<Libro> librosDelAutor = libroService.obtenerLibrosPorAutor(autorDTO.getNombreAutor());

                    // Verificar si el autor tiene libros registrados
                    if (!librosDelAutor.isEmpty()) {
                        System.out.print("Libros: ");
                        for (int i = 0; i < librosDelAutor.size(); i++) {
                            System.out.print(librosDelAutor.get(i).getTitulo());
                            if (i < librosDelAutor.size() - 1) {
                                System.out.print(", ");
                            }
                        }
                        System.out.println();
                    } else {
                        System.out.println("Libros: Ninguno registrado.");
                    }
                }
            }
        } else {
            System.out.println("No se encontraron autores registrados.");
        }
    }


    private void buscarAutoresVivos() {
        System.out.print("Ingresa el año para buscar autores vivos: ");
        int fecha = teclado.nextInt();
        teclado.nextLine(); // Limpiar buffer
        List<AutorDTO> autoresVivos = libroService.obtenerAutoresVivosPorFechaDe(fecha);
        if (!autoresVivos.isEmpty()) {
            for (AutorDTO autorDTO : autoresVivos) {
                System.out.println("\nAutor: " + autorDTO.getNombreAutor());
                System.out.println("Fecha de nacimiento: " + autorDTO.getFechaNacimiento());
                System.out.println("Fecha de fallecimiento: " + (autorDTO.getFechaMuerte() != null ? autorDTO.getFechaMuerte() : "Aún vivo"));
            }
        } else {
            System.out.println("No se encontraron autores vivos en ese año.");
        }
    }

    private void filtrarLibrosPorIdioma() {
        System.out.print("Ingresa el idioma (por ejemplo, 'es' para español 'en' para ingles 'fr' para frances '): ");
        String idioma = teclado.nextLine();
        try {
            List<Libro> libros = libroService.obtenerLibrosPorIdioma(idioma);
            if (!libros.isEmpty()) {
                for (Libro libro : libros) {
                    System.out.println("\nTítulo: " + libro.getTitulo());
                    System.out.println("Autor: " + libro.getAutor().getNombreAutor());
                    System.out.println("Idioma: " + libro.getIdioma());
                    System.out.println("Número de descargas: " + libro.getNumeroDescargas());
                }
            } else {
                System.out.println("No se encontraron libros en ese idioma.");
            }
        } catch (Exception e) {
            System.out.println("Error al buscar libros: " + e.getMessage());
        }
    }
}
