package core;

import game.MenuSystem;
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

        MusicComponent mc = add(new MusicComponent());
        add(new MusicSystem(mc));

        add(new MenuSystem());
    }

    public void startWorld() {
        rc = add(new ResourcesComponent());
        sc = add(new SelectorComponent());
        InterfaceComponent ic = add(new InterfaceComponent());
        add(new InterfaceSystem(rc, sc, ic));
        add(new GuiSystem(rc, ic));

        gc = add(new GridComponent());
        add(new GridSystem(gc));
    }

    public void stopWorld() {
        rmc.viewPos = new Vec2();
        rmc.viewSize = new Vec2(1920, 1080);
        rmc.zoom = 0;

        rc.destroy();
        componentList.remove(rc);

        sc.destroy();
        componentList.remove(sc);

        getComponent(InterfaceComponent.class).destroy();
        componentList.remove(getComponent(InterfaceComponent.class));

        gc.destroy();
        componentList.remove(gc);

        getSystem(InterfaceSystem.class).destroy();
        systemList.remove(getSystem(InterfaceSystem.class));

        getSystem(GuiSystem.class).destroy();
        systemList.remove(getSystem(GuiSystem.class));

        getSystem(GridSystem.class).destroy();
        systemList.remove(getSystem(GridSystem.class));
    }
}
