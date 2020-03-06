package qq.com.pojo;

import qq.com.proj.Base;
import qq.com.proj.Gender;
import qq.com.proj.Item;
import qq.com.proj.MTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Group {
    ArrayList<Person> people = new ArrayList<>();
    HashSet<Item> items = new HashSet<>();
    Gender gender = Gender.NONE;
    Base base = Base.LAND;
    private PlayData playData = new PlayData();
    private boolean isAppend = false;
    private MTime poolTime = null;
    private MTime groundTime = null;

    public int add(Person person){
        if(gender == Gender.NONE){
            gender = person.gender;
            if(person.items[0].base == Base.WATERS && person.items[1].base == Base.WATERS){
                base = Base.D;
            } else if (person.items[0].base == Base.WATERS) {
                base = Base.L;
            } else if (person.items[1].base == Base.WATERS) {
                base = Base.S;
            }
        }
        assert gender == person.gender;
        people.add(person);
        items.addAll(Arrays.asList(person.items));
        if(person.append != null)isAppend = true;

        return people.size();
    }

    public int addAll(ArrayList<Person> personArrayList){
        int c = 14 - people.size();
        c = Math.min(c, personArrayList.size());
        for(int i = 0; i < c; i++){
            Person person = personArrayList.get(0);
            add(person);
            personArrayList.remove(0);
        }
        return people.size();
    }


    public boolean isAppend() {
        return isAppend;
    }

    public Gender getGender() {
        return gender;
    }

    public void rmTime(MTime time) {
        if(time.ordinal() == time.i()){
            assert groundTime == null;
            groundTime = time;
        }else{
            assert poolTime == null;
            poolTime = time;
        }
        playData.remove(time);
    }

    public boolean testTime(MTime time) {
        return playData.ok[time.ordinal()];
    }

    public MTime getPoolTime() {
        return poolTime;
    }

    public MTime getGroundTime() {
        return groundTime;
    }

    @Override
    public Group clone(){
        Group group = new Group();
        group.people.addAll(people);
        group.items.addAll(items);
        group.gender = gender;
        group.base = base;
        group.playData = playData.clone();
        group.isAppend = isAppend;
        group.poolTime = poolTime;
        group.groundTime = groundTime;
        return group;
    }

    public String toPString() {
        return "Group{" +
                "items=" + items +
                ", gender=" + gender +
                ", playData=" + playData +
                ", poolTime=" + poolTime +
                ", groundTime=" + groundTime +
                '}';
    }
}
