package roadbuilder;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Point2D;
import roadbuilder.model.CityRoadGraphModel;
import roadbuilder.MainGameRunner;

public class MainMenu extends GameApplication {

    private CityRoadGraphModel graphModel;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Game Menu");
    }

    @Override
    protected void initUI() {
        graphModel = new CityRoadGraphModel();

        VBox menuBox = new VBox(10);
        menuBox.setTranslateX(350);
        menuBox.setTranslateY(250);

        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> showLevelSelectionMenu());

        menuBox.getChildren().addAll(startButton);
        FXGL.getGameScene().addUINode(menuBox);
    }

    private void showLevelSelectionMenu() {
        VBox levelBox = new VBox(10);
        levelBox.setTranslateX(350);
        levelBox.setTranslateY(250);

        for (int i = 1; i <= 5; i++) {
            Button levelButton = new Button("Level " + i);
            int level = i;
            levelButton.setOnAction(e -> startGame(level));
            levelBox.getChildren().add(levelButton);
        }

        FXGL.getGameScene().clearUINodes();
        FXGL.getGameScene().addUINode(levelBox);
    }

    private void startGame(int level) {
        if (level == 1) {
            System.out.println("Starting Level 1");
            MainGameRunner.getInstance().startLevel1();
        } else {
            System.out.println("Starting game at level " + level);
            // Add logic for other levels if needed
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}