package roadbuilder;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import roadbuilder.levels.level1.*;
import roadbuilder.util.Point2DUtil;

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainGameRunner {

    private static MainGameRunner instance;
    private List<Point2D> cities = new ArrayList<>();
    private Set<String> roads = new HashSet<>();
    private GameInitializer gameInitializer = new GameInitializer();
    private UIManager uiManager = new UIManager();
    private InputHandler inputHandler = new InputHandler(10000);
    private Level1Game level1Game = new Level1Game();

    private MainGameRunner() {}

    public static MainGameRunner getInstance() {
        if (instance == null) {
            instance = new MainGameRunner();
        }
        return instance;
    }

    public void startLevel1() {
        initGame();
        initUI();
        initInput();
        initGraph();
        displayGraphType();
    }

    private void initGame() {
        gameInitializer.initGame(cities);
        level1Game.initializeLevel(cities);
    }

    private void initUI() {
        uiManager.initUI();
    }

    private void initInput() {
        inputHandler.initInput(cities, roads);
    }

    private void initGraph() {
        for (String road : roads) {
            String[] points = road.split("-");
            Point2D start = Point2DUtil.fromString(points[0]);
            Point2D end = Point2DUtil.fromString(points[1]);
            level1Game.addRoad(start, end);
        }
        level1Game.getConnectedCities(cities.get(0)); // Example usage
    }

    private void displayGraphType() {
        GraphTypeDetector.GraphType graphType = level1Game.analyzeGraphType(roads, cities);
        System.out.println("The graph type is: " + graphType);
    }

    public static void main(String[] args) {
        MainGameRunner.getInstance().startLevel1();
    }
}