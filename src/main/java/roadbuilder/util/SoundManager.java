package roadbuilder.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SoundManager {
    private static SoundManager instance;
    private MediaPlayer backgroundMusicPlayer;

    private SoundManager() {
        loadBackgroundMusic();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void loadBackgroundMusic() {
        try {
            String musicPath = "/roadbuilder/sounds/background_music.mp3";
            java.net.URL resource = getClass().getResource(musicPath);
            if (resource == null) {
                throw new RuntimeException("Background music file not found at: " + musicPath);
            }
            Path path = Paths.get(resource.toURI());
            Media media = new Media(path.toUri().toString());
            backgroundMusicPlayer = new MediaPlayer(media);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Error loading background music", e);
        }
    }

    public void playBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundMusicPlayer.play();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
    }
}