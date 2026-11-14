package vista.administrador;

import gestores.GestorBiblioteca;
import gestores.GestorPersonas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.personas.Administrador;
import modelo.personas.Bibliotecario;
import modelo.personas.Persona;

/**
 * Ventana de listados del administrador: muestra el personal (bibliotecarios y administradores)
 * y los usuarios, con la opcion de ver solo los activos o los dados de baja.
 */
public class VentanaListados {

    private GestorPersonas gestorPersonas;
    private GestorBiblioteca gestorBiblioteca;

    //constructor
    public VentanaListados(GestorPersonas gestorPersonas, GestorBiblioteca gestorBiblioteca) {
        this.gestorPersonas = gestorPersonas;
        this.gestorBiblioteca = gestorBiblioteca;
    }

    //metodo
    public void mostrar(Stage ventanaPrincipal) {
        Stage ventana = new Stage();
        ventana.initOwner(ventanaPrincipal);

        Label titulo = new Label("Listados");

        //opciones de listado
        ComboBox<String> opciones = new ComboBox<>();

        //(los listados de usuarios todavia estan en desarrollo)
        opciones.getItems().addAll("Todo el personal", "Personal activo", "Bibliotecarios", "Administradores");

        opciones.setPromptText("Seleccione un listado");

        ListView<String> resultados = new ListView<>();
        resultados.setPrefSize(600, 380);
        resultados.setMaxWidth(600);

        Button volver = new Button("Volver");

        //estilo
        String estiloBoton = "-fx-background-color: #E6DDF2;" + "-fx-text-fill: #43245E;" + "-fx-font-size: 15px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 10px;" + "-fx-cursor: hand;";

        volver.setStyle(estiloBoton);
        volver.setPrefSize(300, 40);

        titulo.setStyle("-fx-font-size: 26px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");

        opciones.setStyle("-fx-font-size: 14px;");

        //accion: cada vez que se elige una opcion se carga la lista
        opciones.setOnAction(e -> {

            ObservableList<String> lista = FXCollections.observableArrayList();

            String seleccion = opciones.getValue();

            switch (seleccion) {
                case "Todo el personal":
                    for (Persona persona : gestorPersonas.listarTodas()) {
                        lista.add(formatoPersonal(persona));
                    }
                    break;
                case "Personal activo":
                    for (Persona persona : gestorPersonas.listarActivas()) {
                        lista.add(formatoPersonal(persona));
                    }
                    break;
                case "Bibliotecarios":
                    for (Bibliotecario bibliotecario : gestorPersonas.listarBibliotecarios()) {
                        lista.add(formatoPersonal(bibliotecario));
                    }
                    break;
                case "Administradores":
                    for (Administrador administrador : gestorPersonas.listarAdministradores()) {
                        lista.add(formatoPersonal(administrador));
                    }
                    break;
            }

            if (lista.isEmpty()) {
                lista.add("No hay registros para mostrar.");
            }

            resultados.setItems(lista);
        });

        volver.setOnAction(e -> ventana.close());

        VBox contenido = new VBox(15);
        contenido.setAlignment(Pos.CENTER);

        contenido.getChildren().addAll(titulo, opciones, resultados, volver);

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

        Scene escena = new Scene(root, 700, 600);

        ventana.setTitle("Listados");
        ventana.setScene(escena);
        ventana.setResizable(false);
        ventana.show();
    }

    //arma el texto de una persona del personal.
    //getTipo() es polimorfico: cada clase hija devuelve su propio tipo.
    private String formatoPersonal(Persona persona) {
        String texto = persona.getTipo() + " | DNI: " + persona.getDni() + " | " + persona.getNombre() + " " + persona.getApellido()
                + " | Email: " + persona.getEmail();
        //solo los bibliotecarios tienen legajo.
        if (persona instanceof Bibliotecario) {
            texto += " | Legajo: " + ((Bibliotecario) persona).getLegajo();
        }
        return texto + " | " + estado(persona);
    }

    private String estado(Persona persona) {
        return persona.isActivo() ? "Activo" : "Dado de baja";
    }
}