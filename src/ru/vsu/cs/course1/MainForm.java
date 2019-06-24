package ru.vsu.cs.course1;

import javax.swing.*;
import java.awt.*;

public class MainForm {
    private JPanel rootPanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Task 8_15 by @kalitkin_a_v");
        frame.setContentPane(new MainForm().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(512, 512);
        frame.setVisible(true);
    }

    private MainForm() {
        JLabel label = new JLabel();
        Plane plane = new Plane();
        label.setIcon(new Editor(plane, label, rootPanel));
        rootPanel.setLayout(new GridLayout());
        rootPanel.add(label);
    }
}
