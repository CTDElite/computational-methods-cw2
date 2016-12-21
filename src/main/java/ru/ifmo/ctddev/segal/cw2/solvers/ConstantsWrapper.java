package ru.ifmo.ctddev.segal.cw2.solvers;

/**
 * Created by Aleksei Latyshev
 */

public class ConstantsWrapper {
    public final double dt;
    public final double dz;
    public final double k;
    public final double E;
    public final double alpha;
    public final double h;
    public final double maxTime;

    public final double R = 8.314;
    public final double Q = 7e5;
    public final double T0 = 293;
    public final double c = 1980;
    public final double ro = 830;
    public final double lambda = 0.13;
    public final double D = 8e-12;

    public final double kappa;
    public final double Tm;
    public final double U;
    public final double sigmaT;
    public final double beta;
    public final double sigmaW;
    public final double sigmaU;


    public ConstantsWrapper(ConstantsWrapper other) {
        this.dt = other.dt;
        this.dz = other.dz;
        this.k = other.k;
        this.E = other.E;
        this.alpha = other.alpha;
        this.h = other.h;
        this.maxTime = other.maxTime;
        this.kappa = other.kappa;
        this.Tm = other.Tm;
        this.U = other.U;
        this.sigmaT = other.sigmaT;
        this.beta = other.beta;
        this.sigmaW = other.sigmaW;
        this.sigmaU = other.sigmaU;
    }

    public ConstantsWrapper(double dt, double dz, double h, double maxTime, double k, double e, double alpha) {
        this.dt = dt;
        this.dz = dz;
        this.h = h;
        this.maxTime = maxTime;
        this.k = k;
        E = e;
        this.alpha = alpha;

        kappa = lambda / (ro * c);
        Tm = T0 + Q / c;
        U = Math.pow((2 * k * lambda) / (Q * ro * (Tm - T0)) * Math.pow(R * Tm * Tm / E, 2) * Math.exp(-E / (R * Tm)), .5);
        sigmaT = kappa / U;
        beta = R * Tm / E;
        sigmaW = sigmaT * beta;
        sigmaU = D / U;
        checkConstraints();
    }

    public ConstantsWrapper(double dt, double dz, double h, double maxTime, double k, double e) {
        this(dt, dz, h, maxTime, k, e, 1.0);
    }

    public ConstantsWrapper(double dt, double dz, double h, double maxTime) {
        this(dt, dz, h, maxTime, 1.6e6, 8e4);
    }

    public ConstantsWrapper(double dt, double dz) {
        this(dt, dz, 0.03, 300);
    }

    public double W (double X, double T) {
        return -k * Math.pow(X, alpha) * Math.exp(-E / (R * T));
    }

    public double WMagic (double X, double T) {
        return -k * Math.pow(X, alpha - 1) * Math.exp(-E / (R * T));
    }

    private static boolean muchLess(double l, double r) {
        return l * 10 < r;
    }

    private static boolean lessAndNearlyEqual(double l, double r) {
        return Math.abs(l - r) < 0.1 && l < r;
    }

    public void checkConstraints() {
        // beta << 1
        if (!muchLess(beta, 1)) {
            System.err.printf("beta (%f) should be much less than 1\n", beta);
        }
        // dx <~ sigmaW
        if (!lessAndNearlyEqual(dz, sigmaW)) {
            System.err.printf("dz (%f) should be less and nearly equal to sigmaW (%f)\n", dz, sigmaW);
        }
        // h >> sigmaT
        if (!muchLess(sigmaT, h)) {
            System.err.printf("h (%f) should be much more than sigmaT (%f)\n", h, sigmaT);
        }
    }
}