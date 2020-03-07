package qq.com.proj;

/**
 *
 */
public enum Item {

    i1000米跑(1,"1000米跑", Tn.T1, Base.LAND, Gender.男),
    i800米跑(2, "800米跑", Tn.T1, Base.LAND, Gender.女),
    i200米游泳(3, "200米游泳", Tn.T1,  Base.WATERS, Gender.BOTH),
    i50米跑(1, "50米跑", Tn.T2, Base.LAND, Gender.BOTH),
    i立定跳远(2, "立定跳远", Tn.T2, Base.LAND, Gender.BOTH),
    i实心球(3, "实心球", Tn.T2, Base.LAND, Gender.BOTH),
    i跳绳(4, "跳绳", Tn.T2, Base.LAND, Gender.BOTH),
    i引体向上(5, "引体向上", Tn.T2, Base.LAND, Gender.男),
    i仰卧起坐(6, "仰卧起坐", Tn.T2, Base.LAND, Gender.女),
    i25米游泳(7, "25米游泳", Tn.T2, Base.WATERS, Gender.BOTH),
    i垫上运动(1, "垫上运动", Tn.T3, Base.LAND, Gender.BOTH),
    i单杠(2, "单杠", Tn.T3, Base.LAND, Gender.BOTH),
    i双杠(3, "双杠", Tn.T3, Base.LAND, Gender.BOTH),
    i横箱分腿腾越(4, "横箱分腿腾越", Tn.T3, Base.LAND, Gender.BOTH),
    i篮球(1, "篮球", Tn.T4, Base.LAND, Gender.BOTH),
    i排球(2, "排球", Tn.T4, Base.LAND, Gender.BOTH),
    i足球(3, "足球", Tn.T4, Base.LAND, Gender.BOTH),

    ;

    public int num;
    /**
     *
     */
    public String name;
    /**
     *
     */
    public Tn tn;
    /**
     *
     */
    public Base base;

    public Gender gender;

    Item(int num, String name, Tn tn, Base base, Gender gender) {
        this.num = num;
        this.name = name;
        this.tn = tn;
        this.base = base;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return name;
    }
}
