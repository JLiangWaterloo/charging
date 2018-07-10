package ece659;

/**
 *
 * @author jimmy
 */
public class Device {

    private int x;
    private int y;
    private double battery;

    public Device(int x, int y, double battery) {
        this.x = x;
        this.y = y;
        this.battery = battery;
    }

    public Device(Position position, double battery) {
        this(position.getX(), position.getY(), battery);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Position getPosition() {
        return new Position(x, y);
    }

    public double getBattery() {
        return battery;
    }

    public void setBattery(double battery) {
        this.battery = battery;
    }
}
