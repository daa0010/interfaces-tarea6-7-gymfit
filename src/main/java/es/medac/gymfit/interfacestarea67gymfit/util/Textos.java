package es.medac.gymfit.interfacestarea67gymfit.util;

import java.text.Normalizer;
import java.util.Locale;

public final class Textos {
    private Textos() {
    }

    /** Para buscar sin importar mayúsculas ni acentos: "jose" encuentra a "José". */
    public static String paraBuscar(String texto) {
        return Normalizer.normalize(texto == null ? "" : texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .trim();
    }
}
