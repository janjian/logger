package qq.com;

import qq.com.pojo.Handle;
import qq.com.pojo.Person;
import qq.com.pojo.Plan;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ExcelReader {

    public static String workDir = "/Users/ludesong/workspace/chongqi/4";
    public static void main(String[] args) throws Exception {

//        System.out.println(Item.valueOf("i1000米跑"));
//        ExcelOpt excelOpt = getOptFile(null);
//        excelOpt.toPersons();
        // 按指定模式在字符串查找
//        String line = "Thi#{ed} for QT3000! OK?";
//        String pattern = "#\\{(\\D+)\\}";
//
//        // 创建 Pattern 对象
//        Pattern r = Pattern.compile(pattern);
//
//        // 现在创建 matcher 对象
//        Matcher m = r.matcher(line);
//        if (m.find( )) {
//            System.out.println("Found value: " + m.groupCount() );
//            System.out.println("Found value: " + m.group(0) );
//            System.out.println("Found value: " + m.group(1) );
//            System.out.println("Found value: " + m.start() );
//            System.out.println("Found value: " + m.end() );
//            StringBuilder sb = new StringBuilder();
//            sb.append(line, m.end(), line.length());
//            System.out.println(sb);
//        } else {
//            System.out.println("NO MATCH");
//        }

        run(args);
    }

    public static void run(String[] args) throws Exception {
        ExcelOpt excelOpt = getOptFile(args.length > 1 ? args[0] : null);
        ArrayList<Person> people =  excelOpt.toPersons();
        Collections.shuffle(people);
        System.out.println("处理了"+people.size()+"条数据");
        Plan plan = Handle.calc(people);
        excelOpt.write(plan);
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
