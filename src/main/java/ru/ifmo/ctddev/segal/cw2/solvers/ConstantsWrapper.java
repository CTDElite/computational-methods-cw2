package ru.ifmo.ctddev.segal.cw2.solvers;

/**
 * Created by Aleksei Latyshev
 */

public abstract class ConstantsWrapper {
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
    }

    public ConstantsWrapper(double dt, double dz, double h, double maxTime, double k, double e) {
        this(dt, dz, h, maxTime, k, e, 1.0);
    }

    public ConstantsWrapper(double dt, double dz, double h, double maxTime) {
        this(dt, dz, h, maxTime, 1.6e6, 8e4);
    }

    public ConstantsWrapper(double dt, double dz) {
        this(dt, dz, 1, 100);
    }
}