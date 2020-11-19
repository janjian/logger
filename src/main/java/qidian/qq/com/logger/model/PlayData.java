package qidian.qq.com.logger.model;

import java.util.HashSet;
import java.util.Set;

public class PlayData {
    Set<Integer> usedDay = new HashSet<>();

    public void remove(MTime time) {
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
}
