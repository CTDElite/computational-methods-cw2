package ru.ifmo.ctddev.segal.cw2.solvers;

/**
 * Created Aleksei Latyshev
 */

public class SomeSolver extends Solver {

    public SomeSolver(double dt, double dz, double h, double maxTime, double k, double e, double alpha) {
        super(dt, dz, h, maxTime, k, e, alpha);
    }

    public SomeSolver(double dt, double dz, double h, double maxTime, double k, double e) {
        super(dt, dz, h, maxTime, k, e);
    }

    public SomeSolver(double dt, double dz, double h, double maxTime) {
        super(dt, dz, h, maxTime);
    }

    public SomeSolver(double dt, double dz) {
        super(dt, dz);
    }

    public SomeSolver(ConstantsWrapper other) {
        super(other);
    }

    private double[][] getMatrix(double a, double b, double c) {
        double[][] matrix = new double[size][];
        matrix[0] = new double[]{1, 0};
        matrix[size - 1] = new double[]{-1, 1};
        for (int i = 1; i < size - 1; i++) {
            matrix[i] = new double[]{a, b, c};
        }
        return matrix;
    }

    public SolutionStep step(SolutionStep prev) {
        double[][] matrixX = getMatrix(
                -D / (dz * dz),
                1 / dt + 2 * D / (dz * dz),
                -D / (dz * dz)
        );
        double[] freeX = new double[size];
        freeX[0] = 1;
        freeX[size - 1] = 0;
        for (int i = 1; i < size - 1; i++) {
            freeX[i] = W(prev.solX[i], prev.solT[i]) + prev.solX[i] / dt;
        }

        double[][] matrixT = getMatrix(
                -lambda / (dz * dz),
                ro * c / dt + 2 * lambda / (dz * dz),
                -lambda / (dz * dz)
        );
        double[] freeT = new double[size];
        freeT[0] = Tm;
        freeT[size - 1] = 0;
        for (int i = 1; i < size - 1; i++) {
            freeT[i] = -ro * Q * W(prev.solX[i], prev.solT[i]) + ro * c / dt * prev.solT[i];
        }

        return new SolutionStep(TridiagonalSolver.solve(matrixT, freeT), TridiagonalSolver.solve(matrixX, freeX));
    }
}