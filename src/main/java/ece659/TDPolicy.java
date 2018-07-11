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

    private final Map<IntArray, double[]> q = new HashMap<>();
    private final Random rand = new Random();

    private int[] currentState = null;
    private Action[] currentActions = null;
    private double currentReward = 0;

    private double epsilon = 0.05;

    private int pow(int base, int exponent) {
        int pow = 1;
        for (int i = 0; i < exponent; i++) {
            pow *= base;
        }
        return pow;
    }

    private int getIndex(Action[] actions) {
        int index = 0;
        for (Action action : actions) {
            index *= Action.values().length;
            index += action.ordinal();
        }
        return index;
    }

    private double[] getQ(IntArray intQ, int numActions) {
        double[] d = q.get(intQ);
        if (d == null) {
            d = new double[pow(Action.values().length, numActions)];
            for (int i = 0; i < d.length; i++) {
                d[i] = rand.nextDouble();
            }
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
            double newQ = getQ(new IntArray(state), actions.length)[getIndex(actions)];
            if (newQ > bestQ) {
                bestAction =  Arrays.copyOf(actions, actions.length);
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

        if (currentState != null) {
            double[] newQs = getQ(new IntArray(state.getChargerState()), actions.length);
            double[] oldQs = getQ(new IntArray(currentState), actions.length);
            int newIndex = getIndex(actions);
            int oldIndex = getIndex(currentActions);
            oldQs[oldIndex] = oldQs[oldIndex] + 0.05 * (currentReward + 0.99 * newQs[newIndex] - oldQs[oldIndex]);
        }

        currentState = state.getChargerState();
        currentActions = actions;
        currentReward = reward;

        return reward;
    }

}
