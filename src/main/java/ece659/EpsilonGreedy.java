package ece659;

import java.util.Random;

/**
 *
 * @author jimmy
 */
public class EpsilonGreedy implements Policy {

    private final Random rand = new Random();
    private final Policy policy;
    private final double epsilon;

    public EpsilonGreedy(Policy policy, double epsilon) {
        this.policy = policy;
        this.epsilon = epsilon;
    }

    @Override
    public double performActions(Environment environment) {
        if (rand.nextDouble() < epsilon) {
            Action[][] possibleActions = environment.getPossibleActions();
            Action[] actions = new Action[possibleActions.length];
            for (int i = 0; i < actions.length; i++) {
                actions[i] = possibleActions[i][rand.nextInt(possibleActions[i].length)];
            }
            return environment.performActions(actions);
        }
        return policy.performActions(environment);
    }
}
