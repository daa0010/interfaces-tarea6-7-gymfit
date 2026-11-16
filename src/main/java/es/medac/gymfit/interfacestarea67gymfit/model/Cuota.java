package es.medac.gymfit.interfacestarea67gymfit.model;

import java.util.Optional;

/** Tipos de cuota. Precios y límites de edad son valores de ejemplo: cámbialos aquí. */
public enum Cuota {
    BASICA("Básica", 29, 0, Integer.MAX_VALUE),
    PLUS("Plus", 39, 0, Integer.MAX_VALUE),
    PREMIUM("Premium", 55, 0, Integer.MAX_VALUE),
    JOVEN("Joven", 25, 0, 25),
    SENIOR("Senior", 25, 65, Integer.MAX_VALUE);

    private final String nombre;
    private final int precioMensual;
    private final int edadMinima;
    private final int edadMaxima;

    Cuota(String nombre, int precioMensual, int edadMinima, int edadMaxima) {
        this.nombre = nombre;
        this.precioMensual = precioMensual;
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
    }

    public String nombre() {
        return nombre;
    }

    public String precioTexto() {
        return precioMensual + " €/mes";
    }

    /** Explica por qué una persona de esa edad no puede tener esta cuota, si es el caso. */
    public Optional<String> motivoNoDisponible(int edad) {
        if (edad > edadMaxima) {
            return Optional.of("La cuota " + nombre + " es para menores de " + (edadMaxima + 1)
                    + " años y esta persona tiene " + edad + ".");
        }
        if (edad < edadMinima) {
            return Optional.of("La cuota " + nombre + " es a partir de " + edadMinima
                    + " años y esta persona tiene " + edad + ".");
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        return nombre;
    }
}
