package org.ruxlsr.view;

import org.ruxlsr.view.admin.AdminFrame;
import org.ruxlsr.view.eleve.EleveFrame;
import org.ruxlsr.view.enseignant.EnseignantFrame;

import javax.swing.*;

public class MainLauncher {
    public static void main(String[] args) {
        String[] roles = {"ADMINISTRATEUR", "ENSEIGNANT", "ELEVE"};
        String choix = (String) JOptionPane.showInputDialog(null, "Choisir un rôle à simuler :", "Launcher",
                JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);

        if (choix == null) return;

        switch (choix) {
            case "ADMINISTRATEUR" -> new AdminFrame().setVisible(true);
            case "ENSEIGNANT" -> {
                String id = JOptionPane.showInputDialog("ID de l'enseignant :");
                new EnseignantFrame(Integer.parseInt(id)).setVisible(true);
            }
            case "ELEVE" -> {
                String id = JOptionPane.showInputDialog("ID de l'élève :");
                new EleveFrame(Integer.parseInt(id)).setVisible(true);
            }
        }
    }
}
