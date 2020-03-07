package qq.com.pojo;

import qq.com.ConsoleProgressBar;
import qq.com.ExcelOpt;
import qq.com.ExcelReader;
import qq.com.NotAble;

import java.util.ArrayList;
import java.util.List;

public class Plan {
    PlayPool playPool;
    PlayGround playGround;
    int src = Integer.MIN_VALUE;

    public int getSrc() {
        return src;
    }


    public Plan(List<Person> people, GroupList groupList) throws InterruptedException {
        groupList.makeNo();
        playPool = new PlayPool(people, groupList);
        int maxi = ExcelReader.RE_GROUND_COUNT;
        ConsoleProgressBar cpb = new ConsoleProgressBar(0, maxi, 44, "优化陆地项目批次");
        List<NotAble> notAbles = new ArrayList<>();
        for (int i = 0; i < maxi; i++) {
            GroupList inner = groupList.clone();
            PlayGround p = null;
            try {
                p = new PlayGround(inner);
                int pm = p.mark(false);
                if (src < pm) {
                    this.playGround = p;
                    src = pm;
                }else if(src == pm){
                    if(Math.random() > 0.5){
                        this.playGround = p;
                        src = pm;
                    }
                }
            } catch (NotAble notAble) {
                notAbles.add(notAble);
            }
            cpb.show(i);
        }
        assert src != Integer.MIN_VALUE;
        cpb.show(maxi);
        playGround.mark(true);
        int t = groupList.groups.size();
    }

    public PlayPool getPlayPool() {
        return playPool;
    }

    public PlayGround getPlayGround() {
        return playGround;
    }
}
