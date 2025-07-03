package org.ruxlsr.view.enseignant;

import org.ruxlsr.service.EnseignantService;

import javax.swing.*;
import java.awt.*;

public class PanelSaisieExamen extends JPanel {
    public PanelSaisieExamen(int enseignantId) {
        setLayout(new GridLayout(6, 2, 10, 10));

        JTextField anonymatField = new JTextField();
        JTextField coursIdField = new JTextField();
        JTextField trimestreField = new JTextField();
        JTextField noteField = new JTextField();

        JButton valider = new JButton("Enregistrer");

        add(new JLabel("Anonymat élève :")); add(anonymatField);
        add(new JLabel("ID Cours :")); add(coursIdField);
        add(new JLabel("Trimestre (1-3) :")); add(trimestreField);
        add(new JLabel("Note examen :")); add(noteField);
        add(new JLabel()); add(valider);

        valider.addActionListener(e -> {
            try {
                String anonymat = anonymatField.getText();
                int coursId = Integer.parseInt(coursIdField.getText());
                int trimestre = Integer.parseInt(trimestreField.getText());
                float note = Float.parseFloat(noteField.getText());

                new EnseignantService().saisirNoteExamen(anonymat, coursId, trimestre, note);
                JOptionPane.showMessageDialog(this, "Note examen enregistrée.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            }
        });
    }
}
