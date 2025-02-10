package roadbuilder.levels.level1;

import javafx.geometry.Point2D;
import java.util.*;

public class GraphTypeDetector {

    public enum GraphType {
        SIMPLE, COMPLETE, BIPARTITE, COMPLEX
    }

    public static GraphType detectGraphType(Set<String> roads, List<Point2D> cities) {
        int cityCount = cities.size();
        int roadCount = roads.size();

        if (roadCount == cityCount * (cityCount - 1) / 2) {
            return GraphType.COMPLETE;
        }

        if (isBipartiteGraph(cities, roads)) {
            return GraphType.BIPARTITE;
        }

        if (roadCount == cityCount - 1) {
            return GraphType.SIMPLE;
        }

        return GraphType.COMPLEX;
    }

    private static boolean isBipartiteGraph(List<Point2D> cities, Set<String> roads) {
        Map<Point2D, Integer> colorMap = new HashMap<>();
        for (Point2D city : cities) {
            if (!colorMap.containsKey(city)) {
                if (!bipartiteDFS(city, roads, colorMap, 0)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean bipartiteDFS(Point2D city, Set<String> roads, Map<Point2D, Integer> colorMap, int color) {
        colorMap.put(city, color);
        for (String road : roads) {
            String[] points = road.split("-");
            Point2D start = new Point2D(Double.parseDouble(points[0]), Double.parseDouble(points[1]));
            Point2D end = new Point2D(Double.parseDouble(points[2]), Double.parseDouble(points[3]));

            if (start.equals(city) || end.equals(city)) {
                Point2D neighbor = start.equals(city) ? end : start;
                if (!colorMap.containsKey(neighbor)) {
                    if (!bipartiteDFS(neighbor, roads, colorMap, 1 - color)) {
                        return false;
                    }
                } else if (colorMap.get(neighbor) == color) {
                    return false;
                }
            }
        }
        return true;
    }
}