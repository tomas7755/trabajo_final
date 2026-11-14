package vista.bibliotecario;

import excepciones.ErrorBuscandoLibro;
import gestores.GestorBiblioteca;
import modelo.Libro;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Ventana donde el bibliotecario hace el ABMC de los libros:
 * registrar (alta), buscar por numero (consulta), modificar, dar de baja (baja logica) y reactivar.
 */
public class VentanaLibro {

    private GestorBiblioteca bibliotecario;
    //constructor
    public VentanaLibro (GestorBiblioteca bibliotecario){
        this.bibliotecario = bibliotecario;
    }
    //metodo
    public void mostrar(Stage ventanaPrincipal){
        Stage ventana = new Stage();
        ventana.initOwner(ventanaPrincipal);

        Label titulo = new Label("Gestionar libros");

        //cajas para rellenar los datos del libro.
        TextField identificador = new TextField();
        identificador.setPromptText("Numero identificador");

        TextField nombre = new TextField();
        nombre.setPromptText("Nombre del libro");

        TextField autor = new TextField();
        autor.setPromptText("Nombre del autor");

        TextField genero = new TextField();
        genero.setPromptText("Género (novela, cuento, poesía...)");

        //muestra el estado del libro buscado.
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

        TextField [] campos = {identificador, nombre, autor, genero};

        for (TextField campo : campos){
            campo.setStyle(estiloCampo);
            campo.setMaxWidth(300);
        }

        String estiloBoton = "-fx-background-color: #E6DDF2;" + "-fx-text-fill: #43245E;" + "-fx-font-size: 15px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 10px;" + "-fx-cursor: hand;";

        Button [] botones = {registrar, buscar, modificar, darDeBaja, reactivar, limpiar};

        for (Button boton : botones){
            boton.setStyle(estiloBoton);
            boton.setPrefSize(145, 40);
        }
        volver.setStyle(estiloBoton);
        volver.setPrefSize(300, 40);

        titulo.setStyle("-fx-font-size: 26px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");
        estado.setStyle("-fx-font-size: 14px;" + "-fx-text-fill: #E6DDF2;");

        //acciones

        //ALTA: registra un libro nuevo.
        registrar.setOnAction(e -> {
            try {
                if (hayCamposVacios(campos)) {
                    mostrarMensaje("Error", "Complete todos los campos.");
                    return;
                }
                int numero = Integer.parseInt(identificador.getText());

                Libro libro = new Libro(numero, nombre.getText(), autor.getText(), genero.getText());

                if (bibliotecario.agregarLibro(libro)) {
                    mostrarMensaje("Registro exitoso", "El libro se registró correctamente.");
                    limpiarCampos(campos, estado);
                } else {
                    mostrarMensaje("Error", "Ya existe un libro con ese identificador.");
                }
            } catch (NumberFormatException ex) {
                mostrarMensaje("Error", "El identificador debe ser un número entero.");
            }
        });

        //CONSULTA: busca por numero y carga los datos en las cajas.
        buscar.setOnAction(e -> {
            try {
                int numero = Integer.parseInt(identificador.getText());
                Libro libro = bibliotecario.buscarLibro(numero);

                if (libro == null) {
                    mostrarMensaje("Error", "No existe un libro con ese identificador.");
                    return;
                }
                nombre.setText(libro.getNombre());
                autor.setText(libro.getAutor());
                genero.setText(libro.getGenero());
                estado.setText("Estado: " + libro.getEstaDispponible() + (libro.isActivo() ? "" : " (dado de baja)"));

            } catch (NumberFormatException ex) {
                mostrarMensaje("Error", "Ingrese el número identificador del libro.");
            }
        });

        //MODIFICACION: cambia nombre, autor y genero del libro con ese numero.
        modificar.setOnAction(e -> {
            try {
                if (hayCamposVacios(campos)) {
                    mostrarMensaje("Error", "Busque el libro y complete todos los campos.");
                    return;
                }
                int numero = Integer.parseInt(identificador.getText());
                Libro modificado = new Libro(numero, nombre.getText(), autor.getText(), genero.getText());

                bibliotecario.modificarLibro(numero, modificado);
                mostrarMensaje("Modificación exitosa", "Los datos del libro se actualizaron.");

            } catch (NumberFormatException ex) {
                mostrarMensaje("Error", "El identificador debe ser un número entero.");
            } catch (ErrorBuscandoLibro ex) {
                mostrarMensaje("Error", ex.getMessage());
            }
        });

        //BAJA LOGICA: el libro se marca como inactivo (solo si esta disponible).
        darDeBaja.setOnAction(e -> {
            try {
                int numero = Integer.parseInt(identificador.getText());
                bibliotecario.eliminarLibro(numero);
                estado.setText("Estado: dado de baja");
                mostrarMensaje("Baja exitosa", "El libro fue dado de baja.");

            } catch (NumberFormatException ex) {
                mostrarMensaje("Error", "Ingrese el número identificador del libro.");
            } catch (ErrorBuscandoLibro ex) {
                mostrarMensaje("Error", ex.getMessage());
            }
        });

        //vuelve a activar un libro dado de baja.
        reactivar.setOnAction(e -> {
            try {
                int numero = Integer.parseInt(identificador.getText());
                bibliotecario.reactivarLibro(numero);
                estado.setText("Estado: activo");
                mostrarMensaje("Reactivación exitosa", "El libro fue reactivado.");

            } catch (NumberFormatException ex) {
                mostrarMensaje("Error", "Ingrese el número identificador del libro.");
            } catch (ErrorBuscandoLibro ex) {
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

        contenido.getChildren().addAll(titulo, identificador, nombre, autor, genero, estado, fila1, fila2, fila3, volver);

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

        Scene escena = new Scene(root, 450, 620);

        ventana.setTitle("Gestionar libros");
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
