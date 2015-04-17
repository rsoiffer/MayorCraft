package gui;

import buildings.BuildingType;
import core.AbstractComponent;

public class InterfaceComponent extends AbstractComponent {

    public boolean muted;
    public boolean constructionMode;
    public int buildingSelected = -1;

    public BuildingType selected() {
        if (buildingSelected == -1) {
            return null;
        }
        return BuildingType.values()[buildingSelected];
    }
}
