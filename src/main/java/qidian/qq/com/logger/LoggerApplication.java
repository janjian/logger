package qidian.qq.com.logger;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import qidian.qq.com.logger.excel.ExcelOpt;

@SpringBootApplication
public class LoggerApplication {

    public static void main(String[] args) throws Exception {
        ExcelOpt.main(args);
//        SpringApplication.run(LoggerApplication.class, args);
    }

}
