package units;

import core.AbstractComponent;
import core.Vec2;
import java.util.ArrayList;

public class SelectorComponent extends AbstractComponent {

    public ArrayList<SelectableComponent> all = new ArrayList();
    public ArrayList<SelectableComponent> selected = new ArrayList();
    public Vec2 dragStart;
    public int id;
}
