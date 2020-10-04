package qq.com.pojo;

import qq.com.proj.Base;
import qq.com.proj.Gender;
import qq.com.proj.MTime;
import qq.com.proj.Tn;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayPool {
    public Pool[] pools = new Pool[4];
    ArrayList[][] water = new ArrayList[2][];

    public double getRate(List<Person> people){
        int wb = 0;
        int wg = 0;
        for(Person person : people){
            if(person.getGender() == Gender.男){
                if(person.getItems()[0].base == Base.WATERS && person.getItems()[1].base == Base.WATERS){
                    wb+=3;
                }else if(person.getItems()[1].base == Base.WATERS){
                    wb++;
                }else if(person.getItems()[0].base == Base.WATERS){
                    wb+=2;
                }
            }else{
                if(person.getItems()[0].base == Base.WATERS && person.getItems()[1].base == Base.WATERS){
                    wg+=3;
                }else if(person.getItems()[1].base == Base.WATERS){
                    wg++;
                }else if(person.getItems()[0].base == Base.WATERS){
                    wg+=2;
                }
            }
        }
        double rate = wb * 1.0 / (wb+wg);
        DecimalFormat formater = new DecimalFormat("#.##%");
        System.out.println("男女泳池项目资源配比："+wb+"/"+wg+", 男生占比："+formater.format(rate));
        return rate;
    }

    public void initWater(GroupList groupList){

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

        for(Group group : groupList.groups){
            if(group.base != Base.LAND){
                addList(water[group.gender.ordinal()][group.base.i()], group);
            }
        }

    }

    public PlayPool(List<Person> people, GroupList groupList){
        initWater(groupList);
        pools[MTime.FIRST_UP.i()] = new Pool(MTime.FIRST_UP);
        pools[MTime.FIRST_DOWN.i()] = new Pool(MTime.FIRST_DOWN);
        pools[MTime.SENCOND_UP.i()] = new Pool(MTime.SENCOND_UP);
        pools[MTime.SENCOND_DOWN.i()] = new Pool(MTime.SENCOND_DOWN);
        System.out.println("（兼游，单短程，单耐力）男孩泳池使用组数：("+
                water[Gender.男.ordinal()][Base.D.i()].size()+","+
                water[Gender.男.ordinal()][Base.S.i()].size()+","+
                water[Gender.男.ordinal()][Base.L.i()].size()+")");
        System.out.println("（兼游，单短程，单耐力）女孩泳池使用组数：("+
                water[Gender.女.ordinal()][Base.D.i()].size()+","+
                water[Gender.女.ordinal()][Base.S.i()].size()+","+
                water[Gender.女.ordinal()][Base.L.i()].size()+")");
        double rate = getRate(people);
        if(rate < 0.25){
            addList(pools[MTime.FIRST_UP.i()], water[Gender.男.ordinal()], 0, 1);

            double sum = 9;
            addList(pools[MTime.SENCOND_UP.i()], water[Gender.女.ordinal()], 0, 3/sum);
            addList(pools[MTime.FIRST_DOWN.i()], water[Gender.女.ordinal()], 3/sum, 6/sum);
            addList(pools[MTime.SENCOND_DOWN.i()], water[Gender.女.ordinal()], 6/sum, 1);

            System.out.println("使用方案1：第一天上午男生用泳池，其他时间女生用泳池。");
        }else if(rate < 0.357142857){
            addList(pools[MTime.FIRST_DOWN.i()], water[Gender.男.ordinal()], 0, 1);

            double sum = 9;
            addList(pools[MTime.FIRST_UP.i()], water[Gender.女.ordinal()], 0, 3/sum);
            addList(pools[MTime.SENCOND_UP.i()], water[Gender.女.ordinal()], 3/sum, 6/sum);
            addList(pools[MTime.SENCOND_DOWN.i()], water[Gender.女.ordinal()], 6/sum, 1);

            System.out.println("使用方案2：第一天下午男生用泳池，其他时间女生用泳池。");
        }else if(rate < 0.464285714){
            addList(pools[MTime.FIRST_UP.i()], water[Gender.男.ordinal()], 0, 0.5);
            addList(pools[MTime.SENCOND_UP.i()], water[Gender.男.ordinal()], 0.5, 1);

            addList(pools[MTime.FIRST_DOWN.i()], water[Gender.女.ordinal()], 0, 0.5);
            addList(pools[MTime.SENCOND_DOWN.i()], water[Gender.女.ordinal()], 0.5, 1);
            System.out.println("使用方案3：上午男生用泳池，下午女生用泳池。");
        }else if(rate < 0.535714286){
            if(Math.random() > 0.7){
                addList(pools[MTime.FIRST_UP.i()], water[Gender.男.ordinal()], 0, 0.5);
                addList(pools[MTime.SENCOND_DOWN.i()], water[Gender.男.ordinal()], 0.5, 1);

                addList(pools[MTime.SENCOND_UP.i()], water[Gender.女.ordinal()], 0, 0.5);
                addList(pools[MTime.FIRST_DOWN.i()], water[Gender.女.ordinal()], 0.5, 1);
                System.out.println("使用方案4(错开版)：第一天上午和第二天下午男生用泳池，第二天上午和第一天下午女生用泳池。");
            }else{
                addList(pools[MTime.FIRST_UP.i()], water[Gender.男.ordinal()], 0, 0.5);
                addList(pools[MTime.FIRST_DOWN.i()], water[Gender.男.ordinal()], 0.5, 1);

                addList(pools[MTime.SENCOND_UP.i()], water[Gender.女.ordinal()], 0, 0.5);
                addList(pools[MTime.SENCOND_DOWN.i()], water[Gender.女.ordinal()], 0.5, 1);
                System.out.println("使用方案4(连续版)：第一天男生用泳池，第二天女生用泳池。");
            }
        }else if(rate < 0.642857143) {
            addList(pools[MTime.FIRST_UP.i()], water[Gender.女.ordinal()], 0, 0.5);
            addList(pools[MTime.SENCOND_UP.i()], water[Gender.女.ordinal()], 0.5, 1);

            addList(pools[MTime.FIRST_DOWN.i()], water[Gender.男.ordinal()], 0, 0.5);
            addList(pools[MTime.SENCOND_DOWN.i()], water[Gender.男.ordinal()], 0.5, 1);
            System.out.println("使用方案5：下午男生用泳池，上午女生用泳池。");
        }else if(rate < 0.75) {
            addList(pools[MTime.FIRST_DOWN.i()], water[Gender.女.ordinal()], 0, 1);

            double sum = 9;
            addList(pools[MTime.FIRST_UP.i()], water[Gender.男.ordinal()], 0, 3/sum);
            addList(pools[MTime.SENCOND_UP.i()], water[Gender.男.ordinal()], 3/sum, 6/sum);
            addList(pools[MTime.SENCOND_DOWN.i()], water[Gender.男.ordinal()], 6/sum, 1);

            System.out.println("使用方案6：第一天下午女生用泳池，其他时间男生用泳池。");
        }else {
            addList(pools[MTime.FIRST_UP.i()], water[Gender.女.ordinal()], 0, 1);

            double sum = 9;
            addList(pools[MTime.SENCOND_UP.i()], water[Gender.男.ordinal()], 0, 3/sum);
            addList(pools[MTime.FIRST_DOWN.i()], water[Gender.男.ordinal()], 3/sum, 6/sum);
            addList(pools[MTime.SENCOND_DOWN.i()], water[Gender.男.ordinal()], 6/sum, 1);
            System.out.println("使用方案7：第一天上午女生用泳池，其他时间男生用泳池。");
        }
    }
    /*
    public PlayPool(LinkedList<Person> people, GroupList groupList){
        int wb = 0;
        int wg = 0;
        for(Person person : people){
            if(person.getGender() == Gender.男){
                if(person.getItems()[0].base == Base.WATERS && person.getItems()[1].base == Base.WATERS){
                    addList(dwaterb, person);
                    wb+=3;
                }else if(person.getItems()[1].base == Base.WATERS){
                    addList(swaterb, person);
                    wb++;
                }else if(person.getItems()[0].base == Base.WATERS){
                    addList(swaterbl, person);
                    wb+=2;
                }else {
                    addList(landb, person);
                }

            }else{
                if(person.getItems()[0].base == Base.WATERS && person.getItems()[1].base == Base.WATERS){
                    addList(dwaterg, person);
                    wg+=3;
                }else if(person.getItems()[1].base == Base.WATERS){
                    addList(swaterg, person);
                    wg++;
                }else if(person.getItems()[0].base == Base.WATERS){
                    addList(swatergl, person);
                    wg+=2;
                }else {
                    addList(landg, person);
                }
            }
        }
        double rate = wb * 1.0 / (wb+wg);
        DecimalFormat formater = new DecimalFormat("#.##%");
        System.out.println("男女泳池项目资源配比："+wb+"/"+wg+", 男生占比："+formater.format(rate));

        pools[0] = new Pool(Time.FIRST_UP_1);
        pools[1] = new Pool(Time.SENCOND_UP_1);
        pools[2] = new Pool(Time.FIRST_DOWN_1);
        pools[3] = new Pool(Time.SENCOND_DOWN_1);
        int dbsize = dwaterb.size();
        int sbsize = swaterb.size();
        int sblsize = swaterbl.size();
        int dgsize = dwaterg.size();
        int sgsize = swaterg.size();
        int sglsize = swatergl.size();
        System.out.println("（兼游，单短程，单耐力）男孩泳池使用人数：("+dbsize+","+sbsize+","+sblsize+")");
        System.out.println("（兼游，单短程，单耐力）女孩泳池使用人数：("+dgsize+","+sgsize+","+sglsize+")");
        if(rate < 0.25){
            addList(pools[0], dwaterb, swaterb, swaterbl);

            int dg1 = (int) (3.0 * dgsize / (3+4+4));
            int sg1 = (int) (3.0 * sgsize / (3+4+4));
            int sgl1 = (int) (3.0 * sglsize / (3+4+4));
            addList(pools[1],
                    dwaterg.subList(0,dg1),
                    swaterg.subList(0,sg1),
                    swatergl.subList(0,sgl1));
            int dg2 = (int) (7.0 * dgsize / (3+4+4));
            int sg2 = (int) (7.0 * sgsize / (3+4+4));
            int sgl2 = (int) (7.0 * sglsize / (3+4+4));
            addList(pools[2],
                    dwaterg.subList(dg1,dg2),
                    swaterg.subList(sg1,sg2),
                    swatergl.subList(sgl1,sgl2));
            addList(pools[3],
                    dwaterg.subList(dg2,dgsize),
                    swaterg.subList(sg2,sgsize),
                    swatergl.subList(sgl2,sglsize));

            System.out.println("使用方案1：第一天上午男生用泳池，其他时间女生用泳池。");
        }else if(rate < 0.357142857){
            addList(pools[2], dwaterb, swaterb, swaterbl);

            int dg1 = (int) (3.0 * dgsize / (3+3+4));
            int sg1 = (int) (3.0 * sgsize / (3+3+4));
            int sgl1 = (int) (3.0 * sglsize / (3+3+4));
            addList(pools[0],
                    dwaterg.subList(0,dg1),
                    swaterg.subList(0,sg1),
                    swatergl.subList(0,sgl1));
            int dg2 = (int) (6.0 * dgsize / (3+3+4));
            int sg2 = (int) (6.0 * sgsize / (3+3+4));
            int sgl2 = (int) (6.0 * sglsize / (3+3+4));
            addList(pools[1],
                    dwaterg.subList(dg1,dg2),
                    swaterg.subList(sg1,sg2),
                    swatergl.subList(sgl1,sgl2));
            addList(pools[3],
                    dwaterg.subList(dg2,dgsize),
                    swaterg.subList(sg2,sgsize),
                    swatergl.subList(sgl2,sglsize));

            System.out.println("使用方案2：第一天下午男生用泳池，其他时间女生用泳池。");
        }else if(rate < 0.464285714){
            int db1 = (int) (0.5 * dbsize);
            int sb1 = (int) (0.5 * sbsize);
            int sbl1 = (int) (0.5 * sblsize);
            addList(pools[0], dwaterb.subList(0,db1), swaterb.subList(0,sb1), swaterbl.subList(0,sbl1));
            addList(pools[1], dwaterb.subList(db1,dbsize), swaterb.subList(sb1,sbsize), swaterbl.subList(sbl1,sblsize));

            int dg1 = (int) (0.5 * dgsize);
            int sg1 = (int) (0.5 * sgsize);
            int sgl1 = (int) (0.5 * sglsize);
            addList(pools[2], dwaterg.subList(0,dg1), swaterg.subList(0,sg1), swatergl.subList(0,sgl1));
            addList(pools[3], dwaterg.subList(dg1,dgsize), swaterg.subList(sg1,sgsize), swatergl.subList(sgl1,sglsize));
            System.out.println("使用方案4：上午男生用泳池，下午女生用泳池。");
        }else if(rate < 0.535714286){
            int db1 = (int) (0.4285714286 * dbsize);
            int sb1 = (int) (0.4285714286 * sbsize);
            int sbl1 = (int) (0.4285714286 * sblsize);
            addList(pools[0], dwaterb.subList(0,db1), swaterb.subList(0,sb1), swaterbl.subList(0,sbl1));
            addList(pools[2], dwaterb.subList(db1,dbsize), swaterb.subList(sb1,sbsize), swaterbl.subList(sbl1,sblsize));

            int dg1 = (int) (0.4285714286 * dgsize);
            int sg1 = (int) (0.4285714286 * sgsize);
            int sgl1 = (int) (0.4285714286 * sglsize);
            addList(pools[1], dwaterg.subList(0,dg1), swaterg.subList(0,sg1), swatergl.subList(0,sgl1));
            addList(pools[3], dwaterg.subList(dg1,dgsize), swaterg.subList(sg1,sgsize), swatergl.subList(sgl1,sglsize));
            System.out.println("使用方案3：第一天男生用泳池，第二天女生用泳池。");
        }else if(rate < 0.642857143) {
            int db1 = (int) (0.5 * dbsize);
            int sb1 = (int) (0.5 * sbsize);
            int sbl1 = (int) (0.5 * sblsize);
            addList(pools[2], dwaterb.subList(0,db1), swaterb.subList(0,sb1), swaterbl.subList(0,sbl1));
            addList(pools[3], dwaterb.subList(db1,dbsize), swaterb.subList(sb1,sbsize), swaterbl.subList(sbl1,sblsize));

            int dg1 = (int) (0.5 * dgsize);
            int sg1 = (int) (0.5 * sgsize);
            int sgl1 = (int) (0.5 * sglsize);
            addList(pools[0], dwaterg.subList(0,dg1), swaterg.subList(0,sg1), swatergl.subList(0,sgl1));
            addList(pools[1], dwaterg.subList(dg1,dgsize), swaterg.subList(sg1,sgsize), swatergl.subList(sgl1,sglsize));
            System.out.println("使用方案5：下午男生用泳池，上午女生用泳池。");
        }else if(rate < 0.75) {

            addList(pools[2], dwaterg, swaterg, swatergl);

            int db1 = (int) (3.0 * dbsize / (3+3+4));
            int sb1 = (int) (3.0 * sbsize / (3+3+4));
            int sbl1 = (int) (3.0 * sblsize / (3+3+4));
            addList(pools[0],
                    dwaterb.subList(0,db1),
                    swaterb.subList(0,sb1),
                    swaterbl.subList(0,sbl1));
            int db2 = (int) (6.0 * dbsize / (3+3+4));
            int sb2 = (int) (6.0 * sbsize / (3+3+4));
            int sbl2 = (int) (6.0 * sblsize / (3+3+4));
            addList(pools[1],
                    dwaterb.subList(db1,db2),
                    swaterb.subList(sb1,sb2),
                    swaterbl.subList(sbl1,sbl2));
            addList(pools[3],
                    dwaterb.subList(db2,dbsize),
                    swaterb.subList(sb2,sbsize),
                    swaterbl.subList(sbl2,sblsize));

            System.out.println("使用方案6：第一天下午女生用泳池，其他时间男生用泳池。");
        }else {

            addList(pools[0], dwaterg, swaterg, swatergl);

            int db1 = (int) (3.0 * dbsize / (3+4+4));
            int sb1 = (int) (3.0 * sbsize / (3+4+4));
            int sbl1 = (int) (3.0 * sblsize / (3+4+4));
            addList(pools[1],
                    dwaterb.subList(0,db1),
                    swaterb.subList(0,sb1),
                    swaterbl.subList(0,sbl1));
            int db2 = (int) (7.0 * dbsize / (3+4+4));
            int sb2 = (int) (7.0 * sbsize / (3+4+4));
            int sbl2 = (int) (7.0 * sblsize / (3+4+4));
            addList(pools[2],
                    dwaterb.subList(db1,db2),
                    swaterb.subList(sb1,sb2),
                    swaterbl.subList(sbl1,sbl2));
            addList(pools[3],
                    dwaterb.subList(db2,dbsize),
                    swaterb.subList(sb2,sbsize),
                    swaterbl.subList(sbl2,sblsize));

            System.out.println("使用方案7：第一天上午女生用泳池，其他时间男生用泳池。");
        }
    }
     */

    private static void addList(Pool playRound, ArrayList<Group>[] people, double peoplel, double peoples){
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

    private static void addListS(Pool pool, List<Group> people, List<Group> peoples, Tn tn){
        Iterator<Group> pb = people.iterator();
        Group pbi = null;
        boolean hasGap = false;
        while (pb.hasNext()){
            pbi = pb.next();
            if(pbi.isAppend()){
                hasGap = true;
                break;
            }
            addList(pool.getPool(tn),pbi);
            pbi.rmTime(pool.time);
        }
        for(Group person : peoples){
            addList(pool.getPool(tn),person);
            person.rmTime(pool.time);
        }
        if(hasGap){
            addList(pool.getPool(tn),pbi);
            pbi.rmTime(pool.time);
            while (pb.hasNext()){
                pbi = pb.next();
                addList(pool.getPool(tn),pbi);
                pbi.rmTime(pool.time);
            }
        }
    }

    private static void addList(ArrayList<Group> people, Group person){
        if(person.isAppend()){
            people.add(person);
        }else {
            people.add(0, person);
        }
    }
}
