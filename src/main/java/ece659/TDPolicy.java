package ece659;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author jimmy
 */
public class TDPolicy implements Policy {

    private final Map<IntQ, Double> q = new HashMap<>();
    private final Random rand = new Random();

    private int[] currentState = null;
    private Action[] currentActions = null;
    private double currentReward = 0;

    private double epsilon = 0.05;

    private double getQ(IntQ intQ) {
        Double d = q.get(intQ);
        if (d == null) {
            d = rand.nextDouble();
            q.put(intQ, d);
        }
        return d;
    }

    private Action[] bestActions(int[] state, Action[][] possibleActions) {
        if (rand.nextDouble() < epsilon) {
            Action[] actions = new Action[possibleActions.length];
            for (int i = 0; i < actions.length; i++) {
                actions[i] = possibleActions[i][rand.nextInt(possibleActions[i].length)];
            }
            return actions;
        }

        double bestQ = Double.NEGATIVE_INFINITY;
        Action[] bestAction = null;

        Action[] actions = new Action[possibleActions.length];
        int[] index = new int[possibleActions.length];
        for (int i = 0; i < actions.length; i++) {
            actions[i] = possibleActions[i][0];
        }
        while (true) {
            Action[] actionCopy = Arrays.copyOf(actions, actions.length);
            double newQ = getQ(new IntQ(state, actionCopy));
            if (newQ > bestQ) {
                bestAction = actionCopy;
                bestQ = newQ;
            }
            int j;
            for (j = index.length - 1; j >= 0; j--) {
                index[j]++;
                if (index[j] >= possibleActions[j].length) {
                    index[j] = 0;
                }
                actions[j] = possibleActions[j][index[j]];
                if (index[j] > 0) {
                    break;
                }
            }
            if (j < 0) {
                break;
            }
        }

        return bestAction;
    }

    @Override
    public double performActions(Environment environment) {
        State state = environment.getState();
        Action[][] possibleActions = environment.getPossibleActions();

        Action[] actions = bestActions(state.getChargerState(), possibleActions);
        double reward = environment.performActions(actions);

        double newQ = getQ(new IntQ(state.getChargerState(), actions));
        IntQ oldIntQ = new IntQ(currentState, currentActions);
        double oldQ = getQ(oldIntQ);
        q.put(oldIntQ, oldQ + 0.05 * (currentReward + 0.99 * newQ - oldQ));

        currentState = state.getChargerState();
        currentActions = actions;
        currentReward = reward;

        return reward;
    }

}
