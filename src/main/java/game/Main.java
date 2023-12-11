package game;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The Main class is the entry point of the application.
 * It extends the Application class from JavaFX, which is an abstract class that represents a JavaFX application.
 * The start method is overridden to launch the game.
 */
public class Main extends Application {

    /**
     * The main method is the entry point of the Java application.
     * It calls the launch method inherited from the Application class, which launches the JavaFX application.
     *
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method is called after the init method has returned, and after the system is ready for the application to begin running.
     * A Stage (a window) is created and passed as an argument to the start method.
     * The game is started on this stage.
     *
     * @param primaryStage the primary stage for this application, onto which the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        Game game = new Game();
        game.start(primaryStage);
    }
}