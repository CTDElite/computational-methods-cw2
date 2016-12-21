package ru.ifmo.ctddev.segal.cw2;

import ru.ifmo.ctddev.segal.cw2.solvers.ConstantsWrapper;
import ru.ifmo.ctddev.segal.cw2.solvers.Solver;
import ru.ifmo.ctddev.segal.cw2.ui.MainUI;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static class MockSolver extends Solver {
        public MockSolver(double dt, double dz) {
            super(dt, dz);
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
        JFrame frame = new JFrame("a plot panel");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        double dt = 0.01;
        double dz = 0.00001;
        List<? extends Solver> solvers = Arrays.asList(new MockSolver(dt, dz), new MockSolver(dt, dz));
        frame.setContentPane(new MainUI(new ConstantsWrapper(dt, dz), solvers).mainPanel);
        frame.setVisible(true);
    }
}
