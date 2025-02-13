package roadbuilder.levels.level1;

import javafx.geometry.Point2D;
import roadbuilder.levels.level1.GraphTypeDetector;
import roadbuilder.model.CityRoadGraphModel;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Level1Game {
    private CityRoadGraphModel graphModel;

    public Level1Game() {
        graphModel = new CityRoadGraphModel();
    }

    public void initializeLevel(List<Point2D> initialCities) {
        try {
            for (Point2D city : initialCities) {
                addCity(city);
            }
            graphModel.printCityConnections();
        } catch (Exception e) {
            System.err.println("Error initializing level: " + e.getMessage());
            throw e;
        }
    }

    public void addCity(Point2D city) {
        try {
            boolean cityAdded = graphModel.addCity(city);
            if (cityAdded) {
                System.out.println("City added at: " + city);
                displayGraphType();
            } else {
                System.out.println("City already exists at: " + city);
            }
        } catch (Exception e) {
            System.err.println("Error adding city: " + e.getMessage());
            throw e;
        }
    }

    public void addRoad(Point2D start, Point2D end) {
        try {
            boolean roadAdded = graphModel.addRoad(start, end);
            if (roadAdded) {
                System.out.println("Road added between: " + start + " and " + end);
                displayGraphType(); // Call displayGraphType after adding the road
            } else {
                System.out.println("Road already exists between: " + start + " and " + end);
            }
        } catch (Exception e) {
            System.err.println("Error adding road: " + e.getMessage());
            throw e;
        }
    }

    public List<Point2D> getConnectedCities(Point2D city) {
        try {
            return graphModel.getConnectedCities(city);
        } catch (Exception e) {
            System.err.println("Error getting connected cities: " + e.getMessage());
            throw e;
        }
    }

    public boolean checkLevelCompletion() {
        try {
            for (Point2D city : graphModel.getCities()) {
                if (graphModel.getConnectedCities(city).isEmpty()) {
                    System.out.println("City at " + city + " is disconnected. Level not complete.");
                    return false;
                }
            }
            System.out.println("All cities are connected. Level complete!");
            return true;
        } catch (Exception e) {
            System.err.println("Error checking level completion: " + e.getMessage());
            throw e;
        }
    }

    public Map<Point2D, List<Point2D>> getGraphAnalysis() {
        try {
            return graphModel.getRoads();
        } catch (Exception e) {
            System.err.println("Error getting graph analysis: " + e.getMessage());
            throw e;
        }
    }

    public void displayGraphType() {
        try {
            GraphTypeDetector.GraphType graphType = analyzeGraphType();
            System.out.println("\nCurrent Graph Type: " + graphType);

            switch (graphType) {
                case SIMPLE:
                    System.out.println("This is a simple graph with no complex connections.");
                    break;
                case COMPLETE:
                    System.out.println("This is a complete graph where every city is connected to every other city.");
                    break;
                case BIPARTITE:
                    System.out.println("This is a bipartite graph where cities can be divided into two sets with connections only between sets.");
                    break;
                case COMPLEX:
                    System.out.println("This is a complex graph with no clear pattern or structure.");
                    break;
            }

            System.out.println("Number of cities: " + graphModel.getCities().size());
            System.out.println("Number of roads: " + getRoadCount());
            System.out.println("Connected cities: " + getAllConnectedCities());
        } catch (Exception e) {
            System.err.println("Error displaying graph type: " + e.getMessage());
            throw e;
        }
    }

    private int getRoadCount() {
        try {
            return graphModel.getRoads().values().stream()
                    .mapToInt(List::size)
                    .sum() / 2; // Each road is counted twice
        } catch (Exception e) {
            System.err.println("Error counting roads: " + e.getMessage());
            throw e;
        }
    }

    private Set<Point2D> getAllConnectedCities() {
        try {
            return graphModel.getCities().stream()
                    .filter(city -> !graphModel.getConnectedCities(city).isEmpty())
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            System.err.println("Error getting connected cities: " + e.getMessage());
            throw e;
        }
    }

    private GraphTypeDetector.GraphType analyzeGraphType() {
        try {
            return GraphTypeDetector.detectGraphType(graphModel);
        } catch (Exception e) {
            System.err.println("Error analyzing graph type: " + e.getMessage());
            throw e;
        }
    }

    public boolean areAllCitiesConnected() {
        try {
            return graphModel.getCities().stream()
                    .allMatch(city -> !graphModel.getConnectedCities(city).isEmpty());
        } catch (Exception e) {
            System.err.println("Error checking city connections: " + e.getMessage());
            throw e;
        }
    }
}