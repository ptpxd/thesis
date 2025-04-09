package roadbuilder.levels.level1;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lombok.Getter;
import roadbuilder.MainMenu;
import roadbuilder.MainGameRunner;
import roadbuilder.app.TileComponent;
import roadbuilder.model.ButtonType;
import roadbuilder.util.ImageLoader;
import roadbuilder.util.SoundManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UIManager {
    private Button backButton;
    @Getter
    private ImageView soundButton;
    private boolean soundMuted = false;

    public void initUI() {
        double sceneWidth = FXGL.getGameScene().getWidth();
        double sceneHeight = FXGL.getGameScene().getHeight();

        // Create back button styled "Back to Menu"
        backButton = new Button("Back to Menu");
        backButton.setStyle("-fx-base: #009688;");
        backButton.setPrefWidth(150);
        backButton.setPrefHeight(40);
        // Place the button near the top-right corner; using fixed values as per design.
        backButton.setTranslateX(600);
        backButton.setTranslateY(20);
        backButton.setOnAction(e -> {
            FXGL.getGameScene().clearUINodes();
            // Return to the level selection menu instead of the main menu
            MainMenu.getInstance().showLevelSelectionMenu();
        });

        // Initialize the SOUND button
        initSoundButton();

        // Add UI nodes (back button and sound button) to the game scene on the JavaFX Application Thread.
        Platform.runLater(() -> {
            FXGL.getGameScene().addUINode(backButton);
            FXGL.getGameScene().addUINode(soundButton);
        });
    }

    // Initializes the soundButton with specified style and position.
    private void initSoundButton() {
        soundButton = new ImageView(ImageLoader.getButtonImage(ButtonType.SOUND));
        // Set size to 50x50
        soundButton.setFitWidth(50);
        soundButton.setFitHeight(50);
        // Make entire rectangular area clickable even if parts are transparent.
        soundButton.setPickOnBounds(true);
        // Position the SOUND button in the bottom-right corner with a 10px margin:
        // For example, if sceneWidth = 800 and sceneHeight = 600,
        // x = 800 - 50 - 10 = 740, y = 600 - 50 - 10 = 540.
        // Here we use fixed values as provided in the design.
        soundButton.setTranslateX(500);
        soundButton.setTranslateY(20);
        // Toggle sound state on click.
        soundButton.setOnMouseClicked((MouseEvent event) -> {
            SoundManager soundManager = SoundManager.getInstance();
            if (!soundMuted) {
                soundManager.muteSound();
                soundButton.setImage(ImageLoader.getButtonImage(ButtonType.SOUND_MUTED));
                soundMuted = true;
            } else {
                soundManager.unMuteSound();
                soundButton.setImage(ImageLoader.getButtonImage(ButtonType.SOUND));
                soundMuted = false;
            }
        });
    }

    /**
     * Triggers the back button action.
     * Removes board-related entities, clears roads, and redirects to the level selection menu.
     */
    public void triggerBackButton() {
        List<Entity> currentEntities = new ArrayList<>(FXGL.getGameWorld().getEntities());
        currentEntities.stream()
                .filter(Objects::nonNull)
                .filter(entity -> entity.hasComponent(TileComponent.class))
                .forEach(entity -> FXGL.getGameWorld().removeEntity(entity));
        MainGameRunner.getInstance().clearRoads();
        MainMenu.getInstance().showLevelSelectionMenu();
        System.out.println("UIManager - triggerBackButton executed: Navigated to level selection menu and removed board tiles.");
    }
}