package brickGame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

public class Sound {

    private final MediaPlayer mediaPlayer;
    private boolean soundOn = true;

    public Sound() {
        Media music = new Media(Objects.requireNonNull(getClass().getResource("/SFX/Background Music.mp3")).toString());
        mediaPlayer = new MediaPlayer(music);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.2);
        mediaPlayer.play();
    }

    public void playSound(String soundFilePath) {
        if (!soundOn) {
            return;
        }
        Media sound = new Media(Objects.requireNonNull(getClass().getResource("/SFX/" + soundFilePath)).toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    public void musicOn() {
        mediaPlayer.play();
    }

    public void musicOff() {
        mediaPlayer.stop();
    }

    public void toggleSound() {
        soundOn = !soundOn;
    }
}
