package es.medac.gymfit.interfacestarea67gymfit.model;


import java.time.LocalDate;
import java.util.List;

/** Personas ficticias para ver la aplicación con contenido la primera vez que se abre. */
final class DatosEjemplo {
    private DatosEjemplo() {
    }

    static List<Socio> crear(LocalDate hoy) {
        return List.of(
                new Socio("ejemplo-ana", "Ana", "Romero", "Vidal", LocalDate.of(1999, 3, 12), Sexo.MUJER,
                        Provincia.SEVILLA, Cuota.PREMIUM, List.of("Pilates", "Yoga"),
                        "ES9121000418450200051332", hoy.plusDays(24), Situacion.ALTA),
                new Socio("ejemplo-luis", "Luis", "Ortega", "", LocalDate.of(2001, 7, 21), Sexo.HOMBRE,
                        Provincia.SEVILLA, Cuota.BASICA, List.of(),
                        "ES7921000813610123456789", hoy.plusDays(40), Situacion.CONGELADO),
                new Socio("ejemplo-marta", "Marta", "Gil", "Navarro", LocalDate.of(1988, 11, 30), Sexo.MUJER,
                        Provincia.HUELVA, Cuota.PLUS, List.of("Spinning"),
                        "ES6621000418401234567891", hoy.plusDays(5), Situacion.ALTA),
                new Socio("ejemplo-jose", "José", "Pardo", "Molina", LocalDate.of(2005, 2, 2), Sexo.HOMBRE,
                        Provincia.MALAGA, Cuota.JOVEN, List.of("Boxeo", "HIIT"),
                        "ES1000492352082414205416", hoy.minusDays(3), Situacion.ALTA));
    }
}
