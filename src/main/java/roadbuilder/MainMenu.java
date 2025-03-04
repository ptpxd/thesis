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
 * The buttons (Play, Settings, Exit) are positioned accordingly.
 * A SOUND toggle button is placed in the bottom-right corner. On click it toggles between mute and unmute.
 */
public class MainMenu extends GameApplication {
    private static MainMenu instance;
    private ImageView playButton;
    private ImageView settingsButton;
    private ImageView exitButton;
    private ImageView soundButton;
    private boolean soundMuted = false;

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
    public void initUI() {
        // Create background image view for main menu
        ImageView background = new ImageView(ImageLoader.getBackgroundImage());
        background.setFitWidth(800);
        background.setFitHeight(600);

        // Create a VBox container for menu buttons (Play, Settings, Exit)
        VBox menuBox = new VBox(10);
        // Position the menu box
        menuBox.setTranslateX(350);
        menuBox.setTranslateY(170);

        // Initialize Play button with hover effects
        playButton = new ImageView(ImageLoader.getButtonImage(ButtonType.PLAY));
        playButton.setOnMouseEntered(event -> playButton.setImage(ImageLoader.getButtonImage(ButtonType.PLAY_HOVER)));
        playButton.setOnMouseExited(event -> playButton.setImage(ImageLoader.getButtonImage(ButtonType.PLAY)));
        playButton.setOnMouseClicked(event -> {
            FXGL.getGameScene().clearUINodes();
            showLevelSelectionMenu();
        });

        // Initialize Settings button with hover effects
        settingsButton = new ImageView(ImageLoader.getButtonImage(ButtonType.SETTINGS));
        settingsButton.setOnMouseEntered(event -> settingsButton.setImage(ImageLoader.getButtonImage(ButtonType.SETTINGS_HOVER)));
        settingsButton.setOnMouseExited(event -> settingsButton.setImage(ImageLoader.getButtonImage(ButtonType.SETTINGS)));
        // Show settings menu on click
        settingsButton.setOnMouseClicked(event -> {
            roadbuilder.SettingsMenu.show();
        });

        // Initialize Exit button with hover effects
        exitButton = new ImageView(ImageLoader.getButtonImage(ButtonType.EXIT));
        exitButton.setTranslateY(-10);
        exitButton.setOnMouseEntered(event -> exitButton.setImage(ImageLoader.getButtonImage(ButtonType.EXIT_HOVER)));
        exitButton.setOnMouseExited(event -> exitButton.setImage(ImageLoader.getButtonImage(ButtonType.EXIT)));
        exitButton.setOnMouseClicked(event -> System.exit(0));

        // Add Play, Settings, and Exit buttons to the container
        menuBox.getChildren().addAll(playButton, settingsButton, exitButton);

        // Initialize SOUND toggle button
        initSoundButton();

        // Add background, the menu container, and the SOUND button to the scene
        FXGL.getGameScene().addUINode(background);
        FXGL.getGameScene().addUINode(menuBox);
        FXGL.getGameScene().addUINode(soundButton);
    }

    // Helper to (re)initialize the soundButton.
    private void initSoundButton() {
        soundButton = new ImageView(ImageLoader.getButtonImage(ButtonType.SOUND));
        // Set preferred size for the sound button (assumption 50x50)
        soundButton.setFitWidth(50);
        soundButton.setFitHeight(50);
        // Ensure the entire rectangular area is clickable even if parts are transparent.
        soundButton.setPickOnBounds(true);
        // Position the SOUND button in the bottom-right corner with a 10px margin:
        // x = 800 - 50 - 10 = 740, y = 600 - 50 - 10 = 540
        soundButton.setTranslateX(740);
        soundButton.setTranslateY(540);
        // Set on-click event to toggle sound state using muteSound() and unMuteSound() methods.
        soundButton.setOnMouseClicked(event -> {
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

    @Override
    protected void initGame() {
        // Start background music
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.playBackgroundMusic();
    }

    public void showLevelSelectionMenu() {
        FXGL.getGameScene().clearUINodes();

        // Create background image for level menu using levelMenuBackgroundImage
        ImageView levelBackground = new ImageView(ImageLoader.getLevelMenuBackgroundImage());
        levelBackground.setFitWidth(800);
        levelBackground.setFitHeight(600);

        // Container for level selection buttons
        VBox levelBox = new VBox(10);
        levelBox.setTranslateX(350);
        levelBox.setTranslateY(250);

        for (int i = 1; i <= 5; i++) {
            Button levelButton = new Button();
            // Set button state based on level progression
            if (i <= ProgressManager.getHighestCompletedLevel()) {
                levelButton.setDisable(true);
                levelButton.setStyle("-fx-base: #f44336;");
                levelButton.setText("Level " + i + " - Locked (Teljesítve)");
            } else if (isLevelAvailable(i)) {
                levelButton.setDisable(false);
                levelButton.setStyle("-fx-base: #4CAF50;");
                String graphType = getRequiredGraphType(i);
                levelButton.setText("Level " + i + " - " + graphType);
                int level = i;
                levelButton.setOnAction(e -> {
                    FXGL.getGameScene().clearUINodes();
                    startGame(level);
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

        // Create Back to Menu button at the top-right corner
        Button backToMenuButton = new Button("Back to Menu");
        backToMenuButton.setStyle("-fx-base: #009688;");
        backToMenuButton.setTranslateX(700);
        backToMenuButton.setTranslateY(20);
        backToMenuButton.setOnAction(e -> {
            FXGL.getGameScene().clearUINodes();
            initUI();
        });

        // Before re-adding the SOUND button, ensure it is not null.
        if (soundButton == null) {
            initSoundButton();
        }

        // Add nodes to the scene, ensuring none are null.
        if (levelBackground != null) {
            FXGL.getGameScene().addUINode(levelBackground);
        }
        if (levelBox != null) {
            FXGL.getGameScene().addUINode(levelBox);
        }
        if (backToMenuButton != null) {
            FXGL.getGameScene().addUINode(backToMenuButton);
        }
        if (soundButton != null) {
            FXGL.getGameScene().addUINode(soundButton);
        }
    }

    private boolean isLevelAvailable(int level) {
        // Level 1 is always available; others must be unlocked and not completed.
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
            // Additional game initialization for other levels can be added here.
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