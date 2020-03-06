package qq.com.pojo;

import qq.com.proj.Gender;
import qq.com.proj.MTime;
import qq.com.proj.Tn;

import java.util.ArrayList;

public class Pool {
    public ArrayList<Group> pools = new ArrayList<>();
    public ArrayList<Group> pooll = new ArrayList<>();
    public Gender gender;
    public MTime time;

    public Pool(MTime time) {
        this.time = time;
    }

    public ArrayList<Group> getPool(Tn tn) {
        if(tn == Tn.T1){
            return pooll;
        }else{
            return pools;
        }
    }
}
