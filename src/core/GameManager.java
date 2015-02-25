package core;

import graphics.RenderManagerComponent;
import graphics.RenderManagerSystem;

public class GameManager extends AbstractEntity {

    public RenderManagerComponent rmc;
    public EntityListComponent elc;

    public GameManager() {
        rmc = add(new RenderManagerComponent());
        add(new RenderManagerSystem(rmc));

        FPSManagerComponent fmc = add(new FPSManagerComponent());
        add(new FPSManagerSystem(fmc));

        elc = add(new EntityListComponent());
    }
}
