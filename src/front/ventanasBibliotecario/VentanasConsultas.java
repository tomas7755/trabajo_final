package front.ventanasBibliotecario;
import bibliotecario.Bibliotecario;
import bibliotecario.Libro;
import ClasesAuxiliares.LibroAuxiliar;
import Usuario.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
public class VentanasConsultas {
    private Bibliotecario bibliotecario;
    //connstructor
    public VentanasConsultas (Bibliotecario bibliotecario){
        this.bibliotecario = bibliotecario;
    }
    //metodo
    public void Mostrar(Stage ventanaPrincipal){
        Stage ventana = new Stage();
        ventana.initOwner(ventanaPrincipal);

        Label titulo = new Label("Consultas");

        ComboBox<String> opciones = new ComboBox<>();

        opciones.getItems().addAll("Todos los libros", "Libros disponibles", "Todos los usuarios", "Libros prestados y reservados");

        opciones.setPromptText("Seleccione una consulta");
        opciones.setPrefHeight(350);

        ListView<String> resultados = new ListView<>();
        resultados.setPrefSize(500, 350);

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
                        lista.add("ID: " + libro.getNumeroIdentificador() + " | Nombre: " + libro.getNombre() + " | Autor: " + libro.getAutor() + " | Estado: " + libro.getEstaDispponible());
                    }
                    break;
                case "Libros disponibles":
                    for (Libro libro : bibliotecario.listarLibrosDisponibles()) {

                        lista.add("ID: " + libro.getNumeroIdentificador() + " | Nombre: " + libro.getNombre() + " | Autor: " + libro.getAutor());
                    }
                    break;
                case "Todos los usuarios":
                    for (Usuario usuario : bibliotecario.getUsuarios().values()) {
                        lista.add("DNI: " + usuario.getDni() + " | Nombre: " + usuario.getNombre());
                    }
                    break;
                case "Libros prestados y reservados":
                    for (LibroAuxiliar auxiliar :
                            bibliotecario.listarUsuarioconLibros()) {
                        lista.add("Usuario: " + auxiliar.getUsuario().getNombre() + " | DNI: " + auxiliar.getUsuario().getDni() + " | Libro: " + auxiliar.getLibro().getNombre() + " | Estado: " + auxiliar.getEstaDisponible());
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

        Scene escena = new Scene(root, 600, 650);

        ventana.setTitle("Consultas");
        ventana.setScene(escena);
        ventana.setResizable(false);
        ventana.show();
    }
}
