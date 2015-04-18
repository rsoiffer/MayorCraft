package gui;

import buildings.BuildingType;
import core.*;
import game.ResourcesComponent;
import graphics.*;
import static graphics.SpriteContainer.loadSprite;
import java.awt.Font;
import java.io.IOException;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import units.Unit;
import world.GridComponent;
import static world.GridComponent.SQUARE_SIZE;

public class GuiSystem extends AbstractSystem {

    private ResourcesComponent rc;
    private InterfaceComponent ic;

    public GuiSystem(ResourcesComponent rc, InterfaceComponent ic) {
        this.rc = rc;
        this.ic = ic;
        FontContainer.add("GUI", "Brush Script MT", Font.PLAIN, 30);
    }

    @Override
    public int getLayer() {
        return 2;
    }

    @Override
    public void update() {
        try {
            RenderManagerComponent rmc = Main.gameManager.rmc;

            //Draw grid
            if (ic.constructionMode && rmc.zoom < 10) {
                Color4d.BLACK.glColor();
                glDisable(GL_TEXTURE_2D);
                glBegin(GL_LINES);
                for (int i = SQUARE_SIZE * (int) Math.ceil(rmc.LL().x / SQUARE_SIZE); i < rmc.UR().x; i += SQUARE_SIZE) {
                    new Vec2(i, rmc.LL().y).glVertex();
                    new Vec2(i, rmc.UR().y).glVertex();
                    //System.out.println(i);
                }
                for (int i = SQUARE_SIZE * (int) Math.ceil(rmc.LL().y / SQUARE_SIZE); i < rmc.UR().y; i += SQUARE_SIZE) {
                    new Vec2(rmc.LL().x, i).glVertex();
                    new Vec2(rmc.UR().x, i).glVertex();
                }
                glEnd();
            }
            //Draw potential buildings
            if (ic.constructionMode && ic.buildingSelected >= 0 && ic.selected().enoughResources()) {
                if (MouseInput.mouseScreen().y < 1030 && !MouseInput.mouseScreen().containedBy(new Vec2(1720, 1030 - BuildingType.values().length * 100), new Vec2(1920, 1030))) {
                    Vec2 pos = GridComponent.gridlock(MouseInput.mouse());
                    if (ic.selected().validPos()) {
                        Graphics.fillRect(pos.subtract(new Vec2(100, 100)), new Vec2(200, 200), new Color4d(0, 1, 0, .5));
                    } else {
                        Graphics.fillRect(pos.subtract(new Vec2(100, 100)), new Vec2(200, 200), new Color4d(1, 0, 0, .5));
                    }
                }
            }

            //Set view for easy drawing
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(0, rmc.displaySize.x, 0, rmc.displaySize.y, -1, 1);
            glMatrixMode(GL_MODELVIEW);

            //Top bar
            Graphics.fillRect(new Vec2(0, 1030), new Vec2(1920, 1080), new Color4d(.5, .5, 1));
            //Materials
            Graphics.drawSprite(loadSprite("material_icon"), new Vec2(40, 1055), new Vec2(1, 1), 0, Color4d.WHITE);
            Graphics.drawText("" + rc.materials, "GUI", new Vec2(80, 1070), Color.black);
            //Money
            Graphics.drawSprite(loadSprite("money_icon"), new Vec2(145, 1055), new Vec2(1, 1), 0, Color4d.WHITE);
            Graphics.drawText("" + rc.money, "GUI", new Vec2(170, 1070), Color.black);
            //Science
            Graphics.drawSprite(loadSprite("science_icon"), new Vec2(250, 1055), new Vec2(1, 1), 0, Color4d.WHITE);
            Graphics.drawText("" + rc.science, "GUI", new Vec2(275, 1070), Color.black);
            //Food
            Graphics.drawSprite(loadSprite("food_icon"), new Vec2(350, 1055), new Vec2(1, 1), 0, Color4d.WHITE);
            Graphics.drawText("" + rc.food, "GUI", new Vec2(375, 1070), Color.black);
            //Population
            Graphics.drawSprite(loadSprite("population_icon"), new Vec2(450, 1055), new Vec2(1, 1), 0, Color4d.WHITE);
            Graphics.drawText("" + Main.gameManager.elc.getList(Unit.class).size(), "GUI", new Vec2(475, 1070), Color.black);

            //Construction
            if (ic.constructionMode) {
                Graphics.drawSprite(loadSprite("construction_icon"), new Vec2(1750, 1055), new Vec2(1, 1), 0, new Color4d(.5, .5, .5));
            } else {
                Graphics.drawSprite(loadSprite("construction_icon"), new Vec2(1750, 1055), new Vec2(1, 1), 0, Color4d.WHITE);
            }
            //Pause
            if (Main.paused) {
                Graphics.drawSprite(loadSprite("pause"), new Vec2(1840, 1055), new Vec2(1, 1), Math.PI, Color4d.WHITE);
            } else {
                Graphics.drawSprite(loadSprite("pause"), new Vec2(1840, 1055), new Vec2(1, 1), 0, Color4d.WHITE);
            }
            //Mute
            if (ic.muted) {
                Graphics.drawSprite(loadSprite("mute2"), new Vec2(1890, 1055), new Vec2(1, 1), 0, Color4d.WHITE);
            } else {
                Graphics.drawSprite(loadSprite("mute1"), new Vec2(1890, 1055), new Vec2(1, 1), 0, Color4d.WHITE);
            }

            //Building buttons
            if (ic.constructionMode) {
                Graphics.fillRect(new Vec2(1920, 1030), new Vec2(-200, -BuildingType.values().length * 100), new Color4d(.5, .5, 1));
                for (int i = 0; i < BuildingType.values().length; i++) {
                    if (!BuildingType.values()[i].enoughResources()) {
                        Graphics.fillRect(new Vec2(1730, 950 - 100 * i), new Vec2(180, 70), new Color4d(.7, .5, .5));
                    } else if (i == ic.buildingSelected) {
                        Graphics.fillRect(new Vec2(1730, 950 - 100 * i), new Vec2(180, 70), new Color4d(.7, 1, 1));
                    } else {
                        Graphics.fillRect(new Vec2(1730, 950 - 100 * i), new Vec2(180, 70), new Color4d(.7, .7, 1));
                    }
                    Graphics.drawText(BuildingType.values()[i].toString(), "GUI", new Vec2(1730, 1000 - 100 * i), Color.black);
                }
            }

            //Zoom buttons
            Graphics.drawSprite(loadSprite("zoom_in"), new Vec2(50, 125), new Vec2(1, 1), 0, Color4d.WHITE);
            Graphics.drawSprite(loadSprite("zoom_out"), new Vec2(50, 50), new Vec2(1, 1), 0, Color4d.WHITE);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
