package ece659;

/**
 *
 * @author jimmy
 */
public class State {

    private final int[] chargerState;
    private final double[] deviceState;

    public State(int[] chargerState, double[] deviceState) {
        this.chargerState = chargerState;
        this.deviceState = deviceState;
    }

    public int[] getChargerState() {
        return chargerState;
    }

    public double[] getDeviceState() {
        return deviceState;
    }
}
