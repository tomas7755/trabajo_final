package front.ventanasBibliotecario;
import bibliotecario.Bibliotecario;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
public class VentanaDevoluciones {
    private Bibliotecario bibliotecario;
    //constructor
    public VentanaDevoluciones(Bibliotecario bibliotecario){
        this.bibliotecario = bibliotecario;
    }
    //metodo
    public void mostrar(Stage ventanaPrincipal){
        Stage ventana = new Stage();
        ventana.initOwner(ventanaPrincipal);

        Label titulo = new Label("Registrar devoluciones");

        TextField numeroPrestamo = new TextField();
        numeroPrestamo.setPromptText("Numero identificador del prestamo");

        Button registrar = new Button("Registrar devolucion");
        Button volver = new Button("Volver");

        numeroPrestamo.setStyle("-fx-background-radius: 8px;" + "-fx-font-size: 14px;" + "-fx-padding: 10px;");

        numeroPrestamo.setPrefWidth(300);

        String estiloBoton =  "-fx-background-color: #E6DDF2;" + "-fx-text-fill: #43245E;" + "-fx-font-size: 15px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 10px;" + "-fx-cursor: hand;";

        Button [] botones = {registrar, volver};

        for (Button boton : botones){
            boton.setStyle(estiloBoton);
            boton.setPrefSize(300, 40);
        }

        titulo.setStyle("-fx-font-size: 26px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");

        registrar.setOnAction(e -> {
            try {
                if (numeroPrestamo.getText().isEmpty()) {
                    mostrarMensaje(
                            "Error",
                            "Ingrese el número del préstamo."
                    );
                    return;
                }
                int numero = Integer.parseInt(numeroPrestamo.getText());
                bibliotecario.eliminarPrestamo(numero);
                mostrarMensaje(
                        "Devolución registrada",
                        "El libro se devolvió correctamente."
                );
                numeroPrestamo.clear();
            }
            catch (NumberFormatException ex) {
                mostrarMensaje(
                        "Error",
                        "El número del préstamo debe ser un número entero."
                );
            }
            catch (Exception ex) {
                mostrarMensaje(
                        "No se pudo registrar la devolución",
                        ex.getMessage()
                );
            }
        });
        volver.setOnAction(e -> ventana.close());

        VBox contenido = new VBox(15);
        contenido.setAlignment(Pos.CENTER);

        contenido.getChildren().addAll(
                titulo,
                numeroPrestamo,
                registrar,
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

        Scene escena = new Scene(root, 450, 400);

        ventana.setTitle("Registrar devolucion");
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
