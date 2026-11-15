package vista.administrador;

import excepciones.ErrorBuscandoUsuario;
import excepciones.PersonaDuplicadaException;
import gestores.GestorBiblioteca;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.personas.Usuario;

/**
 * Ventana donde el administrador hace el ABMC de los usuarios de la biblioteca:
 * registrar (alta), dar de baja (baja logica), reactivar, modificar y buscar por DNI (consulta).
 * Los usuarios se guardan en el GestorBiblioteca, porque ahi estan conectados con sus prestamos y reservas.
 */
public class VentanaGestionUsuarios {

    private GestorBiblioteca gestorBiblioteca;

    //constructor
    public VentanaGestionUsuarios(GestorBiblioteca gestorBiblioteca) {
        this.gestorBiblioteca = gestorBiblioteca;
    }

    //metodo
    public void mostrar(Stage ventanaPrincipal) {
        Stage ventana = new Stage();
        ventana.initOwner(ventanaPrincipal);

        Label titulo = new Label("Gestionar usuarios");

        //cajas para completar los datos
        TextField dni = new TextField();
        dni.setPromptText("DNI");

        TextField nombre = new TextField();
        nombre.setPromptText("Nombre");

        TextField apellido = new TextField();
        apellido.setPromptText("Apellido");

        TextField email = new TextField();
        email.setPromptText("Email");

        //PasswordField oculta lo que se escribe (muestra puntitos).
        PasswordField contrasenia = new PasswordField();
        contrasenia.setPromptText("Contraseña");

        TextField direccion = new TextField();
        direccion.setPromptText("Dirección");

        TextField telefono = new TextField();
        telefono.setPromptText("Teléfono");

        //muestra si el usuario buscado esta activo o dado de baja.
        Label estado = new Label("");

        Button registrar = new Button("Registrar");
        Button buscar = new Button("Buscar");
        Button modificar = new Button("Modificar");
        Button darDeBaja = new Button("Dar de baja");
        Button reactivar = new Button("Reactivar");
        Button limpiar = new Button("Limpiar");
        Button volver = new Button("Volver");

        //estilo
        String estiloCampo = "-fx-background-radius: 8px;" + "-fx-font-size: 14px;" + "-fx-padding: 10px;";

        TextField[] campos = {dni, nombre, apellido, email, contrasenia, direccion, telefono};

        for (TextField campo : campos) {
            campo.setStyle(estiloCampo);
            campo.setMaxWidth(300);
        }

        String estiloBoton = "-fx-background-color: #E6DDF2;" + "-fx-text-fill: #43245E;" + "-fx-font-size: 15px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 10px;" + "-fx-cursor: hand;";

        Button[] botones = {registrar, buscar, modificar, darDeBaja, reactivar, limpiar};

        for (Button boton : botones) {
            boton.setStyle(estiloBoton);
            boton.setPrefSize(145, 40);
        }

        volver.setStyle(estiloBoton);
        volver.setPrefSize(300, 40);

        titulo.setStyle("-fx-font-size: 26px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");
        estado.setStyle("-fx-font-size: 14px;" + "-fx-text-fill: #E6DDF2;");

        //acciones

        //ALTA: crea un usuario nuevo con los datos de las cajas.
        registrar.setOnAction(e -> {
            if (hayCamposVacios(campos)) {
                mostrarMensaje("Error", "Complete todos los campos.");
                return;
            }
            Usuario nuevo = new Usuario(dni.getText(), nombre.getText(), apellido.getText(), email.getText(),
                    contrasenia.getText(), direccion.getText(), telefono.getText());

            try {
                //agregarUsuario devuelve false si ya existe un usuario con ese DNI.
                if (gestorBiblioteca.agregarUsuario(nuevo)) {
                    mostrarMensaje("Registro exitoso", "El usuario se registró correctamente.");
                    limpiarCampos(campos, estado);
                } else {
                    mostrarMensaje("Error", "Ya existe un usuario con el DNI " + dni.getText() + ".");
                }
            } catch (PersonaDuplicadaException ex) {
                //el DNI ya lo tiene un bibliotecario o administrador (lo detecta la base de datos).
                mostrarMensaje("Error", ex.getMessage());
            }
        });

        //CONSULTA: busca por DNI y carga los datos en las cajas.
        buscar.setOnAction(e -> {
            if (dni.getText().isEmpty()) {
                mostrarMensaje("Error", "Ingrese el DNI a buscar.");
                return;
            }
            //buscarUsuario devuelve null si no lo encuentra.
            Usuario usuario = gestorBiblioteca.buscarUsuario(dni.getText());

            if (usuario == null) {
                mostrarMensaje("Error", "No existe un usuario con el DNI " + dni.getText() + ".");
                return;
            }
            nombre.setText(usuario.getNombre());
            apellido.setText(usuario.getApellido());
            email.setText(usuario.getEmail());
            contrasenia.setText(usuario.getContrasenia());
            direccion.setText(usuario.getDireccion());
            telefono.setText(usuario.getTelefono());
            estado.setText("Estado: " + (usuario.isActivo() ? "Activo" : "Dado de baja"));
        });

        //MODIFICACION: reemplaza los datos del usuario con ese DNI.
        modificar.setOnAction(e -> {
            if (hayCamposVacios(campos)) {
                mostrarMensaje("Error", "Busque el usuario y complete todos los campos.");
                return;
            }
            try {
                Usuario modificado = new Usuario(dni.getText(), nombre.getText(), apellido.getText(), email.getText(),
                        contrasenia.getText(), direccion.getText(), telefono.getText());

                gestorBiblioteca.modificarDatosUsuario(dni.getText(), modificado);

                mostrarMensaje("Modificación exitosa", "Los datos se actualizaron correctamente.");

            } catch (ErrorBuscandoUsuario ex) {
                mostrarMensaje("Error", ex.getMessage());
            }
        });

        //BAJA LOGICA: marca al usuario como inactivo (no se borra).
        //el gestor no deja darlo de baja si tiene prestamos o reservas.
        darDeBaja.setOnAction(e -> {
            if (dni.getText().isEmpty()) {
                mostrarMensaje("Error", "Ingrese el DNI del usuario.");
                return;
            }
            try {
                gestorBiblioteca.eliminarUsuario(dni.getText());
                estado.setText("Estado: Dado de baja");
                mostrarMensaje("Baja exitosa", "El usuario fue dado de baja.");

            } catch (ErrorBuscandoUsuario ex) {
                mostrarMensaje("Error", ex.getMessage());
            }
        });

        //vuelve a activar a un usuario dado de baja.
        reactivar.setOnAction(e -> {
            if (dni.getText().isEmpty()) {
                mostrarMensaje("Error", "Ingrese el DNI del usuario.");
                return;
            }
            try {
                gestorBiblioteca.reactivarUsuario(dni.getText());
                estado.setText("Estado: Activo");
                mostrarMensaje("Reactivación exitosa", "El usuario fue reactivado.");

            } catch (ErrorBuscandoUsuario ex) {
                mostrarMensaje("Error", ex.getMessage());
            }
        });

        limpiar.setOnAction(e -> limpiarCampos(campos, estado));

        volver.setOnAction(e -> ventana.close());

        //los botones van de a dos por fila
        HBox fila1 = new HBox(10, registrar, buscar);
        HBox fila2 = new HBox(10, modificar, darDeBaja);
        HBox fila3 = new HBox(10, reactivar, limpiar);

        HBox[] filas = {fila1, fila2, fila3};

        for (HBox fila : filas) {
            fila.setAlignment(Pos.CENTER);
        }

        VBox contenido = new VBox(12);
        contenido.setAlignment(Pos.CENTER);

        contenido.getChildren().addAll(titulo, dni, nombre, apellido, email, contrasenia, direccion, telefono,
                estado, fila1, fila2, fila3, volver);

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

        Scene escena = new Scene(root, 450, 770);

        ventana.setTitle("Gestionar usuarios");
        ventana.setScene(escena);
        ventana.setResizable(false);
        ventana.show();
    }

    //devuelve true si alguna de las cajas esta vacia.
    private boolean hayCamposVacios(TextField[] campos) {
        for (TextField campo : campos) {
            if (campo.getText().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    //borra el contenido de todas las cajas.
    private void limpiarCampos(TextField[] campos, Label estado) {
        for (TextField campo : campos) {
            campo.clear();
        }
        estado.setText("");
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Biblioteca");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}