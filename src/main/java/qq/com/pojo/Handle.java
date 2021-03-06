package qq.com.pojo;

import qq.com.ConsoleProgressBar;
import qq.com.ExcelReader;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static qq.com.ExcelReader.MINI_GROUP_SIZE;

public class Handle {
    public static Plan calc(ArrayList<Person> people) throws InterruptedException {
        return makePlan(people, makeGroup(people));
    }

    private static Plan makePlan(ArrayList<Person> people, GroupList groupList) throws InterruptedException {
        return new Plan(people, groupList);
    }

    private static GroupList makeGroup(ArrayList<Person> people) throws InterruptedException {
        ConsoleProgressBar cpb1 = new ConsoleProgressBar(0, people.size(), 51, "初次组队");
        LinkedHashMap<String, ArrayList<Person>> cal = new LinkedHashMap<>();
        int i4 = 0;
        GroupList groupList = new GroupList();
        for(Person person:people){
            cpb1.show(++i4);
            if(person.append == null){
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
                assert t <= 14;
                if(t == 14){
                    groupList.add(group);
                    group = new Group();
                }
            }
            return group.people;
        });
        System.out.println("完美分组共"+groupList.groups.size()+"队, 特殊情况考生共"+groupList.getRest().size()+"人全部拿出来不参与筛选，放在未成功分组处");

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        ConcurrentLinkedQueue<GroupList> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        int count = 50;
        int maxi = ExcelReader.RE_GROUP_COUNT / count;
        ConsoleProgressBar cpb = new ConsoleProgressBar(0, maxi, 44, "优化落单学生组队");
        for(int i3 = 0; i3 < maxi; i3++){
            CountDownLatch latch = new CountDownLatch(count);
            cpb.show(0);
            for(int i = 0; i < count; i++){
                cachedThreadPool.execute(() -> {
                    LinkedHashMap<String, ArrayList<Person>> c = copy(cal);
                    GroupList groupList2 = chou(c, new GroupList());

                    GroupList mini = concurrentLinkedQueue.poll();
                    if (mini == null) {
                        mini = groupList2;
                    } else {
                        if (mini.rest.size() > groupList2.rest.size()) {
                            mini = groupList2;
//                    System.out.println("分组共"+mini.groups.size()+"队, 剩余"+mini.rest.size()+"无法分组");
                        } else if (mini.rest.size() == groupList2.rest.size()
                                && mini.groups.size() > groupList2.groups.size()) {
                            mini = groupList2;
//                    System.out.println("分组共"+mini.groups.size()+"队, 剩余"+mini.rest.size()+"无法分组");
                        }
                    }
                    concurrentLinkedQueue.add(mini);
                    latch.countDown();
                });
            }
            latch.await();
            cpb.show(i3);
        }
        GroupList mini = null;
        for(GroupList groupList2 : concurrentLinkedQueue){
            if(mini == null){
                mini = groupList2;
            }else{
                if(mini.rest.size() > groupList2.rest.size()){
                    mini = groupList2;
//                    System.out.println("分组共"+mini.groups.size()+"队, 剩余"+mini.rest.size()+"无法分组");
                }else if(mini.rest.size() == groupList2.rest.size()
                        && mini.groups.size() > groupList2.groups.size()){
                    mini = groupList2;
//                    System.out.println("分组共"+mini.groups.size()+"队, 剩余"+mini.rest.size()+"无法分组");
                }
            }
        }
        cpb.show(maxi);
        assert mini != null;
        groupList.addAll(mini);
        System.out.println("分组共"+groupList.groups.size()+"队, 剩余"+groupList.rest.size()+"无分组");
        return groupList;
    }

    public static LinkedHashMap<String, ArrayList<Person>> copy(HashMap<String, ArrayList<Person>> cal){
        LinkedHashMap<String, ArrayList<Person>> res = new LinkedHashMap<>(cal);
        res.replaceAll((k, v) -> {
            ArrayList<Person> people = new ArrayList<>(v);
            return people;
        });
        return res;
    }

    private static GroupList chou(LinkedHashMap<String, ArrayList<Person>> cal,
                      GroupList groupList){
        ArrayList<Integer> is = new ArrayList<>();
        for(int i = 0; i < cal.entrySet().size(); i++) {
            is.add(i);
        }
        Collections.shuffle(is);
        int si = 5;
        final int bound = 14 - MINI_GROUP_SIZE + 1;
        do{
            boolean res = false;
            Collections.shuffle(is);
            int g = Math.random() < 0.5 ? 2 : 3;
            int j = 14 - (int)(Math.random()*bound); // (9, 14]
            if(g > 2){
                if(Math.random() > 0.5){
                    for (int i : is) {
                        res |= tripa(cal.entrySet(), groupList, i, g, j);
                    }
                }else{
                    for (int i : is) {
                        res |= pare(cal.entrySet(), groupList, i, g, j);
                    }
                }
            }else{
                for (int i : is) {
                    res |= pare(cal.entrySet(), groupList, i, g, j);
                }
            }
            if(res){
                si = si < 4 ? si + 1 : 4;
            }else{
                si--;
            }
        }while (si > 0);

        for(int g = 3; g >= 2; g--){
            for( int j = 14; j > 12; j--){
                for (int i : is){
                    pare(cal.entrySet(), groupList, i, g, j);
                }
            }
        }
        Collections.shuffle(is);
        for(int g = 3; g >= 2; g--) {
            for( int j = 14; j > 10; j--) {
                for (int i : is) {
                    tripa(cal.entrySet(), groupList, i, g, j);
                }
            }
        }
        Collections.shuffle(is);
        for(int g = 3; g >= 2; g--){
            for( int j = 14; j > 7; j--){
                for (int i : is){
                    pare(cal.entrySet(), groupList, i, g, j);
                }
            }
        }
        cal.forEach((key, value) -> groupList.restAll(value));
        return groupList;
    }

    private static boolean pare(Set<Map.Entry<String, ArrayList<Person>>> entries, GroupList groupList, int start_p, int gap, int mini) {
        Map.Entry<String, ArrayList<Person>> mark = null;
        int start = start_p + 1;
        for (Map.Entry<String, ArrayList<Person>> par : entries) {
            start--;
            if (start == 0){
                mark = par;
                if(mark == null || mark.getValue().size() == 0) {
                    return false;
                }
                break;
            }
        }
        Map.Entry<String, ArrayList<Person>> closet = null;
        assert mark != null;
        int max = mark.getValue().size();
        for (Map.Entry<String, ArrayList<Person>> par : entries) {
            if (par == mark || par == null || par.getValue().size() == 0) {
                continue;
            }
            if (mark.getValue().get(0).mache(par.getValue().get(0)) >= gap) {
                int sum = mark.getValue().size() + par.getValue().size();
                if (sum >= mini && sum <= 14) {
                    if(sum > max){
                        max = sum;
                        closet = par;
                    }
                }
            }
        }
        if(closet != null){
            Group group = new Group();
            int t1 = group.addAll(mark.getValue());
            mark.setValue(mark.getValue());
            assert t1 <= 14;
            int old = closet.getValue().size();
            int t2 = group.addAll(closet.getValue());
            assert t2 <= 14;
            int last = old - t2 + t1;
            assert closet.getValue().size() == last;
//            System.out.println(mark.getKey() + "类型与" + closet.getKey() + "类型凑成一对(" + t2 + ")，后者剩余" + last + "人");
            groupList.add(group);
            return true;
        }else if(max >= mini){
            Group group = new Group();
            int t1 = group.addAll(mark.getValue());
            mark.setValue(mark.getValue());
            assert t1 <= 14;
//            System.out.println(mark.getKey() + "类型自己凑成一对(" + t1 + ")");
            groupList.add(group);
            return true;
        }
        return false;
    }

    private static boolean tripa(Set<Map.Entry<String, ArrayList<Person>>> entries, GroupList groupList, int i, int gap, int mini){
        Map.Entry<String, ArrayList<Person>> mark = null;
        ArrayList<Map.Entry<String, ArrayList<Person>>> list = new ArrayList<>();
        int start = i + 1;
        for (Map.Entry<String, ArrayList<Person>> par : entries) {
            start--;
            if (start == 0){
                mark = par;
                if(mark == null || mark.getValue().size() == 0) {
                    return false;
                }
            }else{
                if(par == null || par.getValue().size() == 0) {
                    continue;
                }
                list.add(par);
            }
        }
        Map.Entry<String, ArrayList<Person>> finalMark = mark;
        assert finalMark != null;
        list.removeIf(e -> finalMark.getValue().get(0).mache(e.getValue().get(0)) < gap);
        ArrayList<Integer> is = new ArrayList<>();
        for(int ii = 0; ii < list.size(); ii++) {
            is.add(ii);
        }
        Collections.shuffle(is);
        int max = mark.getValue().size();
        Map.Entry<String, ArrayList<Person>> prei = null;
        Map.Entry<String, ArrayList<Person>> prej = null;
        for(int j : is){
            if(list.get(j) == mark)continue;
            if(list.get(j) == null || list.get(j).getValue().size() == 0)continue;
            Map.Entry<String, ArrayList<Person>> pre = tripb(list,
                    mark, j,gap,
                    mark.getValue().size(), mini);
            if(pre == null)continue;
            int sum = mark.getValue().size() + list.get(j).getValue().size() + pre.getValue().size();
            assert sum <= 14;
            if(sum>max){
                max = sum;
                prei = list.get(j);
                prej = pre;
            }
        }
        if(prei != null){
            assert prej != null;
            Group group = new Group();
            int t1 = group.addAll(mark.getValue());
            assert t1 <= 14;
            int t2 = group.addAll(prei.getValue());
            assert t2 <= 14;
            int t3 = group.addAll(prej.getValue());
            assert t3 <= 14;
//            System.out.println(mark.getKey()+"类型与"+prei.getKey()+"类型与"+prej.getKey()+"类型凑成一组("+(t3)+")，后者剩余"+prej.getValue().size()+"人");
            groupList.add(group);
            return true;
        }
        return false;
    }

    private static Map.Entry<String, ArrayList<Person>> tripb(
            ArrayList<Map.Entry<String, ArrayList<Person>>> list,
            Map.Entry<String, ArrayList<Person>> mark, int j, int gap,
            int allready, int mini) {
        Map.Entry<String, ArrayList<Person>> pre = null;
        int arr = allready + list.get(j).getValue().size();
        int max = arr;
        for(Map.Entry<String, ArrayList<Person>> t : list){
            if(t == list.get(j))continue;
            if(t == null || t.getValue().size() == 0)continue;
            if(list.get(j).getValue().get(0).mache(t.getValue().get(0)) >= gap){
                int sum = arr + t.getValue().size();
                if(sum <= 14 && sum >= mini){
                    if(max < sum){
                        max = sum;
                        pre = t;
                    }
                }
            }
        }
        return pre;
    }

    private static void trip(Set<Map.Entry<String, ArrayList<Person>>> entries, GroupList groupList, int start_p, int start_j) {
        int start = start_p + 1;
        int j = start_j + 1;
        Map.Entry<String, ArrayList<Person>> mark = null;
        Map.Entry<String, ArrayList<Person>> mark2 = null;
        for (Map.Entry<String, ArrayList<Person>> par : entries) {
            start--;
            j--;
            if (start > 0) {
                continue;
            } else if (start == 0){
                mark = par;
                if(mark == null || mark.getValue().size() == 0) {
                    return;
                }
            } else {
                if (j > 0) {
                    continue;
                }else if(j==0){
                    mark2 = par;
                    if(mark2 == null || mark2.getValue().size() == 0) {
                        return;
                    }
                    assert mark != null;
                    if (mark.getValue().get(0).mache(par.getValue().get(0)) < 3) {
                        return;
                    }
                }else{
                    assert mark != null;
                    assert mark2 != null;
                    if(par == null || par.getValue().size() == 0) {
                        continue;
                    }
                    if (mark.getValue().get(0).mache(par.getValue().get(0)) >= 3) {
                        if (mark.getValue().size() + mark2.getValue().size() + par.getValue().size() >= 10) {
                            Group group = new Group();
                            int t1 = group.addAll(mark.getValue());
                            assert t1 <= 14;
                            int t2 = group.addAll(mark2.getValue());
                            assert t2 <= 14;
                            int t3 = group.addAll(par.getValue());
                            assert t3 <= 14;
//                            System.out.println(mark.getKey()+"类型与"+mark2.getKey()+"类型与"+par.getKey()+"类型凑成一组("+(t3)+")，后者剩余"+par.getValue().size()+"人");
                            groupList.add(group);
                            return;
                        }
                    }
                }
            }
        }

    }
}
