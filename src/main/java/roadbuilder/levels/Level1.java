package roadbuilder.levels;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
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
    private Point2D firstCityClicked = null;

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
        placeCity(2, 6);
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
                        .filter(city -> isClickWithinCityBounds(city, clickPoint))
                        .findFirst()
                        .ifPresent(city -> handleCityClick(city));
            }
        });
    }

    private boolean isClickWithinCityBounds(Point2D city, Point2D clickPoint) {
        double cityX = city.getX();
        double cityY = city.getY();
        return clickPoint.getX() >= cityX && clickPoint.getX() <= cityX + TILE_SIZE &&
               clickPoint.getY() >= cityY && clickPoint.getY() <= cityY + TILE_SIZE;
    }

    private void handleCityClick(Point2D city) {
        if (firstCityClicked == null) {
            firstCityClicked = city;
            System.out.println("First city selected: " + city);
        } else {
            if (!city.equals(firstCityClicked) && !isRoadExists(firstCityClicked, city)) {
                buildRoad(firstCityClicked, city);
                System.out.println("New road created between: " + firstCityClicked + " and " + city);
            }
            firstCityClicked = null; // Reset after second click
        }
    }

    private boolean isRoadExists(Point2D start, Point2D end) {
        String roadKey = start.toString() + "-" + end.toString();
        String reverseRoadKey = end.toString() + "-" + start.toString();
        return roads.contains(roadKey) || roads.contains(reverseRoadKey);
    }

    private void buildRoad(Point2D start, Point2D end) {
        if (!isRoadExists(start, end)) {
            roads.add(start.toString() + "-" + end.toString());

            // Use Bresenham's line algorithm to determine the path
            int x0 = (int) start.getX() / TILE_SIZE;
            int y0 = (int) start.getY() / TILE_SIZE;
            int x1 = (int) end.getX() / TILE_SIZE;
            int y1 = (int) end.getY() / TILE_SIZE;

            int dx = Math.abs(x1 - x0);
            int dy = Math.abs(y1 - y0);
            int sx = x0 < x1 ? 1 : -1;
            int sy = y0 < y1 ? 1 : -1;
            int err = dx - dy;

            while (true) {
                if (!isCityTile(x0, y0)) {
                    placeRoadSegment(x0, y0, x0 != x1 && y0 != y1, sx, sy);
                }
                if (x0 == x1 && y0 == y1) break;
                int e2 = 2 * err;
                if (e2 > -dy) {
                    err -= dy;
                    x0 += sx;
                }
                if (e2 < dx) {
                    err += dx;
                    y0 += sy;
                }
            }
        }
    }

    private boolean isCityTile(int x, int y) {
        Point2D tilePoint = new Point2D(x * TILE_SIZE, y * TILE_SIZE);
        return cities.contains(tilePoint);
    }

    private void placeRoadSegment(int x, int y, boolean isDiagonal, int sx, int sy) {
        Point2D roadPoint = new Point2D(x * TILE_SIZE, y * TILE_SIZE);

        TileType roadType;
        if (isDiagonal) {
            if (sx == sy) {
                roadType = TileType.DIAGONAL_ROAD_RIGHT_TO_LEFT;
            } else {
                roadType = TileType.DIAGONAL_ROAD_LEFT_TO_RIGHT;
            }
        } else {
            roadType = TileType.ROAD_STRAIGHT;
        }

        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(ImageLoader.getImage(roadType));
        imageView.setFitWidth(TILE_SIZE);
        imageView.setFitHeight(TILE_SIZE);

        Entity road = FXGL.entityBuilder()
                .at(roadPoint)
                .view(new Rectangle(TILE_SIZE, TILE_SIZE, Color.DARKGRAY))
                .view(imageView)
                .with(new TileComponent(roadType))
                .buildAndAttach();
    }

    public static void main(String[] args) {
        launch(args);
    }
}