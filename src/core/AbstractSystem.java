package core;

public abstract class AbstractSystem {

    public AbstractSystem() {
        Main.systems.get(getLayer()).add(this);
    }

    void destroy() {
        Main.systems.get(getLayer()).remove(this);
    }

    protected int getLayer() {
        return 0;
    }

    public abstract void update();
}
