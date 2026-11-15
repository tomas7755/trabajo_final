package vista.bibliotecario;

import gestores.GestorBiblioteca;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Libro;
import modelo.Reserva;
import modelo.personas.Usuario;

/**
 * Ventana donde el bibliotecario registra la reserva de un libro prestado,
 * la busca por el numero del libro, cambia el usuario que la reservo o la cancela (baja).
 */
public class VentanaReserva {
    private GestorBiblioteca bibliotecario;
    //reserva que se cargo con "Buscar" (es la que se va a modificar).
    private Reserva reservaCargada = null;

    //constructor
    public VentanaReserva(GestorBiblioteca bibliotecario){
        this.bibliotecario = bibliotecario;
    }

    //metodo
    public void mostrar(Stage ventanaPrincipal){
        Stage ventana = new Stage();
        ventana.initOwner(ventanaPrincipal);

        Label titulo = new Label("Reservas");

        //se crea las cajas para subir los datos y los botones
        TextField dni = new TextField();
        dni.setPromptText("Dni del usuario");

        TextField numeroLibro = new TextField();
        numeroLibro.setPromptText("Numero identificador del libro");

        Button registrar = new Button("Registrar");
        Button buscar = new Button("Buscar");
        Button modificar = new Button("Modificar");
        Button cancelar = new Button("Cancelar");
        Button limpiar = new Button("Limpiar");
        Button volver = new Button("Volver");

        //estilo
        String estiloCampo = "-fx-background-radius: 8px;" + "-fx-font-size: 14px;" + "-fx-padding: 10px;";

        TextField [] campos = {dni, numeroLibro};

        for (TextField campo : campos){
            campo.setStyle(estiloCampo);
            campo.setMaxWidth(300);
        }

        String estiloBoton = "-fx-background-color: #E6DDF2;" + "-fx-text-fill: #43245E;" + "-fx-font-size: 15px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 10px;" + "-fx-cursor: hand;";

        Button [] botones = {registrar, buscar, modificar, cancelar};

        for (Button boton : botones){
            boton.setStyle(estiloBoton);
            boton.setPrefSize(145, 40);
        }

        Button [] botonesAnchos = {limpiar, volver};

        for (Button boton : botonesAnchos){
            boton.setStyle(estiloBoton);
            boton.setPrefSize(300, 40);
        }

        titulo.setStyle("-fx-font-size: 26px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");

        //acciones

        //ALTA: registra una reserva nueva (el libro tiene que estar prestado).
        registrar.setOnAction(e -> {
            try {
                if (dni.getText().isEmpty() || numeroLibro.getText().isEmpty()) {
                    mostrarMensaje("Error", "Complete todos los campos.");
                    return;
                }
                int numero = Integer.parseInt(numeroLibro.getText());
                boolean resultado = bibliotecario.agregarReservas(dni.getText(), numero);
                if (resultado) {
                    mostrarMensaje("Reserva registrada", "La reserva se registró correctamente.");
                    limpiarTodo(dni, numeroLibro);
                }
            }
            catch (NumberFormatException ex) {
                mostrarMensaje("Error", "El número del libro debe ser un número entero.");
            }
            catch (Exception ex) {
                mostrarMensaje("No se pudo registrar la reserva", ex.getMessage());
            }
        });

        //CONSULTA: busca la reserva del libro y carga el DNI de quien la hizo.
        buscar.setOnAction(e -> {
            try {
                if (numeroLibro.getText().isEmpty()) {
                    mostrarMensaje("Error", "Ingrese el número del libro.");
                    return;
                }
                int numero = Integer.parseInt(numeroLibro.getText());
                Libro libro = bibliotecario.buscarLibro(numero);
                Reserva reserva = (libro == null) ? null : bibliotecario.buscarReservaDeLibro(libro);

                if (reserva == null) {
                    reservaCargada = null;
                    mostrarMensaje("Error", "Ese libro no tiene ninguna reserva.");
                    return;
                }
                reservaCargada = reserva;
                dni.setText(reserva.getUsuario().getDni());

            } catch (NumberFormatException ex) {
                mostrarMensaje("Error", "El número del libro debe ser un número entero.");
            }
        });

        //MODIFICACION: cambia el usuario de la reserva que se busco.
        //el libro no se puede cambiar: si se equivocaron de libro, se cancela y se reserva de nuevo.
        modificar.setOnAction(e -> {
            if (reservaCargada == null) {
                mostrarMensaje("Error", "Primero busque la reserva por el número del libro.");
                return;
            }
            if (dni.getText().isEmpty()) {
                mostrarMensaje("Error", "Ingrese el DNI del nuevo usuario.");
                return;
            }
            try {
                Usuario usuario = bibliotecario.buscarUsuario(dni.getText());
                if (usuario == null) {
                    mostrarMensaje("Error", "No existe un usuario con ese DNI.");
                    return;
                }
                Reserva nueva = new Reserva(reservaCargada.getNumeroReserva(), usuario, reservaCargada.getLibro());

                bibliotecario.modificarReserva(reservaCargada.getNumeroReserva(), nueva);
                mostrarMensaje("Modificación exitosa", "La reserva se actualizó correctamente.");

            } catch (Exception ex) {
                mostrarMensaje("No se pudo modificar la reserva", ex.getMessage());
            }
        });

        //CANCELAR (baja de la reserva): solo hace falta el numero del libro.
        cancelar.setOnAction(e -> {
            try {
                int numero = Integer.parseInt(numeroLibro.getText());
                bibliotecario.cancelarReservaDeLibro(numero);
                mostrarMensaje("Reserva cancelada", "La reserva del libro fue cancelada.");
                limpiarTodo(dni, numeroLibro);
            }
            catch (NumberFormatException ex) {
                mostrarMensaje("Error", "Ingrese el número del libro.");
            }
            catch (Exception ex) {
                mostrarMensaje("No se pudo cancelar", ex.getMessage());
            }
        });

        limpiar.setOnAction(e -> limpiarTodo(dni, numeroLibro));

        volver.setOnAction(e -> ventana.close());

        //los botones van de a dos por fila
        HBox fila1 = new HBox(10, registrar, buscar);
        HBox fila2 = new HBox(10, modificar, cancelar);

        HBox[] filas = {fila1, fila2};
        for (HBox fila : filas) {
            fila.setAlignment(Pos.CENTER);
        }

        VBox contenido = new VBox(15);
        contenido.setAlignment(Pos.CENTER);

        contenido.getChildren().addAll(titulo, dni, numeroLibro, fila1, fila2, limpiar, volver);

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

        Scene escena = new Scene(root, 450, 560);

        ventana.setTitle("Reservas");
        ventana.setScene(escena);
        ventana.setResizable(false);
        ventana.show();
    }

    //borra las cajas y olvida la reserva que se habia buscado.
    private void limpiarTodo(TextField dni, TextField numeroLibro) {
        dni.clear();
        numeroLibro.clear();
        reservaCargada = null;
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Biblioteca");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}