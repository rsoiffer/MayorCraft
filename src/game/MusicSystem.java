package game;

import core.AbstractSystem;
import core.Sounds;

public class MusicSystem extends AbstractSystem {

    private MusicComponent mc;

    public MusicSystem(MusicComponent mc) {
        this.mc = mc;
    }

    @Override
    public void update() {
        if (Sounds.GLOBAL_VOLUME > 0 && (mc.current == null || !Sounds.existsSound(mc.current))) {
            mc.current = mc.all[(int) (Math.random() * mc.all.length)];
            Sounds.playSound(mc.current, false, .5);
        }
    }
}
