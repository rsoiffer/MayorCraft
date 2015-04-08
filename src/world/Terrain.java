package world;

import graphics.SpriteContainer;
import graphics.Texture;
import java.io.IOException;

public enum Terrain {

    ROCK("Rock"), TREE("tree");
    public Texture tex;

    private Terrain(String name) {
        try {
            tex = SpriteContainer.loadSprite(name, 1, 1).get(0);
        } catch (IOException ex) {
        }
    }
}
