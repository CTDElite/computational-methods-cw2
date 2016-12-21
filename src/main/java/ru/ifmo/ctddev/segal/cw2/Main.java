package ru.ifmo.ctddev.segal.cw2;

import ru.ifmo.ctddev.segal.cw2.solvers.ConstantsWrapper;
import ru.ifmo.ctddev.segal.cw2.ui.MainUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("a plot panel");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(new MainUI(new ConstantsWrapper(0.13, 0.26)).mainPanel);
        frame.setVisible(true);
    }
}
