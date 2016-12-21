package ru.ifmo.ctddev.segal.cw2.solvers;

/**
 * Created by Aleksei Latyshev
 */

public class SolutionStep {
    public final double[] solT;
    public final double[] solX;

    public SolutionStep(double[] solT, double[] solX) {
        this.solT = solT;
        this.solX = solX;
    }
}