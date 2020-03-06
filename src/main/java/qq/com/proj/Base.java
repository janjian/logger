package qq.com.proj;

public enum Base {
    LAND, WATERS, D, S, L;
    public int i(){
        return ordinal()-2;
    }
}
