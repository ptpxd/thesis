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

public class SettingsMenu {

    public static void show() {
        FXGL.getGameScene().clearUINodes();

        double width = FXGL.getAppWidth();
        double height = FXGL.getAppHeight();

        ImageView background = new ImageView(ImageLoader.getBackgroundImage());
        background.setFitWidth(width);
        background.setFitHeight(height);
        FXGL.getGameScene().addUINode(background);

        Rectangle darkOverlay = new Rectangle(width, height);
        darkOverlay.setFill(Color.rgb(0, 0, 0, 0.5));  // 50% transparent black overlay
        FXGL.getGameScene().addUINode(darkOverlay);

        VBox settingsBox = new VBox(15);
        settingsBox.setPadding(new Insets(20));
        settingsBox.setAlignment(Pos.CENTER);
        settingsBox.setTranslateX(width / 2 - 150);
        settingsBox.setTranslateY(height / 2 - 100);

        Label volumeLabel = new Label("Background Music Volume:");
        volumeLabel.setTextFill(Color.WHITE); // Set label text to white


        Slider volumeSlider = new Slider(0.0, 1.0, SoundManager.getInstance().getVolume());
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(0.25);
        volumeSlider.setBlockIncrement(0.05);
        volumeSlider.setPrefWidth(300);

        volumeSlider.setStyle("-fx-tick-label-fill: white;");


        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) ->
            SoundManager.getInstance().setVolume(newVal.doubleValue())
        );

        Button backButton = new Button("Back to Menu");
        backButton.setOnAction(e -> {
            FXGL.getGameScene().clearUINodes();
            MainMenu.getInstance().initUI();
        });
        backButton.setStyle("-fx-text-fill: white; -fx-background-color: #333333;");

        settingsBox.getChildren().addAll(volumeLabel, volumeSlider, backButton);
        FXGL.getGameScene().addUINode(settingsBox);
    }
}