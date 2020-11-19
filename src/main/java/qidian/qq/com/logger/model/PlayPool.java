package qidian.qq.com.logger.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayPool {
    public Pool[] pools = new Pool[4];
    ArrayList[][] water = new ArrayList[2][];

    public double getRate(List<Person> people) {
        int wb = 0;
        int wg = 0;
        for (Person person : people) {
            if (person.getGender() == Gender.男) {
                if (person.getItems()[0].base == Base.WATERS && person.getItems()[1].base == Base.WATERS) {
                    wb += 3;
                } else if (person.getItems()[1].base == Base.WATERS) {
                    wb++;
                } else if (person.getItems()[0].base == Base.WATERS) {
                    wb += 2;
                }
            } else {
                if (person.getItems()[0].base == Base.WATERS && person.getItems()[1].base == Base.WATERS) {
                    wg += 3;
                } else if (person.getItems()[1].base == Base.WATERS) {
                    wg++;
                } else if (person.getItems()[0].base == Base.WATERS) {
                    wg += 2;
                }
            }
        }
        double rate = wb * 1.0 / (wb + wg);
        DecimalFormat formater = new DecimalFormat("#.##%");
        System.out.println("男女泳池项目资源配比：" + wb + "/" + wg + ", 男生占比：" + formater.format(rate));
        return rate;
    }

    public void initWater(GroupList groupList) {

        ArrayList[] waterb = new ArrayList[3];
        waterb[0] = new ArrayList<Group>();
        waterb[1] = new ArrayList<Group>();
        waterb[2] = new ArrayList<Group>();
        ArrayList[] waterg = new ArrayList[3];
        waterg[0] = new ArrayList<Group>();
        waterg[1] = new ArrayList<Group>();
        waterg[2] = new ArrayList<Group>();
        water[0] = waterb;
        water[1] = waterg;

        for (Group group : groupList.groups) {
            if (group.base != Base.LAND) {
                addList(water[group.gender.ordinal()][group.base.i()], group);
            }
        }

    }

    public PlayPool(List<Person> people, GroupList groupList) {
        initWater(groupList);
        pools[MTime.FIRST_UP.i()] = new Pool(MTime.FIRST_UP);
        pools[MTime.FIRST_DOWN.i()] = new Pool(MTime.FIRST_DOWN);
        pools[MTime.SENCOND_UP.i()] = new Pool(MTime.SENCOND_UP);
        pools[MTime.SENCOND_DOWN.i()] = new Pool(MTime.SENCOND_DOWN);
        System.out.println("（兼游，单短程，单耐力）男孩泳池使用组数：(" +
                water[Gender.男.ordinal()][Base.D.i()].size() + "," +
                water[Gender.男.ordinal()][Base.S.i()].size() + "," +
                water[Gender.男.ordinal()][Base.L.i()].size() + ")");
        System.out.println("（兼游，单短程，单耐力）女孩泳池使用组数：(" +
                water[Gender.女.ordinal()][Base.D.i()].size() + "," +
                water[Gender.女.ordinal()][Base.S.i()].size() + "," +
                water[Gender.女.ordinal()][Base.L.i()].size() + ")");
        double rate = getRate(people);
        if (rate < 0.25) {
            addList(pools[MTime.FIRST_UP.i()], water[Gender.男.ordinal()], 0, 1);

            double sum = 9;
            addList(pools[MTime.SENCOND_UP.i()], water[Gender.女.ordinal()], 0, 3 / sum);
            addList(pools[MTime.FIRST_DOWN.i()], water[Gender.女.ordinal()], 3 / sum, 6 / sum);
            addList(pools[MTime.SENCOND_DOWN.i()], water[Gender.女.ordinal()], 6 / sum, 1);

            System.out.println("使用方案1：第一天上午男生用泳池，其他时间女生用泳池。");
        } else if (rate < 0.357142857) {
            addList(pools[MTime.FIRST_DOWN.i()], water[Gender.男.ordinal()], 0, 1);

            double sum = 9;
            addList(pools[MTime.FIRST_UP.i()], water[Gender.女.ordinal()], 0, 3 / sum);
            addList(pools[MTime.SENCOND_UP.i()], water[Gender.女.ordinal()], 3 / sum, 6 / sum);
            addList(pools[MTime.SENCOND_DOWN.i()], water[Gender.女.ordinal()], 6 / sum, 1);

            System.out.println("使用方案2：第一天下午男生用泳池，其他时间女生用泳池。");
        } else if (rate < 0.464285714) {
            addList(pools[MTime.FIRST_UP.i()], water[Gender.男.ordinal()], 0, 0.5);
            addList(pools[MTime.SENCOND_UP.i()], water[Gender.男.ordinal()], 0.5, 1);

            addList(pools[MTime.FIRST_DOWN.i()], water[Gender.女.ordinal()], 0, 0.5);
            addList(pools[MTime.SENCOND_DOWN.i()], water[Gender.女.ordinal()], 0.5, 1);
            System.out.println("使用方案3：上午男生用泳池，下午女生用泳池。");
        } else if (rate < 0.535714286) {
            if (Math.random() > 0.7) {
                addList(pools[MTime.FIRST_UP.i()], water[Gender.男.ordinal()], 0, 0.5);
                addList(pools[MTime.SENCOND_DOWN.i()], water[Gender.男.ordinal()], 0.5, 1);

                addList(pools[MTime.SENCOND_UP.i()], water[Gender.女.ordinal()], 0, 0.5);
                addList(pools[MTime.FIRST_DOWN.i()], water[Gender.女.ordinal()], 0.5, 1);
                System.out.println("使用方案4(错开版)：第一天上午和第二天下午男生用泳池，第二天上午和第一天下午女生用泳池。");
            } else {
                addList(pools[MTime.FIRST_UP.i()], water[Gender.男.ordinal()], 0, 0.5);
                addList(pools[MTime.FIRST_DOWN.i()], water[Gender.男.ordinal()], 0.5, 1);

                addList(pools[MTime.SENCOND_UP.i()], water[Gender.女.ordinal()], 0, 0.5);
                addList(pools[MTime.SENCOND_DOWN.i()], water[Gender.女.ordinal()], 0.5, 1);
                System.out.println("使用方案4(连续版)：第一天男生用泳池，第二天女生用泳池。");
            }
        } else if (rate < 0.642857143) {
            addList(pools[MTime.FIRST_UP.i()], water[Gender.女.ordinal()], 0, 0.5);
            addList(pools[MTime.SENCOND_UP.i()], water[Gender.女.ordinal()], 0.5, 1);

            addList(pools[MTime.FIRST_DOWN.i()], water[Gender.男.ordinal()], 0, 0.5);
            addList(pools[MTime.SENCOND_DOWN.i()], water[Gender.男.ordinal()], 0.5, 1);
            System.out.println("使用方案5：下午男生用泳池，上午女生用泳池。");
        } else if (rate < 0.75) {
            addList(pools[MTime.FIRST_DOWN.i()], water[Gender.女.ordinal()], 0, 1);

            double sum = 9;
            addList(pools[MTime.FIRST_UP.i()], water[Gender.男.ordinal()], 0, 3 / sum);
            addList(pools[MTime.SENCOND_UP.i()], water[Gender.男.ordinal()], 3 / sum, 6 / sum);
            addList(pools[MTime.SENCOND_DOWN.i()], water[Gender.男.ordinal()], 6 / sum, 1);

            System.out.println("使用方案6：第一天下午女生用泳池，其他时间男生用泳池。");
        } else {
            addList(pools[MTime.FIRST_UP.i()], water[Gender.女.ordinal()], 0, 1);

            double sum = 9;
            addList(pools[MTime.SENCOND_UP.i()], water[Gender.男.ordinal()], 0, 3 / sum);
            addList(pools[MTime.FIRST_DOWN.i()], water[Gender.男.ordinal()], 3 / sum, 6 / sum);
            addList(pools[MTime.SENCOND_DOWN.i()], water[Gender.男.ordinal()], 6 / sum, 1);
            System.out.println("使用方案7：第一天上午女生用泳池，其他时间男生用泳池。");
        }
    }

    private static void addList(Pool playRound, ArrayList<Group>[] people, double peoplel, double peoples) {
        int startd = (int) Math.ceil(peoplel * people[Base.D.i()].size());
        int starts = (int) Math.ceil(peoplel * people[Base.S.i()].size());
        int startl = (int) Math.ceil(peoplel * people[Base.L.i()].size());
        int endd = (int) Math.ceil(peoples * people[Base.D.i()].size());
        int ends = (int) Math.ceil(peoples * people[Base.S.i()].size());
        int endl = (int) Math.ceil(peoples * people[Base.L.i()].size());
        addListS(playRound,
                people[Base.D.i()].subList(startd, endd),
                people[Base.S.i()].subList(starts, ends),
                Tn.T2);
        addListS(playRound,
                people[Base.D.i()].subList(startd, endd),
                people[Base.L.i()].subList(startl, endl),
                Tn.T1);
    }

    private static void addListS(Pool pool, List<Group> people, List<Group> peoples, Tn tn) {
        Iterator<Group> pb = people.iterator();
        Group pbi = null;
        boolean hasGap = false;
        while (pb.hasNext()) {
            pbi = pb.next();
            addList(pool.getPool(tn), pbi);
            pbi.rmTime(pool.time);
        }
        for (Group person : peoples) {
            addList(pool.getPool(tn), person);
            person.rmTime(pool.time);
        }
        if (hasGap) {
            addList(pool.getPool(tn), pbi);
            pbi.rmTime(pool.time);
            while (pb.hasNext()) {
                pbi = pb.next();
                addList(pool.getPool(tn), pbi);
                pbi.rmTime(pool.time);
            }
        }
    }

    private static void addList(ArrayList<Group> people, Group person) {
        people.add(0, person);
    }
}
