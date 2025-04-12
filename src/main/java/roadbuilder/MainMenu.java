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

        ImageView background = new ImageView(ImageLoader.getBackgroundImage());
        background.setFitWidth(800);
        background.setFitHeight(600);

        VBox menuBox = new VBox(10);

        menuBox.setTranslateX(350);
        menuBox.setTranslateY(170);

        playButton = new ImageView(ImageLoader.getButtonImage(ButtonType.PLAY));
        playButton.setOnMouseEntered(event -> playButton.setImage(ImageLoader.getButtonImage(ButtonType.PLAY_HOVER)));
        playButton.setOnMouseExited(event -> playButton.setImage(ImageLoader.getButtonImage(ButtonType.PLAY)));
        playButton.setOnMouseClicked(event -> {
            FXGL.getGameScene().clearUINodes();
            showLevelSelectionMenu();
        });

        settingsButton = new ImageView(ImageLoader.getButtonImage(ButtonType.SETTINGS));
        settingsButton.setOnMouseEntered(event -> settingsButton.setImage(ImageLoader.getButtonImage(ButtonType.SETTINGS_HOVER)));
        settingsButton.setOnMouseExited(event -> settingsButton.setImage(ImageLoader.getButtonImage(ButtonType.SETTINGS)));

        settingsButton.setOnMouseClicked(event -> {
            roadbuilder.SettingsMenu.show();
        });

        exitButton = new ImageView(ImageLoader.getButtonImage(ButtonType.EXIT));
        exitButton.setTranslateY(-10);
        exitButton.setOnMouseEntered(event -> exitButton.setImage(ImageLoader.getButtonImage(ButtonType.EXIT_HOVER)));
        exitButton.setOnMouseExited(event -> exitButton.setImage(ImageLoader.getButtonImage(ButtonType.EXIT)));
        exitButton.setOnMouseClicked(event -> System.exit(0));

        menuBox.getChildren().addAll(playButton, settingsButton, exitButton);

        initSoundButton();

        FXGL.getGameScene().addUINode(background);
        FXGL.getGameScene().addUINode(menuBox);
        FXGL.getGameScene().addUINode(soundButton);
    }

    private void initSoundButton() {
        soundButton = new ImageView(ImageLoader.getButtonImage(ButtonType.SOUND));

        soundButton.setFitWidth(50);
        soundButton.setFitHeight(50);

        soundButton.setPickOnBounds(true);

        soundButton.setTranslateX(740);
        soundButton.setTranslateY(540);

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
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.playBackgroundMusic();
    }

    public void showLevelSelectionMenu() {
        FXGL.getGameScene().clearUINodes();

        ImageView levelBackground = new ImageView(ImageLoader.getLevelMenuBackgroundImage());
        levelBackground.setFitWidth(800);
        levelBackground.setFitHeight(600);

        VBox levelBox = new VBox(10);
        levelBox.setTranslateX(350);
        levelBox.setTranslateY(250);

        for (int i = 1; i <= 5; i++) {
            Button levelButton = new Button();
            if (i <= ProgressManager.getHighestCompletedLevel()) {
                levelButton.setDisable(true);
                levelButton.setStyle("-fx-base: #f44336;");
                levelButton.setText("Level " + i + " - Locked (Completed)");
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

        Button backToMenuButton = new Button("Back to Menu");
        backToMenuButton.setStyle("-fx-base: #009688;");
        backToMenuButton.setTranslateX(700);
        backToMenuButton.setTranslateY(20);
        backToMenuButton.setOnAction(e -> {
            FXGL.getGameScene().clearUINodes();
            initUI();
        });

        if (soundButton == null) {
            initSoundButton();
        }

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
        } else if (level == 3) {
            System.out.println("Starting Level 3");
            MainGameRunner.getInstance().startLevel3();
        } else if (level == 4) {
            System.out.println("Starting Level 4");
            MainGameRunner.getInstance().startLevel4();
        } else if (level == 5) {
            System.out.println("Starting Level 5");
            MainGameRunner.getInstance().startLevel5();
        } else {
            System.out.println("Starting game at level " + level);
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