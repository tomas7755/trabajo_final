
package vista.bibliotecario;

import gestores.GestorBiblioteca;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class VentanaBibliotecario {

    private GestorBiblioteca bibliotecario;
    // Constructor
    public VentanaBibliotecario(GestorBiblioteca bibliotecario) {
        this.bibliotecario = bibliotecario;
    }
    // Metodo
    public void mostrar(Stage ventanaPrincipal) {

        Stage ventana = new Stage();
        ventana.initOwner(ventanaPrincipal);

        Label titulo = new Label("Panel Bibliotecario");

        Button registrarUsuario = new Button("Registrar usuario");
        Button registrarLibro = new Button("Registrar libro");
        Button prestamos = new Button("Registrar préstamos");
        Button devoluciones = new Button("Registrar devolución");
        Button reservas = new Button("Registrar reserva");
        Button consultas = new Button("Consultar libros y usuarios");
        Button volver = new Button("Volver");

        // estilo de los botones
        String estilo = "-fx-background-color: #E6DDF2;" + "-fx-text-fill: #43245E;" + "-fx-font-size: 16px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 10px;" + "-fx-border-color: #9875B5;" + "-fx-border-radius: 10px;" + "-fx-border-width: 1.5px;" + "-fx-cursor: hand;";

        Button[] botones = {
                registrarUsuario, registrarLibro,
                prestamos, devoluciones, reservas,
                consultas, volver
        };

        for (Button boton : botones) {
            boton.setStyle(estilo);
            boton.setPrefSize(330, 45);
        }

        // estilo del título
        titulo.setStyle("-fx-font-size: 28px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");

        // Acciones
        registrarUsuario.setOnAction(e -> mostrarMensaje("Registrar usuario", "Pasando a registro de usuarios"));

        registrarLibro.setOnAction(e -> new VentanaLibro(bibliotecario).mostrar(ventana));

        prestamos.setOnAction(e -> new VentanaPrestamo(bibliotecario).mostrar(ventana));

        reservas.setOnAction(e -> new VentanaReserva(bibliotecario).mostrar(ventana));

        devoluciones.setOnAction(e -> new VentanaDevoluciones(bibliotecario).mostrar(ventana));

        consultas.setOnAction(e -> new VentanasConsultas(bibliotecario).Mostrar(ventana));

        volver.setOnAction(e -> ventana.close());

        // Menu central
        VBox menu = new VBox(15);
        menu.setAlignment(Pos.CENTER);

        menu.getChildren().addAll(
                titulo,
                registrarUsuario,
                registrarLibro,
                prestamos,
                reservas,
                devoluciones,
                consultas,
                volver
        );

        // lineas de decoracion
        Region lineaSuperior = new Region();
        lineaSuperior.setPrefHeight(5);
        lineaSuperior.setStyle("-fx-background-color: #A56DE2;");

        Region lineaInferior = new Region();
        lineaInferior.setPrefHeight(5);
        lineaInferior.setStyle("-fx-background-color: #A56DE2;");

        // distribucion
        BorderPane root = new BorderPane();
        root.setTop(lineaSuperior);
        root.setCenter(menu);
        root.setBottom(lineaInferior);

        // fondo
        root.setStyle("-fx-background-color: #352044;");

        Scene escena = new Scene(root, 600, 750);

        ventana.setTitle("Bibliotecario");
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
