package qidian.qq.com.logger.model;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
public class Plan {
    PlayPool playPool;
    PlayGround playGround;

    public ConcurrentHashMap<String, Pair<String, String>> prasePeopleWater(String personKey){
        ConcurrentHashMap<String, Pair<String, String>> res = new ConcurrentHashMap<>();
        playGround.ground.forEach((k,v) -> {
            String noNum = String.format("%01d", k);//日期
            for (int i = 0; i < v.size(); i++) {
                String noNum2 = noNum + String.format("%02d", i+1);
                Round round = v.get(i);
                for (int j = 0; j < round.bgroups.size(); j++) {
                    String noNum3 = noNum2 + String.format("%02d", j+1);
                    Group group = round.bgroups.get(j);
                    for (int l = 0; l < group.people.size(); l++) {
                        String noNum4 = noNum3 + String.format("%02d", l+1);
                        Pair<String, String> num = Pair.of(noNum4,group.playData.waterTime());
                        Person person = group.people.get(l);
                        res.put(person.get(personKey), num);
                    }
                }
                for (int j = 0; j < round.ggroups.size(); j++) {
                    String noNum3 = noNum2 + String.format("%02d", j+round.bgroups.size()+1);
                    Group group = round.ggroups.get(j);
                    for (int l = 0; l < group.people.size(); l++) {
                        String noNum4 = noNum3 + String.format("%02d", l+1);
                        Pair<String, String> num = Pair.of(noNum4,group.playData.waterTime());
                        Person person = group.people.get(l);
                        res.put(person.get(personKey), num);
                    }
                }
            }
        });
        return res;
    }
}
