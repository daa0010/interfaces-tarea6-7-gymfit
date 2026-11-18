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

import java.io.IOException;
import java.nio.file.Path;
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

    // --- DATOS INTERNOS ---
    private Socio socioActual = null;

    // Repositorio que maneja la base de datos (se guardará en la carpeta del usuario)
    private final SocioRepository repositorio = new SocioRepository(Path.of(System.getProperty("user.home"), "gymfit_socios.db"));


    public void initialize() {
        // 0. CARGAR BASE DE DATOS
        try {
            repositorio.cargar(LocalDate.now());
        } catch (IOException e) {
            mostrarError("Error fatal", "No se pudo cargar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }

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
        tabla.setItems(repositorio.socios());

        columnaNombre.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().nombre()));
        columnaApellidos.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().apellido1() + " " + cell.getValue().apellido2()));

        // Formateo la fecha a DD/MM/YYYY
        columnaNacimiento.setCellValueFactory(cell -> new SimpleStringProperty(Fechas.formatear(cell.getValue().fechaNacimiento())));
        columnaCuota.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().cuota().nombre()));
        columnaProvincia.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().provincia().nombre()));

        // Detectar selecciones en la tabla
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, socioAnterior, socioSeleccionado) -> {
            if (socioSeleccionado != null) {
                cargarSocio(socioSeleccionado);
            }
        });

        // Construyo el menú contextual para cuando el usuario haga clic derecho en la tabla
        ContextMenu menuContextual = new ContextMenu();

        MenuItem editarItem = new MenuItem("Editar socio");
        editarItem.setOnAction(e -> {
            Socio seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) cargarSocio(seleccionado);
        });

        MenuItem borrarItem = new MenuItem("Borrar socio");
        borrarItem.setOnAction(e -> borrar());

        menuContextual.getItems().addAll(editarItem, borrarItem);
        tabla.setContextMenu(menuContextual); // Asigno el menú a la tabla
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

        // Si estoy editando, mantengo su ID y fechas originales. Si es nuevo, genero unos nuevos.
        Socio nuevoSocio = new Socio(
                socioActual == null ? UUID.randomUUID().toString() : socioActual.id(),
                nombreField.getText().trim(),
                ape1, ape2,
                nacimientoField.getValue(),
                sexoGrupo.getSelectedToggle() != null ? (Sexo) sexoGrupo.getSelectedToggle().getUserData() : null,
                provinciaCombo.getValue(),
                (Cuota) cuotaGrupo.getSelectedToggle().getUserData(),
                clasesSeleccionadas,
                Iban.normalizar(ibanField.getText()),
                socioActual == null ? LocalDate.now().plusMonths(1) : socioActual.vencimiento(),
                socioActual == null ? Situacion.ALTA : socioActual.situacion()
        );

        try {
            // El repositorio se encarga de saber si es nuevo o una edición, e insertarlo en SQLite
            repositorio.guardar(nuevoSocio);
        } catch (IOException e) {
            mostrarError("Error al guardar", "No se pudo guardar el socio en la base de datos: " + e.getMessage());
        }

        nuevo(); // Limpio el formulario al terminar
    }

    @FXML
    public void nuevo() {
        socioActual = null;
        tabla.getSelectionModel().clearSelection();
        nombreField.clear();
        apellidosField.clear();
        nacimientoField.setValue(null);
        ibanField.clear();
        provinciaCombo.setValue(null);
        clasesCombo.setValue(null);
        sexoGrupo.selectToggle(null);
        cuotaGrupo.selectToggle(null);
    }

    private void cargarSocio(Socio socio) {
        socioActual = socio;

        nombreField.setText(socio.nombre());
        apellidosField.setText(socio.apellido1() + (socio.apellido2().isEmpty() ? "" : " " + socio.apellido2()));
        nacimientoField.setValue(socio.fechaNacimiento());
        provinciaCombo.setValue(socio.provincia());
        ibanField.setText(socio.iban());

        // Marcar el RadioButton de Sexo correcto
        sexoGrupo.getToggles().forEach(toggle -> {
            if (toggle.getUserData() == socio.sexo()) sexoGrupo.selectToggle(toggle);
        });

        // Marcar el RadioButton de Cuota correcto
        cuotaGrupo.getToggles().forEach(toggle -> {
            if (toggle.getUserData() == socio.cuota()) cuotaGrupo.selectToggle(toggle);
        });

        // Seleccionar la clase en el ComboBox (cogemos la primera de la lista si existe)
        if (!socio.clases().isEmpty()) {
            clasesCombo.setValue(socio.clases().get(0));
        } else {
            clasesCombo.setValue(null);
        }
    }

    @FXML
    public void cancelar() {
        if (socioActual != null) {
            // Si estaba editando, restauro los datos originales descartando lo que haya escrito
            cargarSocio(socioActual);
        } else {
            // Si estaba creando uno nuevo, simplemente limpio el formulario
            nuevo();
        }
    }

    @FXML
    public void borrar() {
        if (socioActual == null) {
            mostrarError("Ningún socio seleccionado", "Por favor, haz clic en un socio de la tabla para poder borrarlo.");
            return;
        }

        // Lanzo un diálogo nativo de confirmación antes de eliminar nada
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar borrado");
        confirmacion.setHeaderText("Vas a dar de baja al socio: " + socioActual.nombre());
        confirmacion.setContentText("¿Estás seguro? Esta acción no se puede deshacer.");

        // Si el usuario pulsa Aceptar (OK), procedo a borrar
        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                repositorio.borrar(socioActual);
            } catch (IOException e) {
                mostrarError("Error al borrar", "Hubo un problema al borrar en disco: " + e.getMessage());
                return;
            }
            nuevo();
        }
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

    @FXML
    public void descartarError() {
        ocultarError(); // Cierro el modal de error
        cancelar(); // Restauro el formulario descartando lo que estuviera mal
    }

    @FXML
    public void salir() {
        // Cierro la aplicación de forma limpia usando el cierre oficial de JavaFX
        javafx.application.Platform.exit();
    }

    @FXML
    public void acercaDe() {
        // Muestro una pequeña ventana emergente nativa con la información de mi aplicación
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Acerca de GymFit");
        alerta.setHeaderText("GymFit - Gestión de Socios");
        alerta.setContentText("Desarrollado para la tarea de Desarrollo de Interfaces.\nVersión 1.0");
        alerta.showAndWait();
    }
}