/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util.Optimizers;

import Models.Problem;
import Models.ProblemVariable;
import Models.Solution;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Usuario
 */
public class LSFIEvaluationOptimizer extends EvaluationOptimizer {

    private ArrayList<Double> valueListCopy;
    private ArrayList<Double> epsilonListCopy;

    public LSFIEvaluationOptimizer(int parts, Problem problem) {
        super(parts, problem);
    }

    @Override
    public void optimize(Solution solution) {

        valueListCopy = (ArrayList<Double>) valueList.clone();
        epsilonListCopy = (ArrayList<Double>) epsilonList.clone();
        double newEvaluation = solution.getEvaluation();
        //Min + (int)(Math.random() * ((Max - Min) + 1)) max=14 min=0
        int selectedChange = (int) (Math.random() * 15);
        Double newEpsilon = solution.getEpsilon();;
        HashMap<Integer, ProblemVariable> newProbVariables = solution.getProbVariables();;
        if (selectedChange != 14) {
            newProbVariables = this.cloneMap(solution.getProbVariables());
        }
        while (newEvaluation >= solution.getEvaluation() && (!valueListCopy.isEmpty() && !epsilonListCopy.isEmpty())) {
            if (selectedChange == 14) {
                newEpsilon = this.getNewEpsilon();
            } else {
                Double newValue = this.getNewValue();
                Boolean changeAlpha = new Random().nextBoolean();
                if (changeAlpha) {
                    newProbVariables.get(selectedChange).setAlfa(newValue);
                } else {
                    newProbVariables.get(selectedChange).setBeta(newValue);
                }

            }
            newEvaluation = this.evaluate(newProbVariables, newEpsilon);
            if (newEvaluation < solution.getEvaluation()) {
                solution.setEpsilon(newEpsilon);
                solution.setProbVariables(newProbVariables);
                solution.setEvaluation(newEvaluation);
                break;
            }
        }

    }

    @Override
    protected Double getNewEpsilon() {
        Double newEpsilon = epsilonListCopy.get((new Random()).nextInt(epsilonListCopy.size()));
        epsilonListCopy.remove(newEpsilon);
        return newEpsilon;
    }

    @Override
    protected Double getNewValue() {
        Double newValue =  valueListCopy.get((new Random()).nextInt(valueListCopy.size()));
        valueListCopy.remove(newValue);
        return newValue;
    }

}