package Sinking.common;

public class Tripel<X, Y, Z> {
    public final X x;
    public final Y y;
    public final Z z;

    public Tripel(X x, Y y, Z z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public X _1() {
        return x;
    }

    public Y _2() {
        return y;
    }

    public Z _3() {
        return z;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
