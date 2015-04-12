package buildings;

import core.Color4d;

public enum BuildingType {

    FIRE_STATION(new Color4d(1, 0, 0)),
    LUMBER_YARD(new Color4d(.6, .6, 0)),
    HOUSE(new Color4d(1, .8, .4));

    public final Color4d color;

    private BuildingType(Color4d color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return name().toLowerCase().replace('_', ' ');
    }
}
