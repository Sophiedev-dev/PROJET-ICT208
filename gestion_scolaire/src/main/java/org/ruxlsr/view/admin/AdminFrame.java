package org.ruxlsr.view.admin;

import javax.swing.*;

public class AdminFrame extends JFrame {

    public AdminFrame() {
        setTitle("Interface Administrateur");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Niveaux", new PanelNiveaux());
        tabs.add("Classes", new PanelClasses());
        tabs.add("Enseignants", new PanelEnseignants());
        tabs.add("Cours", new PanelCours());
        tabs.add("Élèves", new PanelEleves());
        tabs.add("Résultats", new PanelResultatsAdmin());

        add(tabs);
    }
}
