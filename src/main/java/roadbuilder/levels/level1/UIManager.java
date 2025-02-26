package roadbuilder.levels.level1;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import roadbuilder.MainMenu;
import roadbuilder.app.TileComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UIManager {

    private Button backButton;

    public void initUI() {
        // Ensure the scene dimensions are correctly set
        double sceneWidth = FXGL.getGameScene().getWidth();
        double sceneHeight = FXGL.getGameScene().getHeight();

        System.out.println("Scene width: " + sceneWidth + ", Scene height: " + sceneHeight);

        // Create a "Back to Menu" button with styling so it is visually prominent
        backButton = new Button("Vissza a menübe");
        // Default style
        backButton.setStyle("-fx-background-color: lightgray; -fx-font-size: 14px;");
        backButton.setPrefWidth(150);
        backButton.setPrefHeight(40);
        // Position the button near the top-right corner
        backButton.setTranslateX(sceneWidth - 170);
        backButton.setTranslateY(50);
        // Set the button visible by default
        backButton.setVisible(true);

        // Add hover effect to change style on mouse enter and exit
        backButton.setOnMouseEntered((MouseEvent event) -> {
            backButton.setStyle("-fx-background-color: darkgray; -fx-font-size: 14px;");
        });
        backButton.setOnMouseExited((MouseEvent event) -> {
            backButton.setStyle("-fx-background-color: lightgray; -fx-font-size: 14px;");
        });

        // Set the mouse click event to navigate back to the level selection menu and remove tile entities
        backButton.setOnMouseClicked((MouseEvent event) -> {
            // Create a copy of current entities to avoid ConcurrentModificationException.
            List<Entity> currentEntities = new ArrayList<>(FXGL.getGameWorld().getEntities());
            currentEntities.stream()
                .filter(Objects::nonNull)
                .filter(entity -> entity.hasComponent(TileComponent.class))
                .forEach(entity -> FXGL.getGameWorld().removeEntity(entity));

            // Open the level selection menu
            MainMenu.getInstance().showLevelSelectionMenu();
            System.out.println("Back button clicked: Navigated to level selection menu and removed game board tiles.");
        });

        // Ensure the back button is added to the UI on the JavaFX Application Thread
        Platform.runLater(() -> FXGL.getGameScene().addUINode(backButton));

        // Optionally remove the key event handler if it is not needed at all
        // If you wish to keep tracking key events for debugging add them here
    }
}