package roadbuilder.levels.level1;

import javafx.geometry.Point2D;
import roadbuilder.levels.level1.Level1Game;

public class Level1GameTest {
    public static void main(String[] args) {
        Level1Game game = new Level1Game();

        // Add some cities
        game.addCity(new Point2D(0, 0));
        game.addCity(new Point2D(1, 1));
        game.addCity(new Point2D(2, 2));

        // Add some roads
        game.addRoad(new Point2D(0, 0), new Point2D(1, 1));
        game.addRoad(new Point2D(1, 1), new Point2D(2, 2));

        // Display graph type
        game.displayGraphType();
    }
}