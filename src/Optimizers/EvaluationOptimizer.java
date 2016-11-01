/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Optimizers;

import Models.Problem;
import Models.ProblemVariable;
import Models.Solution;
import Models.YearInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Usuario
 */
public abstract class EvaluationOptimizer implements Optimizer {

    protected Problem problem;
    protected final ArrayList<Double> newValueList;
    protected final ArrayList<Double> newEpsilonList;
    protected final int parts;

    protected EvaluationOptimizer(int newParts, Problem problem) {

        newValueList = this.newRandomList(1.0, newParts);
        newEpsilonList = this.newRandomList(5.0, newParts);
        this.parts = newParts;
        this.problem = problem;

    }

    @Override
    public double evaluate(HashMap<Integer, ProblemVariable> variables, Double epsi) {
        int numYears = problem.getYears().size();
        Double aux = 1.0 / numYears;
        Double summation = 0.0;

        for (YearInfo yi : problem.getYears().values()) {
            Double objective = yi.getObj();
            Double predictSum = 0.0;
            for (int j = 0; j < yi.getFullData().size(); j++) {
                ProblemVariable solVar = variables.get(j);
                predictSum += solVar.getAlfa() * Math.pow(yi.getData(j), solVar.getBeta());
            }
            Double predict = epsi + predictSum;
            summation += Math.pow(objective - predict, 2);
        }

        double result = aux * summation;

        return result;
    }

    protected final ArrayList<Double> newRandomList(Double value, int parts) {
        ArrayList<Double> numbers = new ArrayList<>();
        numbers.add(0.0);
        double aux = (parts - 1) / 2;
        double part = value / aux;
        for (int i = 1; i <= aux; i++) {
            numbers.add(i * part);
            numbers.add(-(i * part));
        }
        long seed = System.nanoTime();
        Collections.shuffle(numbers, new Random(seed));
        return numbers;
    }

    protected Double getNewEpsilon() {
        return newEpsilonList.get((new Random()).nextInt(parts));
    }

    protected Double getNewValue() {
        return newValueList.get((new Random()).nextInt(parts));
    }

    @Override
    abstract public void optimize(Solution solution);

}
