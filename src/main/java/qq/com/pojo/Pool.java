package qq.com.pojo;

import qq.com.proj.Gender;
import qq.com.proj.MTime;
import qq.com.proj.Tn;

import java.util.ArrayList;

public class Pool {
    public ArrayList<Group> pools = new ArrayList<>();
    public ArrayList<Group> pooll = new ArrayList<>();
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

    public Gender getGender(){
        if(pools.size() > 0){
           return pools.get(0).getGender();
        }else {
            return pooll.size() > 0 ? pooll.get(0).getGender() : Gender.NONE;
        }
    }
}
