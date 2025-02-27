package roadbuilder.levels.level1;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import roadbuilder.MainMenu;
import roadbuilder.MainGameRunner;
import roadbuilder.app.TileComponent;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UIManager {
    private Button backButton;

    public void initUI() {
        double sceneWidth = FXGL.getGameScene().getWidth();
        double sceneHeight = FXGL.getGameScene().getHeight();
        backButton = new Button("Vissza a menübe");
        backButton.setStyle("-fx-background-color: lightgray; -fx-font-size: 14px;");
        backButton.setPrefWidth(150);
        backButton.setPrefHeight(40);
        backButton.setTranslateX(sceneWidth - 170);
        backButton.setTranslateY(50);
        backButton.setVisible(true);
        backButton.setOnMouseEntered((MouseEvent event) -> {
            backButton.setStyle("-fx-background-color: darkgray; -fx-font-size: 14px;");
        });
        backButton.setOnMouseExited((MouseEvent event) -> {
            backButton.setStyle("-fx-background-color: lightgray; -fx-font-size: 14px;");
        });
        backButton.setOnMouseClicked((MouseEvent event) -> {
            List<Entity> currentEntities = new ArrayList<>(FXGL.getGameWorld().getEntities());
            currentEntities.stream()
                .filter(Objects::nonNull)
                .filter(entity -> entity.hasComponent(TileComponent.class))
                .forEach(entity -> FXGL.getGameWorld().removeEntity(entity));
            MainGameRunner.getInstance().clearRoads();
            MainMenu.getInstance().showLevelSelectionMenu();
            System.out.println("Back button clicked: Navigated to level selection menu and removed game board tiles.");
        });
        Platform.runLater(() -> FXGL.getGameScene().addUINode(backButton));
    }
}