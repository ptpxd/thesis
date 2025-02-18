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

public class MainMenu extends GameApplication {

    private CityRoadGraphModel graphModel;
    private ImageView playButton;
    private ImageView exitButton;

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