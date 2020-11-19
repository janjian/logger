package qidian.qq.com.logger.excel;

import com.sun.tools.javac.util.Pair;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import qidian.qq.com.logger.model.*;
import qidian.qq.com.logger.utils.ConsoleProgressBar;
import qidian.qq.com.logger.utils.GroupUtils;
import qidian.qq.com.logger.utils.Setting;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExcelOpt {

    private Sheet sheet;
    private boolean isXLSX = false;
    private String excelPath;


    static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

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

    public static void main(String[] args) throws Exception {
        ExcelOpt excelOpt = getOptFile(args.length > 1 ? args[0] : null);
        ArrayList<Person> people = excelOpt.toPersons();
        System.out.println("处理了"+people.size()+"条数据");
        Setting setting = Setting.getSetting();
        System.out.println(setting);
        GroupList groupList = getGroupLists(new ArrayList<>(people), setting);
        Plan plan = getPlan(people, groupList, setting);
        excelOpt.write(plan);
    }

    private void write(Plan plan) {
    }

    private static Plan getPlan(ArrayList<Person> people, GroupList groupList, Setting setting) {
        PlayPool playPool = new PlayPool(people, groupList);
        int count = 50;
        int maxi = setting.getReGroupTimes() / count;
        ConcurrentLinkedQueue<PlayGround> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        ConsoleProgressBar cpb = new ConsoleProgressBar(0, maxi, 44, "优化陆地项目批次");
        for(int i3 = 0; i3 < maxi; i3++){
            CountDownLatch latch = new CountDownLatch(count);
            cpb.show(0);
            for(int i = 0; i < count; i++){
                cachedThreadPool.execute(() -> {
                    GroupList inner = groupList.clone();
                    PlayGround p = new PlayGround(inner);

                    PlayGround mini = concurrentLinkedQueue.poll();
                    concurrentLinkedQueue.add(p.mini(mini));
                    latch.countDown();
                });
            }
            latch.await();
            cpb.show(i3);
        }
        PlayGround mini = null;
        for(PlayGround groupList2 : concurrentLinkedQueue){
            if(mini == null){
                mini = groupList2;
            }else{
                mini = mini.mini(groupList2);
            }
        }
        cpb.show(maxi);
        return new Plan(playPool, mini);
    }

    private static GroupList getGroupLists(ArrayList<Person> people, Setting setting) {
        Pair<GroupList, LinkedHashMap<String, ArrayList<Person>>> pair = GroupList.init(people, setting);
        GroupList groupList = pair.fst;
        System.out.println("完美分组共"+groupList.getGroups().size()+"队, 特殊情况考生共"+groupList.getRest().size()+"人全部拿出来不参与筛选，放在未成功分组处");

        ConcurrentLinkedQueue<GroupList> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        int count = 50;
        int maxi = setting.getReGroupTimes() / count;
        ConsoleProgressBar cpb = new ConsoleProgressBar(0, maxi, 44, "优化落单学生组队");
        for(int i3 = 0; i3 < maxi; i3++){
            CountDownLatch latch = new CountDownLatch(count);
            cpb.show(0);
            for(int i = 0; i < count; i++){
                cachedThreadPool.execute(() -> {
                    LinkedHashMap<String, ArrayList<Person>> c = GroupList.copy(pair.snd);
                    GroupList groupList2 = GroupUtils.chou(c, new GroupList(), setting);

                    GroupList mini = concurrentLinkedQueue.poll();
                    concurrentLinkedQueue.add(groupList2.mini(mini));
                    latch.countDown();
                });
            }
            latch.await();
            cpb.show(i3);
        }
        GroupList mini = null;
        for(GroupList groupList2 : concurrentLinkedQueue){
            if(mini == null){
                mini = groupList2;
            }else{
                mini = mini.mini(groupList2);
            }
        }
        cpb.show(maxi);
        assert mini != null;
        groupList.addAll(mini);
        return groupList;
    }

    private ArrayList<Person> toPersons() {

        ArrayList<Person> res = new ArrayList<Person>();
        ConsoleProgressBar cpb = new ConsoleProgressBar(0, getAllRowNumber(), 49, "读取输入文件");
        Person.setHeader(sheet.getRow(1));
        for (int i = 2; i < getAllRowNumber(); i++) {
            Row row = sheet.getRow(i);
            if (row.getLastCellNum() - row.getFirstCellNum() < 7) {
                System.out.println();
                System.out.println("第" + i + "行数据不完整，提前读取结束");
                break;
            }
            try {
                Person person = Person.parsePerson(row);
                if (person == null) {
                    System.out.println();
                    System.out.println("第" + i + "行数据不完整，提前读取结束");
                    break;
                }
                res.add(person);
            } catch (Exception e) {
                System.out.println();
                System.out.println("解析第" + i + "行出错");
                throw e;
            }
            cpb.show(i);
        }
        return res;
    }

    public int getAllRowNumber() {
        return sheet.getLastRowNum();
    }

    public static ExcelOpt getOptFile(String fileName) throws Exception {
        String tName = fileName;
        ExcelOpt excelOpt = null;
        if(tName == null){
            String filePath = new File("").getAbsolutePath();
            File path = new File(filePath);
            for(File file : Objects.requireNonNull(path.listFiles())){
                if(file.getName().indexOf(".输出.") > 0)continue;
                try {
                    excelOpt = new ExcelOpt(file.getAbsolutePath());
                    System.out.println("读取到第一个excel《"+file.getName()+"》开始处理");
                    break;
                } catch (Exception e) {
//                    System.out.println(file.getName()+"不是excel，跳过");
                }
            }
            if(excelOpt == null){
                throw new Exception("未能找到到合适的excel文件!");
            }
        }else{
            excelOpt = new ExcelOpt(tName);
        }
        return excelOpt;
    }
}
