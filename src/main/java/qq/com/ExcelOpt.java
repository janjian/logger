package qq.com;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import qq.com.pojo.Person;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;

/**使用例子
 * ReadExcel excel = new ReadExcel("D:\\myexcel.xlsx");
 * String[] firstLine = excel.readLine(0);//得到第一行数据
 * String[] firstLine = excel.readLine(1);//得到第二行数据
 */
public class ExcelOpt {

    private Sheet sheet;
    private boolean isXLSX = false;

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
        ArrayList<Person> res = new ArrayList<Person>();
        ConsoleProgressBar cpb = new ConsoleProgressBar(0, getAllRowNumber(), 49, "读取输入文件");
        for (int i = 1; i < getAllRowNumber(); i++) {
            Row row = sheet.getRow(i);
            if(row.getLastCellNum() - row.getFirstCellNum() < 7){
                System.out.println();
                System.out.println("第"+i+"行数据不完整，提前读取结束");
                break;
            }
            try{
                res.add(Person.parsePerson(row));
            }catch (Exception e){
                System.out.println();
                System.out.println("解析第"+i+"行出错");
                throw e;
            }
            cpb.show(i);
        }
        return res;
    }
}