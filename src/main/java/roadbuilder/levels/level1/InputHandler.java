package roadbuilder.levels.level1;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import roadbuilder.util.Point2DUtil;
import java.util.List;
import java.util.Set;

public class InputHandler {

    private int budget;
    private Point2D firstCityClicked = null;
    private Level1Game level1Game;
    // Flag to prevent multiple initializations of input bindings.
    private boolean inputInitialized = false;

    public InputHandler(int initialBudget, Level1Game level1Game) {
        this.budget = initialBudget;
        this.level1Game = level1Game;
    }

    public void initInput(List<Point2D> cities, Set<String> roads) {
        // Prevent duplicate input binding registration on level restart.
        if (inputInitialized) {
            return;
        }
        inputInitialized = true;

        FXGL.getInput().addAction(new UserAction("Increase Budget") {
            @Override
            protected void onActionBegin() {
                budget += 100;
                FXGL.getWorldProperties().setValue("budget", budget);
            }
        }, KeyCode.B);

        FXGL.getInput().addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Point2D clickPoint = new Point2D(event.getSceneX(), event.getSceneY());
                cities.stream()
                        .filter(city -> isClickWithinCityBounds(city, clickPoint))
                        .findFirst()
                        .ifPresent(city -> handleCityClick(city, cities, roads));
            }
        });
    }

    private boolean isClickWithinCityBounds(Point2D city, Point2D clickPoint) {
        double cityX = city.getX();
        double cityY = city.getY();
        // Assuming city bounds are 40x40 pixels.
        return clickPoint.getX() >= cityX && clickPoint.getX() <= cityX + 40 &&
               clickPoint.getY() >= cityY && clickPoint.getY() <= cityY + 40;
    }

    private void handleCityClick(Point2D city, List<Point2D> cities, Set<String> roads) {
        if (firstCityClicked == null) {
            firstCityClicked = city;
        } else {
            if (!city.equals(firstCityClicked) && !isRoadExists(firstCityClicked, city, roads)) {
                int cost = calculateRoadCost(firstCityClicked, city);
                if (budget >= cost) {
                    RoadBuilder.buildRoad(firstCityClicked, city, roads, cities, level1Game);
                    budget -= cost;
                    FXGL.getWorldProperties().setValue("budget", budget);
                }
            }
            firstCityClicked = null;
        }
    }

    private boolean isRoadExists(Point2D start, Point2D end, Set<String> roads) {
        String roadKey = start.toString() + "-" + end.toString();
        String reverseRoadKey = end.toString() + "-" + start.toString();
        return roads.contains(roadKey) || roads.contains(reverseRoadKey);
    }

    private int calculateRoadCost(Point2D start, Point2D end) {
        return (int) start.distance(end) * 10;
    }
}