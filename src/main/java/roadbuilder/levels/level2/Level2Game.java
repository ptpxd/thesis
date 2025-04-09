package roadbuilder.levels.level2;

import roadbuilder.app.ProgressManager;
import roadbuilder.model.CityRoadGraphModel;
import roadbuilder.model.GraphType;
import roadbuilder.levels.level1.GraphTypeDetector;
import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Level2Game {
    private CityRoadGraphModel graphModel;

    public Level2Game() {
        graphModel = new CityRoadGraphModel();
    }

    public void initializeLevel() {
        clearCities();

        List<Point2D> freshCities = new ArrayList<>();
        freshCities.add(new Point2D(100, 100));
        freshCities.add(new Point2D(300, 100));
        freshCities.add(new Point2D(100, 300));
        freshCities.add(new Point2D(300, 300));

        for (Point2D city : freshCities) {
            addCity(city);
        }
        graphModel.printCityConnections();
    }

    public void addCity(Point2D city) {
        boolean added = graphModel.addCity(city);
        if (added) {
            System.out.println("Level2 - City added at: " + city);
            displayGraphType();
            checkLevelCompletion();
        } else {
            if (!graphModel.getRoads().containsKey(city)) {
                graphModel.getRoads().put(city, new ArrayList<>());
            }
            System.out.println("Level2 - City already exists at: " + city);
        }
    }

    public void addRoad(Point2D start, Point2D end) {
        boolean added = graphModel.addRoad(start, end);
        if (added) {
            System.out.println("Level2 - Road added between: " + start + " and " + end);
            displayGraphType();
            checkLevelCompletion();
        } else {
            System.out.println("Level2 - Road already exists between: " + start + " and " + end);
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
            System.out.println("Level2 completed! Required graph type: " + requiredGraphType);
            return true;
        } else {
            System.out.println("Level2 not completed. Current: " + currentGraphType + " Required: " + requiredGraphType);
            return false;
        }
    }

    public void displayGraphType() {
        GraphType graphType = analyzeGraphType();
        System.out.println("\nLevel2 - Current Graph Type: " + graphType);
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

    public void clearCities() {
        graphModel = new CityRoadGraphModel();
        System.out.println("Level2 - Cities cleared.");
    }

    public void clearRoads() {
        for (Point2D city : graphModel.getCities()) {
            graphModel.getRoads().put(city, new ArrayList<>());
        }
    }
}