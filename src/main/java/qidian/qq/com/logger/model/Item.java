package qidian.qq.com.logger.model;

import java.util.HashMap;
import java.util.Set;

/**
 *
 */
public class Item {

    static HashMap<String, Item> items;

    public int num;
    /**
     *
     */
    public String name;
    /**
     *
     */
    public Tn tn;
    /**
     *
     */
    public Base base;

    private static int num_ir = 1;
    public static Item of(String name, Tn tn) {
        return items.computeIfAbsent(name, k->{
            Item item = new Item();
            item.num = num_ir++;
            item.name = name;
            item.tn = tn;
            Base base = Base.LAND;
            if("25米游泳".equals(name) || "200米游泳".equals(name)){
                base = Base.WATERS;
            }
            item.base = base;
            return item;
        });
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return name != null ? name.equals(item.name) : item.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
