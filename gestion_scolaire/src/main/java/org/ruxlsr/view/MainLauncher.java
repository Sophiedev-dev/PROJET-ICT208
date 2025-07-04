package org.ruxlsr.view;

import org.ruxlsr.model.Utilisateur;
import org.ruxlsr.service.AuthService;
import org.ruxlsr.view.admin.AdminFrame;
import org.ruxlsr.view.eleve.EleveFrame;
import org.ruxlsr.view.enseignant.EnseignantFrame;

import javax.swing.*;

public class MainLauncher {
    public static void main(String[] args) {
        AuthService auth = new AuthService();
        String[] roles = {"ADMINISTRATEUR", "ENSEIGNANT", "ELEVE"};
        String choix = (String) JOptionPane.showInputDialog(null, "Choisir un rôle à simuler :", "Launcher",
                JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);

        if (choix == null) return;

        switch (choix) {
            case "ADMINISTRATEUR" -> {
                String identifiant = JOptionPane.showInputDialog("Identifiant :");
                String mdp = JOptionPane.showInputDialog("Mot de passe :");
                Utilisateur user = auth.authentifier(identifiant, mdp);
                if (user != null && "ADMINISTRATEUR".equals(user.getRole())) {
                    new AdminFrame().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Identifiants invalides.");
                }
            }

            case "ENSEIGNANT" -> {
                String id = JOptionPane.showInputDialog("ID de l'enseignant :");
                String mdp = JOptionPane.showInputDialog("Mot de passe :");
                Utilisateur user = auth.authentifierParIdEnseignant(Integer.parseInt(id), mdp);
                if (user != null && "ENSEIGNANT".equals(user.getRole())) {
                    new EnseignantFrame(user.getEnseignantId()).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Identifiants invalides.");
                }
            }

            case "ELEVE" -> {
                String id = JOptionPane.showInputDialog("ID de l'élève :");
                new EleveFrame(Integer.parseInt(id)).setVisible(true);
            }
        }
    }
}
