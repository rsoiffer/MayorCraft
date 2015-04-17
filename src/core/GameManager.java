package core;

import game.MusicComponent;
import game.MusicSystem;
import game.ResourcesComponent;
import graphics.RenderManagerComponent;
import graphics.RenderManagerSystem;
import gui.GuiSystem;
import gui.InterfaceComponent;
import gui.InterfaceSystem;
import units.SelectorComponent;
import world.GridComponent;
import world.GridSystem;

public class GameManager extends AbstractEntity {

    public RenderManagerComponent rmc;
    public EntityListComponent elc;
    public SelectorComponent sc;
    public GridComponent gc;
    public ResourcesComponent rc;

    public GameManager() {
        rmc = add(new RenderManagerComponent());
        add(new RenderManagerSystem(rmc));

        FPSManagerComponent fmc = add(new FPSManagerComponent());
        add(new FPSManagerSystem(fmc));

        elc = add(new EntityListComponent());

        rc = new ResourcesComponent();
        sc = add(new SelectorComponent());
        InterfaceComponent ic = add(new InterfaceComponent());
        add(new InterfaceSystem(rc, sc, ic));
        add(new GuiSystem(rc, ic));

        gc = add(new GridComponent());
        add(new GridSystem(gc));

        MusicComponent mc = add(new MusicComponent());
        add(new MusicSystem(mc));
    }
}
