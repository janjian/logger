package qidian.qq.com.logger.model;


import org.apache.commons.lang3.tuple.Pair;
import qidian.qq.com.logger.excel.NotAble;
import qidian.qq.com.logger.utils.Setting;

import java.util.*;

public class PlayGround {
    HashMap<Integer, ArrayList<Round>> ground = new HashMap<>();
    Setting setting;
    public PlayGround(GroupList groupList, Setting setting){
        this.setting = setting;
        for(int i = 1; i<= setting.getLandDays(); i++){
            ArrayList<Round> rounds = new ArrayList<>();
            for (int j = 0; j < setting.getUpBatchSize(); j++){
                rounds.add(new Round(i, j, "上午"));
            }
            for (int j = 0; j < setting.getDownBatchSize(); j++){
                rounds.add(new Round(i, j, "下午"));
            }
            ground.put(i, rounds);
        }
        Collections.shuffle(groupList.groups);
        Item i200米游泳 = Item.of("200米游泳", Tn.T1);
        for(Group group: groupList.groups){
            appGroup(group, group.items.contains(i200米游泳));
        }

    }

    private void appGroup(Group group, boolean goDesc){
        Pair<Integer, Integer> miniIndex = null;
        double mini = Integer.MAX_VALUE;
        if(goDesc){
            for (int j = setting.getUpBatchSize() + setting.getDownBatchSize() - 1; j >= 0; j--) {
                for (int i = 1; i <= setting.getLandDays(); i++) {
                    if(group.playData.usedDay.contains(i)){
                        continue;
                    }
                    if(ground.get(i).get(j).getGroupCount(group.gender) > setting.getOnBatchSize()){
                        continue;
                    }
                    double v = test(i, j, group);
                    if (v - mini < 0) {
                        mini = v;
                        miniIndex = Pair.of(i, j);
                    }
                }
            }
        }else{
            int sum = setting.getUpBatchSize() + setting.getDownBatchSize();
            for (int j = 0; j < sum; j++) {
                for (int i = 1; i <= setting.getLandDays(); i++) {
                    if(group.playData.usedDay.contains(i)){
                        continue;
                    }
                    if(ground.get(i).get(j).getGroupCount(group.gender) > setting.getOnBatchSize()){
                        continue;
                    }
                    double v = test(i, j, group);
                    if (v - mini < 0) {
                        mini = v;
                        miniIndex = Pair.of(i, j);
                    }
                }
            }
        }

        //如果这里报错，则表示无法分配
        if(miniIndex == null){
            throw new NotAble("可能是由于单批次允许组数过少，无法继续分配。");
        }

        ground.get(miniIndex.getLeft()).get(miniIndex.getRight()).add(group);
    }

    private double test(int i, int j, Group group) {
        int inner = ground.get(i).get(j).test(group);
        int prix = cc(i, j-1, group) + cc(i, j+1, group);
        return inner - (prix*0.1);
    }

    private int cc(int i, int j, Group group){
        if(i < 1 || i > setting.getLandDays()){
            return 0;
        }
        if(j < 0 || j >= setting.getUpBatchSize() + setting.getDownBatchSize()){
            return 0;
        }
        Round round = ground.get(i).get(j);
        int sum = 0;
        for(Item item:group.items){
            int size = round.getList(group.gender, item).size();
            sum += size;
        }
        return sum;
    }

    public int mark(boolean out){
        int sum = 0;
        long x = 0;
        int n = 0;
        for (int j = setting.getUpBatchSize() + setting.getDownBatchSize() - 1; j >= 0; j--) {
            for (int i = 1; i <= setting.getLandDays(); i++) {
                Round round = ground.get(i).get(j);
                int m = round.mark();
                sum += m;
                x += round.getGroupCount(Gender.女) + round.getGroupCount(Gender.男);
                n += 2;
            }
        }
        x /= n;
        long q = 0;
        for (int j = setting.getUpBatchSize() + setting.getDownBatchSize() - 1; j >= 0; j--) {
            for (int i = 1; i <= setting.getLandDays(); i++) {
                Round round = ground.get(i).get(j);
                q += (long) q(x, round.getGroupCount(Gender.女));
                q += (long) q(x, round.getGroupCount(Gender.男));
            }
        }
        if(out){
            System.out.println("分组数量方差："+q);

        }
        return (int) (sum-q);
    }

    public static double q(long x, int a){
        long d = a - x;
        return Math.pow(d, 2);
    }

    private Integer mark = null;
    private int getMark(){
        if(mark == null){
            mark = mark(false);
        }
        return mark;
    }
    public PlayGround max(PlayGround other) {
        if(other == null){
            return this;
        }
        if(getMark() > other.getMark()){
            return this;
        }
        return other;
    }
}

