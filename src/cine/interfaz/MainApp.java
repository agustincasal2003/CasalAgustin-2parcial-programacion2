package cine.interfaz;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {

    private BorderPane root;
    // Lista temporal para almacenar las butacas que el usuario va seleccionando
    private List<String> butacasSeleccionadas = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CINE | Sistema de Reservas - UTN FRA");

        root = new BorderPane();
        root.setPrefSize(1000, 650);

        // ==========================================
        // 1. MENÚ LATERAL IZQUIERDO (Navegación Global)
        // ==========================================
        VBox menuLateral = new VBox(12);
        menuLateral.setPadding(new Insets(15));
        menuLateral.setStyle("-fx-background-color: #1a252f;");

        Label lblTituloMenu = new Label("CINE UTN");
        lblTituloMenu.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Button btnCartelera = crearBotonMenu("📅 Cartelera");
        Button btnPeliculas = crearBotonMenu("🎬 Películas");
        Button btnSalas = crearBotonMenu("🏢 Salas");
        Button btnFunciones = crearBotonMenu("🕒 Funciones");
        Button btnReservas = crearBotonMenu("🎟️ Mis Reservas");

        menuLateral.getChildren().addAll(lblTituloMenu, new Separator(), btnCartelera, btnPeliculas, btnSalas, btnFunciones, btnReservas);
        root.setLeft(menuLateral);

        // Vista inicial al abrir: Cartelera en el centro
        root.setCenter(crearVistaCartelera());

        // Eventos del menú lateral
        btnCartelera.setOnAction(e -> root.setCenter(crearVistaCartelera()));
        btnPeliculas.setOnAction(e -> root.setCenter(crearVistaCrud("Administración de Películas", "Gestión de altas, modificaciones y baja lógica de películas.")));
        btnSalas.setOnAction(e -> root.setCenter(crearVistaCrud("Administración de Salas", "Definición de filas, butacas y capacidad total de las salas.")));
        btnFunciones.setOnAction(e -> root.setCenter(crearVistaCrud("Programación de Funciones", "Asociación de películas, salas, horarios y control de superposición.")));
        btnReservas.setOnAction(e -> root.setCenter(crearVistaCrud("Consulta y Cancelación de Reservas", "Búsqueda por código, cliente y exportación a CSV.")));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button crearBotonMenu(String texto) {
        Button btn = new Button(texto);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;");
        return btn;
    }

    // ==========================================
    // VISTA 1: CARTELERA
    // ==========================================
    private VBox crearVistaCartelera() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #ecf0f1;");
        
        Label titulo = new Label("Cartelera de Funciones Disponibles");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        TableView<String[]> tabla = new TableView<>();
        VBox.setVgrow(tabla, Priority.ALWAYS);

        TableColumn<String[], String> colPelicula = new TableColumn<>("Película");
        colPelicula.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[0]));
        colPelicula.setPrefWidth(220);

        TableColumn<String[], String> colSala = new TableColumn<>("Sala");
        colSala.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[1]));
        colSala.setPrefWidth(100);
        
        TableColumn<String[], String> colHorario = new TableColumn<>("Horario");
        colHorario.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[2]));
        colHorario.setPrefWidth(180);

        TableColumn<String[], String> colPrecio = new TableColumn<>("Precio General");
        colPrecio.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()[3]));
        colPrecio.setPrefWidth(130);

        tabla.getColumns().addAll(colPelicula, colSala, colHorario, colPrecio);
        
        tabla.getItems().add(new String[]{"Órbita", "Sala 1", "04/12/2026 - 18:00", "$ 5.000"});
        tabla.getItems().add(new String[]{"El último viaje", "Sala 2", "04/12/2026 - 19:00", "$ 4.500"});

        Button btnReservar = new Button("Seleccionar Butacas →");
        btnReservar.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-cursor: hand;");
        
        btnReservar.setOnAction(e -> {
            butacasSeleccionadas.clear(); // Limpiamos selección anterior al entrar
            root.setCenter(crearVistaSeleccionButacas());
        });

        vbox.getChildren().addAll(titulo, tabla, btnReservar);
        return vbox;
    }

    // ==========================================
    // VISTA 2: SELECCIÓN DE BUTACAS (CON GUARDADO)
    // ==========================================
    private VBox crearVistaSeleccionButacas() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #ecf0f1;");
        
        Label titulo = new Label("Selección de Butacas - Órbita (Sala 1)");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label pantalla = new Label("================ PANTALLA ================");
        pantalla.setStyle("-fx-background-color: #bdc3c7; -fx-padding: 10px; -fx-alignment: center; -fx-font-weight: bold;");
        pantalla.setMaxWidth(Double.MAX_VALUE);

        GridPane gridButacas = new GridPane();
        gridButacas.setHgap(10);
        gridButacas.setVgap(10);
        gridButacas.setAlignment(Pos.CENTER);

        for (int fila = 0; fila < 4; fila++) {
            char letraFila = (char) ('A' + fila);
            for (int col = 1; col <= 6; col++) {
                String idButaca = letraFila + String.valueOf(col);
                Button btnButaca = new Button(idButaca);
                btnButaca.setPrefSize(55, 45);
                btnButaca.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-font-weight: bold; -fx-cursor: hand;");
                
                // Lógica para guardar/quitar la butaca de la lista al hacer clic
                btnButaca.setOnAction(e -> {
                    if (butacasSeleccionadas.contains(idButaca)) {
                        butacasSeleccionadas.remove(idButaca);
                        btnButaca.setStyle("-fx-background-color: white; -fx-border-color: #bdc3c7; -fx-font-weight: bold; -fx-cursor: hand;");
                    } else {
                        butacasSeleccionadas.add(idButaca);
                        btnButaca.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
                    }
                });

                gridButacas.add(btnButaca, col, fila);
            }
        }

        HBox panelBotones = new HBox(15);
        Button btnVolver = new Button("← Volver a Cartelera");
        Button btnContinuar = new Button("Continuar al Cliente →");
        btnContinuar.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        btnVolver.setOnAction(e -> root.setCenter(crearVistaCartelera()));
        
        btnContinuar.setOnAction(e -> {
            if (butacasSeleccionadas.isEmpty()) {
                Alert alerta = new Alert(Alert.AlertType.WARNING, "Debe seleccionar al menos una butaca para continuar.");
                alerta.showAndWait();
            } else {
                root.setCenter(crearVistaCliente());
            }
        });

        panelBotones.getChildren().addAll(btnVolver, btnContinuar);
        vbox.getChildren().addAll(titulo, pantalla, gridButacas, panelBotones);
        return vbox;
    }

    // ==========================================
    // VISTA 3: CLIENTE Y TIPOS DE ENTRADA DINÁMICOS
    // ==========================================
    private VBox crearVistaCliente() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #ecf0f1;");

        Label titulo = new Label("Datos del Cliente y Tipos de Entrada");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Ej: Martín Gómez");
        TextField txtIdentificacion = new TextField();
        txtIdentificacion.setPromptText("Ej: 28456789");

        form.add(new Label("Nombre y Apellido:"), 0, 0);
        form.add(txtNombre, 1, 0);
        form.add(new Label("Identificación (DNI):"), 0, 1);
        form.add(txtIdentificacion, 1, 1);

        Label lblEntradas = new Label("Configuración para las butacas seleccionadas (" + butacasSeleccionadas.toString() + "):");
        lblEntradas.setStyle("-fx-font-weight: bold;");

        VBox contenedorButacasAsignadas = new VBox(10);
        
        // Genera dinámicamente un selector de tipo de entrada por cada butaca que el usuario marcó
        for (String butaca : butacasSeleccionadas) {
            HBox filaButaca = new HBox(10);
            Label lblId = new Label("Butaca " + butaca + ":");
            ComboBox<String> cmbTipo = new ComboBox<>();
            cmbTipo.getItems().addAll("General ($ 5.000)", "Estudiante ($ 4.000)");
            cmbTipo.setValue("General ($ 5.000)");
            filaButaca.getChildren().addAll(lblId, cmbTipo);
            contenedorButacasAsignadas.getChildren().add(filaButaca);
        }

        HBox panelBotones = new HBox(15);
        Button btnVolverButacas = new Button("← Volver a Butacas");
        Button btnRevisar = new Button("Revisar y Confirmar Reserva →");
        btnRevisar.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        btnVolverButacas.setOnAction(e -> root.setCenter(crearVistaSeleccionButacas()));
        
        btnRevisar.setOnAction(e -> {
            if (txtNombre.getText().isEmpty() || txtIdentificacion.getText().isEmpty()) {
                Alert alerta = new Alert(Alert.AlertType.ERROR, "Por favor complete el nombre y la identificación del cliente.");
                alerta.showAndWait();
            } else {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION, "¡Reserva guardada con éxito para " + txtNombre.getText() + "!\nButacas: " + butacasSeleccionadas.toString());
                alerta.showAndWait();
                root.setCenter(crearVistaCartelera());
            }
        });

        panelBotones.getChildren().addAll(btnVolverButacas, btnRevisar);
        vbox.getChildren().addAll(titulo, form, new Separator(), lblEntradas, contenedorButacasAsignadas, new Separator(), panelBotones);
        return vbox;
    }

    // ==========================================
    // VISTA GENÉRICA CRUD / ADMINISTRACIÓN
    // ==========================================
    private VBox crearVistaCrud(String tituloTexto, String descripcion) {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #ecf0f1;");
        
        Label titulo = new Label(tituloTexto);
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label desc = new Label(descripcion);
        desc.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
        
        vbox.getChildren().addAll(titulo, desc);
        return vbox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}