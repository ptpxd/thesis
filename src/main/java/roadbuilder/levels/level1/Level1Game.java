package roadbuilder.levels.level1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.geometry.Point2D;
import roadbuilder.app.ProgressManager;
import roadbuilder.model.CityRoadGraphModel;
import roadbuilder.model.GraphType;
import roadbuilder.levels.level1.GraphTypeDetector;

public class Level1Game {
    private CityRoadGraphModel graphModel;

    public Level1Game() {
        graphModel = new CityRoadGraphModel();
    }

    public void initializeLevel(List<Point2D> initialCities) {
        // Clear the external cities container to prevent duplicate cities on level restart
        initialCities.clear();
        // Optionally, add new default cities here. Adjust coordinates as needed.
        initialCities.add(new Point2D(80, 80));
        initialCities.add(new Point2D(200, 80));
        initialCities.add(new Point2D(80, 200));
        initialCities.add(new Point2D(200, 200));

        // Reinitialize the graphModel for a fresh start
        graphModel = new CityRoadGraphModel();
        for (Point2D city : initialCities) {
            addCity(city);
        }
        graphModel.printCityConnections();
    }

    public void addCity(Point2D city) {
        boolean added = graphModel.addCity(city);
        if (added) {
            System.out.println("Level1 - City added at: " + city);
            displayGraphType();
            checkLevelCompletion();
        } else {
            // In case the city already exists, ensure its roads list is not null
            if (!graphModel.getRoads().containsKey(city)) {
                graphModel.getRoads().put(city, new ArrayList<>());
            }
            System.out.println("Level1 - City already exists at: " + city);
        }
    }

    public void addRoad(Point2D start, Point2D end) {
        boolean added = graphModel.addRoad(start, end);
        if (added) {
            System.out.println("Level1 - Road added between: " + start + " and " + end);
            displayGraphType();
            checkLevelCompletion();
        } else {
            System.out.println("Level1 - Road already exists between: " + start + " and " + end);
        }
    }

    public List<Point2D> getConnectedCities(Point2D city) {
        return graphModel.getConnectedCities(city);
    }

    public Map<Point2D, List<Point2D>> getGraphAnalysis() {
        return graphModel.getRoads();
    }

    public boolean checkLevelCompletion() {
        int currentLevel = ProgressManager.getHighestCompletedLevel() + 1;
        GraphType requiredGraphType = getRequiredGraphType(currentLevel);
        GraphType currentGraphType = analyzeGraphType();
        if (currentGraphType == requiredGraphType) {
            ProgressManager.markLevelAsCompleted(currentLevel);
            System.out.println("Level1 completed! Required graph type: " + requiredGraphType);
            return true;
        } else {
            System.out.println("Not completed. Current: " + currentGraphType + " Required: " + requiredGraphType);
            return false;
        }
    }

    public void displayGraphType() {
        GraphType graphType = analyzeGraphType();
        System.out.println("\nLevel1 - Current Graph Type: " + graphType);
        switch (graphType) {
            case NONE:
                System.out.println("No cities or roads added yet.");
                break;
            case SIMPLE:
                System.out.println("Simple graph with no complex connections.");
                break;
            case COMPLETE:
                System.out.println("Complete graph where every city is connected.");
                break;
            case BIPARTITE:
                System.out.println("Bipartite graph: cities divided into two sets.");
                break;
            case COMPLEX:
                System.out.println("Complex graph with no clear pattern.");
                break;
        }
        System.out.println("Number of cities: " + graphModel.getCities().size());
        System.out.println("Number of roads: " + getRoadCount());
        System.out.println("Connected cities: " + getAllConnectedCities());
    }

    private int getRoadCount() {
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

    private GraphType getRequiredGraphType(int level) {
        switch (level) {
            case 1:
                return GraphType.SIMPLE;
            case 2:
                return GraphType.COMPLETE;
            case 3:
                return GraphType.BIPARTITE;
            case 4:
                return GraphType.COMPLEX;
            case 5:
                return GraphType.COMPLEX;
            default:
                return GraphType.SIMPLE;
        }
    }

    public void clearRoads() {
        // Reset roads for each city to an empty list
        for (Point2D city : graphModel.getCities()) {
            graphModel.getRoads().put(city, new ArrayList<>());
        }
    }
}