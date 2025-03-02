package roadbuilder.levels.level1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import roadbuilder.MainMenu;
import roadbuilder.app.ProgressManager;
import roadbuilder.model.CityRoadGraphModel;
import roadbuilder.model.GraphType;
import roadbuilder.levels.level1.GraphTypeDetector;

public class Level1Game {
    private CityRoadGraphModel graphModel;

    public Level1Game() {
        graphModel = new CityRoadGraphModel();
    }

    /**
     * Initializes the level with default city positions.
     *
     * @param initialCities A list container for city positions.
     */
    public void initializeLevel(List<Point2D> initialCities) {
        // Clear the external cities container to prevent duplicate cities on level restart.
        initialCities.clear();
        // Optionally add new default cities; adjust coordinates as needed.
        initialCities.add(new Point2D(80, 80));
        initialCities.add(new Point2D(200, 80));
        initialCities.add(new Point2D(80, 200));
        initialCities.add(new Point2D(200, 200));

        // Reinitialize the graphModel for a fresh start.
        graphModel = new CityRoadGraphModel();
        for (Point2D city : initialCities) {
            addCity(city);
        }
        graphModel.printCityConnections();
    }

    /**
     * Adds a city to the graph model.
     *
     * @param city The position of the city.
     */
    public void addCity(Point2D city) {
        boolean added = graphModel.addCity(city);
        if (added) {
            System.out.println("Level1 - City added at: " + city);
            displayGraphType();
            checkLevelCompletion();
        } else {
            // In case the city already exists, ensure its roads list is not null.
            if (!graphModel.getRoads().containsKey(city)) {
                graphModel.getRoads().put(city, new ArrayList<>());
            }
            System.out.println("Level1 - City already exists at: " + city);
        }
    }

    /**
     * Adds a road between two cities.
     *
     * @param start Starting point.
     * @param end   Ending point.
     */
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

    /**
     * Retrieves cities directly connected to the given city.
     *
     * @param city The city to query.
     * @return A list of connected cities.
     */
    public List<Point2D> getConnectedCities(Point2D city) {
        return graphModel.getConnectedCities(city);
    }

    /**
     * Provides the current road network analysis.
     *
     * @return The mapping of each city to its connected cities.
     */
    public Map<Point2D, List<Point2D>> getGraphAnalysis() {
        return graphModel.getRoads();
    }

    /**
     * Checks whether the level has been completed by comparing the current graph
     * type with the required graph type.
     *
     * @return true if completed; false otherwise.
     */
    public boolean checkLevelCompletion() {
        int currentLevel = ProgressManager.getHighestCompletedLevel() + 1;
        GraphType requiredGraphType = getRequiredGraphType(currentLevel);
        GraphType currentGraphType = analyzeGraphType();
        if (currentGraphType == requiredGraphType) {
            ProgressManager.markLevelAsCompleted(currentLevel);
            showCompletionPopup();
            System.out.println("Level1 completed! Required graph type: " + requiredGraphType);
            return true;
        } else {
            System.out.println("Not completed. Current: " + currentGraphType + " Required: " + requiredGraphType);
            return false;
        }
    }

    /**
     * Displays the current graph type and additional information.
     */
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

    /**
     * Computes the total number of roads.
     *
     * @return The count of roads.
     */
    private int getRoadCount() {
        return graphModel.getRoads().values().stream()
                .mapToInt(List::size)
                .sum() / 2;
    }

    /**
     * Retrieves a set of cities that have at least one connection.
     *
     * @return A set of connected cities.
     */
    private Set<Point2D> getAllConnectedCities() {
        return graphModel.getCities().stream()
                .filter(city -> !graphModel.getConnectedCities(city).isEmpty())
                .collect(Collectors.toSet());
    }

    /**
     * Analyzes and returns the current graph type.
     *
     * @return The current GraphType.
     */
    private GraphType analyzeGraphType() {
        return GraphTypeDetector.detectGraphType(graphModel);
    }

    /**
     * Determines the required graph type for the given level.
     *
     * @param level The current level.
     * @return The required GraphType.
     */
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

    /**
     * Clears all roads from every city in the graph.
     */
    public void clearRoads() {
        // Reset roads for each city to an empty list.
        for (Point2D city : graphModel.getCities()) {
            graphModel.getRoads().put(city, new ArrayList<>());
        }
    }

    /**
     * Displays a popup indicating level completion, clears the roads immediately,
     * and then navigates back to the level selection.
     */
    private void showCompletionPopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Szint Teljesítve");
        alert.setHeaderText(null);
        alert.setContentText("A szint teljesítve!");
        alert.showAndWait();  // Wait for the user to press OK.

        // Clear the in-memory road data immediately to avoid displaying leftover tiles.
        clearRoads();

        // Trigger UI cleanup and navigation back to the level selection screen.
        new UIManager().triggerBackButton();
    }
}