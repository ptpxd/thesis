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

    public void addCity(Point2D city) {
        cities.add(city);
        roads.putIfAbsent(city, new ArrayList<>());
    }

    public void addRoad(Point2D start, Point2D end) {
        if (cities.contains(start) && cities.contains(end)) {
            roads.get(start).add(end);
            roads.get(end).add(start); // Assuming undirected roads
            System.out.println("Road added between " + start + " and " + end);
        } else {
            System.out.println("Cannot add road: One or both cities not found.");
        }
    }

    public List<Point2D> getConnectedCities(Point2D city) {
        return roads.getOrDefault(city, Collections.emptyList());
    }

    public Set<Point2D> getCities() {
        return cities;
    }

    public void printCityConnections() {
        for (Point2D city : roads.keySet()) {
            System.out.println(city + " is connected to " + roads.get(city));
        }
    }
}