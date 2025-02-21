package roadbuilder.levels.level1;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point2D;
import roadbuilder.app.TileComponent;
import roadbuilder.model.TileType;
import roadbuilder.util.ImageLoader;

import java.util.List;

public class GameInitializer {
    private static final int TILE_SIZE = 40;

    public void initGame(List<Point2D> cities) {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                placeGrass(x, y);
            }
        }

        placeCity(2, 2, cities);
        placeCity(5, 5, cities);
        placeCity(7, 7, cities);
        placeCity(2, 7, cities);
        placeCity(5, 2, cities);

        FXGL.getWorldProperties().setValue("budget", 10000);
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
}