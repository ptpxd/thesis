package roadbuilder;

import javafx.geometry.Point2D;
import lombok.Data;
import roadbuilder.levels.level1.Level1Game;
import roadbuilder.levels.level2.Level2Game;
import roadbuilder.levels.level3.Level3Game;
import roadbuilder.levels.level4.Level4Game;
import roadbuilder.levels.level5.Level5Game;
import roadbuilder.levels.level1.GameInitializer;
import roadbuilder.levels.level1.UIManager;
import roadbuilder.levels.level1.InputHandler;
import roadbuilder.util.Point2DUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class MainGameRunner {
    private static MainGameRunner instance;
    private List<Point2D> cities = new ArrayList<>();
    private Set<String> roads = new HashSet<>();

    private GameInitializer gameInitializer = new GameInitializer();
    private UIManager uiManager = new UIManager();
    private InputHandler inputHandler;

    private Level1Game level1Game = new Level1Game();
    private Level2Game level2Game = new Level2Game();
    private Level3Game level3Game = new Level3Game();
    private Level4Game level4Game = new Level4Game();
    private Level5Game level5Game = new Level5Game();

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
        String levelRequirement = "Simple";
        initGame(levelRequirement);
        initInput();
        initGraph();
        initUI();
    }

    public void startLevel2() {
        cities.clear();
        roads.clear();
        String levelRequirement = "Complete";
        initGame(levelRequirement);
        level2Game.initializeLevel();
        initInput();
        initGraph();
        initUI();
    }

    public void startLevel3() {
        cities.clear();
        roads.clear();
        String levelRequirement = "Bipartite";
        initGame(levelRequirement);
        level3Game.initializeLevel();
        initInput();
        initGraph();
        initUI();
    }

    public void startLevel4() {
        cities.clear();
        roads.clear();
        String levelRequirement = "Complex";
        initGame(levelRequirement);
        level4Game.initializeLevel();
        initInput();
        initGraph();
        initUI();
    }

    public void startLevel5() {
        cities.clear();
        roads.clear();
        String levelRequirement = "Complex";
        initGame(levelRequirement);
        level5Game.initializeLevel();
        initInput();
        initGraph();
        initUI();
    }

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
        MainGameRunner runner = MainGameRunner.getInstance();
    }
}