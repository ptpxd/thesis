package roadbuilder.levels.level1;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import roadbuilder.app.TileComponent;
import roadbuilder.model.TileType;
import roadbuilder.util.ImageLoader;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.Set;

public class RoadBuilder {
    private static final int TILE_SIZE = 40;

    public static void buildRoad(Point2D start, Point2D end, Set<String> roads, List<Point2D> cities, Level1Game level1Game) {
        // Use the provided Level1Game instance to handle road addition

        int x0 = (int) start.getX() / TILE_SIZE;
        int y0 = (int) start.getY() / TILE_SIZE;
        int x1 = (int) end.getX() / TILE_SIZE;
        int y1 = (int) end.getY() / TILE_SIZE;

        if (x0 == x1) {
            // Vertical road
            while (y0 != y1) {
                if (!isCityTile(x0, y0, cities)) {
                    placeRoadSegment(x0, y0, false, 0, y0 < y1 ? 1 : -1);
                }
                y0 += y0 < y1 ? 1 : -1;
            }
        } else if (y0 == y1) {
            // Horizontal road
            while (x0 != x1) {
                if (!isCityTile(x0, y0, cities)) {
                    placeRoadSegment(x0, y0, false, x0 < x1 ? 1 : -1, 0);
                }
                x0 += x0 < x1 ? 1 : -1;
            }
        } else {
            // Diagonal or mixed path
            int dx = Math.abs(x1 - x0);
            int dy = Math.abs(y1 - y0);
            int sx = x0 < x1 ? 1 : -1;
            int sy = y0 < y1 ? 1 : -1;
            int err = dx - dy;

            while (true) {
                if (!isCityTile(x0, y0, cities)) {
                    placeRoadSegment(x0, y0, dx != 0 && dy != 0, sx, sy);
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
        level1Game.addRoad(start, end);
    }

    private static boolean isCityTile(int x, int y, List<Point2D> cities) {
        Point2D tilePoint = new Point2D(x * TILE_SIZE, y * TILE_SIZE);
        return cities.contains(tilePoint);
    }

    private static void placeRoadSegment(int x, int y, boolean isDiagonal, int sx, int sy) {
        Point2D roadPoint = new Point2D(x * TILE_SIZE, y * TILE_SIZE);

        TileType roadType;
        if (isDiagonal) {
            roadType = (sx == sy) ? TileType.DIAGONAL_ROAD_RIGHT_TO_LEFT : TileType.DIAGONAL_ROAD_LEFT_TO_RIGHT;
        } else if (sx != 0 && sy == 0) {
            roadType = TileType.ROAD_HORIZONTAL;
        } else if (sx == 0 && sy != 0) {
            roadType = TileType.ROAD_STRAIGHT;
        } else {
            roadType = TileType.ROAD_HORIZONTAL;
        }

        ImageView imageView = new ImageView(ImageLoader.getImage(roadType));
        imageView.setFitWidth(TILE_SIZE);
        imageView.setFitHeight(TILE_SIZE);

        Entity road = FXGL.entityBuilder()
                .at(roadPoint)
                .view(new Rectangle(TILE_SIZE, TILE_SIZE, Color.DARKGRAY))
                .view(imageView)
                .with(new TileComponent(roadType))
                .buildAndAttach();
    }
}