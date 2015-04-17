package buildings;

import buildings.BuildingType;
import core.AbstractComponent;

public class BuildingTypeComponent extends AbstractComponent {

    public BuildingType type;

    public BuildingTypeComponent(BuildingType type) {
        this.type = type;
    }
}
