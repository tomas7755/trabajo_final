package vista.administrador;

import gestores.GestorBiblioteca;
import gestores.GestorPersonas;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Menu principal del administrador.
 * Desde aca el administrador gestiona al personal (bibliotecarios) y a los usuarios del sistema.
 */
public class VentanaAdministrador {

    //gestor del personal (bibliotecarios y administradores).
    private GestorPersonas gestorPersonas;
    //gestor de la biblioteca (ahi estan los usuarios).
    private GestorBiblioteca gestorBiblioteca;

    //constructor
    public VentanaAdministrador(GestorPersonas gestorPersonas, GestorBiblioteca gestorBiblioteca) {
        this.gestorPersonas = gestorPersonas;
        this.gestorBiblioteca = gestorBiblioteca;
    }

    //metodo
    public void mostrar(Stage ventanaPrincipal) {
        Stage ventana = new Stage();
        ventana.initOwner(ventanaPrincipal);

        Label titulo = new Label("Panel Administrador");

        Button bibliotecarios = new Button("Gestionar bibliotecarios");
        Button usuarios = new Button("Gestionar usuarios");
        Button listados = new Button("Listados");
        Button volver = new Button("Volver");

        //estilo de los botones (el mismo que el panel del bibliotecario)
        String estilo = "-fx-background-color: #E6DDF2;" + "-fx-text-fill: #43245E;" + "-fx-font-size: 16px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 10px;" + "-fx-border-color: #9875B5;" + "-fx-border-radius: 10px;" + "-fx-border-width: 1.5px;" + "-fx-cursor: hand;";

        Button[] botones = {bibliotecarios, usuarios, listados, volver};

        for (Button boton : botones) {
            boton.setStyle(estilo);
            boton.setPrefSize(330, 45);
        }

        //estilo del titulo
        titulo.setStyle("-fx-font-size: 28px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");

        //acciones
        bibliotecarios.setOnAction(e -> new VentanaGestionBibliotecarios(gestorPersonas).mostrar(ventana));

        usuarios.setOnAction(e -> new VentanaGestionUsuarios(gestorBiblioteca).mostrar(ventana));

        //(provisional hasta que este la ventana de listados)
        listados.setOnAction(e -> mostrarMensaje("Listados", "Esta sección todavía está en desarrollo."));

        volver.setOnAction(e -> ventana.close());

        //menu central
        VBox menu = new VBox(15);
        menu.setAlignment(Pos.CENTER);

        menu.getChildren().addAll(titulo, bibliotecarios, usuarios, listados, volver);

        //lineas de decoracion
        Region lineaSuperior = new Region();
        lineaSuperior.setPrefHeight(5);
        lineaSuperior.setStyle("-fx-background-color: #A56DE2;");

        Region lineaInferior = new Region();
        lineaInferior.setPrefHeight(5);
        lineaInferior.setStyle("-fx-background-color: #A56DE2;");

        //distribucion
        BorderPane root = new BorderPane();
        root.setTop(lineaSuperior);
        root.setCenter(menu);
        root.setBottom(lineaInferior);

        //fondo
        root.setStyle("-fx-background-color: #352044;");

        Scene escena = new Scene(root, 600, 500);

        ventana.setTitle("Administrador");
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