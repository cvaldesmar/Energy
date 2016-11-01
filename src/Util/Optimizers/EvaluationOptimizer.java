/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util.Optimizers;

import Models.Problem;
import Models.ProblemVariable;
import Models.Solution;
import Models.YearInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public abstract class EvaluationOptimizer implements Optimizer {

    protected Problem problem;
    protected final ArrayList<Double> valueList;
    protected final ArrayList<Double> epsilonList;
    protected final int parts;

    protected EvaluationOptimizer(int newParts, Problem problem) {

        valueList = this.newRandomList(1.0, newParts);
        epsilonList = this.newRandomList(5.0, newParts);
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

    protected final HashMap<Integer, ProblemVariable> cloneMap(HashMap<Integer, ProblemVariable> original) {
        HashMap<Integer, ProblemVariable> clone = new HashMap<Integer, ProblemVariable>();
        for (Map.Entry<Integer, ProblemVariable> entry : original.entrySet()) {
                try {
                    clone.put(entry.getKey(), (ProblemVariable) entry.getValue().clone());
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(RandomEvaluationOptimizer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        return clone;
    }

    protected Double getNewEpsilon() {
        return epsilonList.get((new Random()).nextInt(parts));
    }

    protected Double getNewValue() {
        return valueList.get((new Random()).nextInt(parts));
    }

    @Override
    abstract public void optimize(Solution solution);

}
