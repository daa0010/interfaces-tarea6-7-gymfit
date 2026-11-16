package es.medac.gymfit.interfacestarea67gymfit.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Ficha de un socio. Es inmutable: cada cambio crea una copia, lo que hace trivial deshacer. */
public record Socio(
        String id,
        String nombre,
        String apellido1,
        String apellido2,
        LocalDate fechaNacimiento,
        Sexo sexo,
        Provincia provincia,
        Cuota cuota,
        List<String> clases,
        String iban,
        LocalDate vencimiento,
        Situacion situacion) {

    private static final int DIAS_AVISO_VENCIMIENTO = 7;

    public Socio {
        clases = List.copyOf(clases);
    }

    public String nombreCompleto() {
        return Stream.of(nombre, apellido1, apellido2)
                .filter(parte -> parte != null && !parte.isBlank())
                .collect(Collectors.joining(" "));
    }

    public int edad(LocalDate hoy) {
        return Period.between(fechaNacimiento, hoy).getYears();
    }

    public Estado estado(LocalDate hoy) {
        return switch (situacion) {
            case BAJA -> Estado.BAJA;
            case CONGELADO -> Estado.CONGELADO;
            case ALTA -> {
                long dias = ChronoUnit.DAYS.between(hoy, vencimiento);
                if (dias < 0) {
                    yield Estado.VENCIDO;
                }
                yield dias <= DIAS_AVISO_VENCIMIENTO ? Estado.VENCE_PRONTO : Estado.AL_DIA;
            }
        };
    }

    /** Suma un mes desde el vencimiento o, si la cuota ya venció, desde hoy. */
    public Socio renovado(LocalDate hoy) {
        LocalDate base = vencimiento.isBefore(hoy) ? hoy : vencimiento;
        return new Socio(id, nombre, apellido1, apellido2, fechaNacimiento, sexo, provincia, cuota,
                clases, iban, base.plusMonths(1), situacion);
    }

    public Socio conSituacion(Situacion nueva) {
        return new Socio(id, nombre, apellido1, apellido2, fechaNacimiento, sexo, provincia, cuota,
                clases, iban, vencimiento, nueva);
    }
}
