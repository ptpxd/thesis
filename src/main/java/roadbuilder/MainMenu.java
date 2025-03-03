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
import roadbuilder.util.SoundManager;

/**
 * MainMenu class implements the main menu UI with adjusted button placement.
 * The buttons (Play, Settings, Exit) are positioned a bit higher than before.
 */
public class MainMenu extends GameApplication {
    private static MainMenu instance;
    private ImageView playButton;
    private ImageView settingsButton;
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
        // Create background image view
        ImageView background = new ImageView(ImageLoader.getBackgroundImage());
        background.setFitWidth(800);
        background.setFitHeight(600);

        VBox menuBox = new VBox(10);
        // Adjusted position: move the menu a bit higher (translateY decreased from 200 to 170)
        menuBox.setTranslateX(350);
        menuBox.setTranslateY(170);

        // Initialize Play button with hover effects
        playButton = new ImageView(ImageLoader.getButtonImage(ButtonType.PLAY));
        playButton.setOnMouseEntered(event -> playButton.setImage(ImageLoader.getButtonImage(ButtonType.PLAY_HOVER)));
        playButton.setOnMouseExited(event -> playButton.setImage(ImageLoader.getButtonImage(ButtonType.PLAY)));

        // Initialize Settings button with hover effects
        settingsButton = new ImageView(ImageLoader.getButtonImage(ButtonType.SETTINGS));
        settingsButton.setOnMouseEntered(event -> settingsButton.setImage(ImageLoader.getButtonImage(ButtonType.SETTINGS_HOVER)));
        settingsButton.setOnMouseExited(event -> settingsButton.setImage(ImageLoader.getButtonImage(ButtonType.SETTINGS)));
        // When the settings button is clicked, show the Settings Menu.
        settingsButton.setOnMouseClicked(event -> {
            roadbuilder.SettingsMenu.show();
        });

        // Initialize Exit button with hover effects
        exitButton = new ImageView(ImageLoader.getButtonImage(ButtonType.EXIT));
        exitButton.setTranslateY(-10);
        exitButton.setOnMouseEntered(event -> exitButton.setImage(ImageLoader.getButtonImage(ButtonType.EXIT_HOVER)));
        exitButton.setOnMouseExited(event -> exitButton.setImage(ImageLoader.getButtonImage(ButtonType.EXIT)));

        // Set actions for Play and Exit buttons
        playButton.setOnMouseClicked(event -> {
            FXGL.getGameScene().clearUINodes();
            showLevelSelectionMenu();
        });
        exitButton.setOnMouseClicked(event -> System.exit(0));

        // Add Play, Settings, and Exit buttons to the menuBox in order
        menuBox.getChildren().addAll(playButton, settingsButton, exitButton);

        // Add background and menuBox to the scene
        FXGL.getGameScene().addUINode(background);
        FXGL.getGameScene().addUINode(menuBox);
    }

    @Override
    protected void initGame() {
        // Initialize and play background music
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.playBackgroundMusic();
    }

    public void showLevelSelectionMenu() {
        FXGL.getGameScene().clearUINodes();

        VBox levelBox = new VBox(10);
        levelBox.setTranslateX(350);
        levelBox.setTranslateY(250);

        // Create buttons for levels 1 to 5.
        for (int i = 1; i <= 5; i++) {
            Button levelButton = new Button();
            // If the level is already completed, disable the button and mark it as locked.
            if (i <= ProgressManager.getHighestCompletedLevel()) {
                levelButton.setDisable(true);
                levelButton.setStyle("-fx-base: #f44336;");
                levelButton.setText("Level " + i + " - Locked (Teljesítve)");
            }
            // Else, if the level is available (unlocked), enable it.
            else if (isLevelAvailable(i)) {
                levelButton.setDisable(false);
                levelButton.setStyle("-fx-base: #4CAF50;");
                String graphType = getRequiredGraphType(i);
                levelButton.setText("Level " + i + " - " + graphType);
                int level = i;
                levelButton.setOnAction(e -> {
                    if (!levelButton.isDisabled()) {
                        FXGL.getGameScene().clearUINodes();
                        startGame(level);
                    }
                });
            } else {
                levelButton.setDisable(true);
                levelButton.setStyle("-fx-base: #f44336;");
                levelButton.setText("Level " + i + " - Locked");
            }
            levelBox.getChildren().add(levelButton);
        }

        Button progressButton = new Button("Progress: Level " + ProgressManager.getHighestCompletedLevel() + " completed");
        progressButton.setDisable(true);
        progressButton.setStyle("-fx-base: #2196F3;");
        levelBox.getChildren().add(progressButton);

        // Create Back to Menu button positioned at the top-right corner.
        Button backToMenuButton = new Button("Back to Menu");
        backToMenuButton.setStyle("-fx-base: #009688;");
        // Position the button at top-right, absolute coordinates.
        backToMenuButton.setTranslateX(700);
        backToMenuButton.setTranslateY(20);
        backToMenuButton.setOnAction(e -> {
            FXGL.getGameScene().clearUINodes();
            initUI();
        });

        FXGL.getGameScene().addUINode(levelBox);
        FXGL.getGameScene().addUINode(backToMenuButton);
    }

    private boolean isLevelAvailable(int level) {
        // Level 1 is always available; for other levels, they must be unlocked but not already completed.
        return level == 1 || (ProgressManager.isLevelUnlocked(level) && level > ProgressManager.getHighestCompletedLevel());
    }

    private void startGame(int level) {
        FXGL.getGameScene().clearUINodes();
        if (level == 1) {
            System.out.println("Starting Level 1");
            MainGameRunner.getInstance().startLevel1();
        } else if (level == 2) {
            System.out.println("Starting Level 2");
            MainGameRunner.getInstance().startLevel2();
        } else {
            System.out.println("Starting game at level " + level);
            // Additional logic for starting the level can be added here.
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