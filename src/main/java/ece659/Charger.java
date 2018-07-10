package ece659;

/**
 *
 * @author jimmy
 */
public class Charger {

    private int x, y;

    public Charger(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Charger(Position position) {
        this(position.getX(), position.getY());
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
}
