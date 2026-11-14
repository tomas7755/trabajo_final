package vista.bibliotecario;
import gestores.GestorBiblioteca;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Ventana donde el bibliotecario registra la devolucion de un libro
 * o renueva su prestamo (le suma 15 dias a la fecha de devolucion).
 * Se trabaja con el numero del libro, que es el que el bibliotecario tiene a mano.
 */
public class VentanaDevoluciones {
    private GestorBiblioteca bibliotecario;
    //constructor
    public VentanaDevoluciones(GestorBiblioteca bibliotecario){
        this.bibliotecario = bibliotecario;
    }
    //metodo
    public void mostrar(Stage ventanaPrincipal){
        Stage ventana = new Stage();
        ventana.initOwner(ventanaPrincipal);

        Label titulo = new Label("Devoluciones y renovaciones");

        //se pide el numero del libro (antes se pedia el numero de prestamo, que no se mostraba en ningun lado).
        TextField numeroLibro = new TextField();
        numeroLibro.setPromptText("Numero identificador del libro");

        Button registrar = new Button("Registrar devolucion");
        Button renovar = new Button("Renovar préstamo (+15 días)");
        Button volver = new Button("Volver");

        numeroLibro.setStyle("-fx-background-radius: 8px;" + "-fx-font-size: 14px;" + "-fx-padding: 10px;");

        numeroLibro.setMaxWidth(300);

        String estiloBoton =  "-fx-background-color: #E6DDF2;" + "-fx-text-fill: #43245E;" + "-fx-font-size: 15px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 10px;" + "-fx-cursor: hand;";

        Button [] botones = {registrar, renovar, volver};

        for (Button boton : botones){
            boton.setStyle(estiloBoton);
            boton.setPrefSize(300, 40);
        }

        titulo.setStyle("-fx-font-size: 26px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");

        //DEVOLUCION: busca el prestamo de ese libro y lo elimina.
        //si alguien lo tenia reservado, el gestor lo deja RESERVADO.
        registrar.setOnAction(e -> {
            try {
                if (numeroLibro.getText().isEmpty()) {
                    mostrarMensaje(
                            "Error",
                            "Ingrese el número del libro."
                    );
                    return;
                }
                int numero = Integer.parseInt(numeroLibro.getText());
                bibliotecario.devolverLibro(numero);
                mostrarMensaje(
                        "Devolución registrada",
                        "El libro se devolvió correctamente."
                );
                numeroLibro.clear();
            }
            catch (NumberFormatException ex) {
                mostrarMensaje(
                        "Error",
                        "El número del libro debe ser un número entero."
                );
            }
            catch (Exception ex) {
                mostrarMensaje(
                        "No se pudo registrar la devolución",
                        ex.getMessage()
                );
            }
        });
        //RENOVACION (modificacion del prestamo): suma 15 dias a la fecha de devolucion.
        renovar.setOnAction(e -> {
            try {
                int numero = Integer.parseInt(numeroLibro.getText());
                bibliotecario.renovarPrestamo(numero);
                mostrarMensaje("Préstamo renovado", "Se agregaron 15 días a la fecha de devolución.");
                numeroLibro.clear();
            }
            catch (NumberFormatException ex) {
                mostrarMensaje("Error", "Ingrese el número del libro.");
            }
            catch (Exception ex) {
                mostrarMensaje("No se pudo renovar", ex.getMessage());
            }
        });
        volver.setOnAction(e -> ventana.close());

        VBox contenido = new VBox(15);
        contenido.setAlignment(Pos.CENTER);

        contenido.getChildren().addAll(
                titulo,
                numeroLibro,
                registrar,
                renovar,
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

        Scene escena = new Scene(root, 450, 420);

        ventana.setTitle("Devoluciones y renovaciones");
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
