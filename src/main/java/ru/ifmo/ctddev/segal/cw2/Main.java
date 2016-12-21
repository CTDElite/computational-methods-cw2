package ru.ifmo.ctddev.segal.cw2;

import ru.ifmo.ctddev.segal.cw2.solvers.ConstantsWrapper;
import ru.ifmo.ctddev.segal.cw2.solvers.SolutionStep;
import ru.ifmo.ctddev.segal.cw2.solvers.Solver;
import ru.ifmo.ctddev.segal.cw2.solvers.SomeSolver;
import ru.ifmo.ctddev.segal.cw2.ui.MainUI;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Main {
    private static class MockSolver extends Solver {
        public MockSolver(double dt, double dz) {
            super(dt, dz);
        }

        public MockSolver(ConstantsWrapper other) {
            super(other);
        }

        public SolutionStep step(SolutionStep prev) {
            return null;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("a plot panel");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        double dt = 0.1;
        double dz = 0.00001;
        List<Function<ConstantsWrapper, ? extends Solver>> solverGenerators =
                Arrays.asList(SomeSolver::new);
        List<String> names = Arrays.asList("Some Solver");
        frame.setContentPane(new MainUI(new ConstantsWrapper(dt, dz), solverGenerators, names).mainPanel);
        frame.setVisible(true);
    }
}
