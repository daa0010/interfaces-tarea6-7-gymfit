package es.medac.gymfit.interfacestarea67gymfit.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Lee el archivo de texto que usaban las versiones anteriores, para pasar sus socios a la base de datos. */
final class SociosTsv {
    private static final int COLUMNAS = 12;

    private SociosTsv() {
    }

    static List<Socio> leer(Path archivo) throws IOException {
        List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
        List<Socio> leidos = new ArrayList<>();
        for (int i = 1; i < lineas.size(); i++) {
            if (!lineas.get(i).isBlank()) {
                try {
                    leidos.add(leerLinea(lineas.get(i)));
                } catch (RuntimeException e) {
                    throw new IOException("La línea " + (i + 1) + " de " + archivo + " no se puede leer.", e);
                }
            }
        }
        return leidos;
    }

    private static Socio leerLinea(String linea) {
        String[] c = linea.split("\t", -1);
        if (c.length != COLUMNAS) {
            throw new IllegalArgumentException("Se esperaban " + COLUMNAS + " columnas y hay " + c.length);
        }
        return new Socio(
                c[0], c[1], c[2], c[3],
                LocalDate.parse(c[4]),
                c[5].isEmpty() ? null : Sexo.valueOf(c[5]),
                Provincia.desdeCodigo(c[6]),
                Cuota.valueOf(c[7]),
                c[8].isEmpty() ? List.of() : List.of(c[8].split("\\|")),
                c[9],
                LocalDate.parse(c[10]),
                Situacion.valueOf(c[11]));
    }
}
