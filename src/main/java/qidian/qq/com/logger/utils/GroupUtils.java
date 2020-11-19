package qidian.qq.com.logger.utils;

import qidian.qq.com.logger.model.Group;
import qidian.qq.com.logger.model.GroupList;
import qidian.qq.com.logger.model.Person;

import java.util.*;

public class GroupUtils {

    public static GroupList chou(LinkedHashMap<String, ArrayList<Person>> cal,
                                  GroupList groupList, Setting setting){
        ArrayList<Integer> is = new ArrayList<>();
        for(int i = 0; i < cal.entrySet().size(); i++) {
            is.add(i);
        }
        Collections.shuffle(is);
        int si = 5;
        final int bound = setting.getGroupSize() - setting.getGroupMinSize() + 1;
        do{
            boolean res = false;
            Collections.shuffle(is);
            int g = Math.random() < 0.5 ? 2 : 3;
            int j = setting.getGroupSize() - (int)(Math.random()*bound); // (9, 14]
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

        int mid = (setting.getGroupSize()+setting.getGroupMinSize())/2;
        for(int g = 3; g >= 2; g--){
            for( int j = setting.getGroupSize(); j > mid; j--){
                for (int i : is){
                    pare(cal.entrySet(), groupList, i, g, j);
                }
            }
        }
        Collections.shuffle(is);
        for(int g = 3; g >= 2; g--) {
            for( int j = setting.getGroupSize(); j > setting.getGroupMinSize(); j--) {
                for (int i : is) {
                    tripa(cal.entrySet(), groupList, i, g, j);
                }
            }
        }
        Collections.shuffle(is);
        for(int g = 3; g >= 2; g--){
            for( int j = setting.getGroupSize(); j > setting.getGroupMinSize(); j--){
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
            if(list.get(j) == mark) {
                continue;
            }
            if(list.get(j) == null || list.get(j).getValue().size() == 0) {
                continue;
            }
            Map.Entry<String, ArrayList<Person>> pre = tripb(list,
                    mark, j,gap,
                    mark.getValue().size(), mini);
            if(pre == null) {
                continue;
            }
            int sum = mark.getValue().size() + list.get(j).getValue().size() + pre.getValue().size();
            assert sum <= 14;
            if(sum>max){
                max = sum;
                prei = list.get(j);
                prej = pre;
            }
        }
        if(prei != null){
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
            if(t == list.get(j)) {
                continue;
            }
            if(t == null || t.getValue().size() == 0) {
                continue;
            }
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

    private static void trip(Set<Map.Entry<String, ArrayList<Person>>> entries, GroupList groupList, int startP, int startJ) {
        int start = startP + 1;
        int j = startJ + 1;
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
