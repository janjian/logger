package qidian.qq.com.logger.model;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

@Getter
public class Person {
    public static Row headers;
    private final HashMap<String, String> data = new LinkedHashMap<>();
    public static void setHeader(Row row) {
        headers = row;
    }
    public static Person parsePerson(Row row) {
        Person person = new Person();
        for (Iterator<Cell> it = row.cellIterator(); it.hasNext();) {
            Cell cell = it.next();
            String key = StringUtils.trimAllWhitespace(headers.getCell(cell.getColumnIndex()).getStringCellValue());
            String nostr = cell.getCellTypeEnum() == CellType.NUMERIC
                    ? ((long) cell.getNumericCellValue()) + ""
                    : cell.getStringCellValue();
            person.data.put(key, nostr);
        }
        person.init();

        return person;
    }

    private void init(){
        items[0] = Item.of(get("第一类项目"), Tn.T1);
        items[1] = Item.of(get("第二类项目"), Tn.T2);
        items[2] = Item.of(get("第三类项目"), Tn.T3);
        items[3] = Item.of(get("第四类项目"), Tn.T4);

        gender = Gender.valueOf(get("性别"));
    }

    public String get(String key){
        return data.getOrDefault(key, "");
    }

    private static final String appendKey = "备注";
    public boolean isAppend() {
        return !StringUtils.isEmpty(data.get(appendKey));
    }

    Gender gender;
    Item[] items = new Item[4];
    public String getItemsKey() {
        return gender+"_"+items[0].num+"_"+items[1].num+"_"+items[2].num+"_"+items[3].num;
    }

    public int mache(Person person){
        if(this.items[0].base != person.items[0].base){
            return 0;
        }
        if(this.items[1].base != person.items[1].base){
            return 0;
        }
        if(this.gender != person.gender){
            return 0;
        }
        int res = 0;
        for(int i = 0; i< 4;i++){
            if(this.items[i] == person.items[i]){
                res++;
            }
        }
        return res;
    }
}
