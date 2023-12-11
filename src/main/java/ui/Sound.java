package ui;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

/**
 * The Sound class is responsible for managing the sound effects and background music in the game.
 * It handles the playing, stopping, and toggling of sounds.
 */
public class Sound {
    private MediaPlayer mediaPlayer;
    private boolean soundOn = true;

    /**
     * Constructs a new Sound object.
     * Initializes the MediaPlayer with the background music and starts playing it.
     */
    public Sound() {
        Platform.runLater(() -> {
            Media music = new Media(Objects.requireNonNull(getClass().getResource("/SFX/Background Music.mp3")).toString());
            mediaPlayer = new MediaPlayer(music);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.2);
            mediaPlayer.play();
        });
    }

    /**
     * Plays the sound effect specified by the file path.
     *
     * @param soundFilePath the file path of the sound effect.
     */
    public void playSound(String soundFilePath) {
        if (!soundOn) {
            return;
        }
        Platform.runLater(() -> {
            Media sound = new Media(Objects.requireNonNull(getClass().getResource("/SFX/" + soundFilePath)).toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        });
    }

    /**
     * Starts playing the background music.
     */
    public void musicOn() {
        mediaPlayer.play();
    }

    /**
     * Stops playing the background music.
     */
    public void musicOff() {
        mediaPlayer.stop();
    }

    /**
     * Toggles the sound on and off.
     */
    public void toggleSound() {
        soundOn = !soundOn;
    }
}
