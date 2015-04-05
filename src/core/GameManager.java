package core;

import graphics.RenderManagerComponent;
import graphics.RenderManagerSystem;
import units.InterfaceSystem;
import units.SelectorComponent;

public class GameManager extends AbstractEntity {

    public RenderManagerComponent rmc;
    public EntityListComponent elc;
    public SelectorComponent sc;

    public GameManager() {
        rmc = add(new RenderManagerComponent());
        add(new RenderManagerSystem(rmc));

        FPSManagerComponent fmc = add(new FPSManagerComponent());
        add(new FPSManagerSystem(fmc));

        elc = add(new EntityListComponent());

        sc = add(new SelectorComponent());
        add(new InterfaceSystem(sc));
    }
}
