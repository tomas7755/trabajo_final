package vista.administrador;

import excepciones.PersonaDuplicadaException;
import excepciones.PersonaNoEncontradaException;
import gestores.GestorPersonas;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.personas.Bibliotecario;
import modelo.personas.Persona;

/**
 * Ventana donde el administrador hace el ABMC de los bibliotecarios:
 * registrar (alta), dar de baja (baja logica), reactivar, modificar y buscar por DNI (consulta).
 */
public class VentanaGestionBibliotecarios {

    private GestorPersonas gestorPersonas;

    //constructor
    public VentanaGestionBibliotecarios(GestorPersonas gestorPersonas) {
        this.gestorPersonas = gestorPersonas;
    }

    //metodo
    public void mostrar(Stage ventanaPrincipal) {
        Stage ventana = new Stage();
        ventana.initOwner(ventanaPrincipal);

        Label titulo = new Label("Gestionar bibliotecarios");

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

        TextField legajo = new TextField();
        legajo.setPromptText("Legajo");

        //muestra si el bibliotecario buscado esta activo o dado de baja.
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

        TextField[] campos = {dni, nombre, apellido, email, contrasenia, legajo};

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

        //ALTA: crea un bibliotecario nuevo con los datos de las cajas.
        registrar.setOnAction(e -> {
            if (hayCamposVacios(campos)) {
                mostrarMensaje("Error", "Complete todos los campos.");
                return;
            }
            try {
                Bibliotecario nuevo = new Bibliotecario(dni.getText(), nombre.getText(), apellido.getText(),
                        email.getText(), contrasenia.getText(), legajo.getText());

                gestorPersonas.agregarPersona(nuevo);

                mostrarMensaje("Registro exitoso", "El bibliotecario se registró correctamente.");
                limpiarCampos(campos, estado);

            } catch (PersonaDuplicadaException ex) {
                mostrarMensaje("Error", ex.getMessage());
            }
        });

        //CONSULTA: busca por DNI y carga los datos en las cajas.
        buscar.setOnAction(e -> {
            if (dni.getText().isEmpty()) {
                mostrarMensaje("Error", "Ingrese el DNI a buscar.");
                return;
            }
            try {
                Persona encontrada = gestorPersonas.buscarPorDni(dni.getText());

                //el gestor tambien guarda administradores, asi que revisamos que sea un bibliotecario.
                if (!(encontrada instanceof Bibliotecario)) {
                    mostrarMensaje("Error", "Ese DNI no pertenece a un bibliotecario.");
                    return;
                }
                Bibliotecario bibliotecario = (Bibliotecario) encontrada;

                nombre.setText(bibliotecario.getNombre());
                apellido.setText(bibliotecario.getApellido());
                email.setText(bibliotecario.getEmail());
                contrasenia.setText(bibliotecario.getContrasenia());
                legajo.setText(bibliotecario.getLegajo());
                estado.setText("Estado: " + (bibliotecario.isActivo() ? "Activo" : "Dado de baja"));

            } catch (PersonaNoEncontradaException ex) {
                mostrarMensaje("Error", ex.getMessage());
            }
        });

        //modificar, dar de baja y reactivar: todavia en desarrollo.

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

        contenido.getChildren().addAll(titulo, dni, nombre, apellido, email, contrasenia, legajo, estado,
                fila1, fila2, fila3, volver);

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

        Scene escena = new Scene(root, 450, 720);

        ventana.setTitle("Gestionar bibliotecarios");
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