package qidian.qq.com.logger.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.swing.*;

@Getter
@AllArgsConstructor
public class Setting {

    private int groupSize;
    private int groupMinSize;
    private int reGroupTimes;
    private int rePlanTimes;
    private int landDays;
    private int upBatchSize;
    private int downBatchSize;

    public static Setting getSetting(){
        JTextField groupSize = new JTextField(new NumberTextField(),"14",10);
        JTextField groupMinSize = new JTextField(new NumberTextField(),"10",10);
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("每组最少人数:"));
        myPanel.add(groupMinSize);
        myPanel.add(new JLabel("每组最多人数:"));
        myPanel.add(groupSize);
        JTextField landDays = new JTextField(new NumberTextField(),"2",10);
        myPanel.add(new JLabel("路上项目天数:"));
        myPanel.add(landDays);
        JTextField upBatchSize = new JTextField(new NumberTextField(),"4",10);
        myPanel.add(new JLabel("上午批次数量:"));
        myPanel.add(upBatchSize);
        JTextField downBatchSize = new JTextField(new NumberTextField(),"3",10);
        myPanel.add(new JLabel("下午批次数量:"));
        myPanel.add(downBatchSize);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        JTextField reGroupTimes = new JTextField(new NumberTextField(),"30000",10);
        myPanel.add(new JLabel("尝试分组次数:"));
        myPanel.add(reGroupTimes);
        JTextField rePlanTimes = new JTextField(new NumberTextField(),"10000",10);
        myPanel.add(new JLabel("尝试编排次数:"));
        myPanel.add(rePlanTimes);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
//            System.out.println("x value: " + xField.getText());
//            System.out.println("y value: " + yField.getText());
            return new Setting(
                    Integer.parseInt(groupSize.getText()),
                    Integer.parseInt(groupMinSize.getText()),
                    Integer.parseInt(reGroupTimes.getText()),
                    Integer.parseInt(rePlanTimes.getText()),
                    Integer.parseInt(landDays.getText()),
                    Integer.parseInt(upBatchSize.getText()),
                    Integer.parseInt(downBatchSize.getText())
            );
        }
        return null;
    }

    public static void main(String[] args) {
        Setting setting = getSetting();
    }

    public static int getInt(String msg){
        Integer i = null;
        while (i == null){
        }
        return 0;
    }

    @Override
    public String toString() {
        return "输入的配置{" +
                "分组大小=" + groupSize +
                '}';
    }
}
