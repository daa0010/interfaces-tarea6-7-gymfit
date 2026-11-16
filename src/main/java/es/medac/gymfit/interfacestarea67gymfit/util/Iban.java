package es.medac.gymfit.interfacestarea67gymfit.util;

import java.util.Locale;
import java.util.Optional;

public final class Iban {

    private Iban() {
    }

    public static String normalizar(String texto) {
        return texto == null ? "" : texto.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }

    /** ES91 2100 0418 4502 0005 1332 */
    public static String agrupar(String texto) {
        return normalizar(texto).replaceAll("(.{4})(?=.)", "$1 ");
    }

    /** Para pantalla: los datos bancarios no deben quedar a la vista en recepción. */
    public static String enmascarar(String texto) {
        String iban = normalizar(texto);
        if (iban.length() < 8) {
            return iban;
        }
        return iban.substring(0, 4) + " •••• •••• " + iban.substring(iban.length() - 4);
    }

    /** Solo exige que esté relleno: no se comprueba que el IBAN sea correcto. */
    public static Optional<String> problema(String texto) {
        return normalizar(texto).isEmpty()
                ? Optional.of("Escribe el IBAN para poder cobrar la cuota.")
                : Optional.empty();
    }
}
