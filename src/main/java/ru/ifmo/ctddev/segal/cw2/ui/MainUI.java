package ru.ifmo.ctddev.segal.cw2.ui;

import org.math.plot.Plot2DPanel;
import ru.ifmo.ctddev.segal.cw2.solvers.ConstantsWrapper;

import javax.swing.*;
import java.awt.*;

public class MainUI {
    public JPanel mainPanel;
    private JPanel constantsPanel;
    private JTextField dzTextField;
    private JTextField dtTextField;
    private JLabel hValueLabel;
    private JButton submitButton;
    private JPanel graphicPanel;
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
    private JPanel textFieldsPanel;

    private static String formatDouble(double d) {
        return String.format("%.2f", d);
    }

    public MainUI(ConstantsWrapper constantsWrapper) {
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
    }

    private void createUIComponents() {
        Plot2DPanel plot2DPanel = new Plot2DPanel();
        double[] xStart = {1, 2, 3, 4, 5, 6, 7};
        double[] xEnd = {2, 3, 4, 5, 6, 7, 8};
        double[] y = {1, 4, 9, 16, 25, 36, 49};

        int MAX = 200;

        // add a line plot to the PlotPanel
        for (int i = 0; i < MAX; i++) {
            double[] x = new double[xStart.length];
            for (int j = 0; j < x.length; j++) {
                x[j] = (xStart[j] * (MAX - i) + xEnd[j] * i) / MAX;
            }
            plot2DPanel.addLinePlot("old plot", new Color(255, 0, 0, 20), x, y);
        }
        plot2DPanel.addLinePlot("my plot", new Color(255, 0, 0), xEnd, y);
        graphicPanel = plot2DPanel;
    }
}
