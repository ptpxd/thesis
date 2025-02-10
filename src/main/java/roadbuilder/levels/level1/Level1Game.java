package roadbuilder.levels.level1;

import javafx.geometry.Point2D;
import java.util.List;
import java.util.Set;
import roadbuilder.levels.level1.GraphTypeDetector.GraphType;
import roadbuilder.model.CityRoadGraphModel;

public class Level1Game {

    private CityRoadGraphModel graphModel;

    public Level1Game() {
        graphModel = new CityRoadGraphModel();
    }

    public void initializeLevel(List<Point2D> initialCities) {
        for (Point2D city : initialCities) {
            graphModel.addCity(city);
        }

        if (initialCities.size() > 1) {
            addRoad(initialCities.get(0), initialCities.get(1));
        }

        graphModel.printCityConnections();
    }

    public void addCity(Point2D city) {
        graphModel.addCity(city);
    }

    public void addRoad(Point2D start, Point2D end) {
        graphModel.addRoad(start, end);
        // Visualization is handled by RoadBuilder, so no need to draw here
    }

    public List<Point2D> getConnectedCities(Point2D city) {
        return graphModel.getConnectedCities(city);
    }

    public boolean checkLevelCompletion() {
        // Example logic to check if all cities are connected
        for (Point2D city : graphModel.getCities()) {
            if (graphModel.getConnectedCities(city).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public GraphType analyzeGraphType(Set<String> roads, List<Point2D> cities) {
        return GraphTypeDetector.detectGraphType(roads, cities);
    }
}