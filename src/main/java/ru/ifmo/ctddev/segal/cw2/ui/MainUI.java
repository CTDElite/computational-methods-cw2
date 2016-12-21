package ru.ifmo.ctddev.segal.cw2.ui;

import org.math.plot.Plot2DPanel;
import ru.ifmo.ctddev.segal.cw2.solvers.ConstantsWrapper;
import ru.ifmo.ctddev.segal.cw2.solvers.Solution;
import ru.ifmo.ctddev.segal.cw2.solvers.Solver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MainUI {
    public JPanel mainPanel;
    private JPanel constantsPanel;
    private JTextField dzTextField;
    private JTextField dtTextField;
    private JLabel hValueLabel;
    private JButton submitButton;
    private JPanel tGraphicPanel;
    private JSlider timeSlider;
    private JLabel kValueLabel;
    private JLabel EValueLabel;
    private JLabel alphaValueLabel;
    private JLabel RValueLabel;
    private JLabel QValueLabel;
    private JLabel T0ValueLabel;
    private JLabel cValueLabel;
    private JLabel roValueLabel;
    private JLabel lambdaValueLabel;
    private JLabel DValueLabel;
    private JLabel kappaValueLabel;
    private JLabel TmValueLabel;
    private JLabel UValueLabel;
    private JLabel sigmaTValue;
    private JLabel sigmaWValueLabel;
    private JLabel sigmaUValueLabel;
    private JLabel betaValueLabel;
    private JComboBox solverComboBox;
    private JPanel xGraphicPanel;
    private JPanel graphicsPanel;
    private JPanel textFieldsPanel;
    private Function<ConstantsWrapper, ? extends Solver> current;
    private Optional<double[]> cachedX;
    private Optional<Solution> cachedSolution;

    private static String formatDouble(double d) {
        return String.format("%.2f", d);
    }

    private static class PrettySolverGenerator {
        private final Function<ConstantsWrapper, ? extends Solver> solverGenerator;
        private final String solverName;

        private PrettySolverGenerator(Function<ConstantsWrapper, ? extends Solver> solverGenerator, String solverName) {
            this.solverGenerator = solverGenerator;
            this.solverName = solverName;
        }

        @Override
        public String toString() {
            return solverName;
        }
    }

    public MainUI(ConstantsWrapper constantsWrapper,
                  List<? extends Function<ConstantsWrapper, ? extends Solver>> solverGenerators,
                  List<String> solverNames) {
        dtTextField.setText(formatDouble(constantsWrapper.dt));
        dzTextField.setText(formatDouble(constantsWrapper.dz));

        hValueLabel.setText(formatDouble(constantsWrapper.h));
        kValueLabel.setText(formatDouble(constantsWrapper.k));
        EValueLabel.setText(formatDouble(constantsWrapper.E));
        alphaValueLabel.setText(formatDouble(constantsWrapper.alpha));
        RValueLabel.setText(formatDouble(constantsWrapper.R));
        QValueLabel.setText(formatDouble(constantsWrapper.Q));
        T0ValueLabel.setText(formatDouble(constantsWrapper.T0));
        cValueLabel.setText(formatDouble(constantsWrapper.c));
        roValueLabel.setText(formatDouble(constantsWrapper.ro));
        lambdaValueLabel.setText(formatDouble(constantsWrapper.lambda));
        DValueLabel.setText(formatDouble(constantsWrapper.D));
        kappaValueLabel.setText(formatDouble(constantsWrapper.kappa));
        TmValueLabel.setText(formatDouble(constantsWrapper.Tm));
        UValueLabel.setText(formatDouble(constantsWrapper.U));
        sigmaTValue.setText(formatDouble(constantsWrapper.sigmaT));
        betaValueLabel.setText(formatDouble(constantsWrapper.beta));
        sigmaWValueLabel.setText(formatDouble(constantsWrapper.sigmaW));
        sigmaUValueLabel.setText(formatDouble(constantsWrapper.sigmaU));

        for (int i = 0; i < solverGenerators.size(); i++) {
            solverComboBox.addItem(new PrettySolverGenerator(solverGenerators.get(i), solverNames.get(i)));
        }

        current = ((PrettySolverGenerator) solverComboBox.getSelectedItem()).solverGenerator;

        solverComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                current = ((PrettySolverGenerator) e.getItem()).solverGenerator;
                System.out.println(current.getClass().getSimpleName());
            }
        });

        submitButton.addActionListener(e -> {
            Solver solver = current.apply(constantsWrapper);
            Solution solution = solver.solve();
            int time = timeSlider.getValue();
            double[] x = new double[solver.size];
            Arrays.setAll(x, i -> i * constantsWrapper.dz);
            Plot2DPanel xPlot = (Plot2DPanel) xGraphicPanel;
            Plot2DPanel tPlot = (Plot2DPanel) tGraphicPanel;
            xPlot.removeAllPlots();
            tPlot.removeAllPlots();
            xPlot.addLinePlot("X plot", Color.RED, x, solution.solX[time]);
            tPlot.addLinePlot("T plot", Color.BLACK, x, solution.solT[time]);
            cachedSolution = Optional.of(solution);
            cachedX = Optional.of(x);
            timeSlider.setMaximum(solver.sizeTime - 1);
        });

        timeSlider.addChangeListener(e -> {
            int time = timeSlider.getValue();
            Plot2DPanel xPlot = (Plot2DPanel) xGraphicPanel;
            Plot2DPanel tPlot = (Plot2DPanel) tGraphicPanel;
            xPlot.removeAllPlots();
            tPlot.removeAllPlots();
            xPlot.addLinePlot("X plot", Color.RED, cachedX.get(), cachedSolution.get().solX[time]);
            tPlot.addLinePlot("T plot", Color.BLACK, cachedX.get(), cachedSolution.get().solT[time]);
        });
    }

    private void createUIComponents() {
        tGraphicPanel = new Plot2DPanel();
        xGraphicPanel = new Plot2DPanel();
    }
}
