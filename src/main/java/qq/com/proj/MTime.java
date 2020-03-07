package qq.com.proj;

public enum MTime {
    FIRST_UP_1(10, "第1天上午第1批"),
    FIRST_UP_2(11, "第1天上午第2批"),
    FIRST_UP_3(12, "第1天上午第3批"),

    FIRST_DOWN_1(15, "第1天下午第1批"),
    FIRST_DOWN_2(16, "第1天下午第2批"),
    FIRST_DOWN_3(17, "第1天下午第3批"),
    FIRST_DOWN_4(18, "第1天下午第4批"),

    SENCOND_UP_1(30, "第2天上午第1批"),
    SENCOND_UP_2(31, "第2天上午第2批"),
    SENCOND_UP_3(32, "第2天上午第3批"),

    SENCOND_DOWN_1(35, "第2天下午第1批"),
    SENCOND_DOWN_2(36, "第2天下午第2批"),
    SENCOND_DOWN_3(37, "第2天下午第3批"),
    SENCOND_DOWN_4(38, "第2天下午第4批"),

    FIRST_UP(40, "第1天上午"),

    FIRST_DOWN(41, "第1天下午"),

    SENCOND_UP(42, "第2天上午"),

    SENCOND_DOWN(43, "第2天下午");

    public final int num;
    public final String des;

    MTime(int num, String des){
        this.num = num;
        this.des = des;
    }

    public int i(){
        if(num >= 40){
            return num-40;
        }else {
            return ordinal();
        }
    }

    public MTime pre(){
        if(ordinal()-1 < 0)return null;
        MTime res = MTime.values()[ordinal()-1];
        if(num - res.num > 1)return null;
        return res;
    }
    public MTime next(){
        if(ordinal()+1 >= MTime.values().length)return null;
        MTime res = MTime.values()[ordinal()+1];
        if(num - res.num < -1)return null;
        return res;
    }
}
