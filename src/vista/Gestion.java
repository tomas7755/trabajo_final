package vista;
import vista.usuario.VentanaIngresoUsuario;
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
import gestores.GestorBiblioteca;
import gestores.GestorPersonas;
import modelo.personas.Administrador;

/**
 * Ventana principal del sistema: el usuario elige con que rol quiere ingresar.
 */
public class Gestion  extends Application{

    private GestorBiblioteca servicioBibliotecario = new GestorBiblioteca();
    //gestor del personal, lo usa el administrador.
    private GestorPersonas gestorPersonas = new GestorPersonas();

    //el metodo tiene que llamarse start porque extiende de application
    public void start(Stage stage){
        //administrador inicial, para poder entrar al sistema la primera vez
        //(despues se va a cargar desde la base de datos).
        gestorPersonas.agregarPersona(new Administrador("11111111", "Admin", "Sistema", "admin@biblioteca.com", "admin123"));

        // se carga la imagen de fondo (el nombre del archivo es Fondo.png, con mayuscula:
        // en Windows da igual, pero en Mac/Linux si no coincide el programa no abre)
        Image imagen = new Image(getClass().getResourceAsStream("/imagenes/Fondo.png"));

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

        //acciones
        //el administrador y el bibliotecario tienen que iniciar sesion antes de entrar a su panel.
        administrador.setOnAction(e -> new VentanaIngresoPersonal(gestorPersonas, servicioBibliotecario, true).mostrar(stage));
        bibliotecario.setOnAction(e -> new VentanaIngresoPersonal(gestorPersonas, servicioBibliotecario, false).mostrar(stage));
        usuario.setOnAction(e -> new VentanaIngresoUsuario(servicioBibliotecario).mostrar(stage));

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
}