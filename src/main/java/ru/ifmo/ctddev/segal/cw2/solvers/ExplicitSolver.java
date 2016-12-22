package ru.ifmo.ctddev.segal.cw2.solvers;

public class ExplicitSolver extends Solver {
    public ExplicitSolver(double dt, double dz, double h, double maxTime, double k, double e, double alpha) {
        super(dt, dz, h, maxTime, k, e, alpha);
    }

    public ExplicitSolver(double dt, double dz, double h, double maxTime, double k, double e) {
        super(dt, dz, h, maxTime, k, e);
    }

    public ExplicitSolver(double dt, double dz, double h, double maxTime) {
        super(dt, dz, h, maxTime);
    }

    public ExplicitSolver(double dt, double dz) {
        super(dt, dz);
    }

    public ExplicitSolver(ConstantsWrapper other) {
        super(other);
    }

    @Override
    public double[] stepT(double[] solT, double[] solX) {
        double[] answer = new double[solT.length];
        answer[0] = Tm;
        for (int i = 1; i < answer.length - 1; i++) {
            answer[i] = (lambda * (solT[i + 1] - 2 * solT[i] + solT[i -1]) / dz / dz - ro * Q * W(solX[i], solT[i])) * dt / ro / c + solT[i];
        }
        answer[answer.length - 1] = answer[answer.length - 2];
        return answer;
    }

    @Override
    public double[] stepX(double[] solT, double[] solX) {
        double[] answer = new double[solX.length];
        answer[0] = 1;
        for (int i = 1; i < answer.length - 1; i++) {
            answer[i] = (D * (solX[i + 1] - 2 * solX[i] + solX[i - 1]) / dz / dz + W(solX[i], solT[i])) * dt + solX[i];
        }
        answer[answer.length - 1] = answer[answer.length - 2];
        return answer;
    }
}
