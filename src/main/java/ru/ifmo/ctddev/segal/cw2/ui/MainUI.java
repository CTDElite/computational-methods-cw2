package ru.ifmo.ctddev.segal.cw2.ui;

import org.math.plot.Plot2DPanel;
import org.math.plot.plots.Plot;
import ru.ifmo.ctddev.segal.cw2.solvers.ConstantsWrapper;
import ru.ifmo.ctddev.segal.cw2.solvers.Solution;
import ru.ifmo.ctddev.segal.cw2.solvers.Solver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.*;
import java.util.List;
import java.util.function.Function;

public class MainUI {
    public JPanel mainPanel;
    private JPanel constantsPanel;
    private JTextField dzTextField;
    private JTextField dtTextField;
    private JButton submitButton;
    private JPanel tGraphicPanel;
    private JSlider timeSlider;
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
    private JTextField hTextField;
    private JTextField kTextField;
    private JTextField ETextField;
    private JTextField alphaTextField;
    private JPanel textFieldsPanel;
    private Function<ConstantsWrapper, ? extends Solver> current;
    private Optional<double[]> cachedX;
    private Optional<Solution> cachedSolution;

    private static String formatDouble(double d) {
        if (d < 1e-3) {
            return String.format("%.3e", d);
        } else {
            return String.format("%.3f", d);
        }
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
        dtTextField.setText(String.format("%f", constantsWrapper.dt));
        dzTextField.setText(String.format("%f", constantsWrapper.dz));

        hTextField.setText(formatDouble(constantsWrapper.h));
        kTextField.setText(formatDouble(constantsWrapper.k));
        ETextField.setText(formatDouble(constantsWrapper.E));
        alphaTextField.setText(formatDouble(constantsWrapper.alpha));
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
            ConstantsWrapper cw = getConstantsWrapper();
            Solver solver = current.apply(cw);
            Solution solution = solver.solve();
            double[] x = new double[solver.size];
            Arrays.setAll(x, i -> i * cw.dz);
            cachedSolution = Optional.of(solution);
            cachedX = Optional.of(x);
            timeSlider.setMaximum(solver.sizeTime - 1);
        });

        timeSlider.addChangeListener(e -> {
            int time = timeSlider.getValue();
            System.out.println(time);
            Plot2DPanel xPlot = (Plot2DPanel) xGraphicPanel;
            Plot2DPanel tPlot = (Plot2DPanel) tGraphicPanel;
            tPlot.removeAllPlots();
            xPlot.removeAllPlots();
            tPlot.addLinePlot("T plot", Color.RED, cachedX.get(), cachedSolution.get().solT[time]);
            xPlot.addLinePlot("X plot", Color.BLACK, cachedX.get(), cachedSolution.get().solX[time]);
        });
    }

    private void createUIComponents() {
        tGraphicPanel = new Plot2DPanel();
        xGraphicPanel = new Plot2DPanel();
    }

    private ConstantsWrapper getConstantsWrapper() {
        double dz = Double.parseDouble(dzTextField.getText());
        double dt = Double.parseDouble(dtTextField.getText());
        double h = Double.parseDouble(hTextField.getText());
        double k = Double.parseDouble(kTextField.getText());
        double E = Double.parseDouble(ETextField.getText());
        double alpha = Double.parseDouble(alphaTextField.getText());
        return new ConstantsWrapper(dt, dz, h, 407, k, E, alpha);
    }
}
