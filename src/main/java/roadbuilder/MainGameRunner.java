package roadbuilder;

import roadbuilder.levels.level1.GameInitializer;
import roadbuilder.levels.level1.UIManager;
import roadbuilder.levels.level1.InputHandler;
import roadbuilder.levels.level1.Level1Game;
import roadbuilder.levels.level2.Level2Game;
import roadbuilder.util.Point2DUtil;
import com.almasb.fxgl.dsl.FXGL;
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
    private InputHandler inputHandler;
    private Level1Game level1Game = new Level1Game();
    private Level2Game level2Game = new Level2Game();

    private MainGameRunner() {
        inputHandler = new InputHandler(level1Game);
    }

    public static MainGameRunner getInstance() {
        if (instance == null) {
            instance = new MainGameRunner();
        }
        return instance;
    }

    public void startLevel1() {
        // Define the level requirement for level1
        String levelRequirement = "Simple";
        initGame(levelRequirement);
        initInput();
        initGraph();
        initUI();
    }

    public void startLevel2() {
        cities.clear();
        roads.clear();
        cities.add(new Point2D(100, 100));
        cities.add(new Point2D(300, 100));
        cities.add(new Point2D(100, 300));
        cities.add(new Point2D(300, 300));
        // Define the level requirement for level2
        String levelRequirement = "Complete";
        initGame(levelRequirement);
        // Updated call: Level2Game.initializeLevel() no longer requires cities.
        level2Game.initializeLevel();
        initInput();
        initGraph();
        initUI();
    }

    // Overloaded initGame method which accepts levelRequirement
    private void initGame(String levelRequirement) {
        gameInitializer.initGame(cities, levelRequirement);
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
        if (!cities.isEmpty()) {
            level1Game.getConnectedCities(cities.get(0));
        }
    }

    public void clearRoads() {
        roads.clear();
        level1Game.clearRoads();
    }

    public static void main(String[] args) {
        MainGameRunner.getInstance().startLevel2();
    }
}