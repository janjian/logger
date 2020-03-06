package qidian.qq.com.logger;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogSplit {
    public static String workDir = "/Users/ludesong/workspace/chongqi/4";
    public static void main(String[] args) throws FileNotFoundException {
        File dir = new File(workDir);
        Pattern pattern1 = Pattern.compile("\\[[^\\]]*\\] \\[[^\\]]*\\] \\[[^\\]]*\\] \\[[^\\]]*\\] \\[[^\\]]*\\] ");
        Pattern pattern2 = Pattern.compile("\\[[^\\]]*\\] $");
        HashMap<Long, PrintStream> outs = new HashMap<>();
        for (File file:
                Objects.requireNonNull(dir.listFiles())
             ) {
            if(file.isDirectory() || file.getName().startsWith("out")){
                break;
            }
            Scanner scanner = new Scanner(new FileInputStream(file));
            long lastId = -1;
            while (scanner.hasNext()){
                String line = scanner.nextLine();
                Matcher matcher = pattern1.matcher(line);
                if(matcher.find()){
                    String temp = matcher.group();
                    Matcher matcher2 = pattern2.matcher(temp);
                    if(matcher2.find()) {
                        String idStr = matcher2.group().substring(1);
                        idStr = idStr.substring(0, idStr.length()-2);
                        lastId = Long.parseLong(idStr);
                    }
                }
                outs.computeIfAbsent(lastId, id -> {
                    try {
                        File out = new File(dir.getAbsolutePath()+"/out"+id+".log");
                        if(!out.exists()){
                            out.createNewFile();
                        }
                        return new PrintStream(out);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                outs.get(lastId).println(line);
            }
            for (PrintStream ps: outs.values()
                 ) {
                ps.flush();
                ps.close();
            }
        }
    }
}
