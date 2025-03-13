package roadbuilder.levels.level3;

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

public class Level3Game {
    private CityRoadGraphModel graphModel;

    public Level3Game() {
        graphModel = new CityRoadGraphModel();
    }

    public void initializeLevel() {
        // Újraszervezzük a városokat Level3 számára.
        clearCities();

        // Definiáljuk a Level3 specifikus városokat.
        List<Point2D> freshCities = new ArrayList<>();
        freshCities.add(new Point2D(100, 150));
        freshCities.add(new Point2D(300, 150));
        freshCities.add(new Point2D(200, 300));
        freshCities.add(new Point2D(400, 200));
        freshCities.add(new Point2D(150, 400));

        // Hozzáadjuk az új városokat az új graph modellhez.
        for (Point2D city : freshCities) {
            addCity(city);
        }
        graphModel.printCityConnections();
    }

    public void addCity(Point2D city) {
        boolean added = graphModel.addCity(city);
        if (added) {
            System.out.println("Level3 - Város hozzáadva: " + city);
            displayGraphType();
            checkLevelCompletion();
        } else {
            if (!graphModel.getRoads().containsKey(city)) {
                graphModel.getRoads().put(city, new ArrayList<>());
            }
            System.out.println("Level3 - Város már létezik: " + city);
        }
    }

    public void addRoad(Point2D start, Point2D end) {
        boolean added = graphModel.addRoad(start, end);
        if (added) {
            System.out.println("Level3 - Út hozzáadva: " + start + " és " + end);
            displayGraphType();
            checkLevelCompletion();
        } else {
            System.out.println("Level3 - Út már létezik: " + start + " és " + end);
        }
    }

    public List<Point2D> getConnectedCities(Point2D city) {
        return graphModel.getConnectedCities(city);
    }

    public Map<Point2D, List<Point2D>> getGraphAnalysis() {
        return graphModel.getRoads();
    }

    public boolean checkLevelCompletion() {
        // A ProgressManager segítségével megkapjuk az aktuális szintet.
        int currentLevel = ProgressManager.getHighestCompletedLevel() + 1;
        GraphType requiredGraphType = getRequiredGraphType();
        GraphType currentGraphType = analyzeGraphType();
        if (currentGraphType == requiredGraphType) {
            ProgressManager.markLevelAsCompleted(currentLevel);
            System.out.println("Level3 kész! Követelmény: " + requiredGraphType);
            return true;
        } else {
            System.out.println("Level3 még nem kész. Aktuális: " + currentGraphType + " Követelmény: " + requiredGraphType);
            return false;
        }
    }

    public void displayGraphType() {
        GraphType graphType = analyzeGraphType();
        System.out.println("\nLevel3 - Aktuális gráf típus: " + graphType);
        switch (graphType) {
            case NONE:
                System.out.println("Nincsenek városok vagy utak.");
                break;
            case SIMPLE:
                System.out.println("Egyszerű gráf, nincs bonyolult kapcsolat.");
                break;
            case COMPLETE:
                System.out.println("Teljes gráf, minden város összeköttetve.");
                break;
            case BIPARTITE:
                System.out.println("Bipartite gráf: városok két halmazba osztva.");
                break;
            case COMPLEX:
                System.out.println("Komplex gráf, összetett kapcsolatokkal.");
                break;
        }
        System.out.println("Városok száma: " + graphModel.getCities().size());
        System.out.println("Utak száma: " + getRoadCount());
        System.out.println("Összekapcsolt városok: " + getAllConnectedCities());
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

    // Level3 esetén a követelményként elvárt gráf típus: BIPARTITE.
    private GraphType getRequiredGraphType() {
        return GraphType.BIPARTITE;
    }

    public void clearCities() {
        graphModel = new CityRoadGraphModel();
        System.out.println("Level3 - Városok törölve.");
    }

    public void clearRoads() {
        for (Point2D city : graphModel.getCities()) {
            graphModel.getRoads().put(city, new ArrayList<>());
        }
    }
}