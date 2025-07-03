package org.ruxlsr.view.enseignant;

import javax.swing.*;
import java.awt.*;

public class EnseignantFrame extends JFrame {
    private int enseignantId;

    public EnseignantFrame(int enseignantId) {
        this.enseignantId = enseignantId;

        setTitle("Interface Enseignant");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("Mes cours & classes", new PanelCoursClasses(enseignantId));
        tabbedPane.add("Saisie CC", new PanelSaisieCC(enseignantId));
        tabbedPane.add("Saisie Examen", new PanelSaisieExamen(enseignantId));
        tabbedPane.add("Voir notes élèves", new PanelVoirNotes(enseignantId));

        add(tabbedPane);
    }
}
