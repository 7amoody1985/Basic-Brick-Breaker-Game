package brickGame;


public class GameEngine {

    public boolean isStopped = true;
    private OnAction onAction;
    private int fps = 60;
    private Thread updateThread;
    private Thread physicsThread;
    private long time = 0;
    private Thread timeThread;

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * @param fps set fps and we convert it to millisecond
     */
    public void setFps(int fps) {
        this.fps = 1000 / fps;
    }

    private synchronized void Update() {
        updateThread = new Thread(() -> {
            while (!updateThread.isInterrupted()) {
                try {
                    onAction.onUpdate();
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        updateThread.start();
    }

    private void Initialize() {
        onAction.onInit();
    }

        private synchronized void PhysicsCalculation() {
            physicsThread = new Thread(() -> {
                while (!physicsThread.isInterrupted()) {
                    try {
                        onAction.onPhysicsUpdate();
                        Thread.sleep(fps);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

        physicsThread.start();

    }

    public void start() {
        time = 0;
        Initialize();
        Update();
        PhysicsCalculation();
        TimeStart();
        isStopped = false;
    }

    public void stop() {
        if (!isStopped) {
            isStopped = true;
            updateThread.stop();
            physicsThread.stop();
            timeThread.stop();
        }
    }

    private void TimeStart() {
        timeThread = new Thread(() -> {
            try {
                while (true) {
                    time++;
                    onAction.onTime(time);
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        timeThread.start();
    }


    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }

}