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
        int x0 = (int) start.getX() / TILE_SIZE;
        int y0 = (int) start.getY() / TILE_SIZE;
        int x1 = (int) end.getX() / TILE_SIZE;
        int y1 = (int) end.getY() / TILE_SIZE;

        // Update the starting city tile if applicable
        if (isCityTile(x0, y0, cities)) {
            updateCityTile(x0, y0);
        }

        // Update the ending city tile if applicable
        if (isCityTile(x1, y1, cities)) {
            updateCityTile(x1, y1);
        }

        if (x0 == x1) {
            while (y0 != y1) {
                if (!isCityTile(x0, y0, cities)) {
                    placeRoadSegment(x0, y0, false, 0, y0 < y1 ? 1 : -1);
                }
                y0 += y0 < y1 ? 1 : -1;
            }
        } else if (y0 == y1) {
            while (x0 != x1) {
                if (!isCityTile(x0, y0, cities)) {
                    placeRoadSegment(x0, y0, false, x0 < x1 ? 1 : -1, 0);
                }
                x0 += x0 < x1 ? 1 : -1;
            }
        } else {
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

    private static void updateCityTile(int x, int y) {
        Point2D cityPoint = new Point2D(x * TILE_SIZE, y * TILE_SIZE);
        FXGL.getGameWorld().getEntitiesAt(cityPoint).stream()
            .filter(entity -> entity.hasComponent(TileComponent.class))
            .forEach(entity -> {
                TileComponent tileComponent = entity.getComponent(TileComponent.class);
                TileType currentType = tileComponent.getTileType();
                TileType newType = getNextCityType(currentType);
                tileComponent.setTileType(newType);
                entity.getViewComponent().clearChildren();
                ImageView newImageView = new ImageView(ImageLoader.getImage(newType));
                newImageView.setFitWidth(TILE_SIZE);
                newImageView.setFitHeight(TILE_SIZE);
                entity.getViewComponent().addChild(newImageView);
            });
    }

    private static TileType getNextCityType(TileType currentType) {
        switch (currentType) {
            case CITY: return TileType.CITY_1;
            case CITY_1: return TileType.CITY_2;
            case CITY_2: return TileType.CITY_3;
            case CITY_3: return TileType.CITY_4;
            case CITY_4: return TileType.CITY_5;
            case CITY_5: return TileType.CITY_6;
            case CITY_6: return TileType.CITY_7;
            case CITY_7: return TileType.CITY_8;
            default: return currentType;
        }
    }
}