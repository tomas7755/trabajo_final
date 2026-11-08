package front.ventanasBibliotecario;

import bibliotecario.Bibliotecario;
import bibliotecario.Libro;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class VentanaLibro {

    private Bibliotecario bibliotecario;
    //constructor
    public VentanaLibro (Bibliotecario bibliotecario){
        this.bibliotecario = bibliotecario;
    }
    //metodo
    public void mostrar(Stage ventanaPrincipal){
        Stage ventana = new Stage();
        ventana.initOwner(ventanaPrincipal);

        Label titulo = new Label("Registrar libro");

        //cajas para rellenar y boton de registrar y volver.

        TextField identificador = new TextField();
        identificador.setPromptText("Numero identificador");

        TextField nombre = new TextField();
        nombre.setPromptText("Nombre del libro");

        TextField autor = new TextField();
        autor.setPromptText("Nombre del autor");

        Button registrar = new Button("Registrar");
        Button volver = new Button("Volver");

        //estilo

        String estiloCampo = "-fx-background-radius: 8px;" + "-fx-font-size: 14px;" + "-fx-padding: 10px;";

        TextField [] campos = {identificador, nombre, autor};

        for (TextField campo : campos){
            campo.setStyle(estiloCampo);
            campo.setPrefWidth(300);
        }

        String estiloBoton = "-fx-background-color: #E6DDF2;" + "-fx-text-fill: #43245E;" + "-fx-font-size: 15px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 10px;" + "-fx-cursor: hand;";

        Button [] botones = {registrar, volver};

        for (Button boton : botones){
            boton.setStyle(estiloBoton);
            boton.setPrefSize(300, 40);
        }

        titulo.setStyle("-fx-font-size: 26px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");

        //acciones

        registrar.setOnAction(e -> {

            try {

                if (identificador.getText().isEmpty() ||
                        nombre.getText().isEmpty() ||
                        autor.getText().isEmpty()) {

                    mostrarMensaje("Error", "Complete todos los campos.");
                    return;
                }

                int numero = Integer.parseInt(identificador.getText());

                Libro libro = new Libro(
                        numero,
                        nombre.getText(),
                        autor.getText()
                );

                if (bibliotecario.agregarLibro(libro)) {

                    mostrarMensaje(
                            "Registro exitoso",
                            "El libro se registró correctamente."
                    );

                    identificador.clear();
                    nombre.clear();
                    autor.clear();

                } else {

                    mostrarMensaje(
                            "Error",
                            "Ya existe un libro con ese identificador."
                    );
                }

            } catch (NumberFormatException ex) {

                mostrarMensaje(
                        "Error",
                        "El identificador debe ser un número entero."
                );

            } catch (Exception ex) {

                mostrarMensaje("Error", ex.getMessage());
            }
        });

        volver.setOnAction(e -> ventana.close());

        VBox contenido = new VBox(15);
        contenido.setAlignment(Pos.CENTER);

        contenido.getChildren().addAll(titulo, identificador, nombre, autor, registrar, volver);

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

        //estilo
        Scene escena = new Scene(root, 450, 500);

        ventana.setTitle("Registrar libro");
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
