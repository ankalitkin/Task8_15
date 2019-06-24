package ru.vsu.cs.course1;

import javax.swing.*;
import java.awt.*;

public class MainForm {
    private JPanel drawPanel;
    private JSpinner maxSpinner;
    private JPanel rootPanel;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        JFrame frame = new JFrame("Task 8_15 by @kalitkin_a_v");
        frame.setContentPane(new MainForm().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(512, 512);
        frame.setVisible(true);
    }

    private MainForm() {
        maxSpinner.setModel(new SpinnerNumberModel(0, 0, 10, 1));
        JLabel label = new JLabel();
        Plane plane = new Plane();
        label.setIcon(new Editor(plane, label, drawPanel, maxSpinner));
        drawPanel.setLayout(new GridLayout());
        drawPanel.add(label);
    }
}
