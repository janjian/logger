package qq.com.pojo;

import qq.com.proj.MTime;

import java.util.Arrays;

public class PlayData {
    public boolean[] ok;

    public PlayData() {
        ok = new boolean[14];
        for (int i = 0; i < 14; i++) {
            ok[i] = true;
        }
    }

    public void remove(MTime time) {
        switch (time){
            case FIRST_UP_1:
            case FIRST_UP_2:
            case FIRST_UP_3:
            case FIRST_DOWN_1:
            case FIRST_DOWN_2:
            case FIRST_DOWN_3:
            case FIRST_DOWN_4:
            case SENCOND_UP_1:
            case SENCOND_UP_2:
            case SENCOND_UP_3:
            case SENCOND_DOWN_1:
            case SENCOND_DOWN_2:
            case SENCOND_DOWN_3:
            case SENCOND_DOWN_4:
                ok[time.ordinal()] = false;
                break;
            case FIRST_UP:
            case FIRST_DOWN:
                remove(MTime.FIRST_UP_1);
                remove(MTime.FIRST_UP_2);
                remove(MTime.FIRST_UP_3);
                remove(MTime.FIRST_DOWN_1);
                remove(MTime.FIRST_DOWN_2);
                remove(MTime.FIRST_DOWN_3);
                remove(MTime.FIRST_DOWN_4);
                break;
            case SENCOND_UP:
            case SENCOND_DOWN:
                remove(MTime.SENCOND_UP_1);
                remove(MTime.SENCOND_UP_2);
                remove(MTime.SENCOND_UP_3);
                remove(MTime.SENCOND_DOWN_1);
                remove(MTime.SENCOND_DOWN_2);
                remove(MTime.SENCOND_DOWN_3);
                remove(MTime.SENCOND_DOWN_4);
                break;
        }
    }

    @Override
    public PlayData clone(){
        PlayData playData = new PlayData();
        for (int i = 0; i < 14; i++) {
            playData.ok[i] = ok[i];
        }
        return playData;
    }

    @Override
    public String toString() {
        return "PlayData{" +
                "ok=" + Arrays.toString(ok) +
                '}';
    }
}
