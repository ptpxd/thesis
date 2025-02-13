package roadbuilder.model;

import javafx.geometry.Point2D;

import java.util.*;

public class CityRoadGraphModel {

    private final Set<Point2D> cities;
    private final Map<Point2D, List<Point2D>> roads;

    public CityRoadGraphModel() {
        cities = new HashSet<>();
        roads = new HashMap<>();
    }

    public boolean addCity(Point2D city) {
        if (cities.add(city)) {
            roads.putIfAbsent(city, new ArrayList<>());
            return true;
        }
        return false;
    }

    public boolean addRoad(Point2D start, Point2D end) {
        if (cities.contains(start) && cities.contains(end)) {
            if (!roads.get(start).contains(end)) {
                roads.get(start).add(end);
                roads.get(end).add(start); // Assuming undirected roads
                return true;
            }
            return false;
        }
        return false;
    }

    public List<Point2D> getConnectedCities(Point2D city) {
        return roads.getOrDefault(city, Collections.emptyList());
    }

    public Set<Point2D> getCities() {
        return cities;
    }

    public Map<Point2D, List<Point2D>> getRoads() {
        return roads;
    }

    public void printCityConnections() {
        for (Point2D city : roads.keySet()) {
            System.out.println(city + " is connected to " + roads.get(city));
        }
    }
}