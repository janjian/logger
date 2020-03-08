package qq.com.pojo;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.util.StringUtils;
import qq.com.proj.Base;
import qq.com.proj.Gender;
import qq.com.proj.Item;
import qq.com.proj.Tn;

import java.util.Arrays;
import java.util.Objects;


public class Person {
    // 考生报名号	姓名	性别	第一类项目	第二类项目	第三类项目	第四类项目	特殊
    private long no;
    private String name;
    Gender gender;
    Item[] items = new Item[4];
    String append = null;
    private String school;

    public static Person parsePerson(Row row){
        Person person = new Person();
        int i = row.getFirstCellNum();
        Cell no = row.getCell(i++);
        String nostr = no.getCellTypeEnum() == CellType.NUMERIC
                ? ((long) no.getNumericCellValue()) + ""
                : no.getStringCellValue();
        if(StringUtils.isEmpty(nostr))return null;
        person.no = Long.parseLong(nostr);
        person.name = row.getCell(i++).getStringCellValue();
        person.school = row.getCell(i++).getStringCellValue();
        person.gender = Gender.valueOf(row.getCell(i++).getStringCellValue());
        person.items[0] = Item.valueOf("i"+row.getCell(i++).getStringCellValue());
        person.items[1] = Item.valueOf("i"+row.getCell(i++).getStringCellValue());
        person.items[2] = Item.valueOf("i"+row.getCell(i++).getStringCellValue());
        person.items[3] = Item.valueOf("i"+row.getCell(i++).getStringCellValue());
        try{
            person.append = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue();
        }catch (Exception e){

        }
        return person;
    }

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Item[] getItems() {
        return items;
    }

    public String getItemsKey(){
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

    public String getAppend() {
        return append;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return no == person.no &&
                name.equals(person.name) &&
                gender == person.gender &&
                Arrays.equals(items, person.items) &&
                Objects.equals(append, person.append);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(no, name, gender, append);
        result = 31 * result + Arrays.hashCode(items);
        return result;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
