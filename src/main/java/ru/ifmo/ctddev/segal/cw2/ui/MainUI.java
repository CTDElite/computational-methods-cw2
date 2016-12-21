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
    private int previousTPlotId = -1;
    private Map<Integer, Plot> plotMap = new HashMap<>();

    private static String formatDouble(double d) {
        if (d < 1e-3) {
            return String.valueOf(d);
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
            ConstantsWrapper cw = getConstantsWrapper();
            Solver solver = current.apply(cw);
            Solution solution = solver.solve();
            double[] x = new double[solver.size];
            Arrays.setAll(x, i -> i * cw.dz);
            Plot2DPanel xPlot = (Plot2DPanel) xGraphicPanel;
            Plot2DPanel tPlot = (Plot2DPanel) tGraphicPanel;
            xPlot.removeAllPlots();
            tPlot.removeAllPlots();
            plotMap.clear();
            for (int i = 0; i < solver.sizeTime; i += 10) {
                int plotId = tPlot.addLinePlot("T old plot", new Color(0, 0, 0, 20), x, solution.solT[i]);
                plotMap.put(i, tPlot.getPlot(plotId));
            }
            cachedSolution = Optional.of(solution);
            cachedX = Optional.of(x);
            timeSlider.setMaximum(solver.sizeTime - 1);
        });

        timeSlider.addChangeListener(e -> {
            int time = timeSlider.getValue();
            System.out.println(time);
            Plot2DPanel xPlot = (Plot2DPanel) xGraphicPanel;
            Plot2DPanel tPlot = (Plot2DPanel) tGraphicPanel;
            xPlot.removeAllPlots();
            plotMap.get(time / 10 * 10).setColor(Color.RED);
            previousTPlotId = tPlot.addLinePlot("T plot", Color.RED, cachedX.get(), cachedSolution.get().solT[time]);
        });
    }

    private void createUIComponents() {
        tGraphicPanel = new Plot2DPanel();
        xGraphicPanel = new Plot2DPanel();
    }

    private ConstantsWrapper getConstantsWrapper() {
        double dz = Double.parseDouble(dzTextField.getText());
        double dt = Double.parseDouble(dtTextField.getText());
        return new ConstantsWrapper(dt, dz);
    }
}
