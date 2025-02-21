package roadbuilder;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import roadbuilder.model.CityRoadGraphModel;
import roadbuilder.util.ImageLoader;
import roadbuilder.model.ButtonType;
import roadbuilder.MainGameRunner;
import roadbuilder.levels.level1.GraphTypeDetector;
import java.util.List;
import java.util.ArrayList;

public class MainMenu extends GameApplication {

    private CityRoadGraphModel graphModel;
    private ImageView playButton;
    private ImageView exitButton;
    private List<Integer> completedLevels = new ArrayList<>();

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

        // Play button setup
        playButton = new ImageView(ImageLoader.getButtonImage(ButtonType.PLAY));

        // Exit button setup
        exitButton = new ImageView(ImageLoader.getButtonImage(ButtonType.EXIT));
        exitButton.setTranslateY(-10); // Position above play button

        // Hover effect for Play button
        playButton.setOnMouseEntered(event -> {
            playButton.setImage(ImageLoader.getButtonImage(ButtonType.PLAY_HOVER));
        });

        playButton.setOnMouseExited(event -> {
            playButton.setImage(ImageLoader.getButtonImage(ButtonType.PLAY));
        });

        // Hover effect for Exit button
        exitButton.setOnMouseEntered(event -> {
            exitButton.setImage(ImageLoader.getButtonImage(ButtonType.EXIT_HOVER));
        });

        exitButton.setOnMouseExited(event -> {
            exitButton.setImage(ImageLoader.getButtonImage(ButtonType.EXIT));
        });

        // Click handlers
        playButton.setOnMouseClicked(event -> showLevelSelectionMenu());
        exitButton.setOnMouseClicked(event -> {
            System.exit(0);
        });

        menuBox.getChildren().addAll(playButton, exitButton);
        FXGL.getGameScene().addUINode(menuBox);
    }

    private void showLevelSelectionMenu() {
        FXGL.getGameScene().clearUINodes();

        VBox levelBox = new VBox(10);
        levelBox.setTranslateX(350);
        levelBox.setTranslateY(250);

        // Add levels based on completion
        for (int i = 1; i <= 5; i++) {
            Button levelButton = new Button("Level " + i);
            levelButton.setDisable(true); // Default disabled

            if (isLevelAvailable(i)) {
                levelButton.setDisable(false);
                levelButton.setStyle("-fx-base: #4CAF50;"); // Green color for available levels
            } else {
                levelButton.setStyle("-fx-base: #f44336;"); // Red color for locked levels
                levelButton.setText("Locked");
            }

            int level = i;
            levelButton.setOnAction(e -> {
                if (!levelButton.isDisabled()) {
                    startGame(level);
                    FXGL.getGameScene().clearUINodes();
                }
            });

            // Add graph type requirement information
            String graphType = getRequiredGraphType(level);
            levelButton.setText("Level " + i + " - " + graphType);

            levelBox.getChildren().add(levelButton);
        }

        // Add progress message
        Button progressButton = new Button("Progress: Level " + getLastCompletedLevel() + " completed");
        progressButton.setDisable(true);
        progressButton.setStyle("-fx-base: #2196F3;");
        levelBox.getChildren().add(progressButton);

        FXGL.getGameScene().addUINode(levelBox);
    }

    private boolean isLevelAvailable(int level) {
        if (level == 1) {
            return true; // Level 1 is always available
        }
        return completedLevels.contains(level - 1);
    }

    private int getLastCompletedLevel() {
        if (completedLevels.isEmpty()) {
            return 0;
        }
        return completedLevels.get(completedLevels.size() - 1);
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

    public void markLevelAsCompleted(int level) {
        if (!completedLevels.contains(level)) {
            completedLevels.add(level);
            System.out.println("Level " + level + " completed!");
        }
    }

    private String getRequiredGraphType(int level) {
        switch (level) {
            case 1:
                return "Simple Graph";
            case 2:
                return "Complete Graph";
            case 3:
                return "Bipartite Graph";
            case 4:
                return "Complex Graph";
            case 5:
                return "Custom Graph";
            default:
                return "";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}