package Sinking.common.Exceptions;

public class CoordinatesOutOfBoundsException  extends Exception{
    public CoordinatesOutOfBoundsException(int x, int y) {
        super(String.format("Coordinates (%d, %d) out of bounds for matrix of size 10x10 ", x, y));
    }
}
