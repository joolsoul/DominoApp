package ru.vsu.cs.joolsoul.view;

import javax.swing.*;

public class MainWindow extends JFrame {

    private final int WIDTH = 1500;
    private final int HEIGHT = 999;

    public MainWindow() {
        setTitle("Domino");
        setSize(WIDTH, HEIGHT);
        ImageIcon icon = new ImageIcon("resources/appImage.jpg");
        setIconImage(icon.getImage());
        add(new MainPanel());
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

}
