package qidian.qq.com.logger.model;

public enum MTime {

    FIRST_UP(40, "第1天上午", 11),

    FIRST_DOWN(41, "第1天下午", 12),

    SENCOND_UP(42, "第2天上午", 21),

    SENCOND_DOWN(43, "第2天下午", 22);

    public final int num;
    public final String des;
    public final int 准考证号前缀;

    MTime(int num, String des, int 准考证号前缀){
        this.num = num;
        this.des = des;
        this.准考证号前缀 = 准考证号前缀;
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

    public String half(){
        switch (this){

            case FIRST_UP:
                return FIRST_UP.des;
            case FIRST_DOWN:
                return FIRST_DOWN.des;
            case SENCOND_UP:
                return SENCOND_UP.des;
            case SENCOND_DOWN:
                return SENCOND_DOWN.des;
        }
        throw new UnsupportedOperationException();
    }
}
