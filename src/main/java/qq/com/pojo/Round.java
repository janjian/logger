package qq.com.pojo;

import qq.com.proj.*;

import java.util.*;

public class Round {
    public HashMap[] grounds = new HashMap[2];
    public final MTime time;

    public Round(MTime time) {
        HashMap<Item, ArrayList<Group>> groundb = new HashMap<>();
        HashMap<Item, ArrayList<Group>> groundg = new HashMap<>();
        for(Item item : Item.values()){
            if(item.base == Base.WATERS){
                continue;
            }
            if(item.gender == Gender.BOTH || item.gender == Gender.男){
                groundb.put(item, new ArrayList<>());
            }
            if(item.gender == Gender.BOTH || item.gender == Gender.女){
                groundg.put(item, new ArrayList<>());
            }
        }
        grounds[0] = groundb;
        grounds[1] = groundg;
        this.time = time;
    }

    public ArrayList<Group> getList(Gender gender, Item item){
        return (ArrayList<Group>) grounds[gender.ordinal()].get(item);
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
    public boolean add(int index, Group group) {
        boolean res = true;
        for(Item item : group.items){
            if(item.base != Base.LAND){
                continue;
            }
            getList(group.gender, item).add(index, group);
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
}
