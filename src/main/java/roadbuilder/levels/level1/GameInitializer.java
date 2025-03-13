package roadbuilder.levels.level1;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import roadbuilder.app.TileComponent;
import roadbuilder.model.TileType;
import roadbuilder.util.ImageLoader;
import javafx.geometry.Point2D;
import java.util.List;

public class GameInitializer {
    private static final int TILE_SIZE = 40;
    private Group budgetGroup; // Referencia a budget dobozhoz a későbbi frissítéshez

    // Updated to accept levelRequirement as a parameter
    public void initGame(List<Point2D> cities, String levelRequirement) {
        // Create the 10x10 grid of grass tiles
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                placeGrass(x, y);
            }
        }

        // Place city tiles and update cities list
        placeCity(2, 2, cities);
        placeCity(5, 2, cities);
        placeCity(2, 5, cities);
        placeCity(5, 5, cities);

        // Set initial budget property
        FXGL.getWorldProperties().setValue("budget", 10000);

        // Add requirement text box on the right center of the level with dynamic level requirement
        addRequirementTextBox(levelRequirement);
        // Add budget text box below the requirement box displaying remaining money
        addBudgetTextBox();
    }

    private void placeCity(int x, int y, List<Point2D> cities) {
        Point2D cityPoint = new Point2D(x * TILE_SIZE, y * TILE_SIZE);
        cities.add(cityPoint);

        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(ImageLoader.getImage(TileType.CITY));
        imageView.setFitWidth(TILE_SIZE);
        imageView.setFitHeight(TILE_SIZE);

        FXGL.entityBuilder()
                .at(cityPoint)
                .view(imageView)
                .with(new TileComponent(TileType.CITY))
                .buildAndAttach();
    }

    private void placeGrass(int x, int y) {
        Point2D grassPoint = new Point2D(x * TILE_SIZE, y * TILE_SIZE);

        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(ImageLoader.getImage(TileType.GRASS));
        imageView.setFitWidth(TILE_SIZE);
        imageView.setFitHeight(TILE_SIZE);

        FXGL.entityBuilder()
                .at(grassPoint)
                .view(imageView)
                .with(new TileComponent(TileType.GRASS))
                .buildAndAttach();
    }

    // Updated to accept the desired level requirement and include it in the displayed text
    private void addRequirementTextBox(String levelRequirement) {
        // Calculate board dimensions (10 tiles x 10 tiles)
        double boardWidth = 10 * TILE_SIZE;
        double boardHeight = 10 * TILE_SIZE;
        double margin = 20; // margin between board and text box

        // Define the size of the text box
        double boxWidth = 200;
        double boxHeight = 50;
        double boxX = boardWidth + margin;
        double boxY = boardHeight / 2 - boxHeight / 2;

        // Create background rectangle for the text box
        Rectangle background = new Rectangle(boxWidth, boxHeight);
        background.setFill(Color.LIGHTGRAY);
        background.setStroke(Color.BLACK);

        // Create the text to display the level requirement dynamically
        Text text = new Text("Teljesítsd ezt: '" + levelRequirement + "'");
        text.setX(10);
        // Align text vertically in the center; 15 is a small offset for visual alignment
        text.setY(boxHeight / 2 + 5);

        // Group the rectangle and text together
        Group requirementTextBox = new Group(background, text);

        // Build and attach the text box entity at the calculated position
        FXGL.entityBuilder()
                .at(boxX, boxY)
                .view(requirementTextBox)
                .buildAndAttach();
    }

    // New method to add a budget text box that displays the remaining amount
    private void addBudgetTextBox() {
        double boardWidth = 10 * TILE_SIZE;
        double boardHeight = 10 * TILE_SIZE;
        double margin = 20;
        double boxWidth = 200;
        double boxHeight = 50;
        double requirementBoxY = boardHeight / 2 - boxHeight / 2;
        double gap = 10; // gap between requirement and budget boxes
        double boxX = boardWidth + margin;
        double boxY = requirementBoxY + boxHeight + gap;

        // Create background rectangle for the budget box
        Rectangle background = new Rectangle(boxWidth, boxHeight);
        background.setFill(Color.BEIGE);
        background.setStroke(Color.BLACK);

        // Create the text to display the remaining budget (initially 10000)
        Text budgetText = new Text("Fennmaradó: " + FXGL.getWorldProperties().getInt("budget"));
        budgetText.setX(10);
        budgetText.setY(boxHeight / 2 + 5);

        // Group the rectangle and text together
        budgetGroup = new Group(background, budgetText);

        // Bind budget changes: whenever the "budget" property changes, update the displayed text
        FXGL.getWorldProperties().<Integer>addListener("budget", (oldValue, newValue) -> {
            budgetText.setText("Fennmaradó összeg: " + newValue);
        });

        // Build and attach the budget box entity at the calculated position
        FXGL.entityBuilder()
                .at(boxX, boxY)
                .view(budgetGroup)
                .buildAndAttach();
    }
}