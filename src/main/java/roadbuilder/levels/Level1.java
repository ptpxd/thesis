package roadbuilder.levels;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import roadbuilder.app.TileComponent;
import roadbuilder.model.TileType;
import roadbuilder.util.ImageLoader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Level1 extends GameApplication {

    private static final int TILE_SIZE = 40;
    private static final int GRID_WIDTH = 10;
    private static final int GRID_HEIGHT = 10;
    private List<Point2D> cities = new ArrayList<>();
    private Set<String> roads = new HashSet<>();

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(GRID_WIDTH * TILE_SIZE);
        settings.setHeight(GRID_HEIGHT * TILE_SIZE);
        settings.setTitle("Level 1: Graph Theory");
        settings.setVersion("1.0");
    }

    @Override
    protected void initGame() {
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                placeGrass(x, y);
            }
        }

        placeCity(2, 2);
        placeCity(5, 5);
        placeCity(7, 7);
    }

    private void placeCity(int x, int y) {
        Point2D cityPoint = new Point2D(x * TILE_SIZE, y * TILE_SIZE);
        cities.add(cityPoint);

        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(ImageLoader.getImage(TileType.CITY));
        imageView.setFitWidth(TILE_SIZE);
        imageView.setFitHeight(TILE_SIZE);

        Entity city = FXGL.entityBuilder()
                .at(cityPoint)
                .view(new Rectangle(TILE_SIZE, TILE_SIZE, Color.LIGHTGRAY))
                .view(imageView)
                .with(new TileComponent(TileType.CITY))
                .buildAndAttach();

        System.out.println("City placed at: " + cityPoint);
    }

    private void placeGrass(int x, int y) {
        Point2D grassPoint = new Point2D(x * TILE_SIZE, y * TILE_SIZE);

        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(ImageLoader.getImage(TileType.GRASS));
        imageView.setFitWidth(TILE_SIZE);
        imageView.setFitHeight(TILE_SIZE);

        Entity grass = FXGL.entityBuilder()
                .at(grassPoint)
                .view(new Rectangle(TILE_SIZE, TILE_SIZE, Color.LIGHTGREEN))
                .view(imageView)
                .with(new TileComponent(TileType.GRASS))
                .buildAndAttach();
    }

    @Override
    protected void initInput() {
        FXGL.getInput().addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Point2D clickPoint = new Point2D(event.getSceneX(), event.getSceneY());
                cities.stream()
                        .filter(city -> city.distance(clickPoint) < TILE_SIZE)
                        .findFirst()
                        .ifPresent(city -> {
                            System.out.println("City clicked: " + city);
                            cities.forEach(otherCity -> {
                                if (!city.equals(otherCity) && !isRoadExists(city, otherCity)) {
                                    buildRoad(city, otherCity);
                                    System.out.println("New road created between: " + city + " and " + otherCity);
                                    System.out.println("Degree of " + city + ": " + getDegree(city));
                                    System.out.println("Degree of " + otherCity + ": " + getDegree(otherCity));
                                }
                            });
                        });
            }
        });
    }

    private boolean isRoadExists(Point2D start, Point2D end) {
        String roadKey = start.toString() + "-" + end.toString();
        String reverseRoadKey = end.toString() + "-" + start.toString();
        return roads.contains(roadKey) || roads.contains(reverseRoadKey);
    }

    private void buildRoad(Point2D start, Point2D end) {
        if (!isRoadExists(start, end)) {
            roads.add(start.toString() + "-" + end.toString());
            Line roadLine = new Line(start.getX(), start.getY(), end.getX(), end.getY());
            roadLine.setStroke(Color.DARKGRAY);
            roadLine.setStrokeWidth(5);
            FXGL.getGameScene().addUINode(roadLine);
        }
    }

    private int getDegree(Point2D city) {
        return (int) roads.stream().filter(road -> road.contains(city.toString())).count();
    }

    public static void main(String[] args) {
        launch(args);
    }
}