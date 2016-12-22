package ru.ifmo.ctddev.segal.cw2;

import ru.ifmo.ctddev.segal.cw2.solvers.*;
import ru.ifmo.ctddev.segal.cw2.ui.MainUI;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public class Main {
    private static class MockSolver extends Solver {
        public MockSolver(double dt, double dz) {
            super(dt, dz);
        }

        public MockSolver(ConstantsWrapper other) {
            super(other);
        }

        @Override
        public double[] stepT(double[] solT, double[] solX) {
            return null;
        }

        @Override
        public double[] stepX(double[] solT, double[] solX) {
            return null;
        }
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        JFrame frame = new JFrame("a plot panel");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        double dt = 0.01;
        double dz = 0.0001;
        List<Function<ConstantsWrapper, ? extends Solver>> solverGenerators =
                Arrays.asList(SomeSolver::new, StupidSolver::new, ExplicitSolver::new);
        List<String> names = Arrays.asList("Implicit Solver", "Implicit+ Solver", "Explicit Solver");
        frame.setContentPane(new MainUI(new ConstantsWrapper(dt, dz), solverGenerators, names).mainPanel);
        frame.setVisible(true);
    }
}
