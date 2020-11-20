package qidian.qq.com.logger.model;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class PlayData {
    Set<Integer> usedDay = new HashSet<>();
    MTime time = null;

    public void remove(MTime time) {
        this.time = time;
        switch (time){
            case FIRST_UP:
            case FIRST_DOWN:
                usedDay.add(1);
                break;
            case SENCOND_UP:
            case SENCOND_DOWN:
                usedDay.add(2);
                break;
        }
    }

    @Override
    protected PlayData clone() {
        PlayData res = new PlayData();
        res.usedDay.addAll(usedDay);
        res.time = this.time;
        return res;
    }

    public String waterTime(){
        return time == null ? "" : time.des;
    }
}
