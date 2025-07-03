package org.ruxlsr.view.enseignant;

import org.ruxlsr.service.EnseignantService;

import javax.swing.*;
import java.awt.*;

public class PanelSaisieCC extends JPanel {
    public PanelSaisieCC(int enseignantId) {
        setLayout(new GridLayout(6, 2, 10, 10));

        JTextField eleveIdField = new JTextField();
        JTextField coursIdField = new JTextField();
        JTextField trimestreField = new JTextField();
        JTextField noteField = new JTextField();

        JButton valider = new JButton("Enregistrer");

        add(new JLabel("ID Élève :")); add(eleveIdField);
        add(new JLabel("ID Cours :")); add(coursIdField);
        add(new JLabel("Trimestre (1-3) :")); add(trimestreField);
        add(new JLabel("Note CC :")); add(noteField);
        add(new JLabel()); add(valider);

        valider.addActionListener(e -> {
            try {
                int eleveId = Integer.parseInt(eleveIdField.getText());
                int coursId = Integer.parseInt(coursIdField.getText());
                int trimestre = Integer.parseInt(trimestreField.getText());
                float note = Float.parseFloat(noteField.getText());

                new EnseignantService().saisirNoteCC(eleveId, coursId, trimestre, note);
                JOptionPane.showMessageDialog(this, "Note enregistrée.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            }
        });
    }
}