package units;

import core.Vec2;
import graphics.SpriteContainer;
import graphics.Texture;
import java.io.IOException;

public class Limb {

    public Vec2 pos;
    public Vec2 target;
    public Texture sprite;

    public Limb(String sprite, Vec2 pos) {
        this.pos = pos;
        target = new Vec2();
        try {
            this.sprite = SpriteContainer.loadSprite(sprite, 1, 1).get(0);
        } catch (IOException ex) {
        }
    }
}
