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

    @Override
    public double[] stepT(double[] solT, double[] solX) {
        double[][] matrixT = getMatrix(
                -lambda / (dz * dz),
                ro * c / dt + 2 * lambda / (dz * dz),
                -lambda / (dz * dz)
        );
        double[] freeT = new double[size];
        freeT[0] = Tm;
        freeT[size - 1] = 0;
        for (int i = 1; i < size - 1; i++) {
            freeT[i] = -ro * Q * W(solX[i], solT[i]) + ro * c / dt * solT[i];
        }
        return TridiagonalSolver.solve(matrixT, freeT);
    }

    @Override
    public double[] stepX(double[] solT, double[] solX) {
        double[][] matrixX = getMatrix(
                -D / (dz * dz),
                1 / dt + 2 * D / (dz * dz),
                -D / (dz * dz)
        );
        double[] freeX = new double[size];
        freeX[0] = 1;
        freeX[size - 1] = 0;
        for (int i = 1; i < size - 1; i++) {
            freeX[i] = W(solX[i], solT[i]) + solX[i] / dt;
        }
        return TridiagonalSolver.solve(matrixX, freeX);
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
}