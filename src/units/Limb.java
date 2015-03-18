package units;

import core.Color4d;
import core.Vec2;
import graphics.SpriteContainer;
import graphics.Texture;
import java.io.IOException;

public class Limb {
    
    public Vec2 pos;
    public Vec2 target;
    public Color4d color1;
    public Color4d color2;
    public Texture sprite;
    
    public Limb(String sprite) {
        pos = new Vec2();
        target = new Vec2();
        color1 = Color4d.RED;
        color2 = Color4d.BLUE;
        try {
            this.sprite = SpriteContainer.loadSprite(sprite, 1, 1).get(0);
        } catch (IOException ex) {
        }
    }
}
