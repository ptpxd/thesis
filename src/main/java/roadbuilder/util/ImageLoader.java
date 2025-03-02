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
    private static Image backgroundImage;

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
        tileImages.put(TileType.RIGHT_TO_TOP,
            new Image(IMAGE_PATH + "right_to_up.png"));
        tileImages.put(TileType.RIGHT_TO_DOWN,
            new Image(IMAGE_PATH + "right_to_down.png"));
        tileImages.put(TileType.LEFT_TO_TOP,
            new Image(IMAGE_PATH + "left_to_up.png"));
        tileImages.put(TileType.LEFT_TO_DOWN,
            new Image(IMAGE_PATH + "left_to_down.png"));

        // Load new city tile images
        tileImages.put(TileType.CITY_1, new Image(IMAGE_PATH + "city_1.png"));
        tileImages.put(TileType.CITY_2, new Image(IMAGE_PATH + "city_2.png"));
        tileImages.put(TileType.CITY_3, new Image(IMAGE_PATH + "city_3.png"));
        tileImages.put(TileType.CITY_4, new Image(IMAGE_PATH + "city_4.png"));
        tileImages.put(TileType.CITY_5, new Image(IMAGE_PATH + "city_5.png"));
        tileImages.put(TileType.CITY_6, new Image(IMAGE_PATH + "city_6.png"));
        tileImages.put(TileType.CITY_7, new Image(IMAGE_PATH + "city_7.png"));
        tileImages.put(TileType.CITY_8, new Image(IMAGE_PATH + "city_8.png"));

        // Load button images
        buttonImages.put(ButtonType.PLAY, new Image(IMAGE_PATH + "play_button.png"));
        buttonImages.put(ButtonType.PLAY_HOVER, new Image(IMAGE_PATH + "play_button_hover.png"));

        // Insert SETTINGS button images between PLAY and EXIT
        buttonImages.put(ButtonType.SETTINGS, new Image(IMAGE_PATH + "settings_button.png"));
        buttonImages.put(ButtonType.SETTINGS_HOVER, new Image(IMAGE_PATH + "settings_button_hover.png"));

        buttonImages.put(ButtonType.EXIT, new Image(IMAGE_PATH + "exit_button.png"));
        buttonImages.put(ButtonType.EXIT_HOVER, new Image(IMAGE_PATH + "exit_button_hover.png"));

        // Load background image
        backgroundImage = new Image(IMAGE_PATH + "background.png");
    }

    public static Image getImage(TileType type) {
        return tileImages.getOrDefault(type, tileImages.get(TileType.GRASS));
    }

    public static Image getButtonImage(ButtonType type) {
        return buttonImages.getOrDefault(type, null);
    }

    public static Image getBackgroundImage() {
        return backgroundImage;
    }
}