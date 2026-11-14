package vista.usuario;

import gestores.GestorBiblioteca;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.personas.Usuario;

/**
 * Ventana de ingreso del usuario: pide DNI y contraseña.
 * Solo deja entrar si el usuario existe, esta activo y la contraseña es correcta.
 */
public class VentanaIngresoUsuario {

    private GestorBiblioteca gestorBiblioteca;

    //constructor
    public VentanaIngresoUsuario(GestorBiblioteca gestorBiblioteca) {
        this.gestorBiblioteca = gestorBiblioteca;
    }

    //metodo
    public void mostrar(Stage ventanaPrincipal) {
        Stage ventana = new Stage();
        ventana.initOwner(ventanaPrincipal);

        Label titulo = new Label("Ingreso de usuario");

        TextField dni = new TextField();
        dni.setPromptText("DNI");

        //PasswordField oculta lo que se escribe (muestra puntitos).
        PasswordField contrasenia = new PasswordField();
        contrasenia.setPromptText("Contraseña");

        Button ingresar = new Button("Ingresar");
        Button volver = new Button("Volver");

        //estilo
        String estiloCampo = "-fx-background-radius: 8px;" + "-fx-font-size: 14px;" + "-fx-padding: 10px;";

        TextField[] campos = {dni, contrasenia};

        for (TextField campo : campos) {
            campo.setStyle(estiloCampo);
            campo.setMaxWidth(300);
        }

        String estiloBoton = "-fx-background-color: #E6DDF2;" + "-fx-text-fill: #43245E;" + "-fx-font-size: 15px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 10px;" + "-fx-cursor: hand;";

        Button[] botones = {ingresar, volver};

        for (Button boton : botones) {
            boton.setStyle(estiloBoton);
            boton.setPrefSize(300, 40);
        }

        titulo.setStyle("-fx-font-size: 26px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");

        //acciones
        ingresar.setOnAction(e -> {
            if (dni.getText().isEmpty() || contrasenia.getText().isEmpty()) {
                mostrarMensaje("Error", "Complete el DNI y la contraseña.");
                return;
            }
            Usuario usuario = gestorBiblioteca.buscarUsuario(dni.getText());

            //por seguridad no se dice si fallo el DNI o la contraseña.
            if (usuario == null) {
                mostrarMensaje("Error", "DNI o contraseña incorrectos.");
                return;
            }
            if (!usuario.isActivo()) {
                mostrarMensaje("Error", "El usuario está dado de baja.");
                return;
            }
            //autenticar es el metodo de la interfaz Autenticable (lo implementa Persona).
            if (!usuario.autenticar(contrasenia.getText())) {
                mostrarMensaje("Error", "DNI o contraseña incorrectos.");
                return;
            }
            //si todo esta bien, se cierra el ingreso y se abre el panel del usuario.
            ventana.close();
            new VentanaUsuario(gestorBiblioteca, usuario).mostrar(ventanaPrincipal);
        });

        volver.setOnAction(e -> ventana.close());

        VBox contenido = new VBox(15);
        contenido.setAlignment(Pos.CENTER);

        contenido.getChildren().addAll(titulo, dni, contrasenia, ingresar, volver);

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

        Scene escena = new Scene(root, 450, 400);

        ventana.setTitle("Ingreso de usuario");
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