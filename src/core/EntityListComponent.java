package core;

import java.util.ArrayList;

public class EntityListComponent extends AbstractComponent {

    public ArrayList<AbstractEntity> list = new ArrayList();

    public <E extends AbstractEntity> E get(Class<E> c) {
        for (AbstractEntity e : list) {
            if (c.isInstance(e)) {
                return (E) e;
            }
        }
        return null;
    }

    public <E extends AbstractEntity> ArrayList<E> getList(Class<E> c) {
        ArrayList<E> r = new ArrayList();
        for (AbstractEntity e : list) {
            if (c.isInstance(e)) {
                r.add((E) e);
            }
        }
        return r;
    }
}
