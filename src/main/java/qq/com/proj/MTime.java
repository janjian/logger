package qq.com.proj;

public enum MTime {
    FIRST_UP_1(10),
    FIRST_UP_2(11),
    FIRST_UP_3(12),

    FIRST_DOWN_1(15),
    FIRST_DOWN_2(16),
    FIRST_DOWN_3(17),
    FIRST_DOWN_4(18),

    SENCOND_UP_1(30),
    SENCOND_UP_2(31),
    SENCOND_UP_3(32),

    SENCOND_DOWN_1(35),
    SENCOND_DOWN_2(36),
    SENCOND_DOWN_3(37),
    SENCOND_DOWN_4(38),

    FIRST_UP(40),

    FIRST_DOWN(41),

    SENCOND_UP(42),

    SENCOND_DOWN(43);

    public final int num;

    MTime(int num){
        this.num = num;
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
