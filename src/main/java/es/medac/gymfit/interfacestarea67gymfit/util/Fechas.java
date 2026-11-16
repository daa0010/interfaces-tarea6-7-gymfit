package es.medac.gymfit.interfacestarea67gymfit.util;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Una sola forma de mostrar fechas (dd/mm/aaaa) y una entrada tolerante: siempre día, mes y año. */
public final class Fechas {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Pattern CON_SEPARADOR = Pattern.compile("(\\d{1,2})[/\\-. ](\\d{1,2})[/\\-. ](\\d{4}|\\d{2})");
    private static final Pattern SIN_SEPARADOR = Pattern.compile("(\\d{2})(\\d{2})(\\d{4}|\\d{2})");

    private Fechas() {
    }

    public static String formatear(LocalDate fecha) {
        return fecha == null ? "" : FORMATO.format(fecha);
    }

    public static Optional<LocalDate> interpretar(String texto, LocalDate hoy) {
        String limpio = texto == null ? "" : texto.trim();
        Matcher m = CON_SEPARADOR.matcher(limpio);
        if (!m.matches()) {
            m = SIN_SEPARADOR.matcher(limpio);
            if (!m.matches()) {
                return Optional.empty();
            }
        }
        try {
            int anio = Integer.parseInt(m.group(3));
            if (m.group(3).length() == 2) {
                // Los años de dos dígitos se interpretan como la fecha pasada más reciente.
                anio += 2000 + anio > hoy.getYear() ? 1900 : 2000;
            }
            return Optional.of(LocalDate.of(anio, Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1))));
        } catch (DateTimeException e) {
            return Optional.empty();
        }
    }

    public static String relativo(LocalDate fecha, LocalDate hoy) {
        long dias = ChronoUnit.DAYS.between(hoy, fecha);
        if (dias == 0) {
            return "hoy";
        }
        if (dias == 1) {
            return "mañana";
        }
        if (dias == -1) {
            return "ayer";
        }
        return dias > 0 ? "en " + dias + " días" : "hace " + -dias + " días";
    }
}
