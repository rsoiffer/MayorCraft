package core;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AbstractEntity {

    ArrayList<AbstractComponent> componentList;
    ArrayList<AbstractSystem> systemList;

    public AbstractEntity() {
        componentList = new ArrayList();
        systemList = new ArrayList();
        if (!(this instanceof GameManager)) {
            Main.gameManager.elc.list.add(this);
        }
    }

    protected <E extends AbstractComponent> E add(E c) {
        componentList.add(c);
        return c;
    }

    protected <E extends AbstractSystem> E add(E s) {
        systemList.add(s);
        return s;
    }

    protected void add(AbstractComponent... c) {
        componentList.addAll(Arrays.asList(c));
    }

    protected void add(AbstractSystem... s) {
        systemList.addAll(Arrays.asList(s));
    }

    public void destroySelf() {
        for (AbstractComponent c : componentList) {
            c.destroy();
        }
        for (AbstractSystem s : systemList) {
            s.destroy();
        }
        Main.gameManager.elc.list.remove(this);
    }

    public <E extends AbstractComponent> E getComponent(Class<E> e) {
        for (AbstractComponent c : componentList) {
            if (e.isInstance(c)) {
                return (E) c;
            }
        }
        return null;
    }

    public <E extends AbstractSystem> E getSystem(Class<E> e) {
        for (AbstractSystem s : systemList) {
            if (e.isInstance(s)) {
                return (E) s;
            }
        }
        return null;
    }
}
