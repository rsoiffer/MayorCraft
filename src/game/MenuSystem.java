package game;

import static buildings.BuildingType.HOUSE;
import core.*;
import graphics.Graphics;
import static graphics.SpriteContainer.loadSprite;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import units.Unit;
import world.Center;
import world.World;
import world.WorldComponent;

public class MenuSystem extends AbstractSystem {

    private int page = 1; //0 for game, 1 for main, 2 for help, 3 for credits

    @Override
    public void update() {
        try {
            switch (page) {
                case 0:
                    if (Keys.isPressed(Keyboard.KEY_ESCAPE)) {
                        page = 1;

                        Main.gameManager.stopWorld();

                        while (!Main.gameManager.elc.list.isEmpty()) {
                            Main.gameManager.elc.list.get(0).destroySelf();
                        }
                    }
                    break;
                case 1:
                    Graphics.drawSprite(loadSprite("menu"), new Vec2(), new Vec2(1, 1), 0, Color4d.WHITE);
                    //Play
                    if (MouseInput.isReleased(0) && MouseInput.mouseScreen().containedBy(new Vec2(840, 660), new Vec2(1080, 540))) {
                        page = 0;

                        Main.gameManager.startWorld();

                        //Create entities
                        World w = new World();

                        Center c;
                        do {
                            c = w.getComponent(WorldComponent.class).centers.get((int) (Math.random() * World.POINTS));
                        } while (!c.isLand || c.terrain != null);

                        new Unit(c.pos.add(Vec2.random(200)), null);
                        for (int i = 0; i < 10; i++) {
                            new Unit(c.pos.add(Vec2.random(200)), HOUSE);
                        }
                    } //Help
                    if (MouseInput.isReleased(0) && MouseInput.mouseScreen().containedBy(new Vec2(840, 540), new Vec2(1080, 420))) {
                        page = 2;
                    }
                    //Credits
                    if (MouseInput.isReleased(0) && MouseInput.mouseScreen().containedBy(new Vec2(840, 420), new Vec2(1080, 300))) {
                        page = 3;
                    }
                    //Exit
                    if (Keys.isPressed(Keyboard.KEY_ESCAPE) || (MouseInput.isReleased(0) && MouseInput.mouseScreen().containedBy(new Vec2(840, 300), new Vec2(1080, 180)))) {
                        System.exit(0);
                    }
                    break;

                case 2:
                    Graphics.drawSprite(loadSprite("help"), new Vec2(), new Vec2(1, 1), 0, Color4d.WHITE);
                    //Back
                    if (Keys.isPressed(Keyboard.KEY_ESCAPE) || (MouseInput.isReleased(0) && MouseInput.mouseScreen().containedBy(new Vec2(1600, 280), new Vec2(1770, 180)))) {
                        page = 1;
                    }
                    break;
                case 3:
                    Graphics.drawSprite(loadSprite("credits"), new Vec2(), new Vec2(1, 1), 0, Color4d.WHITE);
                    //Back
                    if (Keys.isPressed(Keyboard.KEY_ESCAPE) || (MouseInput.isReleased(0) && MouseInput.mouseScreen().containedBy(new Vec2(1600, 280), new Vec2(1770, 180)))) {
                        page = 1;
                    }
                    break;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
