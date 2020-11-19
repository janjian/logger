package qidian.qq.com.logger.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

@Getter
public class Group {
    ArrayList<Person> people = new ArrayList<>();
    HashSet<Item> items = new HashSet<>();
    Gender gender = Gender.NONE;
    Base base = Base.LAND;
    PlayData playData = new PlayData();

    @Override
    public Group clone(){
        Group group = new Group();
        group.people.addAll(people);
        group.items.addAll(items);
        group.gender = gender;
        group.base = base;
        return group;
    }

    public int add(Person person) {
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
        people.add(person);
        items.addAll(Arrays.asList(person.items));

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

    public void rmTime(MTime time) {
        playData.remove(time);
    }
}
