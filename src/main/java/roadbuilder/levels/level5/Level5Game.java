package roadbuilder.levels.level5;

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
 * Level5Game uses the CityRoadGraphModel to manage and analyze the road graph for level 5.
 * For Level5, the graph must be COMPLEX.
 */
public class Level5Game {
    private CityRoadGraphModel graphModel;

    public Level5Game() {
        graphModel = new CityRoadGraphModel();
    }

    public void initializeLevel() {
        // Reset the graph for Level5
        clearCities();

        // Define Level5 specific cities using javafx.geometry.Point2D
        List<Point2D> freshCities = new ArrayList<>();
        freshCities.add(new Point2D(80, 80));
        freshCities.add(new Point2D(280, 80));
        freshCities.add(new Point2D(480, 80));
        freshCities.add(new Point2D(80, 280));
        freshCities.add(new Point2D(280, 280));
        freshCities.add(new Point2D(480, 280));
        freshCities.add(new Point2D(280, 480));

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
            System.out.println("Level5 - City added: " + city);
            displayGraphType();
            checkLevelCompletion();
        } else {
            if (!graphModel.getRoads().containsKey(city)) {
                graphModel.getRoads().put(city, new ArrayList<>());
            }
            System.out.println("Level5 - City already exists: " + city);
        }
    }

    public void addRoad(Point2D start, Point2D end) {
        boolean added = graphModel.addRoad(start, end);
        if (added) {
            System.out.println("Level5 - Road added between: " + start + " and " + end);
            displayGraphType();
            checkLevelCompletion();
        } else {
            System.out.println("Level5 - Road already exists between: " + start + " and " + end);
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
            System.out.println("Level5 completed! Required graph type: " + requiredGraphType);
            return true;
        } else {
            System.out.println("Level5 not completed. Current graph type: " + currentGraphType +
                    ", Required: " + requiredGraphType);
            return false;
        }
    }

    public void displayGraphType() {
        GraphType graphType = analyzeGraphType();
        System.out.println("\nLevel5 - Current graph type: " + graphType);
        switch (graphType) {
            case NONE:
                System.out.println("No cities or roads.");
                break;
            case SIMPLE:
                System.out.println("Simple graph with no intricate connections.");
                break;
            case COMPLETE:
                System.out.println("Complete graph: every city is interconnected.");
                break;
            case BIPARTITE:
                System.out.println("Bipartite graph: cities partitioned into two groups.");
                break;
            case COMPLEX:
                System.out.println("Complex graph with elaborate connections.");
                break;
        }
        System.out.println("Total cities: " + graphModel.getCities().size());
        System.out.println("Total roads: " + getRoadCount());
        System.out.println("Connected cities: " + getAllConnectedCities());
    }

    private int getRoadCount() {
        // Since roads are bidirectional, count them once by dividing by 2.
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

    // For Level5, the required graph type is COMPLEX.
    private GraphType getRequiredGraphType() {
        return GraphType.COMPLEX;
    }

    public void clearCities() {
        graphModel = new CityRoadGraphModel();
        System.out.println("Level5 - Cities cleared.");
    }

    public void clearRoads() {
        for (Point2D city : graphModel.getCities()) {
            graphModel.getRoads().put(city, new ArrayList<>());
        }
    }
}