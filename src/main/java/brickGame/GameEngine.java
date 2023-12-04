package brickGame;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;


public class GameEngine {

    private final BooleanProperty isStopped = new SimpleBooleanProperty(true);
    private final IntegerProperty fps = new SimpleIntegerProperty();
    private OnAction onAction;
    private long time = 0;

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    public int getFps() {
        return fps.get();
    }

    /**
     * @param fps set fps and we convert it to millisecond
     */

    public void setFps(int fps) {
        this.fps.set(fps);
    }

    public void start() {
        isStopped.set(false);
        new Thread(this::gameLoop).start();
    }

    public void stop() {
        if (!isStopped.get()) {
            isStopped.set(true);
        }
    }
    
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
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }

}