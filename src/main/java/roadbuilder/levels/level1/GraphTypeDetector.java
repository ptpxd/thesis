package roadbuilder.levels.level1;

import javafx.geometry.Point2D;
import roadbuilder.model.CityRoadGraphModel;
import java.util.*;

public class GraphTypeDetector {

    public enum GraphType {
        SIMPLE, COMPLETE, BIPARTITE, COMPLEX
    }

    public static GraphType detectGraphType(CityRoadGraphModel graphModel) {
        if (graphModel == null || graphModel.getCities().isEmpty()) {
            return GraphType.SIMPLE;
        }

        Set<Point2D> cities = graphModel.getCities();
        Map<Point2D, List<Point2D>> roads = graphModel.getRoads();
        int cityCount = cities.size();

        // Üres gráf esete
        if (roads.values().stream().allMatch(List::isEmpty)) {
            return GraphType.SIMPLE;
        }

        // Teljes gráf ellenőrzése
        if (isCompleteGraph(cities, roads, cityCount)) {
            return GraphType.COMPLETE;
        }

        // Bipartite gráf ellenőrzése
        if (isBipartiteGraph(cities, roads)) {
            return GraphType.BIPARTITE;
        }

        // Simple gráf ellenőrzése (elsősorban árnyékosztruktúra)
        if (isSimpleGraph(cities, roads, cityCount)) {
            return GraphType.SIMPLE;
        }

        // Alapértelmezetten complex
        return GraphType.COMPLEX;
    }

    private static boolean isCompleteGraph(Set<Point2D> cities, Map<Point2D, List<Point2D>> roads, int cityCount) {
        if (cityCount <= 1) return true;

        int expectedEdges = cityCount * (cityCount - 1);
        int actualEdges = roads.values().stream().mapToInt(List::size).sum();

        for (Point2D city : cities) {
            List<Point2D> connections = roads.get(city);
            if (connections.size() != cityCount - 1) {
                return false;
            }
            for (Point2D otherCity : cities) {
                if (!otherCity.equals(city) && !connections.contains(otherCity)) {
                    return false;
                }
            }
        }
        return actualEdges == expectedEdges;
    }

    private static boolean isBipartiteGraph(Set<Point2D> cities, Map<Point2D, List<Point2D>> roads) {
        Map<Point2D, Integer> colorMap = new HashMap<>();
        for (Point2D city : cities) {
            if (!colorMap.containsKey(city)) {
                if (!bipartiteBFS(city, roads, colorMap)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean bipartiteBFS(Point2D start, Map<Point2D, List<Point2D>> roads, Map<Point2D, Integer> colorMap) {
        Queue<Point2D> queue = new LinkedList<>();
        colorMap.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            Point2D current = queue.poll();
            List<Point2D> neighbors = roads.getOrDefault(current, Collections.emptyList());

            for (Point2D neighbor : neighbors) {
                if (!colorMap.containsKey(neighbor)) {
                    colorMap.put(neighbor, colorMap.get(current) ^ 1);
                    queue.add(neighbor);
                } else if (colorMap.get(neighbor) == colorMap.get(current)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isSimpleGraph(Set<Point2D> cities, Map<Point2D, List<Point2D>> roads, int cityCount) {
        int edgeCount = roads.values().stream().mapToInt(List::size).sum() / 2;
        if (edgeCount > cityCount - 1) {
            return false;
        }

        return !hasCycle(cities, roads);
    }

    private static boolean hasCycle(Set<Point2D> cities, Map<Point2D, List<Point2D>> roads) {
        Map<Point2D, Boolean> visited = new HashMap<>();
        for (Point2D city : cities) {
            if (!visited.containsKey(city)) {
                if (cycleDFS(city, roads, visited, null)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean cycleDFS(Point2D current, Map<Point2D, List<Point2D>> roads, Map<Point2D, Boolean> visited, Point2D parent) {
        visited.put(current, true);
        List<Point2D> neighbors = roads.getOrDefault(current, Collections.emptyList());

        for (Point2D neighbor : neighbors) {
            if (!visited.containsKey(neighbor)) {
                if (cycleDFS(neighbor, roads, visited, current)) {
                    return true;
                }
            } else if (!neighbor.equals(parent)) {
                return true;
            }
        }
        return false;
    }
}