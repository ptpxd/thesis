package roadbuilder.levels.level1;

import javafx.geometry.Point2D;
import roadbuilder.model.CityRoadGraphModel;

public class GraphTestCases {

    public static void main(String[] args) {
        // Complete graph test
        completeGraphTest();

        // Complex graph test
        complexGraphTest();
    }

    private static void completeGraphTest() {
        Level1Game game = new Level1Game();

        // Add 3 cities
        game.addCity(new Point2D(0, 0));
        game.addCity(new Point2D(1, 1));
        game.addCity(new Point2D(2, 2));

        // Connect every city to every other city
        game.addRoad(new Point2D(0, 0), new Point2D(1, 1));
        game.addRoad(new Point2D(0, 0), new Point2D(2, 2));
        game.addRoad(new Point2D(1, 1), new Point2D(2, 2));

        System.out.println("Complete graph test:");
        game.displayGraphType();
    }

    private static void complexGraphTest() {
        Level1Game game = new Level1Game();

        // Add 4 cities
        game.addCity(new Point2D(0, 0));
        game.addCity(new Point2D(1, 1));
        game.addCity(new Point2D(2, 2));
        game.addCity(new Point2D(3, 3));

        // Create a complex connection pattern
        game.addRoad(new Point2D(0, 0), new Point2D(1, 1));
        game.addRoad(new Point2D(1, 1), new Point2D(2, 2));
        game.addRoad(new Point2D(2, 2), new Point2D(3, 3));
        game.addRoad(new Point2D(3, 3), new Point2D(0, 0));
        game.addRoad(new Point2D(0, 0), new Point2D(2, 2));

        System.out.println("Complex graph test:");
        game.displayGraphType();
    }
}