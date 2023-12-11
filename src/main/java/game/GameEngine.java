package game;

import ui.UI;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * The GameEngine class is responsible for managing the game loop.
 * It handles the initialization, update, physics update, and time update of the game.
 * It also manages the game's frames per second (fps) and stop status.
 */
public class GameEngine {

    private final BooleanProperty isStopped = new SimpleBooleanProperty(true);
    private final IntegerProperty fps = new SimpleIntegerProperty();
    private final UI ui;
    private OnAction onAction;
    private long time = 0;

    /**
     * Constructs a new GameEngine object.
     *
     * @param ui the UI instance.
     */
    public GameEngine(UI ui) {
        this.ui = ui;
    }

    /**
     * Sets the OnAction instance for the game.
     *
     * @param onAction the OnAction instance.
     */
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * Returns the frames per second of the game.
     *
     * @return the frames per second of the game.
     */
    public int getFps() {
        return fps.get();
    }

    /**
     * Sets the frames per second of the game.
     *
     * @param fps the frames per second to set.
     */
    public void setFps(int fps) {
        this.fps.set(fps);
    }

    /**
     * Starts the game loop in a new thread.
     */
    public void start() {
        isStopped.set(false);
        new Thread(this::gameLoop).start();
    }

    /**
     * Stops the game loop if it is not already stopped.
     */
    public void stop() {
        if (!isStopped.get()) {
            isStopped.set(true);
        }
    }

    /**
     * Returns the stop status of the game.
     *
     * @return the stop status of the game.
     */
    public boolean isStopped() {
        return isStopped.get();
    }

    /**
     * The loop of the game.
     * It handles the initialization, update, physics update, and time update of the game.
     * It also manages the frames per second of the game.
     */
    private void gameLoop() {
        while (!isStopped.get()) {
            long startTime = System.currentTimeMillis();

            onAction.onInit();
            onAction.onUpdate();
            onAction.onPhysicsUpdate();
            onAction.onTime(time++);

            long endTime = System.currentTimeMillis();
            long deltaTime = endTime - startTime;

            if (deltaTime < 1000 / fps.get()) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(1000 / fps.get() - deltaTime);
                    ui.updateFPS(fps.get());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * The OnAction interface is responsible for handling the game's actions.
     * It handles the initialization, update, physics update, and time update of the game.
     */
    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }

}