package Sinking.common;

public class Tupel<X, Y> {
    private final X x;
    private final Y y;

    public Tupel(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X _1() {
        return x;
    }


    public Y _2() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + this.x.toString() + ", " + this.y.toString() + ")";
    }
}
