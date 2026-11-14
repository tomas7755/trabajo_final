package vista.bibliotecario;
import gestores.GestorBiblioteca;
import modelo.Libro;
import modelo.Prestamo;
import modelo.Reserva;
import util.LibroAuxiliar;
import modelo.personas.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Ventana de consultas del bibliotecario: listados de libros, usuarios, prestamos y reservas.
 */
public class VentanasConsultas {
    private GestorBiblioteca bibliotecario;
    //connstructor
    public VentanasConsultas (GestorBiblioteca bibliotecario){
        this.bibliotecario = bibliotecario;
    }
    //metodo
    public void Mostrar(Stage ventanaPrincipal){
        Stage ventana = new Stage();
        ventana.initOwner(ventanaPrincipal);

        Label titulo = new Label("Consultas");

        ComboBox<String> opciones = new ComboBox<>();

        opciones.getItems().addAll("Todos los libros", "Libros disponibles", "Todos los usuarios", "Libros prestados y reservados",
                "Préstamos activos", "Reservas");

        opciones.setPromptText("Seleccione una consulta");
        opciones.setPrefWidth(300);

        ListView<String> resultados = new ListView<>();
        resultados.setPrefSize(640, 350);
        resultados.setMaxWidth(640);

        Button volver = new Button("Volver");

        String estiloBoton = "-fx-background-color: #E6DDF2;" + "-fx-text-fill: #43245E;" + "-fx-font-size: 15px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 10px;" + "-fx-cursor: hand;";

        volver.setStyle(estiloBoton);
        volver.setPrefSize(300, 40);

        titulo.setStyle("-fx-font-size: 26px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");

        opciones.setStyle("-fx-font-size: 14px;");

        opciones.setOnAction(e -> {

            ObservableList<String> lista =
                    FXCollections.observableArrayList();

            String seleccion = opciones.getValue();

            switch (seleccion) {
                case "Todos los libros":
                    for (Libro libro : bibliotecario.listarTodosLosLibros()) {
                        lista.add("ID: " + libro.getNumeroIdentificador() + " | Nombre: " + libro.getNombre() + " | Autor: " + libro.getAutor() + " | Género: " + libro.getGenero() + " | Estado: " + libro.getEstaDispponible());
                    }
                    break;
                case "Libros disponibles":
                    for (Libro libro : bibliotecario.listarLibrosDisponibles()) {

                        lista.add("ID: " + libro.getNumeroIdentificador() + " | Nombre: " + libro.getNombre() + " | Autor: " + libro.getAutor() + " | Género: " + libro.getGenero());
                    }
                    break;
                case "Todos los usuarios":
                    for (Usuario usuario : bibliotecario.getUsuarios().values()) {
                        lista.add("DNI: " + usuario.getDni() + " | Nombre: " + usuario.getNombre() + " " + usuario.getApellido() + " | " + (usuario.isActivo() ? "Activo" : "Dado de baja"));
                    }
                    break;
                case "Libros prestados y reservados":
                    for (LibroAuxiliar auxiliar :
                            bibliotecario.listarUsuarioconLibros()) {
                        lista.add("Usuario: " + auxiliar.getUsuario().getNombre() + " | DNI: " + auxiliar.getUsuario().getDni() + " | Libro: " + auxiliar.getLibro().getNombre() + " | Estado: " + auxiliar.getEstaDisponible());
                    }

                    break;
                case "Préstamos activos":
                    //muestra el numero de libro, que es el que se usa para devolver o renovar.
                    for (Prestamo prestamo : bibliotecario.listarPrestamos()) {
                        lista.add("Libro ID: " + prestamo.getLibro().getNumeroIdentificador() + " | " + prestamo.getLibro().getNombre() + " | Usuario: " + prestamo.getUsuario().getNombre() + " (DNI " + prestamo.getUsuario().getDni() + ") | Devolver antes de: " + prestamo.getFechaDevolucion());
                    }
                    break;
                case "Reservas":
                    for (Reserva reserva : bibliotecario.listarReservas()) {
                        lista.add("Libro ID: " + reserva.getLibro().getNumeroIdentificador() + " | " + reserva.getLibro().getNombre() + " | Reservado por: " + reserva.getUsuario().getNombre() + " (DNI " + reserva.getUsuario().getDni() + ") | Estado del libro: " + reserva.getLibro().getEstaDispponible());
                    }
                    break;
            }

            if (lista.isEmpty()) {
                lista.add("No hay registros para mostrar.");
            }

            resultados.setItems(lista);
        });

        volver.setOnAction(e -> ventana.close());

        VBox contenido = new VBox(15);

        contenido.setAlignment(Pos.CENTER);

        contenido.getChildren().addAll(
                titulo,
                opciones,
                resultados,
                volver
        );

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

        ventana.setTitle("Consultas");
        ventana.setScene(escena);
        ventana.setResizable(false);
        ventana.show();
    }
}
