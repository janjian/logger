package qq.com.pojo;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Gen {
    /*
    ArrayList<Pare> pares = new ArrayList<>();
    public static final int qa = 10;
    final HashMap<String, ArrayList<Person>> cal;
    HashMap<String, ArrayList<Person>> c;

    static class Pare{
        public String i;
        public int i_count;
        public String j;
        public int j_count;

        public int value;

        public Pare(String i, int i_count, String j, int j_count) throws Exception {
            this.i = i;
            this.i_count = i_count;
            this.j = j;
            this.j_count = j_count;
            value = caclPare();
        }

        private int caclPare() throws Exception {
            int sum = this.i_count+this.j_count;
            if(sum <= 0){
                throw new Exception();
            }else if(sum<qa){
                return this.i_count + this.j_count - qa;
            }else if(sum < 14){
                return 14 - sum;
            }else{
                throw new Exception();
            }
        }
    }

    public static LinkedHashMap<String, ArrayList<Person>> copy(HashMap<String, ArrayList<Person>> cal){
        LinkedHashMap<String, ArrayList<Person>> res = new LinkedHashMap<>(cal);
        res.replaceAll((k, v) -> {
            ArrayList<Person> people = new ArrayList<>(v);
            return people;
        });
        return res;
    }

    public int value(){
        int res = 0;
        AtomicInteger sum = new AtomicInteger();
        for(Pare pare:pares){
            int t = valuePare(pare);
            sum.addAndGet(t);
        }
        c.forEach((k, v)-> sum.addAndGet(-v.size()));
        return sum.get() - (pares.size()/10);
    }

    public int valuePare(Pare pare) throws Exception {
        if(pare.i_count > 0){
            ArrayList<Person> ilist = c.get(pare.i);
            if(ilist == null || pare.i_count > ilist.size())throw new Exception();
            for(int i = pare.i_count; i > 0; i--){
                ilist.remove(0);
            }
        }
        if(pare.j_count > 0){
            ArrayList<Person> jlist = c.get(pare.j);
            if(jlist == null || pare.j_count > jlist.size())throw new Exception();
            for(int i = pare.j_count; i > 0; i--){
                jlist.remove(0);
            }
        }
        return pare.value;
    }

    public ArrayList<Gen> make(Gen gen){
        Collections.shuffle(pares);
        Collections.shuffle(gen.pares);
        ArrayList<Pare> ti = new ArrayList<>();
        for(int i = (pares.size()+gen.pares.size())/3; i > 0; i --){
            ti.add(pares.get(i));
            ti.add(gen.pares.get(i));
        }
        LinkedHashMap<String, ArrayList<Person>> c = copy(cal);
        ArrayList<Gen> gens = new ArrayList<>();
        Gen son = new Gen(ti, cal);
    }

    public Gen(ArrayList<Pare> pares, HashMap<String, ArrayList<Person>> cal) throws Exception {
        this.pares = pares;
        this.cal = cal;
        this.c = copy(cal);
        int t = value();
        ArrayList<Integer> indexs = new ArrayList<>();
        Collections.shuffle(indexs);
        for(int g = 3; g >= 2; g--){
            for(int i : indexs){
                Handle.pare(c.entrySet(), groupList, i, g);
            }
        }
    }

    private void pare(int start_p, int gap) throws Exception {
        int start = start_p + 1;
        Map.Entry<String, ArrayList<Person>> mark = null;
        for (Map.Entry<String, ArrayList<Person>> par : c.entrySet()) {
            start--;
            if (start > 0) {
                continue;
            } else if (start == 0){
                mark = par;
                if(mark == null || mark.getValue().size() == 0) {
                    return;
                }
            } else {
                if(par == null || par.getValue().size() == 0) {
                    continue;
                }
                assert mark != null;
                if (mark.getValue().get(0).mache(par.getValue().get(0)) >= gap) {
                    if (mark.getValue().size() + par.getValue().size() >= qa) {
//                        System.out.println(mark.getKey()+"类型与"+par.getKey()+"类型凑成一对("+t2+")，后者剩余"+last+"人");
                        addPare(new Pare(mark.getKey(), mark.getValue().size(), par.getKey(),
                                14 - mark.getValue().size() > par.getValue().size()));
                        return;
                    }
                }
            }
        }
    }

    void addPare(Pare pare) throws Exception {
        pares.add(pare);
        valuePare(pare);
    }
    */
}
