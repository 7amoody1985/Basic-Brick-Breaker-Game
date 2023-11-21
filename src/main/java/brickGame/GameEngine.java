package brickGame;


public class GameEngine {

    public boolean isStopped = true;
    private OnAction onAction;
    private int fps;
    private long time = 0;

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * @param fps set fps and we convert it to millisecond
     */
    public void setFps(int fps) {
        this.fps = fps;
    }


    public int getFps() {
        return fps;
    }


    public void start() {
        isStopped = false;
        new Thread(this::gameLoop).start();
    }



    private void gameLoop() {
        while (!isStopped) {
            long startTime = System.currentTimeMillis();

            onAction.onInit();
            onAction.onUpdate();
            onAction.onPhysicsUpdate();
            onAction.onTime(time++);

            long endTime = System.currentTimeMillis();
            long deltaTime = endTime - startTime;

            if (deltaTime < 1000 / fps) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(1000 / fps - deltaTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



    public void stop() {
        if (!isStopped) {
            isStopped = true;
        }
    }



    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }

}