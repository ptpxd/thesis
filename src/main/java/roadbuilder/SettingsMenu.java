package roadbuilder;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import roadbuilder.util.SoundManager;

/**
 * SettingsMenu class provides a simple settings UI to control game volume.
 * When opened, it shows a volume slider (0.0 to 1.0) allowing the player to adjust the background music volume.
 */
public class SettingsMenu {

    // Shows the settings menu UI.
    public static void show() {
        // Clear any existing UI nodes.
        FXGL.getGameScene().clearUINodes();

        // Create a VBox layout to hold settings controls.
        VBox settingsBox = new VBox(15);
        settingsBox.setPadding(new Insets(20));
        settingsBox.setAlignment(Pos.CENTER);
        settingsBox.setTranslateX(250);
        settingsBox.setTranslateY(150);

        // Create a label for volume control.
        Label volumeLabel = new Label("Background Music Volume:");

        // Create a slider with range 0.0 (mute) to 1.0 (max)
        Slider volumeSlider = new Slider(0.0, 1.0, SoundManager.getInstance().getVolume());
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(0.25);
        volumeSlider.setBlockIncrement(0.05);
        volumeSlider.setPrefWidth(300);

        // Listen to slider changes and update the SoundManager volume.
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            SoundManager.getInstance().setVolume(newVal.doubleValue());
        });

        // Button to return to previous menu (Main Menu in this case).
        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(e -> {
            // Clear UI nodes and return to main menu.
            FXGL.getGameScene().clearUINodes();
            // Since MainMenu is using static instance pattern, retrieve instance to reinitialize the UI.
            MainMenu.getInstance().initUI();
        });

        // Add all controls to the settingsBox layout.
        settingsBox.getChildren().addAll(volumeLabel, volumeSlider, backButton);

        // Add the settingsBox to the game's UI nodes.
        FXGL.getGameScene().addUINode(settingsBox);
    }
}