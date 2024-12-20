package roadbuilder.util;

import javafx.scene.image.Image;
import roadbuilder.model.TileType;

import java.util.EnumMap;
import java.util.Map;

public class ImageLoader {

    private static final String IMAGE_PATH = "/roadbuilder/images/";

     private static final Map<TileType, Image> images = new EnumMap<>(TileType.class);

    static {
        images.put(TileType.CITY, new Image(IMAGE_PATH + "city.png"));
        images.put(TileType.GRASS, new Image(IMAGE_PATH + "grass.png"));
        images.put(TileType.ROAD_STRAIGHT, new Image(IMAGE_PATH + "two_way_road.png"));
        images.put(TileType.ROAD_CURVED, new Image(IMAGE_PATH + "left_to_up.png"));
    }

    public static Image getImage(TileType type) {
        return images.getOrDefault(type, images.get(TileType.GRASS));
    }
}