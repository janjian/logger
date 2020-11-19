package qidian.qq.com.logger.model;


import qidian.qq.com.logger.utils.Setting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayGround {
    private Round[] rounds = new Round[14];
    public PlayGround(GroupList groupList, Setting setting){
        for(int i = 1; i<= setting.getUpBatchSize(); i++){
            rounds[i] = new Round(i);
        }

//        ConsoleProgressBar cpb1 = new ConsoleProgressBar(0, groupList.groups.size()*3, 51, "编排考试");
        int i = 0;

        Collections.shuffle(groupList.groups);
        for(Group group: groupList.groups){
            if(group.items.contains(Item.i200米游泳)){

                try{
                    appGroup(group, is);
                }catch (NotAble notAble){

                }
//                cpb1.show(++i);
            }
        }
        for (Group group : groupList.groups) {
            appGroup(group, all);
//            cpb1.show(++i);
        }

//        cpb1.show(groupList.groups.size()*3);

    }

    private final static MTime[] is = new MTime[]{MTime.SENCOND_DOWN_4,MTime.FIRST_DOWN_4, MTime.FIRST_UP_3, MTime.FIRST_UP_3};
    private final static MTime[] end = new MTime[]{MTime.SENCOND_DOWN_4,MTime.FIRST_DOWN_4};


    private void appGroup(Group group, MTime[] ls){
        if(group.getGroundTime() != null) {
            return;
        }
        int mxi = -1;
        int mini = Integer.MAX_VALUE;
        List<MTime> ll = Arrays.asList(ls);
        Collections.shuffle(ll);
        int mv = -1;
        int mt = -1;
        int mc = -1;
        for(MTime t : ll){
            if(group.testTime(t)){
                int v = rounds[t.ordinal()].getGround(group);
                int c = rounds[t.ordinal()].getGroupCount(group.gender);
//                System.out.println(" v = "+v+",\tt = "+t+",\tc = "+c+",\tres = "+(v+c));
                int cc = cc(t.pre(), group) + cc(t.next(), group);
                int ma = v+c*4-cc;
                if(ma <= mini && c < 12){
                    if(ma < mini || Math.random() > 0.5){
                        mini = v;
                        mxi = t.ordinal();
                    }
                }
            }
        }
//        System.out.println("                                             "+ll.indexOf(MTime.values()[mxi])+" => "+ MTime.values()[mxi]);
//        System.out.println(" v = "+v+",\tt = "+t+",\tc = "+c+",\tres = "+(v+c));
        if(mxi == -1){
            for(MTime t : ll){
                if(group.testTime(t)){
                    int v = rounds[t.ordinal()].getGround(group);
                    int c = rounds[t.ordinal()].getGroupCount(group.gender);
//                System.out.println(" v = "+v+",\tt = "+t+",\tc = "+c+",\tres = "+(v+c));
                    int cc = cc(t.pre(), group) + cc(t.next(), group);
                    if(v+c*4-cc < mini && c < 12){
                        mini = v;
                        mxi = t.ordinal();
                    }
                }
            }
            throw new NotAble();
        }
        if(group.gender == Gender.男){
            rounds[mxi].add(0, group);
        }else{
            rounds[mxi].add(group);
        }
        group.rmTime(rounds[mxi].time);
    }

    private int cc(MTime time, Group group){
        if(time == null)return 0;
        int i = time.ordinal();
        if(i < 0){
            return 0;
        }
        if(i >= rounds.length){
            return 0;
        }
        int sum = 0;
        for(Item item:group.items){
            Round round = rounds[i];
            List list = round.getList(group.gender, item);
            if(list == null)continue;
            sum += (list.size() > 0 ? 1 : 0);
        }
        return sum;
    }

    public int mark(boolean out){
        int sum = 0;
        long x = 0;
        int n = 0;
        for(Round round : rounds){
            int m = round.mark();
            sum += m;
            x += round.getGroupCount(Gender.女) + round.getGroupCount(Gender.男);
            n+=2;
//            System.out.println("sum"+(n/2)+":"+m);
        }
        x /= n;
        long q = 0;
        for(Round round : rounds){
            q += (long) q(x, round.getGroupCount(Gender.女));
            q += (long) q(x, round.getGroupCount(Gender.男));
        }
        if(out){
//            System.out.println("分组数量方差："+q+"\tsum = "+sum+"\tres = "+(sum-q));
            System.out.println("分组数量方差："+q);

//            for(Round round:rounds){
//                System.out.println(round.getGroupCount(Gender.女)+"("+round.getList(Gender.女, Item.i800米跑).size()+")("+round.mark()+")\t"+round.getGroupCount(Gender.男)+"("+round.getList(Gender.男, Item.i1000米跑).size()+")");
//            }
        }
        return (int) (sum-q);
    }

    public static double q(long x, int a){
        long d = a - x;
        return Math.pow(d, 2);
    }

    public Round[] getRounds() {
        return rounds;
    }

    public GroupList getGroupList() {
        return groupList;
    }

    public PlayGround mini(PlayGround mini) {
    }
}

