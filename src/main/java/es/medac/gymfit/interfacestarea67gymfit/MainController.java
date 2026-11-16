package es.medac.gymfit.interfacestarea67gymfit;

import es.medac.gymfit.interfacestarea67gymfit.model.*;
import es.medac.gymfit.interfacestarea67gymfit.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MainController {

    // --- ELEMENTOS DEL FORMULARIO ---
    @FXML
    private TextField nombreField;
    @FXML
    private TextField apellidosField;
    @FXML
    private DatePicker nacimientoField;
    @FXML
    private ComboBox<Provincia> provinciaCombo;
    @FXML
    private ComboBox<String> clasesCombo;
    @FXML
    private TextField ibanField;
    @FXML
    private HBox sexoBox;
    @FXML
    private FlowPane cuotaPane;

    // --- ELEMENTOS DE LA TABLA ---
    @FXML
    private TableView<Socio> tabla;
    @FXML
    private TableColumn<Socio, String> columnaNombre;
    @FXML
    private TableColumn<Socio, String> columnaApellidos;
    @FXML
    private TableColumn<Socio, String> columnaNacimiento;
    @FXML
    private TableColumn<Socio, String> columnaCuota;
    @FXML
    private TableColumn<Socio, String> columnaProvincia;

    // --- ELEMENTOS DEL MODAL DE ERROR ---
    @FXML
    private StackPane capaOscura;
    @FXML
    private Label modalTitulo;
    @FXML
    private Label modalTexto;

    // --- DATOS INTERNOS ---
    private final ToggleGroup sexoGrupo = new ToggleGroup();
    private final ToggleGroup cuotaGrupo = new ToggleGroup();

    // Esta es la lista que se conecta a la tabla
    private final ObservableList<Socio> listaSocios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Cargo los RadioButtons de Sexo y Cuota
        sexoBox.getChildren().clear();
        for (Sexo sexo : Sexo.values()) {
            RadioButton opcion = new RadioButton(sexo.toString());
            opcion.setUserData(sexo);
            opcion.setToggleGroup(sexoGrupo);
            sexoBox.getChildren().add(opcion);
        }

        cuotaPane.getChildren().clear();
        for (Cuota cuota : Cuota.values()) {
            RadioButton opcion = new RadioButton(cuota.nombre());
            opcion.setUserData(cuota);
            opcion.setToggleGroup(cuotaGrupo);
            cuotaPane.getChildren().add(opcion);
        }

        // 2. Cargo Provincias y Clases
        provinciaCombo.getItems().addAll(Provincia.values());
        clasesCombo.getItems().addAll(Clases.TODAS);

        // 3. IBAN en mayúsculas automático
        ibanField.textProperty().addListener((obs, oldText, newText) -> {
            if (newText != null) {
                ibanField.setText(newText.toUpperCase());
            }
        });

        // 4. CONFIGURAR LA TABLA Y SUS COLUMNAS
        tabla.setItems(listaSocios);

        columnaNombre.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().nombre()));
        columnaApellidos.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().apellido1() + " " + cell.getValue().apellido2()));

        // Formateo la fecha a DD/MM/YYYY
        columnaNacimiento.setCellValueFactory(cell -> new SimpleStringProperty(Fechas.formatear(cell.getValue().fechaNacimiento())));
        columnaCuota.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().cuota().nombre()));
        columnaProvincia.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().provincia().nombre()));
    }

    @FXML
    public void guardar() {
        // --- VALIDACIONES DEL MODAL ---
        if (nombreField.getText().isBlank()) {
            mostrarError("Falta el nombre", "Por favor, escribe el nombre del socio.");
            return;
        }
        if (apellidosField.getText().isBlank()) {
            mostrarError("Faltan los apellidos", "Por favor, escribe los apellidos del socio.");
            return;
        }
        if (nacimientoField.getValue() == null) {
            mostrarError("Falta la fecha", "Selecciona una fecha de nacimiento válida.");
            return;
        }
        if (provinciaCombo.getValue() == null) {
            mostrarError("Falta la provincia", "Selecciona una provincia de la lista.");
            return;
        }
        if (cuotaGrupo.getSelectedToggle() == null) {
            mostrarError("Falta la cuota", "Debes seleccionar un tipo de cuota.");
            return;
        }

        Optional<String> problemaIban = Iban.problema(ibanField.getText());
        if (problemaIban.isPresent()) {
            mostrarError("Error en el formato del IBAN", problemaIban.get());
            return;
        }

        // --- PROCESAR DATOS Y GUARDAR ---
        String apeCompleto = apellidosField.getText().trim();
        String ape1 = apeCompleto;
        String ape2 = "";
        if (apeCompleto.contains(" ")) {
            ape1 = apeCompleto.substring(0, apeCompleto.indexOf(" "));
            ape2 = apeCompleto.substring(apeCompleto.indexOf(" ") + 1);
        }

        List<String> clasesSeleccionadas = clasesCombo.getValue() != null ? List.of(clasesCombo.getValue()) : List.of();

        Socio nuevoSocio = new Socio(
                UUID.randomUUID().toString(),
                nombreField.getText().trim(),
                ape1, ape2,
                nacimientoField.getValue(),
                sexoGrupo.getSelectedToggle() != null ? (Sexo) sexoGrupo.getSelectedToggle().getUserData() : null, provinciaCombo.getValue(),
                (Cuota) cuotaGrupo.getSelectedToggle().getUserData(),
                clasesSeleccionadas,
                Iban.normalizar(ibanField.getText()),
                LocalDate.now().plusMonths(1),
                Situacion.ALTA
        );

        // Añadir el nuevo socio a la tabla
        listaSocios.add(nuevoSocio);

        // Limpiar formulario
        nuevo();
    }

    @FXML
    public void nuevo() {
        nombreField.clear();
        apellidosField.clear();
        nacimientoField.setValue(null);
        ibanField.clear();
        provinciaCombo.setValue(null);
        clasesCombo.setValue(null);
        sexoGrupo.selectToggle(null);
        cuotaGrupo.selectToggle(null);
    }

    @FXML
    public void cancelar() {
        nuevo();
    }

    private void mostrarError(String titulo, String mensaje) {
        modalTitulo.setText(titulo);
        modalTexto.setText(mensaje);
        capaOscura.setVisible(true);
    }

    @FXML
    public void ocultarError() {
        capaOscura.setVisible(false);
    }
}