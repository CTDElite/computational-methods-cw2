package ru.ifmo.ctddev.segal.cw2.solvers;

import java.util.Arrays;

/**
 * Created by Aleksei Latyshev
 */

public abstract class Solver extends ConstantsWrapper {
    public final int sizeTime = (int) (maxTime / dt);
    public final int size = (int) (h / dz);

    public Solver(double dt, double dz, double h, double maxTime, double k, double e, double alpha) {
        super(dt, dz, h, maxTime, k, e, alpha);
    }

    public Solver(double dt, double dz, double h, double maxTime, double k, double e) {
        super(dt, dz, h, maxTime, k, e);
    }

    public Solver(double dt, double dz, double h, double maxTime) {
        super(dt, dz, h, maxTime);
    }

    public Solver(double dt, double dz) {
        super(dt, dz);
    }

    public Solution solve() {
        double[] stepForT = getInitialStepT();
        double[] stepForX = getInitialStepX();

        double[][] solT = new double[sizeTime][size];
        double[][] solX = new double[sizeTime][size];
        for (int time = 0; time < sizeTime; time++) {
            solT[time] = stepForT;
            solX[time] = stepForX;

            double[] oldStepT = stepForT;
            stepForT = stepT(oldStepT, stepForX);
            stepForX = stepX(oldStepT, stepForX);
        }
        return new Solution(solT, solX);
    }

    public abstract double[] stepT(double[] solT, double[] solX);
    public abstract double[] stepX(double[] solT, double[] solX);

    public double[] getInitialStepT() {
        double[] solT = new double[size];
        Arrays.fill(solT, T0);
        solT[0] = Tm;

        return solT;
    }

    public double[] getInitialStepX() {
        double[] solX = new double[size];
        Arrays.fill(solX, 1);
        solX[0] = 0;

        return solX;
    }
}
