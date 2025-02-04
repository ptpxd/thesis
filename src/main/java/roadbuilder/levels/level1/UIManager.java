package roadbuilder.levels.level1;

import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.paint.Color;

public class UIManager {
    public void initUI() {
        var budgetText = FXGL.getUIFactoryService().newText("", Color.BLACK, 24);
        budgetText.textProperty().bind(FXGL.getWorldProperties().intProperty("budget").asString("Budget: %d"));
        FXGL.addUINode(budgetText, 10, 30);
    }
}