package qq.com;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import qq.com.pojo.*;
import qq.com.proj.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**使用例子
 * ReadExcel excel = new ReadExcel("D:\\myexcel.xlsx");
 * String[] firstLine = excel.readLine(0);//得到第一行数据
 * String[] firstLine = excel.readLine(1);//得到第二行数据
 */
public class ExcelOpt {

    private Sheet sheet;
    private boolean isXLSX = false;
    private String excelPath;

    public int getAllRowNumber() {
        return sheet.getLastRowNum();
    }

    /*读取 excel 下标为 rowNumber 的那一行的全部数据*/
    public String[] readLine(int rowNumber) {
        Row row = sheet.getRow(rowNumber);
        if (row != null) {
            String[] resultStr = new String[row.getLastCellNum()];
            for (int i = 0; i < row.getLastCellNum(); i++) {
                resultStr[i] = row.getCell(i).getStringCellValue();
            }
            return resultStr;
        }
        return null;
    }

    public ExcelOpt(String excelPath) throws Exception {
        this.excelPath = excelPath;
        String fileType = excelPath.substring(excelPath.lastIndexOf(".") + 1);
        // 创建工作文档对象
        InputStream in = new FileInputStream(excelPath);
        HSSFWorkbook hssfWorkbook = null;//.xls
        XSSFWorkbook xssfWorkbook = null;//.xlsx
        //根据后缀创建读取不同类型的excel
        if ("xls".equalsIgnoreCase(fileType)) {
            hssfWorkbook = new HSSFWorkbook(in);//它是专门读取.xls的
        } else if ("xlsx".equalsIgnoreCase(fileType)) {
            xssfWorkbook = new XSSFWorkbook(in);//它是专门读取.xlsx的
            isXLSX = true;
        } else {
            throw new Exception("文档格式后缀不正确!!！");
        }
        /*这里默认只读取第 1 个sheet*/
        if (hssfWorkbook != null) {
            this.sheet = hssfWorkbook.getSheetAt(0);
        } else if (xssfWorkbook != null) {
            this.sheet = xssfWorkbook.getSheetAt(0);
        }
    }

    public ArrayList<Person> toPersons(){
        return toPersons(true);
    }

    public ArrayList<Person> toPersons(boolean out){
        ArrayList<Person> res = new ArrayList<Person>();
        ConsoleProgressBar cpb = new ConsoleProgressBar(0, getAllRowNumber(), 49, "读取输入文件");
        for (int i = 1; i < getAllRowNumber(); i++) {
            Row row = sheet.getRow(i);
            if(row.getLastCellNum() - row.getFirstCellNum() < 7){
                if(out){
                    System.out.println();
                    System.out.println("第"+i+"行数据不完整，提前读取结束");
                }
                break;
            }
            try{
                Person person = Person.parsePerson(row);
                if(person == null){
                    if(out){
                        System.out.println();
                        System.out.println("第"+i+"行数据不完整，提前读取结束");
                    }
                    break;
                }
                res.add(person);
            }catch (Exception e){
                if(out){
                    System.out.println();
                    System.out.println("解析第"+i+"行出错");
                }
                throw e;
            }
            if(out){
                cpb.show(i);
            }
        }
        return res;
    }

    public void write(Plan plan) throws IOException {
        int split = excelPath.lastIndexOf(".");
        int i = 0;
        File file;
        String name;
        System.out.println("准备输出结果");
        do {
            name = excelPath.substring(0, split) + ".输出." + (i++) + excelPath.substring(split);
            file = new File(name);
            if(file.exists())continue;
        }while (!file.createNewFile());
        // 创建工作文档对象
        OutputStream out = new FileOutputStream(name);
        System.out.println("准备生成结果文件:"+name);
        Workbook workbook = insert(plan);
        System.out.println("准备写入文件");
        workbook.write(out);
        System.out.println("写入文件完成");
    }

    private Workbook insert(Plan plan){
        Workbook workbook = null;
        if(isXLSX){
            workbook = new XSSFWorkbook();
        }else {
            workbook = new HSSFWorkbook();
        }
        outPeople(workbook, plan);
        outGorup(workbook, plan);
        outLand(workbook, plan);
        outWater(workbook, plan);
        outGreat(workbook, plan);
        return workbook;
    }

    private void outGreat(Workbook workbook, Plan plan){
        for(Group group : plan.getPlayGround().getGroupList().getGroups()){
            Sheet sheet = workbook.createSheet("第"+group.getNo()+"组");
            int start = 0;
            int row = 0;
            Row title = getRow(sheet, row++);
            int call;
            title.createCell(0, CellType.STRING).setCellValue("第"+group.getNo()+"组, 分组性别："+group.getGender());
            MTime mTime = group.getGroundTime();
            if(mTime != null){
                title = getRow(sheet, row++);
                title.createCell(0, CellType.STRING).setCellValue("陆地项目批次："+ mTime.des);
            }
            mTime = group.getPoolTime();
            if(mTime != null){
                title = getRow(sheet, row++);
                title.createCell(0, CellType.STRING).setCellValue("水上项目批次："+ mTime.des);
            }
            title = getRow(sheet, row++);
            title.createCell(0, CellType.STRING).setCellValue("本组涉及项目："+group.getItemsString());
            title = getRow(sheet, row++);
            call = start;
            title.createCell(call++, CellType.STRING).setCellValue("序号");
            title.createCell(call++, CellType.STRING).setCellValue("考生报名号");
            title.createCell(call++, CellType.STRING).setCellValue("姓名");
            title.createCell(call++, CellType.STRING).setCellValue("备注");
            ArrayList<Item> items = group.getItems();
            for(Item item : items){
                title.createCell(call++, CellType.STRING).setCellValue(item.name);
            }
            for(int i = 0; i < group.getPeople().size(); i++){
                call = start;
                Row sheetRow = getRow(sheet, row++);
                sheetRow.createCell(call++, CellType.NUMERIC).setCellValue(i+1);
                sheetRow.createCell(call++, CellType.NUMERIC).setCellValue(group.getPeople().get(i).getNo());
                sheetRow.createCell(call++, CellType.STRING).setCellValue(group.getPeople().get(i).getName());
                sheetRow.createCell(call++, CellType.STRING).setCellValue(group.getPeople().get(i).getAppend());
                for(Item item : items){
                    boolean has = group.getPeople().get(i).getItems()[item.tn.ordinal()] == item;
                    sheetRow.createCell(call++, CellType.STRING).setCellValue(has ? "" : "--------");
                }
            }
        }
    }

    private void outWater(Workbook workbook, Plan plan){
        Sheet sheet = workbook.createSheet("水上项目安排");
        int row = 0;
        for(Pool pool : plan.getPlayPool().pools){
            Row sheetRow = getRow(sheet, row++);
            sheetRow.createCell(0, CellType.STRING).setCellValue(pool.time.des+"("+pool.getGender()+")");
            int call = 0;
            int arow = row;
            AtomicInteger rowa;
            for(Tn tn : new Tn[]{Tn.T2, Tn.T1}){
                rowa = new AtomicInteger(row);
                call = outWater(sheet, call+1, rowa, tn, pool, true);
                arow = Math.max(rowa.get(), arow);
                rowa = new AtomicInteger(row);
                call = outWater(sheet, call+1, rowa, tn, pool, false);
                arow = Math.max(rowa.get(), arow);
            }
            row = arow;
        }
    }

    private int outWater(Sheet sheet, int call, AtomicInteger row, Tn tn, Pool pool, boolean doubleit){
        int temp = call;
        int i = 0;
        call = temp;
        Row sheetRow = getRow(sheet, row.getAndIncrement());
        String apped = doubleit?"(兼游)":"(单游)";
        if(tn == Item.i25米游泳.tn){
            sheetRow.createCell(call++, CellType.STRING).setCellValue(Item.i25米游泳.name+apped);
        }else{
            sheetRow.createCell(call++, CellType.STRING).setCellValue(Item.i200米游泳.name+apped);
        }
        sheetRow = getRow(sheet, row.getAndIncrement());
        call = temp;
        sheetRow.createCell(call++, CellType.STRING).setCellValue("序号");
        sheetRow.createCell(call++, CellType.STRING).setCellValue("分组号");
        sheetRow.createCell(call++, CellType.STRING).setCellValue("考生报名号");
        sheetRow.createCell(call++, CellType.STRING).setCellValue("姓名");
        sheetRow.createCell(call++, CellType.STRING).setCellValue("备注");
        for(Group group : pool.getPool(tn)){
            for(Person person : group.getPeople()){
                if(person.getItems()[tn.ordinal()].base == Base.LAND)continue;
                boolean itdoube = person.getItems()[0].base == Base.WATERS
                        && person.getItems()[1].base == Base.WATERS;
                if(itdoube == doubleit){
                    call = temp;
                    sheetRow = getRow(sheet, row.getAndIncrement());
                    sheetRow.createCell(call++, CellType.NUMERIC).setCellValue(i++);
                    sheetRow.createCell(call++, CellType.NUMERIC).setCellValue(group.getNo());
                    sheetRow.createCell(call++, CellType.NUMERIC).setCellValue(person.getNo());
                    sheetRow.createCell(call++, CellType.STRING).setCellValue(person.getName());
                    sheetRow.createCell(call++, CellType.STRING).setCellValue(person.getAppend());
                    call++;
                }
            }
        }
        return call;
    }

    private void outLand(Workbook workbook, Plan plan){
        Sheet sheet = workbook.createSheet("路上项目安排");
        int row = 0;
        for(MTime mTime : MTime.values()){
            if(mTime.i() != mTime.ordinal()){
                continue;
            }
            Round round = plan.getPlayGround().getRounds()[mTime.ordinal()];
            Row sheetRow = getRow(sheet, row++);
            int clum = 0;
            sheetRow.createCell(0, CellType.STRING).setCellValue(mTime.des);
            for(Item item : Item.values()){
                if(item.base == Base.WATERS)continue;
                clum = outLand(sheet, row, clum, round, item, Gender.男);
                clum = outLand(sheet, row, clum, round, item, Gender.女);
                clum++;
            }
            row = sheet.getLastRowNum() + 1;
        }
    }

    private int outLand(Sheet sheet, int x, int y, Round round, Item item, Gender gender){
        ArrayList<Group> groups = round.getList(gender, item);
        if(groups == null)return y;
        Row sheetRow = getRow(sheet,  x++);
        sheetRow.createCell(y, CellType.STRING).setCellValue(item.name+"("+gender+")");
        sheetRow = getRow(sheet,  x++);
        int call = y;
        sheetRow.createCell(call++, CellType.STRING).setCellValue("序号");
        sheetRow.createCell(call++, CellType.STRING).setCellValue("分组号");
        sheetRow.createCell(call++, CellType.STRING).setCellValue("考生报名号");
        sheetRow.createCell(call++, CellType.STRING).setCellValue("姓名");
        sheetRow.createCell(call++, CellType.STRING).setCellValue("备注");
        int no = 1;
        for(int i = 0; i < groups.size(); i++){
            for(Person person : groups.get(i).getPeople()){
                if(person.getItems()[item.tn.ordinal()] != item)continue;
                call = y;
                sheetRow = getRow(sheet,  x++);
                sheetRow.createCell(call++, CellType.NUMERIC).setCellValue(no++);
                sheetRow.createCell(call++, CellType.NUMERIC).setCellValue(groups.get(i).getNo());
                sheetRow.createCell(call++, CellType.NUMERIC).setCellValue(person.getNo());
                sheetRow.createCell(call++, CellType.STRING).setCellValue(person.getName());
                sheetRow.createCell(call++, CellType.STRING).setCellValue(person.getAppend());
            }
        }
        return call;
    }

    private void outGorup(Workbook workbook, Plan plan){
        Sheet sheet = workbook.createSheet("分组情况");
        int row = 0;
        int call = 0;
        Row title = sheet.createRow(row++);
        title.createCell(0, CellType.STRING).setCellValue("未成功分组人员");
        title = sheet.createRow(row++);
        title.createCell(call++, CellType.STRING).setCellValue("序号");
        title.createCell(call++, CellType.STRING).setCellValue("考生报名号");
        title.createCell(call++, CellType.STRING).setCellValue("姓名");
        title.createCell(call++, CellType.STRING).setCellValue("备注");
        for(Person person : plan.getPlayGround().getGroupList().getRest()){
            call = 0;
            Row sheetRow = sheet.createRow(row++);
            sheetRow.createCell(call++, CellType.NUMERIC).setCellValue(row - 2);
            sheetRow.createCell(call++, CellType.NUMERIC).setCellValue(person.getNo());
            sheetRow.createCell(call++, CellType.STRING).setCellValue(person.getName());
            sheetRow.createCell(call++, CellType.STRING).setCellValue(person.getAppend());
        }

        int last = call;
        ArrayList<Group> groups = plan.getPlayGround().getGroupList().getGroups();
        groups.sort(Comparator.comparingInt(Group::getNo));
        for (Group group : groups) {
            last = outGroup(sheet, group, last + 2);
        }
    }

    private Row getRow(Sheet sheet, int i){
        Row sheetRow = sheet.getRow(i);
        if(sheetRow == null){
            sheetRow = sheet.createRow(i);
        }
        return sheetRow;
    }

    private int outGroup(Sheet sheet, Group group, int start){
        int row = 0;
        Row title = getRow(sheet, row++);
        int call;
        call = start;
        title.createCell(call++, CellType.STRING).setCellValue("第"+group.getNo()+"组, 分组性别："+group.getGender());
        MTime mTime = group.getGroundTime();
        if(mTime != null){
            title = getRow(sheet, row++);
            call = start;
            title.createCell(call++, CellType.STRING).setCellValue("陆地项目批次："+ mTime.des);
        }
        mTime = group.getPoolTime();
        if(mTime != null){
            title = getRow(sheet, row++);
            call = start;
            title.createCell(call++, CellType.STRING).setCellValue("水上项目批次："+ mTime.des);
        }
        title = getRow(sheet, row++);
        call = start;
        title.createCell(call++, CellType.STRING).setCellValue("本组涉及项目："+group.getItemsString());
        title = getRow(sheet, row++);
        call = start;
        title.createCell(call++, CellType.STRING).setCellValue("序号");
        title.createCell(call++, CellType.STRING).setCellValue("考生报名号");
        title.createCell(call++, CellType.STRING).setCellValue("姓名");
        title.createCell(call++, CellType.STRING).setCellValue("备注");
        for(int i = 0; i < group.getPeople().size(); i++){
            call = start;
            Row sheetRow = getRow(sheet, row++);
            sheetRow.createCell(call++, CellType.NUMERIC).setCellValue(i+1);
            sheetRow.createCell(call++, CellType.NUMERIC).setCellValue(group.getPeople().get(i).getNo());
            sheetRow.createCell(call++, CellType.STRING).setCellValue(group.getPeople().get(i).getName());
            sheetRow.createCell(call++, CellType.STRING).setCellValue(group.getPeople().get(i).getAppend());
        }

        Row sheetRow = sheet.getRow(group.getPeople().size());
        if(sheetRow == null){
            sheetRow = sheet.createRow(group.getPeople().size());
        }
        return call;
    }


    private void outPeople(Workbook workbook, Plan plan){
        Sheet sheet = workbook.createSheet("学生列表");
        int row = 0;
        Row title = sheet.createRow(row++);
        int call = 0;
        title.createCell(call++, CellType.STRING).setCellValue("考生报名号");
        title.createCell(call++, CellType.STRING).setCellValue("姓名");
        title.createCell(call++, CellType.STRING).setCellValue("学校");
        title.createCell(call++, CellType.STRING).setCellValue("性别");
        title.createCell(call++, CellType.STRING).setCellValue("第一类项目");
        title.createCell(call++, CellType.STRING).setCellValue("第二类项目");
        title.createCell(call++, CellType.STRING).setCellValue("第三类项目");
        title.createCell(call++, CellType.STRING).setCellValue("第四类项目");
        title.createCell(call++, CellType.STRING).setCellValue("特殊");
        title.createCell(call++, CellType.STRING).setCellValue("分组编号");
        title.createCell(call++, CellType.STRING).setCellValue("路上项目批次");
        title.createCell(call++, CellType.STRING).setCellValue("水上项目批次");
        sheet.createFreezePane(0, row);
        HashMap<Person, Row> personIntegerHashMap = new HashMap<>();
        ArrayList<Person> personArrayList = toPersons(false);
        for (Person p : personArrayList){
            Row sheetRow = sheet.createRow(row++);
            personIntegerHashMap.put(p, sheetRow);
            call = 0;
            sheetRow.createCell(call++, CellType.NUMERIC).setCellValue(p.getNo());
            sheetRow.createCell(call++, CellType.STRING).setCellValue(p.getName());
            sheetRow.createCell(call++, CellType.STRING).setCellValue(p.getSchool());
            sheetRow.createCell(call++, CellType.STRING).setCellValue(p.getGender().name());
            sheetRow.createCell(call++, CellType.STRING).setCellValue(p.getItems()[Tn.T1.ordinal()].name);
            sheetRow.createCell(call++, CellType.STRING).setCellValue(p.getItems()[Tn.T2.ordinal()].name);
            sheetRow.createCell(call++, CellType.STRING).setCellValue(p.getItems()[Tn.T3.ordinal()].name);
            sheetRow.createCell(call++, CellType.STRING).setCellValue(p.getItems()[Tn.T4.ordinal()].name);
            sheetRow.createCell(call++, CellType.STRING).setCellValue(p.getAppend());

        }
        ArrayList<Group> groups = plan.getPlayGround().getGroupList().getGroups();
        LinkedHashMap<MTime, Integer> mtimeCount = new LinkedHashMap<>();//用于统计每个批次目前有多少组了
        for(int i = 0; i < groups.size(); i++){
            MTime 陆地批次 = groups.get(i).getGroundTime();
            Integer 当前批次组编号 = mtimeCount.compute(陆地批次, (key, value) -> {
                if (value == null) {
                    return 1; // 第一次出现为第一组
                }
                return value + 1;
            });
            MTime 水上批次 = groups.get(i).getPoolTime();
            for (int j = 0; j < groups.get(i).getPeople().size(); j++) {
                Person person = groups.get(i).getPeople().get(j);
                int callIndex = call;
                Row sheetRow = personIntegerHashMap.get(person);
                assert sheetRow != null;
                sheetRow.createCell(callIndex++, CellType.NUMERIC).setCellValue(i + 1);
                if(陆地批次 == null){
                    sheetRow.createCell(callIndex++, CellType.STRING).setCellValue("");
                }else {
                    String 准考证号 = String.format("%d%02d%02d", 陆地批次.准考证号前缀, 当前批次组编号, j + 1);
                    sheetRow.createCell(callIndex++, CellType.STRING).setCellValue(准考证号);
                }
                sheetRow.createCell(callIndex++, CellType.STRING).setCellValue(水上批次 == null ? "" : 水上批次.des);
            }
        }
        for(Person person : plan.getPlayGround().getGroupList().getRest()){
            int callIndex = call;
            Row sheetRow = personIntegerHashMap.get(person);
            sheetRow.createCell(callIndex++, CellType.STRING).setCellValue("暂无分组");
            sheetRow.createCell(callIndex++, CellType.STRING).setCellValue("暂无分组");
            sheetRow.createCell(callIndex++, CellType.STRING).setCellValue("暂无分组");
        }
    }
}