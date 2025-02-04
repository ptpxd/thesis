package roadbuilder;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import roadbuilder.levels.level1.GameSettingsManager;
import roadbuilder.levels.level1.GameInitializer;
import roadbuilder.levels.level1.UIManager;
import roadbuilder.levels.level1.InputHandler;

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
    }

    private void initGame() {
        gameInitializer.initGame(cities);
    }

    private void initUI() {
        uiManager.initUI();
    }

    private void initInput() {
        inputHandler.initInput(cities, roads);
    }
}