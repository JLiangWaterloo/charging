package ece659;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author jimmy
 */
public class TestUtil {

    public static Environment randomSingleChargerEnvironment(int seed) {
        Set<Position> occupied = new HashSet<>();

        int width = 100;
        int height = 100;

        Device[] devices = new Device[100];
        Obstacle[] obstacles = new Obstacle[1000];

        Random rand = new Random(seed);

        Charger charger = new Charger(rand.nextInt(width), rand.nextInt(height));
        occupied.add(charger.getPosition());

        for (int i = 0; i < devices.length; i++) {
            Position position = new Position(rand.nextInt(width), rand.nextInt(height));
            while (!occupied.add(position)) {
                position = new Position(rand.nextInt(width), rand.nextInt(height));
            }
            devices[i] = new Device(position, 1);
        }

        for (int i = 0; i < obstacles.length; i++) {
            Position position = new Position(rand.nextInt(width), rand.nextInt(height));
            while (!occupied.add(position)) {
                position = new Position(rand.nextInt(width), rand.nextInt(height));
            }
            obstacles[i] = new Obstacle(position);
        }

        return new Environment(width, height, new Charger[]{charger}, devices, obstacles);
    }

    public static void main(String[] args) {
        Environment e = randomSingleChargerEnvironment(0);
        Policy p = new RandomPolicy();

        double r = e.getReward();
        for (int i = 0; i < 1000000; i++) {
            double newR = p.performActions(e);
            if (newR > r || i % 1000 == 0) {
                System.out.println(i + ": " + newR + " > " + r);
            }
            r = newR;
        }
        System.out.println(e.getReward());
        System.out.println(e.getLiveAverage());
    }
}
