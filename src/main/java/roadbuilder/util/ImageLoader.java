package roadbuilder.util;

import javafx.scene.image.Image;
import roadbuilder.model.TileType;
import roadbuilder.model.ButtonType;

import java.util.EnumMap;
import java.util.Map;

public class ImageLoader {

    private static final String IMAGE_PATH = "/roadbuilder/images/";

    private static final Map<TileType, Image> tileImages = new EnumMap<>(TileType.class);
    private static final Map<ButtonType, Image> buttonImages = new EnumMap<>(ButtonType.class);

    static {
        // Load tile images
        tileImages.put(TileType.CITY, new Image(IMAGE_PATH + "city.png"));
        tileImages.put(TileType.GRASS, new Image(IMAGE_PATH + "grass.png"));
        tileImages.put(TileType.ROAD_STRAIGHT, new Image(IMAGE_PATH + "two_way_road.png"));
        tileImages.put(TileType.ROAD_CURVED, new Image(IMAGE_PATH + "left_to_up.png"));
        tileImages.put(TileType.DIAGONAL_ROAD_LEFT_TO_RIGHT,
            new Image(IMAGE_PATH + "diagonal_road_left_to_right.png"));
        tileImages.put(TileType.DIAGONAL_ROAD_RIGHT_TO_LEFT,
            new Image(IMAGE_PATH + "diagonal_road_right_to_left.png"));
        tileImages.put(TileType.ROAD_HORIZONTAL,
            new Image(IMAGE_PATH + "two_way_road_horizontal.png"));

        // Load button images
        buttonImages.put(ButtonType.PLAY, new Image(IMAGE_PATH + "play_button.png"));
        buttonImages.put(ButtonType.PLAY_HOVER, new Image(IMAGE_PATH + "play_button_hover.png"));
        buttonImages.put(ButtonType.EXIT, new Image(IMAGE_PATH + "exit_button.png"));
        buttonImages.put(ButtonType.EXIT_HOVER, new Image(IMAGE_PATH + "exit_button_hover.png"));
    }

    public static Image getImage(TileType type) {
        return tileImages.getOrDefault(type, tileImages.get(TileType.GRASS));
    }

    public static Image getButtonImage(ButtonType type) {
        return buttonImages.getOrDefault(type, null);
    }
}