import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;
import roadbuilder.levels.level1.Level1Game;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class Level1GameTest {

    // Initializes level with 4 default cities at specific coordinates
    @Test
    public void test_initialize_level_adds_four_default_cities() {
        // Arrange
        Level1Game level1Game = new Level1Game();
        List<Point2D> initialCities = new ArrayList<>();

        // Act
        level1Game.initializeLevel(initialCities);

        // Assert
        assertEquals(4, initialCities.size());
        assertTrue(initialCities.contains(new Point2D(80, 80)));
        assertTrue(initialCities.contains(new Point2D(200, 80)));
        assertTrue(initialCities.contains(new Point2D(80, 200)));
        assertTrue(initialCities.contains(new Point2D(200, 200)));

        // Verify cities were added to the graph model
        Map<Point2D, List<Point2D>> roads = level1Game.getGraphAnalysis();
        assertEquals(4, roads.keySet().size());
    }

    // Handles initialCities being null
    @Test
    public void test_initialize_level_throws_exception_when_initial_cities_is_null() {
        // Arrange
        Level1Game level1Game = new Level1Game();
        List<Point2D> initialCities = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            level1Game.initializeLevel(initialCities);
        });
    }

    // Test adding a city updates the graph model
    @Test
    public void test_add_city_updates_graph_model() {
        // Arrange
        Level1Game level1Game = new Level1Game();
        Point2D city = new Point2D(100, 100);

        // Act
        level1Game.addCity(city);

        // Assert
        Map<Point2D, List<Point2D>> roads = level1Game.getGraphAnalysis();
        assertTrue(roads.containsKey(city));
        assertEquals(0, roads.get(city).size()); // No roads connected yet
    }

    // Test adding a road updates the graph model
    @Test
    public void test_add_road_updates_graph_model() {
        // Arrange
        Level1Game level1Game = new Level1Game();
        Point2D city1 = new Point2D(100, 100);
        Point2D city2 = new Point2D(200, 200);
        level1Game.addCity(city1);
        level1Game.addCity(city2);

        // Act
        level1Game.addRoad(city1, city2);

        // Assert
        Map<Point2D, List<Point2D>> roads = level1Game.getGraphAnalysis();
        assertTrue(roads.get(city1).contains(city2));
        assertTrue(roads.get(city2).contains(city1));
    }

    // Test clearing roads removes all connections
    @Test
    public void test_clear_roads_removes_all_connections() {
        // Arrange
        Level1Game level1Game = new Level1Game();
        Point2D city1 = new Point2D(100, 100);
        Point2D city2 = new Point2D(200, 200);
        level1Game.addCity(city1);
        level1Game.addCity(city2);
        level1Game.addRoad(city1, city2);

        // Act
        level1Game.clearRoads();

        // Assert
        Map<Point2D, List<Point2D>> roads = level1Game.getGraphAnalysis();
        assertTrue(roads.get(city1).isEmpty());
        assertTrue(roads.get(city2).isEmpty());
    }

    // Test displaying graph type
    @Test
    public void test_display_graph_type_outputs_correct_information() {
        // Arrange
        Level1Game level1Game = new Level1Game();
        List<Point2D> initialCities = new ArrayList<>();
        level1Game.initializeLevel(initialCities);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Act
        level1Game.displayGraphType();

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Current Graph Type: NONE"));
        assertTrue(output.contains("Number of cities: 4"));
        assertTrue(output.contains("Number of roads: 0"));
    }
}