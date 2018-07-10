package ece659;

/**
 *
 * @author jimmy
 */
public class Obstacle {

    private final int x, y;

    public Obstacle(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Obstacle(Position position) {
        this(position.getX(), position.getY());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Position getPosition() {
        return new Position(x, y);
    }
}
