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
import modelo.Prestamo;
import modelo.personas.Usuario;

import java.time.LocalDate;

/**
 * Ventana donde el bibliotecario registra un prestamo, lo busca por el numero del libro
 * y modifica el usuario o las fechas.
 */
public class VentanaPrestamo {
    private GestorBiblioteca bibliotecario;
    //prestamo que se cargo con "Buscar" (es el que se va a modificar).
    private Prestamo prestamoCargado = null;

    //constructor
    public VentanaPrestamo(GestorBiblioteca bibliotecario){
        this.bibliotecario = bibliotecario;
    }

    //metodo
    public void mostrar(Stage ventanaPrincipal){
        Stage ventana = new Stage();
        ventana.initOwner(ventanaPrincipal);

        Label titulo = new Label("Préstamos");

        TextField dni = new TextField();
        dni.setPromptText("Dni del Usuario");

        TextField numeroLibro = new TextField();
        numeroLibro.setPromptText("Numero Identificador del libro");

        //las fechas solo se usan para buscar y modificar (al registrar se usa la fecha de hoy).
        DatePicker fechaPrestamo = new DatePicker();
        fechaPrestamo.setPromptText("Fecha del préstamo");

        DatePicker fechaDevolucion = new DatePicker();
        fechaDevolucion.setPromptText("Fecha de devolución");

        Button registrar = new Button("Registrar");
        Button buscar = new Button("Buscar");
        Button modificar = new Button("Modificar");
        Button limpiar = new Button("Limpiar");
        Button volver = new Button("Volver");

        //estilo
        String estiloCampo = "-fx-background-radius: 8px;" + "-fx-font-size: 14px;" + "-fx-padding: 10px;";

        TextField [] campos = {dni, numeroLibro};

        for (TextField campo : campos){
            campo.setStyle(estiloCampo);
            campo.setMaxWidth(300);
        }

        DatePicker [] fechas = {fechaPrestamo, fechaDevolucion};

        for (DatePicker fecha : fechas){
            fecha.setStyle("-fx-font-size: 14px;");
            fecha.setPrefWidth(300);
            fecha.setMaxWidth(300);
        }

        String estiloBoton = "-fx-background-color: #E6DDF2;" + "-fx-text-fill: #43245E;" + "-fx-font-size: 15px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 10px;" + "-fx-cursor: hand;";

        Button [] botones = {registrar, buscar, modificar, limpiar};

        for (Button boton : botones){
            boton.setStyle(estiloBoton);
            boton.setPrefSize(145, 40);
        }
        volver.setStyle(estiloBoton);
        volver.setPrefSize(300, 40);

        titulo.setStyle("-fx-font-size: 26px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");

        //acciones

        //ALTA: registra un prestamo nuevo (la fecha es hoy y se devuelve a los 15 dias).
        registrar.setOnAction(e -> {
            try {
                if (dni.getText().isEmpty() || numeroLibro.getText().isEmpty()) {
                    mostrarMensaje("Error", "Complete el DNI y el número del libro.");
                    return;
                }
                int numero = Integer.parseInt(numeroLibro.getText());
                boolean resultado = bibliotecario.agregarPrestamo(dni.getText(), numero);
                if (resultado) {
                    mostrarMensaje("Préstamo registrado", "El préstamo se registró correctamente.");
                    limpiarTodo(dni, numeroLibro, fechaPrestamo, fechaDevolucion);
                }
            } catch (NumberFormatException ex) {
                mostrarMensaje("Error", "El número del libro debe ser un número entero.");
            } catch (Exception ex) {
                mostrarMensaje("No se pudo registrar el préstamo", ex.getMessage());
            }
        });

        //CONSULTA: busca el prestamo del libro y carga los datos en las cajas.
        buscar.setOnAction(e -> {
            try {
                if (numeroLibro.getText().isEmpty()) {
                    mostrarMensaje("Error", "Ingrese el número del libro.");
                    return;
                }
                int numero = Integer.parseInt(numeroLibro.getText());
                Libro libro = bibliotecario.buscarLibro(numero);
                Prestamo prestamo = (libro == null) ? null : bibliotecario.buscarPrestamoDeLibro(libro);

                if (prestamo == null) {
                    prestamoCargado = null;
                    mostrarMensaje("Error", "Ese libro no está prestado.");
                    return;
                }
                prestamoCargado = prestamo;
                dni.setText(prestamo.getUsuario().getDni());
                fechaPrestamo.setValue(prestamo.getFechaPrestamo());
                fechaDevolucion.setValue(prestamo.getFechaDevolucion());

            } catch (NumberFormatException ex) {
                mostrarMensaje("Error", "El número del libro debe ser un número entero.");
            }
        });

        //MODIFICACION: cambia el usuario y las fechas del prestamo que se busco.
        //el libro no se puede cambiar: si se equivocaron de libro, se devuelve y se presta de nuevo.
        modificar.setOnAction(e -> {
            if (prestamoCargado == null) {
                mostrarMensaje("Error", "Primero busque el préstamo por el número del libro.");
                return;
            }
            if (dni.getText().isEmpty() || fechaPrestamo.getValue() == null || fechaDevolucion.getValue() == null) {
                mostrarMensaje("Error", "Complete el DNI y las dos fechas.");
                return;
            }
            try {
                Usuario usuario = bibliotecario.buscarUsuario(dni.getText());
                if (usuario == null) {
                    mostrarMensaje("Error", "No existe un usuario con ese DNI.");
                    return;
                }
                Prestamo nuevo = new Prestamo(prestamoCargado.getNumeroPrestamo(), usuario, prestamoCargado.getLibro(),
                        fechaPrestamo.getValue(), fechaDevolucion.getValue());

                bibliotecario.modificarPrestamo(prestamoCargado.getNumeroPrestamo(), nuevo);
                mostrarMensaje("Modificación exitosa", "El préstamo se actualizó correctamente.");

            } catch (Exception ex) {
                mostrarMensaje("No se pudo modificar el préstamo", ex.getMessage());
            }
        });

        limpiar.setOnAction(e -> limpiarTodo(dni, numeroLibro, fechaPrestamo, fechaDevolucion));

        volver.setOnAction(e -> ventana.close());

        //los botones van de a dos por fila
        HBox fila1 = new HBox(10, registrar, buscar);
        HBox fila2 = new HBox(10, modificar, limpiar);

        HBox[] filas = {fila1, fila2};
        for (HBox fila : filas) {
            fila.setAlignment(Pos.CENTER);
        }

        VBox contenido = new VBox(15);
        contenido.setAlignment(Pos.CENTER);

        contenido.getChildren().addAll(titulo, dni, numeroLibro, fechaPrestamo, fechaDevolucion, fila1, fila2, volver);

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

        Scene escena = new Scene(root, 450, 600);

        ventana.setTitle("Préstamos");
        ventana.setScene(escena);
        ventana.setResizable(false);
        ventana.show();
    }

    //borra las cajas y olvida el prestamo que se habia buscado.
    private void limpiarTodo(TextField dni, TextField numeroLibro, DatePicker fechaPrestamo, DatePicker fechaDevolucion) {
        dni.clear();
        numeroLibro.clear();
        fechaPrestamo.setValue(null);
        fechaDevolucion.setValue(null);
        prestamoCargado = null;
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Biblioteca");
        alerta.setHeaderText(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
