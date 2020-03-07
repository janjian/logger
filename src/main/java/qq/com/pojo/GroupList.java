package qq.com.pojo;

import java.util.ArrayList;
import java.util.Collection;

public class GroupList{
    ArrayList<Group> groups = new ArrayList<>();
    ArrayList<Person> rest = new ArrayList<>();

    public void add(Group group) {
        groups.add(group);
    }

    public void addAll(GroupList c){
        groups.addAll(c.groups);
        rest.addAll(c.rest);
    }

    public void restAll(Collection<? extends Person> c){
        if(c == null) {
            return;
        }
        rest.addAll(c);
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

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public ArrayList<Person> getRest() {
        return rest;
    }

    public void makeNo(){
        for(int i = 0; i < groups.size(); i++){
            groups.get(i).setNo(i+1);
        }
    }
}
