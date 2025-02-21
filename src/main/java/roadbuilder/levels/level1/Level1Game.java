package roadbuilder.levels.level1;

import javafx.geometry.Point2D;
import roadbuilder.levels.level1.GraphTypeDetector;
import roadbuilder.model.CityRoadGraphModel;
import roadbuilder.model.GraphType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Level1Game {
    private CityRoadGraphModel graphModel;
    private List<Integer> completedLevels = new ArrayList<>();

    public Level1Game() {
        graphModel = new CityRoadGraphModel();
    }

    public void initializeLevel(List<Point2D> initialCities) {
        try {
            completedLevels.clear(); // Reset completed levels for new game
            for (Point2D city : initialCities) {
                addCity(city);
            }
            graphModel.printCityConnections();
        } catch (Exception e) {
            System.err.println("Error initializing level: " + e.getMessage());
            throw e;
        }
    }

    public boolean addCity(Point2D city) {
        try {
            boolean cityAdded = graphModel.addCity(city);
            if (cityAdded) {
                System.out.println("City added at: " + city);
                displayGraphType();
            } else {
                System.out.println("City already exists at: " + city);
            }
            return cityAdded;
        } catch (Exception e) {
            System.err.println("Error adding city: " + e.getMessage());
            throw e;
        }
    }

    public boolean addRoad(Point2D start, Point2D end) {
        try {
            boolean roadAdded = graphModel.addRoad(start, end);
            if (roadAdded) {
                System.out.println("Road added between: " + start + " and " + end);
                displayGraphType();
            } else {
                System.out.println("Road already exists between: " + start + " and " + end);
            }
            return roadAdded;
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
            int currentLevel = getLastCompletedLevel() + 1;
            GraphType requiredGraphType = getRequiredGraphType(currentLevel);
            GraphType currentGraphType = analyzeGraphType();

            if (currentGraphType == requiredGraphType) {
                markLevelAsCompleted(currentLevel);
                System.out.println("Level completed! Required graph type: " + requiredGraphType);
                return true;
            } else {
                System.out.println("Graph type does not match the required type for this level");
                System.out.println("Current graph type: " + currentGraphType);
                System.out.println("Required graph type: " + requiredGraphType);
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error checking level completion: " + e.getMessage());
            return false;
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
            GraphType graphType = analyzeGraphType();
            System.out.println("\nCurrent Graph Type: " + graphType);

            switch (graphType) {
                case NONE:
                    System.out.println("No cities or roads have been added yet.");
                    break;
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

    private GraphType analyzeGraphType() {
        try {
            return GraphTypeDetector.detectGraphType(graphModel);
        } catch (Exception e) {
            System.err.println("Error analyzing graph type: " + e.getMessage());
            throw e;
        }
    }

    private int getLastCompletedLevel() {
        if (completedLevels.isEmpty()) {
            return 0; // Default to level 0 if no levels are completed
        }
        return completedLevels.get(completedLevels.size() - 1);
    }

    private void markLevelAsCompleted(int level) {
        if (!completedLevels.contains(level)) {
            completedLevels.add(level);
            System.out.println("Level " + level + " marked as completed");
        }
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
                return GraphType.COMPLEX; // Custom complex requirements
            default:
                return GraphType.SIMPLE;
        }
    }
}