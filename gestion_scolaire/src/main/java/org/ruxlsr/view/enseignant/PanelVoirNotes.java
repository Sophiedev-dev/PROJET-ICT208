package org.ruxlsr.view.enseignant;

import org.ruxlsr.dao.NoteDAO;
import org.ruxlsr.view.JTextAreaOutputStream;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

public class PanelVoirNotes extends JPanel {
    public PanelVoirNotes(int enseignantId) {
        setLayout(new GridLayout(6, 2, 10, 10));

        JTextField classeField = new JTextField();
        JTextField coursField = new JTextField();
        JTextField trimestreField = new JTextField();

        JButton afficher = new JButton("Afficher");

        JTextArea result = new JTextArea();
        result.setEditable(false);

        add(new JLabel("ID Classe :")); add(classeField);
        add(new JLabel("ID Cours :")); add(coursField);
        add(new JLabel("Trimestre (1-3) :")); add(trimestreField);
        add(new JLabel()); add(afficher);
        add(new JLabel("Résultats :")); add(new JScrollPane(result));

        afficher.addActionListener(e -> {
            try {
                int classeId = Integer.parseInt(classeField.getText());
                int coursId = Integer.parseInt(coursField.getText());
                int trimestre = Integer.parseInt(trimestreField.getText());

                result.setText("");
                PrintStream out = new PrintStream(new JTextAreaOutputStream(result));
                PrintStream oldOut = System.out;
                System.setOut(out);
                new NoteDAO().afficherNotesParClasseEtCours(classeId, coursId, trimestre);
                System.setOut(oldOut);
            } catch (Exception ex) {
                result.setText("Erreur : " + ex.getMessage());
            }
        });
    }
}