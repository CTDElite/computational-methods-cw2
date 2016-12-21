package ru.ifmo.ctddev.segal.cw2.solvers;

/**
 * Created by Aleksei Latyshev on 21.12.2016.
 */

public class SmartSolver extends Solver {

    public SmartSolver(double dt, double dz, double h, double maxTime, double k, double e, double alpha) {
        super(dt, dz, h, maxTime, k, e, alpha);
    }

    public SmartSolver(double dt, double dz, double h, double maxTime, double k, double e) {
        super(dt, dz, h, maxTime, k, e);
    }

    public SmartSolver(double dt, double dz, double h, double maxTime) {
        super(dt, dz, h, maxTime);
    }

    public SmartSolver(double dt, double dz) {
        super(dt, dz);
    }

    public SmartSolver(ConstantsWrapper other) {
        super(other);
    }

    @Override
    public double[] stepX(double[] solT, double[] solX) {
        double[][] matrixX = new double[size][];
        matrixX[0] = new double[]{1, 0};
        matrixX[size - 1] = new double[]{-1, 1};
        for (int time = 1; time < size - 1; time++) {
            matrixX[time] = new double[]{
                    -D / (dz * dz),
                    1 / dt + 2 * D / (dz * dz) - WMagic(solX[time], solT[time]),
                    -D / (dz * dz)
            };
        }

        double[] freeX = new double[size];
        freeX[0] = 1;
        freeX[size - 1] = 0;
        for (int time = 1; time < size - 1; time++) {
            freeX[time] = solX[time] / dt;
        }
        return TridiagonalSolver.solve(matrixX, freeX);
    }

    @Override
    public double[] stepT(double[] solT, double[] solX) {
        double[][] matrixT = new double[size][];
        matrixT[0] = new double[]{1, 0};
        matrixT[size - 1] = new double[]{-1, 1};
        for (int time = 1; time < size - 1; time++) {
            matrixT[time] = new double[]{
                    -lambda / (dz * dz),
                    ro * c / dt + 2 * lambda / (dz * dz) + ro * Q * WMagic(solX[time], solT[time]),
                    -lambda / (dz * dz)
            };
        }

        double[] freeT = new double[size];
        freeT[0] = Tm;
        freeT[size - 1] = 0;
        for (int i = 1; i < size - 1; i++) {
            freeT[i] = ro * c / dt * solT[i];
        }

        return TridiagonalSolver.solve(matrixT, freeT);
    }
}