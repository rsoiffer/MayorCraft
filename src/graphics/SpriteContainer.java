package graphics;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class SpriteContainer {

    private static HashMap<String, ArrayList<Texture>> spriteMap = new HashMap();
    private static String path = "sprites/";
    private static String type = ".png";

    public static Texture loadSprite(String name) throws IOException {
        return loadSprite(name, 1, 1).get(0);
    }

    public static ArrayList<Texture> loadSprite(String name, int x, int y) throws IOException {
        if (spriteMap.containsKey(name)) {
            return spriteMap.get(name);
        }
        spriteMap.put(name, TextureLoader.getTextures(path + name + type, x, y));
        return spriteMap.get(name);
    }
}
