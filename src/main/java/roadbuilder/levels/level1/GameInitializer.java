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
    private Group budgetGroup;

    public void initGame(List<Point2D> cities, String levelRequirement) {
        // Create the 10x10 grid of grass tiles
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                placeGrass(x, y);
            }
        }

        placeCity(2, 2, cities);
        placeCity(5, 2, cities);
        placeCity(2, 5, cities);
        placeCity(5, 5, cities);

        FXGL.getWorldProperties().setValue("budget", 10000);

        addRequirementTextBox(levelRequirement);
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

    private void addRequirementTextBox(String levelRequirement) {
        // Calculate board dimensions (10 tiles x 10 tiles)
        double boardWidth = 10 * TILE_SIZE;
        double boardHeight = 10 * TILE_SIZE;
        double margin = 20; // margin between board and text box

        double boxWidth = 200;
        double boxHeight = 50;
        double boxX = boardWidth + margin;
        double boxY = boardHeight / 2 - boxHeight / 2;

        Rectangle background = new Rectangle(boxWidth, boxHeight);
        background.setFill(Color.LIGHTGRAY);
        background.setStroke(Color.BLACK);

        Text text = new Text("Goal: '" + levelRequirement + "'");
        text.setX(10);
        text.setY(boxHeight / 2 + 5);

        Group requirementTextBox = new Group(background, text);

        FXGL.entityBuilder()
                .at(boxX, boxY)
                .view(requirementTextBox)
                .buildAndAttach();
    }

    private void addBudgetTextBox() {
        double boardWidth = 10 * TILE_SIZE;
        double boardHeight = 10 * TILE_SIZE;
        double margin = 20;
        double boxWidth = 200;
        double boxHeight = 50;
        double requirementBoxY = boardHeight / 2 - boxHeight / 2;
        double gap = 10;
        double boxX = boardWidth + margin;
        double boxY = requirementBoxY + boxHeight + gap;

        Rectangle background = new Rectangle(boxWidth, boxHeight);
        background.setFill(Color.BEIGE);
        background.setStroke(Color.BLACK);

        Text budgetText = new Text("Budget: " + FXGL.getWorldProperties().getInt("budget"));
        budgetText.setX(10);
        budgetText.setY(boxHeight / 2 + 5);

        budgetGroup = new Group(background, budgetText);

        FXGL.getWorldProperties().<Integer>addListener("budget", (oldValue, newValue) -> {
            budgetText.setText("Fennmaradó összeg: " + newValue);
        });

        FXGL.entityBuilder()
                .at(boxX, boxY)
                .view(budgetGroup)
                .buildAndAttach();
    }
}