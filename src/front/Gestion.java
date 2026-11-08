package front;
import front.ventanasBibliotecario.VentanaBibliotecario;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert;
import bibliotecario.Bibliotecario;

public class Gestion  extends Application{

    private Bibliotecario servicioBibliotecario = new Bibliotecario();

    //el metodo tiene que llamarse start porque extiende de application
    public void start(Stage stage){
        // se carga la imagen de fondo
        Image imagen = new Image(getClass().getResourceAsStream("/imagenes/fondo.png"));

        // se crea la imageView
        ImageView fondo = new ImageView(imagen);
        fondo.setFitWidth(1200);
        fondo.setFitHeight(700);
        fondo.setPreserveRatio(false);

        //se crea el titulo
        Label titulo = new Label("Sistema de Biblioteca");
        titulo.setStyle("-fx-font-size: 32px;" + "-fx-font-weight: bold;" + "-fx-text-fill: white;");

        //se crea los botones
        Button administrador = new Button("ADMINISTRADOR");
        Button bibliotecario = new Button("BIBLIOTECARIO");
        Button usuario = new Button("USUARIO");

        //se cambia el estilo de los botones
        String estilo = "-fx-background-color: #E6DDF2;" + "-fx-text-fill: #43245E;" + "-fx-font-size: 18px;" + "-fx-font-weight: bold;" + "-fx-background-radius: 10px;" + "-fx-border-color: #9875B5;" + "-fx-border-radius: 10px;" + "-fx-border-width: 1.5px;" + "-fx-cursor: hand;";

        administrador.setStyle(estilo);
        bibliotecario.setStyle(estilo);
        usuario.setStyle(estilo);

        administrador.setPrefSize(280, 50);
        bibliotecario.setPrefSize(280, 50);
        usuario.setPrefSize(280, 50);

        //acciones provisionales
        administrador.setOnAction(e -> mostrarMensaje("administrador"));
        bibliotecario.setOnAction(e -> new VentanaBibliotecario(servicioBibliotecario).mostrar(stage));
        usuario.setOnAction(e -> mostrarMensaje("usuario"));

        //organizar los elementos
        VBox menu = new VBox(25);
        menu.setAlignment(Pos.CENTER);
        menu.getChildren().addAll(titulo, administrador, bibliotecario, usuario);

        //superponer los datos arriba del fondo
        StackPane root = new StackPane();
        root.getChildren().addAll(fondo, menu);

        //crea la ventana
        Scene escena = new Scene(root, 1200, 700);

        stage.setTitle("Gestion Biblioteca");
        stage.setScene(escena);
        stage.setResizable(false);
        stage.show();
    }

    // Mensaje provisional
    public void mostrarMensaje(String rol) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Biblioteca");
        alerta.setHeaderText(rol);
        alerta.setContentText("Esta sección todavía está en desarrollo.");
        alerta.showAndWait();
    }


}
