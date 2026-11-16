package es.medac.gymfit.interfacestarea67gymfit.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Guarda los socios en una base de datos SQLite cada vez que algo cambia: el usuario nunca tiene
 * que acordarse de guardar. Cada cambio es una transacción, así que si la aplicación se interrumpe
 * a mitad los datos se quedan como estaban.
 */
public class SocioRepository {
    /** PRAGMA user_version vale 0 en una base de datos recién creada, antes de preparar sus tablas. */
    private static final int VERSION_ESQUEMA = 1;
    private static final String ARCHIVO_ANTIGUO = "socios.tsv";

    private static final String CREAR_SOCIOS = """
            CREATE TABLE socios (
                id          TEXT PRIMARY KEY,
                nombre      TEXT NOT NULL,
                apellido1   TEXT NOT NULL,
                apellido2   TEXT NOT NULL,
                nacimiento  TEXT NOT NULL,
                sexo        TEXT,
                provincia   TEXT NOT NULL,
                cuota       TEXT NOT NULL,
                iban        TEXT NOT NULL,
                vencimiento TEXT NOT NULL,
                situacion   TEXT NOT NULL
            )""";
    private static final String CREAR_CLASES = """
            CREATE TABLE socio_clases (
                socio_id TEXT NOT NULL REFERENCES socios(id),
                posicion INTEGER NOT NULL,
                clase    TEXT NOT NULL,
                PRIMARY KEY (socio_id, posicion)
            )""";
    private static final String GUARDAR_SOCIO = """
            INSERT INTO socios (id, nombre, apellido1, apellido2, nacimiento, sexo, provincia, cuota,
                                iban, vencimiento, situacion)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON CONFLICT (id) DO UPDATE SET
                nombre = excluded.nombre,
                apellido1 = excluded.apellido1,
                apellido2 = excluded.apellido2,
                nacimiento = excluded.nacimiento,
                sexo = excluded.sexo,
                provincia = excluded.provincia,
                cuota = excluded.cuota,
                iban = excluded.iban,
                vencimiento = excluded.vencimiento,
                situacion = excluded.situacion""";

    private final Path archivo;
    private final ObservableList<Socio> socios = FXCollections.observableArrayList();

    public SocioRepository(Path archivo) {
        this.archivo = archivo;
    }

    public Path archivo() {
        return archivo;
    }

    public ObservableList<Socio> socios() {
        return socios;
    }

    /**
     * Carga la base de datos. Si es nueva, la rellena con los socios del archivo de texto antiguo
     * que haya en la misma carpeta o, si no hay ninguno, con datos de ejemplo.
     */
    public void cargar(LocalDate hoy) throws IOException {
        Files.createDirectories(archivo.toAbsolutePath().getParent());
        try (Connection conexion = abrir()) {
            if (versionEsquema(conexion) == 0) {
                preparar(conexion, sociosIniciales(hoy));
            }
            socios.setAll(leerSocios(conexion));
        } catch (SQLException | RuntimeException e) {
            throw new IOException("No se puede leer la base de datos " + archivo + ": " + e.getMessage(), e);
        }
    }

    /** Añade el socio o sustituye la versión anterior con el mismo id. */
    public void guardar(Socio socio) throws IOException {
        try (Connection conexion = abrir()) {
            conexion.setAutoCommit(false);
            escribir(conexion, socio);
            conexion.commit();
        } catch (SQLException e) {
            throw new IOException("No se puede escribir en la base de datos " + archivo + ": " + e.getMessage(), e);
        }
        // La lista cambia solo tras confirmar, para no enseñar datos que no se han guardado.
        for (int i = 0; i < socios.size(); i++) {
            if (socios.get(i).id().equals(socio.id())) {
                socios.set(i, socio);
                return;
            }
        }
        socios.add(socio);
    }

    private Connection abrir() throws SQLException {
        Connection conexion = DriverManager.getConnection("jdbc:sqlite:" + archivo.toAbsolutePath());
        try (Statement sentencia = conexion.createStatement()) {
            sentencia.execute("PRAGMA foreign_keys = ON");
        }
        return conexion;
    }

    private static int versionEsquema(Connection conexion) throws SQLException {
        try (Statement sentencia = conexion.createStatement();
             ResultSet fila = sentencia.executeQuery("PRAGMA user_version")) {
            return fila.getInt(1);
        }
    }

    private List<Socio> sociosIniciales(LocalDate hoy) throws IOException {
        Path antiguo = archivo.toAbsolutePath().resolveSibling(ARCHIVO_ANTIGUO);
        return Files.exists(antiguo) ? SociosTsv.leer(antiguo) : DatosEjemplo.crear(hoy);
    }

    /** Todo o nada: si algo falla, cerrar la conexión sin confirmar deja la base de datos por preparar. */
    private static void preparar(Connection conexion, List<Socio> iniciales) throws SQLException {
        conexion.setAutoCommit(false);
        try (Statement sentencia = conexion.createStatement()) {
            sentencia.execute(CREAR_SOCIOS);
            sentencia.execute(CREAR_CLASES);
            for (Socio socio : iniciales) {
                escribir(conexion, socio);
            }
            sentencia.execute("PRAGMA user_version = " + VERSION_ESQUEMA);
        }
        conexion.commit();
        conexion.setAutoCommit(true);
    }

    private static void escribir(Connection conexion, Socio s) throws SQLException {
        try (PreparedStatement sentencia = conexion.prepareStatement(GUARDAR_SOCIO)) {
            sentencia.setString(1, s.id());
            sentencia.setString(2, textoOVacio(s.nombre()));
            sentencia.setString(3, textoOVacio(s.apellido1()));
            sentencia.setString(4, textoOVacio(s.apellido2()));
            sentencia.setString(5, s.fechaNacimiento().toString());
            sentencia.setString(6, s.sexo() == null ? null : s.sexo().name());
            sentencia.setString(7, s.provincia().codigoIne());
            sentencia.setString(8, s.cuota().name());
            sentencia.setString(9, textoOVacio(s.iban()));
            sentencia.setString(10, s.vencimiento().toString());
            sentencia.setString(11, s.situacion().name());
            sentencia.executeUpdate();
        }
        try (PreparedStatement sentencia = conexion.prepareStatement("DELETE FROM socio_clases WHERE socio_id = ?")) {
            sentencia.setString(1, s.id());
            sentencia.executeUpdate();
        }
        try (PreparedStatement sentencia = conexion.prepareStatement(
                "INSERT INTO socio_clases (socio_id, posicion, clase) VALUES (?, ?, ?)")) {
            for (int i = 0; i < s.clases().size(); i++) {
                sentencia.setString(1, s.id());
                sentencia.setInt(2, i);
                sentencia.setString(3, s.clases().get(i));
                sentencia.executeUpdate();
            }
        }
    }

    private static List<Socio> leerSocios(Connection conexion) throws SQLException {
        Map<String, List<String>> clases = leerClases(conexion);
        List<Socio> leidos = new ArrayList<>();
        try (Statement sentencia = conexion.createStatement();
             ResultSet fila = sentencia.executeQuery("SELECT * FROM socios ORDER BY rowid")) {
            while (fila.next()) {
                String id = fila.getString("id");
                String sexo = fila.getString("sexo");
                leidos.add(new Socio(
                        id,
                        fila.getString("nombre"),
                        fila.getString("apellido1"),
                        fila.getString("apellido2"),
                        LocalDate.parse(fila.getString("nacimiento")),
                        sexo == null ? null : Sexo.valueOf(sexo),
                        Provincia.desdeCodigo(fila.getString("provincia")),
                        Cuota.valueOf(fila.getString("cuota")),
                        clases.getOrDefault(id, List.of()),
                        fila.getString("iban"),
                        LocalDate.parse(fila.getString("vencimiento")),
                        Situacion.valueOf(fila.getString("situacion"))));
            }
        }
        return leidos;
    }

    private static Map<String, List<String>> leerClases(Connection conexion) throws SQLException {
        Map<String, List<String>> clases = new HashMap<>();
        try (Statement sentencia = conexion.createStatement();
             ResultSet fila = sentencia.executeQuery(
                     "SELECT socio_id, clase FROM socio_clases ORDER BY socio_id, posicion")) {
            while (fila.next()) {
                clases.computeIfAbsent(fila.getString("socio_id"), id -> new ArrayList<>()).add(fila.getString("clase"));
            }
        }
        return clases;
    }

    private static String textoOVacio(String texto) {
        return texto == null ? "" : texto;
    }
}
