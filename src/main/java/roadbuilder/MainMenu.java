package roadbuilder;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import roadbuilder.model.ButtonType;
import roadbuilder.util.ImageLoader;
import roadbuilder.app.ProgressManager;

public class MainMenu extends GameApplication {
    private static MainMenu instance;
    private ImageView playButton;
    private ImageView exitButton;

    public static MainMenu getInstance() {
        if (instance == null) {
            instance = new MainMenu();
        }
        return instance;
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Game Menu");
    }

    @Override
    protected void initUI() {
        VBox menuBox = new VBox(10);
        menuBox.setTranslateX(350);
        menuBox.setTranslateY(250);

        playButton = new ImageView(ImageLoader.getButtonImage(ButtonType.PLAY));
        playButton.setOnMouseEntered(event -> playButton.setImage(ImageLoader.getButtonImage(ButtonType.PLAY_HOVER)));
        playButton.setOnMouseExited(event -> playButton.setImage(ImageLoader.getButtonImage(ButtonType.PLAY)));

        exitButton = new ImageView(ImageLoader.getButtonImage(ButtonType.EXIT));
        exitButton.setTranslateY(-10);
        exitButton.setOnMouseEntered(event -> exitButton.setImage(ImageLoader.getButtonImage(ButtonType.EXIT_HOVER)));
        exitButton.setOnMouseExited(event -> exitButton.setImage(ImageLoader.getButtonImage(ButtonType.EXIT)));

        playButton.setOnMouseClicked(event -> {
            FXGL.getGameScene().clearUINodes();
            showLevelSelectionMenu();
        });
        exitButton.setOnMouseClicked(event -> System.exit(0));

        menuBox.getChildren().addAll(playButton, exitButton);
        FXGL.getGameScene().addUINode(menuBox);
    }

    public void showLevelSelectionMenu() {
        FXGL.getGameScene().clearUINodes();
        VBox levelBox = new VBox(10);
        levelBox.setTranslateX(350);
        levelBox.setTranslateY(250);

        for (int i = 1; i <= 5; i++) {
            Button levelButton = new Button();
            if (isLevelAvailable(i)) {
                levelButton.setDisable(false);
                levelButton.setStyle("-fx-base: #4CAF50;");
            } else {
                levelButton.setDisable(true);
                levelButton.setStyle("-fx-base: #f44336;");
                levelButton.setText("Locked");
            }
            int level = i;
            levelButton.setOnAction(e -> {
                if (!levelButton.isDisabled()) {
                    FXGL.getGameScene().clearUINodes();
                    startGame(level);
                }
            });
            String graphType = getRequiredGraphType(i);
            levelButton.setText("Level " + i + " - " + graphType +
                (ProgressManager.getHighestCompletedLevel() >= i ? " (Completed)" : ""));
            levelBox.getChildren().add(levelButton);
        }

        Button progressButton = new Button("Progress: Level " + ProgressManager.getHighestCompletedLevel() + " completed");
        progressButton.setDisable(true);
        progressButton.setStyle("-fx-base: #2196F3;");
        levelBox.getChildren().add(progressButton);

        FXGL.getGameScene().addUINode(levelBox);
    }

    private boolean isLevelAvailable(int level) {
        return level == 1 || ProgressManager.isLevelUnlocked(level);
    }

    private void startGame(int level) {
        if (level == 1) {
            System.out.println("Starting Level 1");
            FXGL.getGameScene().clearUINodes();
            MainGameRunner.getInstance().startLevel1();
        } else if (level == 2) {
            System.out.println("Starting Level 2");
            FXGL.getGameScene().clearUINodes();
            MainGameRunner.getInstance().startLevel2();
        } else {
            System.out.println("Starting game at level " + level);
            FXGL.getGameScene().clearUINodes();
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