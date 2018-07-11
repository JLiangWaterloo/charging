package ece659;

import java.util.Arrays;

/**
 *
 * @author jimmy
 */
public class IntArray {

    private final int[] array;

    public IntArray(int[] state) {
        this.array = state;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntArray) {
            IntArray other = (IntArray) obj;
            return Arrays.equals(array, other.array);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(array);
    }
}
