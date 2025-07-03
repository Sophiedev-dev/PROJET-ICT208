package org.ruxlsr.view.enseignant;

import org.ruxlsr.service.EnseignantService;
import org.ruxlsr.view.JTextAreaOutputStream;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

public class PanelCoursClasses extends JPanel {
    public PanelCoursClasses(int enseignantId) {
        setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);

        JButton refreshButton = new JButton("Rafraîchir");
        refreshButton.addActionListener(e -> {
            area.setText("");
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(new JTextAreaOutputStream(area)));
            new EnseignantService().afficherCoursEtClasses(enseignantId);
            System.setOut(originalOut);
        });

        add(refreshButton, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }
}
