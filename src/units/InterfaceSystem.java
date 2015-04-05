package units;

import core.AbstractSystem;
import core.Color4d;
import core.MouseInput;
import graphics.Graphics;

public class InterfaceSystem extends AbstractSystem {

    private SelectorComponent sc;

    public InterfaceSystem(SelectorComponent sc) {
        this.sc = sc;
    }

    @Override
    public int getLayer() {
        return 2;
    }

    @Override
    public void update() {
        if (MouseInput.isPressed(0)) {
            sc.dragStart = MouseInput.mouse();
        }
        if (MouseInput.isDown(0)) {
            if (MouseInput.getTime(0) > 10 && sc.dragStart.subtract(MouseInput.mouse()).lengthSquared() > 100) {
                Graphics.fillRect(sc.dragStart, MouseInput.mouse().subtract(sc.dragStart), new Color4d(.2, .2, .8, .4));
            }
        }
        if (MouseInput.isReleased(0)) {
            if (MouseInput.getTime(0) > 10 && sc.dragStart.subtract(MouseInput.mouse()).lengthSquared() > 100) {
                //Select by dragging
                sc.selected.clear();
                for (SelectableComponent s : sc.all) {
                    if (s.pc.pos.containedBy(sc.dragStart, MouseInput.mouse())) {
                        sc.selected.add(s);
                    }
                }
            } else {
                //Click
                for (SelectableComponent s : sc.all) {
                    if (s.pc.pos.subtract(MouseInput.mouse()).lengthSquared() < s.size * s.size) {
                        sc.selected.clear();
                        sc.selected.add(s);
                        return;
                    }
                }
                for (SelectableComponent s : sc.selected) {
                    if (s.dc != null) {
                        s.dc.des = MouseInput.mouse();
                    }
                }
            }
        }
    }
}
