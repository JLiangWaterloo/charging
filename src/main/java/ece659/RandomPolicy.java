package ece659;

import java.util.Random;

/**
 *
 * @author jimmy
 */
public class RandomPolicy implements Policy {

    private final Random rand = new Random();

    @Override
    public double performActions(Environment environment) {
        Action[][] possibleActions = environment.getPossibleActions();
        Action[] actions = new Action[possibleActions.length];
        for (int i = 0; i < actions.length; i++) {
            actions[i] = possibleActions[i][rand.nextInt(possibleActions[i].length)];
        }
        return environment.performActions(actions);
    }
}
