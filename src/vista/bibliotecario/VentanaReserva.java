package vista.bibliotecario;
import gestores.GestorBiblioteca;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
public class VentanaReserva {
    private GestorBiblioteca bibliotecario;
    //constructor
    public VentanaReserva(GestorBiblioteca bibliotecario){
        this.bibliotecario = bibliotecario;
    }
    //metodo
    public void mostrar(Stage ventanaPrincipal){
        Stage ventana = new Stage();
        ventana.initOwner(ventanaPrincipal);

        Label titulo = new Label("Registrar Reserva");

        //se crea las cajas para subir los datos y los botones de registrar y volver

        TextField dni = new TextField();
        dni.setPromptText("Dni del usuario");

        TextField numeroLibro = new TextField();
        numeroLibro.setPromptText("Numero identificador del libro");

        Button registrar = new Button("Registrar reserva");
        Button volver = new Button("Volver");

        String estiloCampo = "-fx-background-radius: 8px;" + "-fx-font-size: 14px;" + "-fx-padding: 10px;";

        TextField [] campos = {dni, numeroLibro};

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
                if (dni.getText().isEmpty() ||
                        numeroLibro.getText().isEmpty()) {

                    mostrarMensaje("Error", "Complete todos los campos.");
                    return;
                }
                int numero = Integer.parseInt(numeroLibro.getText());
                boolean resultado = bibliotecario.agregarReservas(
                        dni.getText(),
                        numero
                );
                if (resultado) {
                    mostrarMensaje(
                            "Reserva registrada",
                            "La reserva se registró correctamente."
                    );
                    dni.clear();
                    numeroLibro.clear();
                }
            }
            catch (NumberFormatException ex) {
                mostrarMensaje(
                        "Error",
                        "El número del libro debe ser un número entero."
                );
            }
            catch (Exception ex) {
                mostrarMensaje(
                        "No se pudo registrar la reserva",
                        ex.getMessage()
                );
            }
        });
        volver.setOnAction(e -> ventana.close());

        VBox contenido = new VBox(15);
        contenido.setAlignment(Pos.CENTER);

        contenido.getChildren().addAll(titulo, dni, numeroLibro, registrar, volver);

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

        ventana.setTitle("Registrar Reserva");
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
