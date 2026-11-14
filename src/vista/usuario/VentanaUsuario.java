package vista.usuario;

import excepciones.LibroNoDisponible;
import excepciones.SinLibrosException;
import excepciones.SinUsuarioException;
import gestores.GestorBiblioteca;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Libro;
import modelo.Prestamo;
import modelo.Reserva;
import modelo.personas.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Panel del usuario que ya inicio sesion.
 * Puede buscar libros por titulo o autor, ver sus prestamos y reservas, y reservar libros.
 */
public class VentanaUsuario {

    private GestorBiblioteca gestorBiblioteca;
    //el usuario que inicio sesion.
    private Usuario usuario;
    //libros que se estan mostrando en la lista (para saber cual se selecciono al reservar).
    private List<Libro> librosMostrados = new ArrayList<>();

    //constructor
    public VentanaUsuario(GestorBiblioteca gestorBiblioteca, Usuario usuario) {
        this.gestorBiblioteca = gestorBiblioteca;
        this.usuario = usuario;
    }

    //metodo
    public void mostrar(Stage ventanaPrincipal) {
        Stage ventana = new Stage();
        ventana.initOwner(ventanaPrincipal);

        Label titulo = new Label("Hola, " + usuario.getNombre());

        //busqueda: se elige por que buscar y se escribe el texto.
        ComboBox<String> buscarPor = new ComboBox<>();
        buscarPor.getItems().addAll("Título", "Autor");
        buscarPor.setValue("Título");

        TextField textoBusqueda = new TextField();
        textoBusqueda.setPromptText("Escriba lo que busca");

        Button buscar = new Button("Buscar");

        ListView<String> resultados = new ListView<>();
        resultados.setPrefSize(600, 330);
        resultados.setMaxWidth(600);

        Button reservar = new Button("Reservar seleccionado");
        Button misLibros = new Button("Mis préstamos y reservas");
        Button volver = new Button("Cerrar sesión");

        //estilo
        String estiloBoton = "-fx-background-color: #E6DDF2;" + "-fx-text-fill: #43245E;" + "-fx-font-size: 15px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 10px;" + "-fx-cursor: hand;";

        Button[] botones = {buscar, reservar, misLibros, volver};

        for (Button boton : botones) {
            boton.setStyle(estiloBoton);
        }
        buscar.setPrefSize(120, 40);
        reservar.setPrefSize(295, 40);
        misLibros.setPrefSize(295, 40);
        volver.setPrefSize(300, 40);

        textoBusqueda.setStyle("-fx-background-radius: 8px;" + "-fx-font-size: 14px;" + "-fx-padding: 10px;");
        textoBusqueda.setPrefWidth(340);
        buscarPor.setStyle("-fx-font-size: 14px;");
        buscarPor.setPrefHeight(40);

        titulo.setStyle("-fx-font-size: 26px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");

        //acciones

        //BUSCAR: trae los libros que coinciden con el titulo o el autor.
        buscar.setOnAction(e -> {
            if (textoBusqueda.getText().isEmpty()) {
                mostrarMensaje("Error", "Escriba un título o autor para buscar.");
                return;
            }
            if (buscarPor.getValue().equals("Título")) {
                librosMostrados = gestorBiblioteca.buscarLibrosPorTitulo(textoBusqueda.getText());
            } else {
                librosMostrados = gestorBiblioteca.buscarLibrosPorAutor(textoBusqueda.getText());
            }

            ObservableList<String> lista = FXCollections.observableArrayList();
            for (Libro libro : librosMostrados) {
                lista.add("ID: " + libro.getNumeroIdentificador() + " | " + libro.getNombre()
                        + " | Autor: " + libro.getAutor() + " | Estado: " + libro.getEstaDispponible());
            }
            if (lista.isEmpty()) {
                lista.add("No se encontraron libros.");
            }
            resultados.setItems(lista);
        });

        //RESERVAR: reserva el libro seleccionado en la lista.
        //el gestor solo deja reservar libros que estan prestados (si esta disponible, se pide el prestamo).
        reservar.setOnAction(e -> {
            int posicion = resultados.getSelectionModel().getSelectedIndex();

            if (posicion < 0 || posicion >= librosMostrados.size()) {
                mostrarMensaje("Error", "Busque un libro y selecciónelo en la lista.");
                return;
            }
            Libro elegido = librosMostrados.get(posicion);
            try {
                gestorBiblioteca.agregarReservas(usuario.getDni(), elegido.getNumeroIdentificador());
                mostrarMensaje("Reserva exitosa", "Reservaste \"" + elegido.getNombre() + "\".");
                //se actualiza la lista para que se vea el nuevo estado.
                buscar.fire();

            } catch (SinLibrosException | SinUsuarioException | LibroNoDisponible ex) {
                mostrarMensaje("No se pudo reservar", ex.getMessage());
            }
        });

        //MIS PRESTAMOS Y RESERVAS: muestra lo que tiene el usuario.
        misLibros.setOnAction(e -> {
            //en esta vista no se puede reservar, asi que se vacia la lista de libros mostrados.
            librosMostrados = new ArrayList<>();

            ObservableList<String> lista = FXCollections.observableArrayList();

            for (Prestamo prestamo : gestorBiblioteca.listarPrestamosDeUsuario(usuario.getDni())) {
                lista.add("PRÉSTAMO | " + prestamo.getLibro().getNombre() + " | Desde: " + prestamo.getFechaPrestamo()
                        + " | Devolver antes de: " + prestamo.getFechaDevolucion());
            }
            for (Reserva reserva : gestorBiblioteca.listarReservasDeUsuario(usuario.getDni())) {
                lista.add("RESERVA | " + reserva.getLibro().getNombre() + " | Autor: " + reserva.getLibro().getAutor());
            }
            if (lista.isEmpty()) {
                lista.add("No tenés préstamos ni reservas.");
            }
            resultados.setItems(lista);
        });

        volver.setOnAction(e -> ventana.close());

        //distribucion
        HBox filaBusqueda = new HBox(10, buscarPor, textoBusqueda, buscar);
        filaBusqueda.setAlignment(Pos.CENTER);

        HBox filaAcciones = new HBox(10, reservar, misLibros);
        filaAcciones.setAlignment(Pos.CENTER);

        VBox contenido = new VBox(15);
        contenido.setAlignment(Pos.CENTER);

        contenido.getChildren().addAll(titulo, filaBusqueda, resultados, filaAcciones, volver);

        //lineas de decoracion
        Region lineaSuperior = new Region();
        lineaSuperior.setPrefHeight(5);
        lineaSuperior.setStyle("-fx-background-color: #A56DE2;");

        Region lineaInferior = new Region();
        lineaInferior.setPrefHeight(5);
        lineaInferior.setStyle("-fx-background-color: #A56DE2;");

        BorderPane root = new BorderPane();
        root.setTop(lineaSuperior);
        root.setCenter(contenido);
        root.setBottom(lineaInferior);

        root.setStyle("-fx-background-color: #352044;");

        Scene escena = new Scene(root, 700, 650);

        ventana.setTitle("Panel del usuario");
        ventana.setScene(escena);
        ventana.setResizable(false);
        ventana.show();
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Biblioteca");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}