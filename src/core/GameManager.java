package core;

import graphics.RenderManagerComponent;
import graphics.RenderManagerSystem;
import gui.GuiSystem;
import gui.InterfaceComponent;
import gui.InterfaceSystem;
import gui.ResourcesComponent;
import units.SelectorComponent;
import world.GridComponent;
import world.GridSystem;

public class GameManager extends AbstractEntity {

    public RenderManagerComponent rmc;
    public EntityListComponent elc;
    public SelectorComponent sc;
    public GridComponent gc;

    public GameManager() {
        rmc = add(new RenderManagerComponent());
        add(new RenderManagerSystem(rmc));

        FPSManagerComponent fmc = add(new FPSManagerComponent());
        add(new FPSManagerSystem(fmc));

        elc = add(new EntityListComponent());

        ResourcesComponent rc = new ResourcesComponent();

        sc = add(new SelectorComponent());
        InterfaceComponent ic = add(new InterfaceComponent());
        add(new InterfaceSystem(sc));
        add(new GuiSystem(rc, ic));

        gc = add(new GridComponent());
        add(new GridSystem(gc));
    }
}
