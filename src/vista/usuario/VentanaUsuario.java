package vista.usuario;

import excepciones.ErrorBuscandoPrestamo;
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
 * Puede buscar libros por titulo, autor o genero, pedir prestados los disponibles, reservar los que estan
 * prestados, ver sus prestamos y reservas, y devolver los libros que tiene.
 */
public class VentanaUsuario {

    private GestorBiblioteca gestorBiblioteca;
    //el usuario que inicio sesion.
    private Usuario usuario;
    //libros que se estan mostrando en la lista (para saber cual se selecciono al pedir o reservar).
    private List<Libro> librosMostrados = new ArrayList<>();
    //prestamos que se estan mostrando en "Mis prestamos y reservas" (para saber cual se quiere devolver).
    private List<Prestamo> prestamosMostrados = new ArrayList<>();

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
        buscarPor.getItems().addAll("Título", "Autor", "Género");
        buscarPor.setValue("Título");

        TextField textoBusqueda = new TextField();
        textoBusqueda.setPromptText("Escriba lo que busca");

        Button buscar = new Button("Buscar");

        ListView<String> resultados = new ListView<>();
        resultados.setPrefSize(600, 330);
        resultados.setMaxWidth(600);

        Button pedir = new Button("Pedir préstamo");
        Button reservar = new Button("Reservar");
        Button misLibros = new Button("Mis préstamos y reservas");
        Button devolver = new Button("Devolver");
        Button volver = new Button("Cerrar sesión");

        //estilo
        String estiloBoton = "-fx-background-color: #E6DDF2;" + "-fx-text-fill: #43245E;" + "-fx-font-size: 15px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 10px;" + "-fx-cursor: hand;";

        Button[] botones = {buscar, pedir, reservar, misLibros, devolver, volver};

        for (Button boton : botones) {
            boton.setStyle(estiloBoton);
        }
        buscar.setPrefSize(120, 40);
        pedir.setPrefSize(295, 40);
        reservar.setPrefSize(295, 40);
        misLibros.setPrefSize(295, 40);
        devolver.setPrefSize(295, 40);
        volver.setPrefSize(300, 40);

        textoBusqueda.setStyle("-fx-background-radius: 8px;" + "-fx-font-size: 14px;" + "-fx-padding: 10px;");
        textoBusqueda.setPrefWidth(320);
        buscarPor.setStyle("-fx-font-size: 14px;");
        buscarPor.setPrefHeight(40);

        titulo.setStyle("-fx-font-size: 26px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");

        //acciones

        //BUSCAR: trae los libros que coinciden con el titulo, el autor o el genero.
        buscar.setOnAction(e -> {
            if (textoBusqueda.getText().isEmpty()) {
                mostrarMensaje("Error", "Escriba un título, autor o género para buscar.");
                return;
            }
            if (buscarPor.getValue().equals("Título")) {
                librosMostrados = gestorBiblioteca.buscarLibrosPorTitulo(textoBusqueda.getText());
            } else if (buscarPor.getValue().equals("Autor")) {
                librosMostrados = gestorBiblioteca.buscarLibrosPorAutor(textoBusqueda.getText());
            } else {
                librosMostrados = gestorBiblioteca.buscarLibrosPorGenero(textoBusqueda.getText());
            }

            //en esta vista no se puede devolver, asi que se vacia la lista de prestamos mostrados.
            prestamosMostrados = new ArrayList<>();

            ObservableList<String> lista = FXCollections.observableArrayList();
            for (Libro libro : librosMostrados) {
                lista.add("ID: " + libro.getNumeroIdentificador() + " | " + libro.getNombre()
                        + " | Autor: " + libro.getAutor() + " | Género: " + libro.getGenero() + " | Estado: " + libro.getEstaDispponible());
            }
            if (lista.isEmpty()) {
                lista.add("No se encontraron libros.");
            }
            resultados.setItems(lista);
        });

        //PEDIR PRESTAMO: el usuario se lleva el libro seleccionado.
        //solo se puede si esta disponible, o si esta reservado a su nombre (el gestor lo controla).
        pedir.setOnAction(e -> {
            Libro elegido = libroSeleccionado(resultados);
            if (elegido == null) {
                mostrarMensaje("Error", "Busque un libro y selecciónelo en la lista.");
                return;
            }
            try {
                gestorBiblioteca.agregarPrestamo(usuario.getDni(), elegido.getNumeroIdentificador());
                mostrarMensaje("Préstamo registrado", "Te llevaste \"" + elegido.getNombre() + "\". Tenés 15 días para devolverlo.");
                //se actualiza la lista para que se vea el nuevo estado.
                buscar.fire();

            } catch (SinLibrosException | SinUsuarioException | LibroNoDisponible ex) {
                mostrarMensaje("No se pudo pedir el préstamo", ex.getMessage());
            }
        });

        //RESERVAR: reserva el libro seleccionado en la lista.
        //el gestor solo deja reservar libros que estan prestados (si esta disponible, se pide el prestamo).
        reservar.setOnAction(e -> {
            Libro elegido = libroSeleccionado(resultados);
            if (elegido == null) {
                mostrarMensaje("Error", "Busque un libro y selecciónelo en la lista.");
                return;
            }
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
            //en esta vista no se puede pedir ni reservar, asi que se vacia la lista de libros mostrados.
            librosMostrados = new ArrayList<>();
            //los prestamos van primero en la lista, asi la posicion seleccionada coincide con esta lista.
            prestamosMostrados = gestorBiblioteca.listarPrestamosDeUsuario(usuario.getDni());

            ObservableList<String> lista = FXCollections.observableArrayList();

            for (Prestamo prestamo : prestamosMostrados) {
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

        //DEVOLVER: devuelve el libro del prestamo seleccionado en "Mis prestamos y reservas".
        devolver.setOnAction(e -> {
            int posicion = resultados.getSelectionModel().getSelectedIndex();

            if (posicion < 0 || posicion >= prestamosMostrados.size()) {
                mostrarMensaje("Error", "Toque \"Mis préstamos y reservas\" y seleccione un préstamo.");
                return;
            }
            Prestamo elegido = prestamosMostrados.get(posicion);
            try {
                gestorBiblioteca.devolverLibro(elegido.getLibro().getNumeroIdentificador());
                mostrarMensaje("Devolución registrada", "Devolviste \"" + elegido.getLibro().getNombre() + "\".");
                //se actualiza la lista.
                misLibros.fire();

            } catch (ErrorBuscandoPrestamo ex) {
                mostrarMensaje("No se pudo devolver", ex.getMessage());
            }
        });

        volver.setOnAction(e -> ventana.close());

        //distribucion
        HBox filaBusqueda = new HBox(10, buscarPor, textoBusqueda, buscar);
        filaBusqueda.setAlignment(Pos.CENTER);

        HBox filaAcciones = new HBox(10, pedir, reservar);
        filaAcciones.setAlignment(Pos.CENTER);

        HBox filaMisLibros = new HBox(10, misLibros, devolver);
        filaMisLibros.setAlignment(Pos.CENTER);

        VBox contenido = new VBox(15);
        contenido.setAlignment(Pos.CENTER);

        contenido.getChildren().addAll(titulo, filaBusqueda, resultados, filaAcciones, filaMisLibros, volver);

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

        Scene escena = new Scene(root, 700, 700);

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

    //devuelve el libro seleccionado en la lista de busqueda, o null si no hay ninguno.
    private Libro libroSeleccionado(ListView<String> resultados) {
        int posicion = resultados.getSelectionModel().getSelectedIndex();
        if (posicion < 0 || posicion >= librosMostrados.size()) {
            return null;
        }
        return librosMostrados.get(posicion);
    }
}
