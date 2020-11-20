package qidian.qq.com.logger.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

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
    private int onBatchSize;

    public static Setting getSetting(){
        JTextField groupSize = new JTextField(new NumberTextField(),"14",10);
        JTextField groupMinSize = new JTextField(new NumberTextField(),"10",10);
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new GridLayout(3,3));
        myPanel.setBorder(new EmptyBorder(15, 5, 5, 5));
        myPanel.add(new JLabel("每组最少人数:", SwingConstants.RIGHT));
        myPanel.add(groupMinSize);
        myPanel.add(new JLabel("每组最多人数:", SwingConstants.RIGHT));
        myPanel.add(groupSize);
        JTextField landDays = new JTextField(new NumberTextField(),"2",10);
        myPanel.add(new JLabel("路上项目天数:", SwingConstants.RIGHT));
        myPanel.add(landDays);
        JTextField upBatchSize = new JTextField(new NumberTextField(),"4",10);
        myPanel.add(new JLabel("上午批次数量:", SwingConstants.RIGHT));
        myPanel.add(upBatchSize);
        JTextField downBatchSize = new JTextField(new NumberTextField(),"3",10);
        myPanel.add(new JLabel("下午批次数量:", SwingConstants.RIGHT));
        myPanel.add(downBatchSize);
        JTextField onBatchSize = new JTextField(new NumberTextField(),"14",10);
        myPanel.add(new JLabel("单批次单性别最大分组数量:", SwingConstants.RIGHT));
        myPanel.add(onBatchSize);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        JTextField reGroupTimes = new JTextField(new NumberTextField(),"30000",10);
        myPanel.add(new JLabel("尝试分组次数:", SwingConstants.RIGHT));
        myPanel.add(reGroupTimes);
        JTextField rePlanTimes = new JTextField(new NumberTextField(),"10000",10);
        myPanel.add(new JLabel("尝试编排次数:", SwingConstants.RIGHT));
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
                    Integer.parseInt(downBatchSize.getText()),
                    Integer.parseInt(onBatchSize.getText())
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
        return "Setting{" +
                "分组大小=" + groupSize +
                ", 分组最小大小=" + groupMinSize +
                ", 重新分组次数=" + reGroupTimes +
                ", 重新分批次数=" + rePlanTimes +
                ", 路上项目天数=" + landDays +
                ", 上午批次数=" + upBatchSize +
                ", 下午批次数=" + downBatchSize +
                ", 单批次内组数（男、女各）=" + onBatchSize +
                '}';
    }
}
