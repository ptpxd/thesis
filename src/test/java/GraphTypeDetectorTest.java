import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;
import roadbuilder.levels.level1.GraphTypeDetector;
import roadbuilder.model.CityRoadGraphModel;
import roadbuilder.model.GraphType;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphTypeDetectorTest {

    // Correctly identifies a complete graph where every city is connected to every other city
    @Test
    public void test_complete_graph_detection() {
        // Arrange
        CityRoadGraphModel graphModel = new CityRoadGraphModel();

        // Create cities
        Point2D city1 = new Point2D(0, 0);
        Point2D city2 = new Point2D(1, 1);
        Point2D city3 = new Point2D(2, 2);

        graphModel.addCity(city1);
        graphModel.addCity(city2);
        graphModel.addCity(city3);

        // Connect every city to every other city
        graphModel.addRoad(city1, city2);
        graphModel.addRoad(city1, city3);
        graphModel.addRoad(city2, city3);

        // Act
        GraphType result = GraphTypeDetector.detectGraphType(graphModel);

        // Assert
        assertEquals(GraphType.COMPLETE, result);
    }

    // Returns NONE when graphModel is null
    @Test
    public void test_null_graph_model() {
        // Arrange
        CityRoadGraphModel graphModel = null;

        // Act
        GraphType result = GraphTypeDetector.detectGraphType(graphModel);

        // Assert
        assertEquals(GraphType.NONE, result);
    }

    // Returns NONE for a graph with cities but no roads
    @Test
    public void test_none_graph_with_cities_no_roads() {
        // Arrange
        CityRoadGraphModel graphModel = new CityRoadGraphModel();

        // Create cities
        Point2D city1 = new Point2D(0, 0);
        Point2D city2 = new Point2D(1, 1);

        graphModel.addCity(city1);
        graphModel.addCity(city2);

        // Act
        GraphType result = GraphTypeDetector.detectGraphType(graphModel);

        // Assert
        assertEquals(GraphType.NONE, result);
    }

    // Correctly identifies a simple graph (connected, no cycles)
    @Test
    public void test_simple_graph_detection() {
        // Arrange
        CityRoadGraphModel graphModel = new CityRoadGraphModel();

        // Create cities
        Point2D city1 = new Point2D(0, 0);
        Point2D city2 = new Point2D(1, 1);
        Point2D city3 = new Point2D(2, 2);

        graphModel.addCity(city1);
        graphModel.addCity(city2);
        graphModel.addCity(city3);

        // Connect cities to form a simple graph (tree structure)
        graphModel.addRoad(city1, city2);
        graphModel.addRoad(city2, city3);

        // Act
        GraphType result = GraphTypeDetector.detectGraphType(graphModel);

        // Assert
        assertEquals(GraphType.SIMPLE, result);
    }

    // Correctly identifies a bipartite graph (can be colored with two colors)
    @Test
    public void test_bipartite_graph_detection() {
        // Arrange
        CityRoadGraphModel graphModel = new CityRoadGraphModel();

        // Create cities
        Point2D city1 = new Point2D(0, 0);
        Point2D city2 = new Point2D(1, 1);
        Point2D city3 = new Point2D(2, 2);
        Point2D city4 = new Point2D(3, 3);

        graphModel.addCity(city1);
        graphModel.addCity(city2);
        graphModel.addCity(city3);
        graphModel.addCity(city4);

        // Connect cities in a bipartite manner
        graphModel.addRoad(city1, city3);
        graphModel.addRoad(city1, city4);
        graphModel.addRoad(city2, city3);
        graphModel.addRoad(city2, city4);

        // Act
        GraphType result = GraphTypeDetector.detectGraphType(graphModel);

        // Assert
        assertEquals(GraphType.BIPARTITE, result);
    }

    // Correctly identifies a complex graph when it doesn't match other types
    @Test
    public void test_complex_graph_detection() {
        // Arrange
        CityRoadGraphModel graphModel = new CityRoadGraphModel();

        // Create cities
        Point2D city1 = new Point2D(0, 0);
        Point2D city2 = new Point2D(1, 1);
        Point2D city3 = new Point2D(2, 2);
        Point2D city4 = new Point2D(3, 3);

        graphModel.addCity(city1);
        graphModel.addCity(city2);
        graphModel.addCity(city3);
        graphModel.addCity(city4);

        // Connect cities in a way that doesn't form a complete, simple, or bipartite graph
        graphModel.addRoad(city1, city2);
        graphModel.addRoad(city2, city3);
        graphModel.addRoad(city3, city4);
        graphModel.addRoad(city4, city1);
        graphModel.addRoad(city1, city3); // Adding an extra road to create a cycle

        // Act
        GraphType result = GraphTypeDetector.detectGraphType(graphModel);

        // Assert
        assertEquals(GraphType.COMPLEX, result);
    }
}