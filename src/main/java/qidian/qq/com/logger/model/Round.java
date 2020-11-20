package qidian.qq.com.logger.model;

import java.util.*;

public class Round {
    public HashMap[] grounds = new HashMap[2];
    public final int time;
    final int index;
    final String part;

    public Round(int time, int index, String part) {
        HashMap<Item, ArrayList<Group>> groundb = new HashMap<>();
        HashMap<Item, ArrayList<Group>> groundg = new HashMap<>();
        grounds[0] = groundb;
        grounds[1] = groundg;
        this.time = time;
        this.index = index;
        this.part = part;
    }

    public ArrayList<Group> getList(Gender gender, Item item){
        return (ArrayList<Group>) grounds[gender.ordinal()].computeIfAbsent(item, k -> new ArrayList<Group>());
    }

    public int getGroupCount(Gender gender){
        HashSet<Group> res = new HashSet<>();
        for(Map.Entry<Item,ArrayList<Group>> groups : (Set<Map.Entry<Item,ArrayList<Group>>>)grounds[gender.ordinal()].entrySet()){
            res.addAll(groups.getValue());
        }
        return res.size();
    }

    public int getGroupCount(Gender gender, Group group){
        int sum = 0;
        for(Map.Entry<Item,ArrayList<Group>> groups : (Set<Map.Entry<Item,ArrayList<Group>>>)grounds[gender.ordinal()].entrySet()){
            if(group.items.contains(groups.getKey())){
                int add = 1;
                if(groups.getKey().tn == Tn.T1){
                    add += 1;
                }
                sum += groups.getValue().size() * add;
            }
        }
        return sum;
    }

    public int getGround(Group group){
        return getGroupCount(group.gender, group);
    }

    public boolean add(Group group) {
        boolean res = true;
        for(Item item : group.items){
            if(item.base != Base.LAND){
                continue;
            }
            res &= getList(group.gender, item).add(group);
        }
        return res;
    }

    public int mark() {
        int dev = 0;
        for(HashMap hashMap : grounds){
            for(Map.Entry<Item,ArrayList<Group>> groups : (Set<Map.Entry<Item,ArrayList<Group>>>)hashMap.entrySet()){
                if(groups.getValue().size() > 0){
                    dev++;
                }
            }
        }
        return dev;
    }

    public int test(Group group) {
        return getGround(group) + getGroupCount(group.gender);
    }
}
