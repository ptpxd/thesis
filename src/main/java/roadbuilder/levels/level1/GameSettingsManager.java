package roadbuilder.levels.level1;

import com.almasb.fxgl.app.GameSettings;

public class GameSettingsManager {
    public void initSettings(GameSettings settings, int gridWidth, int gridHeight, int tileSize) {
        settings.setWidth(gridWidth * tileSize);
        settings.setHeight(gridHeight * tileSize);
        settings.setTitle("Level 1: Graph Theory");
        settings.setVersion("1.0");
    }
}