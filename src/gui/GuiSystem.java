package gui;

import core.*;
import graphics.*;
import java.awt.Font;
import java.io.IOException;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.Color;
import units.Unit;

public class GuiSystem extends AbstractSystem {

    private ResourcesComponent rc;
    private InterfaceComponent ic;
    private Texture materialIcon;
    private Texture moneyIcon;
    private Texture scienceIcon;
    private Texture foodIcon;
    private Texture popIcon;
    private Texture constructionIcon;
    private Texture pauseIcon;
    private Texture settingsIcon;

    public GuiSystem(ResourcesComponent rc, InterfaceComponent ic) {
        this.rc = rc;
        this.ic = ic;
        try {
            materialIcon = SpriteContainer.loadSprite("material_icon", 1, 1).get(0);
            moneyIcon = SpriteContainer.loadSprite("money_icon", 1, 1).get(0);
            scienceIcon = SpriteContainer.loadSprite("science_icon", 1, 1).get(0);
            foodIcon = SpriteContainer.loadSprite("food_icon", 1, 1).get(0);
            popIcon = SpriteContainer.loadSprite("population_icon", 1, 1).get(0);
            constructionIcon = SpriteContainer.loadSprite("construction_icon", 1, 1).get(0);
            pauseIcon = SpriteContainer.loadSprite("pause", 1, 1).get(0);
            settingsIcon = SpriteContainer.loadSprite("settings", 1, 1).get(0);
        } catch (IOException ex) {
        }
        FontContainer.add("GUI", "Calibri", Font.PLAIN, 30);
    }

    @Override
    public int getLayer() {
        return 2;
    }

    @Override
    public void update() {
        //Set view for easy drawing
        RenderManagerComponent rmc = Main.gameManager.rmc;
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, rmc.displaySize.x, 0, rmc.displaySize.y, -1, 1);
        glMatrixMode(GL_MODELVIEW);

        //Top bar
        Graphics.fillRect(new Vec2(0, 1030), new Vec2(1920, 1080), new Color4d(.5, .5, 1));
        //Materials
        Graphics.drawSprite(materialIcon, new Vec2(40, 1055), new Vec2(1, 1), 0, Color4d.WHITE);
        Graphics.drawText("" + rc.materials, "GUI", new Vec2(80, 1070), Color.black);
        //Money
        Graphics.drawSprite(moneyIcon, new Vec2(145, 1055), new Vec2(1, 1), 0, Color4d.WHITE);
        Graphics.drawText("" + rc.money, "GUI", new Vec2(170, 1070), Color.black);
        //Science
        Graphics.drawSprite(scienceIcon, new Vec2(250, 1055), new Vec2(1, 1), 0, Color4d.WHITE);
        Graphics.drawText("" + rc.science, "GUI", new Vec2(275, 1070), Color.black);
        //Food
        Graphics.drawSprite(foodIcon, new Vec2(350, 1055), new Vec2(1, 1), 0, Color4d.WHITE);
        Graphics.drawText("" + rc.food, "GUI", new Vec2(375, 1070), Color.black);
        //Population
        Graphics.drawSprite(popIcon, new Vec2(450, 1055), new Vec2(1, 1), 0, Color4d.WHITE);
        Graphics.drawText("" + Main.gameManager.elc.getList(Unit.class).size(), "GUI", new Vec2(475, 1070), Color.black);

        //Construction
        Graphics.drawSprite(constructionIcon, new Vec2(1750, 1055), new Vec2(1, 1), 0, Color4d.WHITE);
        //Pause
        Graphics.drawSprite(pauseIcon, new Vec2(1840, 1055), new Vec2(1, 1), 0, Color4d.WHITE);
        //Settings
        Graphics.drawSprite(settingsIcon, new Vec2(1890, 1055), new Vec2(1, 1), 0, Color4d.WHITE);
    }
}
