package roadbuilder.levels.level4;

import javafx.geometry.Point2D;
import roadbuilder.model.CityRoadGraphModel;
import roadbuilder.model.GraphType;
import roadbuilder.levels.level1.GraphTypeDetector;
import roadbuilder.app.ProgressManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Level4Game uses the CityRoadGraphModel to build and analyze a road graph.
 * The required graph type for Level4 is set to COMPLEX.
 */
public class Level4Game {
    private CityRoadGraphModel graphModel;

    public Level4Game() {
        graphModel = new CityRoadGraphModel();
    }

    public void initializeLevel() {
        // Reset the graph for Level4
        clearCities();

        // Define Level4 specific cities using javafx.geometry.Point2D
        List<Point2D> freshCities = new ArrayList<>();
        freshCities.add(new Point2D(50, 50));
        freshCities.add(new Point2D(250, 50));
        freshCities.add(new Point2D(450, 50));
        freshCities.add(new Point2D(150, 250));
        freshCities.add(new Point2D(350, 250));
        freshCities.add(new Point2D(250, 450));

        // Add the cities to the graph model
        for (Point2D city : freshCities) {
            addCity(city);
        }
        // Optional: print the current city connections
        graphModel.printCityConnections();
    }

    public void addCity(Point2D city) {
        boolean added = graphModel.addCity(city);
        if (added) {
            System.out.println("Level4 - City added: " + city);
            displayGraphType();
            checkLevelCompletion();
        } else {
            if (!graphModel.getRoads().containsKey(city)) {
                // Create an entry for the city if missing
                graphModel.getRoads().put(city, new ArrayList<>());
            }
            System.out.println("Level4 - City already exists: " + city);
        }
    }

    public void addRoad(Point2D start, Point2D end) {
        boolean added = graphModel.addRoad(start, end);
        if (added) {
            System.out.println("Level4 - Road added between: " + start + " and " + end);
            displayGraphType();
            checkLevelCompletion();
        } else {
            System.out.println("Level4 - Road already exists between: " + start + " and " + end);
        }
    }

    public List<Point2D> getConnectedCities(Point2D city) {
        return graphModel.getConnectedCities(city);
    }

    public Map<Point2D, List<Point2D>> getGraphAnalysis() {
        return graphModel.getRoads();
    }

    /**
     * Checks if the current graph type matches the required COMPLEX type.
     * Marks the level as completed if the requirement is met.
     */
    public boolean checkLevelCompletion() {
        int currentLevel = ProgressManager.getHighestCompletedLevel() + 1;
        GraphType requiredGraphType = getRequiredGraphType();
        GraphType currentGraphType = analyzeGraphType();
        if (currentGraphType == requiredGraphType) {
            ProgressManager.markLevelAsCompleted(currentLevel);
            System.out.println("Level4 completed! Required graph type: " + requiredGraphType);
            return true;
        } else {
            System.out.println("Level4 not completed. Current graph type: " + currentGraphType +
                    ", Required: " + requiredGraphType);
            return false;
        }
    }

    public void displayGraphType() {
        GraphType graphType = analyzeGraphType();
        System.out.println("\nLevel4 - Current graph type: " + graphType);
        switch (graphType) {
            case NONE:
                System.out.println("No cities or roads.");
                break;
            case SIMPLE:
                System.out.println("Simple graph with no complex connections.");
                break;
            case COMPLETE:
                System.out.println("Complete graph: every city is interconnected.");
                break;
            case BIPARTITE:
                System.out.println("Bipartite graph: cities separated into two distinct groups.");
                break;
            case COMPLEX:
                System.out.println("Complex graph with intricate connections.");
                break;
        }
        System.out.println("Total cities: " + graphModel.getCities().size());
        System.out.println("Total roads: " + getRoadCount());
        System.out.println("Connected cities: " + getAllConnectedCities());
    }

    private int getRoadCount() {
        // Roads are stored twice (for each city), so divide count by 2.
        return graphModel.getRoads().values().stream()
                .mapToInt(List::size)
                .sum() / 2;
    }

    private Set<Point2D> getAllConnectedCities() {
        return graphModel.getCities().stream()
                .filter(city -> !graphModel.getConnectedCities(city).isEmpty())
                .collect(Collectors.toSet());
    }

    private GraphType analyzeGraphType() {
        return GraphTypeDetector.detectGraphType(graphModel);
    }

    // For Level4, the required graph type is COMPLEX.
    private GraphType getRequiredGraphType() {
        return GraphType.COMPLEX;
    }

    public void clearCities() {
        graphModel = new CityRoadGraphModel();
        System.out.println("Level4 - Cities cleared.");
    }

    public void clearRoads() {
        for (Point2D city : graphModel.getCities()) {
            graphModel.getRoads().put(city, new ArrayList<>());
        }
    }
}