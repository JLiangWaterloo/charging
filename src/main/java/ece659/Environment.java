package ece659;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author jimmy
 */
public class Environment {

    private final int width, height;
    private final Charger[] chargers;
    private final Device[] devices;
    private final Obstacle[] obstacles;
    private final Set<Position> obstaclePositions;

    private long live;
    private int timestep;

    public Environment(int width, int height, Charger[] chargers, Device[] devices, Obstacle[] obstacles) {
        this.width = width;
        this.height = height;
        this.chargers = chargers;
        this.devices = devices;
        this.obstacles = obstacles;
        this.obstaclePositions = new HashSet<>(obstacles.length);
        for (Obstacle obstacle : obstacles) {
            obstaclePositions.add(new Position(obstacle.getX(), obstacle.getY()));
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Charger[] getChargers() {
        return chargers;
    }

    public Device[] getDevices() {
        return devices;
    }

    public Obstacle[] getObstacles() {
        return obstacles;
    }

    public State getState() {
        int[] chargerState = new int[chargers.length * 2];
        for (int i = 0; i < chargers.length; i++) {
            chargerState[i * 2] = chargers[i].getX();
            chargerState[i * 2 + 1] = chargers[i].getY();
        }
        double[] deviceState = new double[devices.length];
        for (int i = 0; i < devices.length; i++) {
            deviceState[i] = devices[i].getBattery();
        }
        return new State(chargerState, deviceState);
    }

    public Action[][] getPossibleActions() {
        Action[][] possibleActions = new Action[chargers.length][];
        for (int i = 0; i < chargers.length; i++) {
            Charger charger = chargers[i];
            Action[] actions = new Action[Action.values().length];
            int size = 0;
            for (Action action : Action.values()) {
                if (checkMove(move(charger, action))) {
                    actions[size++] = action;
                }
            }
            if (size == 0) {
                throw new IllegalStateException();
            }
            if (size != actions.length) {
                actions = Arrays.copyOf(actions, size);
            }
            possibleActions[i] = actions;
        }
        return possibleActions;
    }

    private static Position move(Charger charger, Action action) {
        switch (action) {
            case Left:
                return new Position(charger.getX() - 1, charger.getY());
            case Right:
                return new Position(charger.getX() + 1, charger.getY());
            case Up:
                return new Position(charger.getX(), charger.getY() + 1);
            case Down:
                return new Position(charger.getX(), charger.getY() - 1);
//            case Stay:
//                return new Position(charger.getX(), charger.getY());
            default:
                throw new IllegalArgumentException();
        }
    }

    private boolean checkMove(Position position) {
        if (obstaclePositions.contains(position)) {
            return false;
        }
        if (position.getX() < 0 || position.getX() >= width) {
            return false;
        }
        if (position.getY() < 0 || position.getY() >= height) {
            return false;
        }
        return true;
    }

    public double getReward() {
        double reward = 0;
        for (Device device : devices) {
            double battery = device.getBattery();
            reward += battery == 0 ? -1 : battery;
        }
        return reward;
    }

    public double getLiveAverage() {
        return BigDecimal.valueOf(live).divide(BigDecimal.valueOf(timestep), 4, RoundingMode.HALF_EVEN).doubleValue();
    }

    public double performActions(Action... actions) {
        if (actions.length != chargers.length) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < actions.length; i++) {
            if (!checkMove(move(chargers[i], actions[i]))) {
                throw new IllegalArgumentException();
            }
        }

        // Drain
        for (Device device : devices) {
            double battery = device.getBattery();
            battery -= 0.00001;
            if (battery < 0) {
                battery = 0;
            }
            device.setBattery(battery);
        }

        // Action
        for (int i = 0; i < actions.length; i++) {
            Position position = move(chargers[i], actions[i]);
            chargers[i].setX(position.getX());
            chargers[i].setY(position.getY());

            // Charge
            for (Device device : devices) {
                if (position.getX() == device.getX()
                        && position.getY() == device.getY()) {
                    device.setBattery(1);
                }
            }
        }

        for (Device device : devices) {
            if (device.getBattery() > 0) {
                live++;
            }
        }

        timestep++;
        return getReward();
    }
}
