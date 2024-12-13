package roadbuilder.app;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.GameWorld;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import roadbuilder.model.TileType;
import roadbuilder.util.ImageLoader;

public class Main extends GameApplication {

    private static final int TILE_SIZE = 40;
    private static final int GRID_WIDTH = 10;
    private static final int GRID_HEIGHT = 10;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(GRID_WIDTH * TILE_SIZE);
        settings.setHeight(GRID_HEIGHT * TILE_SIZE);
        settings.setTitle("Tile Game");
        settings.setVersion("1.0");
    }

    @Override
    protected void initGame() {
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(ImageLoader.getImage(TileType.GRASS));
                imageView.setFitWidth(TILE_SIZE);
                imageView.setFitHeight(TILE_SIZE);

                Entity tile = FXGL.entityBuilder()
                        .at(x * TILE_SIZE, y * TILE_SIZE)
                        .view(new Rectangle(TILE_SIZE, TILE_SIZE, Color.LIGHTGRAY))
                        .view(imageView)
                        .with(new TileComponent(TileType.GRASS))
                        .buildAndAttach();

                System.out.println("Tile created at: " + x * TILE_SIZE + ", " + y * TILE_SIZE);
            }
        }
    }

    @Override
    protected void initInput() {
        FXGL.getInput().addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double mouseX = event.getSceneX();
                double mouseY = event.getSceneY();
                int tileX = (int) (mouseX / TILE_SIZE) * TILE_SIZE;
                int tileY = (int) (mouseY / TILE_SIZE) * TILE_SIZE;
                Point2D clickPoint = new Point2D(tileX + TILE_SIZE / 2, tileY + TILE_SIZE / 2);
                System.out.println("Mouse clicked at: " + clickPoint);
                FXGL.getGameWorld().getEntitiesInRange(new javafx.geometry.Rectangle2D(tileX, tileY, TILE_SIZE, TILE_SIZE)).forEach(entity -> {
                    System.out.println("Entity at click point: " + entity);
                });
                FXGL.getGameWorld().getEntitiesInRange(new javafx.geometry.Rectangle2D(tileX, tileY, TILE_SIZE, TILE_SIZE)).stream()
                        .filter(entity -> entity.hasComponent(TileComponent.class))
                        .filter(entity -> entity.getComponent(TileComponent.class).getTileType() == TileType.GRASS)
                        .findFirst()
                        .ifPresent(entity -> {
                            System.out.println("Tile clicked: " + entity);
                            entity.getComponent(TileComponent.class).setTileType(TileType.ROAD_STRAIGHT);
                            entity.getViewComponent().clearChildren();
                            javafx.scene.image.ImageView roadImageView = new javafx.scene.image.ImageView(ImageLoader.getImage(TileType.ROAD_STRAIGHT));
                            roadImageView.setFitWidth(TILE_SIZE);
                            roadImageView.setFitHeight(TILE_SIZE);
                            entity.getViewComponent().addChild(roadImageView);
                            System.out.println("Tile type changed to ROAD_STRAIGHT");
                        });
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}