package roadbuilder.levels.level1;

import javafx.geometry.Point2D;
import roadbuilder.model.CityRoadGraphModel;
import roadbuilder.model.GraphType;

import java.util.*;

public class GraphTypeDetector {

    public static GraphType detectGraphType(CityRoadGraphModel graphModel) {
        if (graphModel == null || graphModel.getCities().isEmpty()) {
            return GraphType.NONE;
        }

        Set<Point2D> cities = graphModel.getCities();
        Map<Point2D, List<Point2D>> roads = graphModel.getRoads();
        int cityCount = cities.size();

        if (roads.values().stream().allMatch(List::isEmpty)) {
            return GraphType.NONE;
        }

        if (isCompleteGraph(cities, roads, cityCount)) {
            return GraphType.COMPLETE;
        }

        if (isSimpleGraph(cities, roads, cityCount)) {
            return GraphType.SIMPLE;
        }

        if (isBipartiteGraph(cities, roads)) {
            return GraphType.BIPARTITE;
        }

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

        if (!isConnected(cities, roads)) {
            return false;
        }

        return !hasCycle(cities, roads);
    }

    private static boolean isConnected(Set<Point2D> cities, Map<Point2D, List<Point2D>> roads) {
        if (cities.isEmpty()) {
            return true;
        }

        Set<Point2D> visited = new HashSet<>();
        Queue<Point2D> queue = new LinkedList<>();
        Point2D start = cities.iterator().next();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Point2D current = queue.poll();
            List<Point2D> neighbors = roads.getOrDefault(current, Collections.emptyList());

            for (Point2D neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        return visited.size() == cities.size();
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
            } else if (neighbor != parent) {
                return true;
            }
        }
        return false;
    }
}