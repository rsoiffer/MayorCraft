package units;

import core.Color4d;
import core.Vec2;
import graphics.Graphics;
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
        color1 = Color4d.BLUE;
        color2 = Color4d.BLUE;
        try {
            this.sprite = SpriteContainer.loadSprite(sprite, 1, 1).get(0);
        } catch (IOException ex) {
        }
    }
    public Limb(String sprite, Color4d c1, Color4d c2){
        pos = new Vec2();
        target = new Vec2();
        color1 = c1;
        color2 = c2;
          try {
            this.sprite = SpriteContainer.loadSprite(sprite, 1, 1).get(0);
        } catch (IOException ex) {
        }
    }
}
