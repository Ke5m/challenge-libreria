package com.km.libreria.dto;
public class AutorDTO {
    private String nombreAutor;
    private Integer fechaNacimiento;
    private Integer fechaMuerte;

    // Constructor
    public AutorDTO(String nombreAutor, Integer fechaNacimiento, Integer fechaMuerte) {
        this.nombreAutor = nombreAutor;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaMuerte = fechaMuerte;
    }

    // Getters y Setters
    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public Integer getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Integer fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(Integer fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }
}