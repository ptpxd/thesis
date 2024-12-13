package roadbuilder.app;

import com.almasb.fxgl.entity.component.Component;
import roadbuilder.model.TileType;

public class TileComponent extends Component {
    private TileType tileType;

    public TileComponent(TileType tileType) {
        this.tileType = tileType;
    }

    public TileType getTileType() {
        return tileType;
    }

    public void setTileType(TileType tileType) {
        this.tileType = tileType;
    }
}