package qidian.qq.com.logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import qq.com.ExcelReader;

@SpringBootApplication
public class LoggerApplication {

    public static void main(String[] args) throws Exception {
        ExcelReader.main(args);
//        SpringApplication.run(LoggerApplication.class, args);
    }

}
