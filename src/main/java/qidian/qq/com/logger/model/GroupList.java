package qidian.qq.com.logger.model;

import com.sun.tools.javac.util.Pair;
import lombok.Getter;
import qidian.qq.com.logger.utils.ConsoleProgressBar;
import qidian.qq.com.logger.utils.Setting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Getter public class GroupList {

    ArrayList<Group> groups = new ArrayList<>();
    ArrayList<Person> rest = new ArrayList<>();

    public static Pair<GroupList, LinkedHashMap<String, ArrayList<Person>>> init(ArrayList<Person> people, Setting setting){
        ConsoleProgressBar cpb1 = new ConsoleProgressBar(0, people.size(), 51, "初次组队");
        LinkedHashMap<String, ArrayList<Person>> cal = new LinkedHashMap<>();
        int i4 = 0;
        GroupList groupList = new GroupList();
        for(Person person:people){
            cpb1.show(++i4);
            if(!person.isAppend()){
                String personKey = person.getItemsKey();
                cal.compute(personKey, (key, value) -> {
                    if(value == null){
                        value = new ArrayList<>();
                    }
                    value.add(person);
                    return value;
                });
            }else{
                groupList.getRest().add(person);
            }
        }
        cal.replaceAll((key, value) -> {
            Group group = new Group();
            for(Person person:value){
                int t = group.add(person);
                if(t == setting.getGroupSize()){
                    groupList.add(group);
                    group = new Group();
                }
            }
            return group.getPeople();
        });
        return Pair.of(groupList, cal);
    }

    public void add(Group group) {
        groups.add(group);
    }

    @Override
    public GroupList clone(){
        GroupList groupList = new GroupList();
        for(Group group: groups){
            groupList.groups.add(group.clone());
        }
        groupList.rest.addAll(rest);
        return groupList;
    }

    public static LinkedHashMap<String, ArrayList<Person>> copy(HashMap<String, ArrayList<Person>> cal){
        LinkedHashMap<String, ArrayList<Person>> res = new LinkedHashMap<>(cal);
        res.replaceAll((k, v) -> {
            return new ArrayList<Person>(v);
        });
        return res;
    }


    public void restAll(Collection<? extends Person> c){
        if(c == null) {
            return;
        }
        rest.addAll(c);
    }


    public GroupList mini(GroupList other){
        if (other == null) {
            return this;
        } else {
            if (other.rest.size() > this.rest.size()) {
                return this;
//                    System.out.println("分组共"+other.groups.size()+"队, 剩余"+other.rest.size()+"无法分组");
            } else if (other.rest.size() == this.rest.size()
                    && other.groups.size() > this.groups.size()) {
                return this;
//                    System.out.println("分组共"+other.groups.size()+"队, 剩余"+other.rest.size()+"无法分组");
            }
        }
        return other;
    }

    public void addAll(GroupList c){
        groups.addAll(c.groups);
        rest.addAll(c.rest);
        System.out.println("分组共"+this.groups.size()+"队, 剩余"+this.rest.size()+"无分组");
    }
}
