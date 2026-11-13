package vista.bibliotecario;
import gestores.GestorBiblioteca;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class VentanaPrestamo {
    private GestorBiblioteca bibliotecario;
    //constructor
    public VentanaPrestamo(GestorBiblioteca bibliotecario){
        this.bibliotecario = bibliotecario;
    }
    //metodo
    public void mostrar(Stage ventanaPrincipal){
        Stage ventana = new Stage();
        ventana.initOwner(ventanaPrincipal);

        //cajas para registrar y botones para registrar y volver

        Label titulo = new Label("Registrar Prestamo");

        TextField dni = new TextField();
        dni.setPromptText("Dni del Usuario");

        TextField numeroLibro = new TextField();
        numeroLibro.setPromptText("Numero Identificador del libro");

        Button registar = new Button("Registrar Prestamo");
        Button volver = new Button("Volver");

        String estiloCampo = "-fx-background-radius: 8px;" + "-fx-font-size: 14px;" + "-fx-padding: 10px;";

        TextField [] campos = {dni, numeroLibro};

        for (TextField campo : campos){
            campo.setStyle(estiloCampo);
            campo.setPrefWidth(300);
        }

        String estiloBoton = "-fx-background-color: #E6DDF2;" + "-fx-text-fill: #43245E;" + "-fx-font-size: 15px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 10px;" + "-fx-cursor: hand;";

        Button [] botones = {registar, volver};

        for (Button boton : botones){
            boton.setStyle(estiloBoton);
            boton.setPrefSize(300, 40);
        }

        titulo.setStyle("-fx-font-size: 26px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");

        //acciones

        registar.setOnAction(e -> {
            try {
                if (dni.getText().isEmpty() ||
                        numeroLibro.getText().isEmpty()) {

                    mostrarMensaje("Error", "Complete todos los campos.");
                    return;
                }
                int numero = Integer.parseInt(numeroLibro.getText());
                boolean resultado = bibliotecario.agregarPrestamo(
                        dni.getText(),
                        numero
                );
                if (resultado) {
                    mostrarMensaje(
                            "Préstamo registrado",
                            "El préstamo se registró correctamente."
                    );
                    dni.clear();
                    numeroLibro.clear();
                }
            } catch (NumberFormatException ex) {
                mostrarMensaje(
                        "Error",
                        "El número del libro debe ser un número entero."
                );
            } catch (Exception ex) {
                mostrarMensaje(
                        "No se pudo registrar el préstamo",
                        ex.getMessage()
                );
            }
        });
        volver.setOnAction(e -> ventana.close());

        VBox contenido = new VBox(15);
        contenido.setAlignment(Pos.CENTER);

        contenido.getChildren().addAll(titulo, dni, numeroLibro, registar, volver);

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

        Scene escena = new Scene(root, 450, 450);

        ventana.setTitle("Registrar Prestamo");
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
