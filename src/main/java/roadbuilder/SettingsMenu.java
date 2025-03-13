package roadbuilder;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import roadbuilder.util.SoundManager;
import roadbuilder.util.ImageLoader;

/**
 * SettingsMenu class provides a simple settings UI to control game volume.
 * When opened, it shows a volume slider (0.0 to 1.0) allowing the player to adjust the background music volume.
 * It reuses the main menu background so that the same background is visible behind the settings UI.
 * All text (slider tick labels, volume label, and button text) is styled in white.
 */
public class SettingsMenu {

    // Shows the settings menu UI.
    public static void show() {
        // Clear any existing UI nodes.
        FXGL.getGameScene().clearUINodes();

        double width = FXGL.getAppWidth();
        double height = FXGL.getAppHeight();

        // Use the same background as the main menu.
        ImageView background = new ImageView(ImageLoader.getBackgroundImage());
        background.setFitWidth(width);
        background.setFitHeight(height);
        FXGL.getGameScene().addUINode(background);

        // Optionally, add a semi-transparent dark overlay to improve readability.
        Rectangle darkOverlay = new Rectangle(width, height);
        darkOverlay.setFill(Color.rgb(0, 0, 0, 0.5));  // 50% transparent black overlay
        FXGL.getGameScene().addUINode(darkOverlay);

        // Create a VBox layout to hold settings controls.
        VBox settingsBox = new VBox(15);
        settingsBox.setPadding(new Insets(20));
        settingsBox.setAlignment(Pos.CENTER);
        // Center the VBox on the screen.
        settingsBox.setTranslateX(width / 2 - 150);
        settingsBox.setTranslateY(height / 2 - 100);

        // Create a label for volume control.
        Label volumeLabel = new Label("Background Music Volume:");
        volumeLabel.setTextFill(Color.WHITE); // Set label text to white

        // Create a slider with range from 0.0 (mute) to 1.0 (max).
        Slider volumeSlider = new Slider(0.0, 1.0, SoundManager.getInstance().getVolume());
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(0.25);
        volumeSlider.setBlockIncrement(0.05);
        volumeSlider.setPrefWidth(300);
        // Ensure the slider's tick label fill is white.
        volumeSlider.setStyle("-fx-tick-label-fill: white;");

        // Listen to slider changes and update the sound volume.
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) ->
            SoundManager.getInstance().setVolume(newVal.doubleValue())
        );

        // Button to return to the main menu.
        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(e -> {
            // Clear UI nodes (removing the settings UI and its layers), then reinitialize the main menu.
            FXGL.getGameScene().clearUINodes();
            MainMenu.getInstance().initUI();
        });
        // Set the button text to white and a dark background for contrast.
        backButton.setStyle("-fx-text-fill: white; -fx-background-color: #333333;");

        settingsBox.getChildren().addAll(volumeLabel, volumeSlider, backButton);
        FXGL.getGameScene().addUINode(settingsBox);
    }
}