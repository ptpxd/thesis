package roadbuilder.util;

import javafx.geometry.Point2D;

public class Point2DUtil {
    public static Point2D fromString(String pointString) {
        String[] coordinates = pointString.replace("Point2D[", "").replace("]", "").split(",");
        double x = Double.parseDouble(coordinates[0].split("=")[1]);
        double y = Double.parseDouble(coordinates[1].split("=")[1]);
        return new Point2D(x, y);
    }
}