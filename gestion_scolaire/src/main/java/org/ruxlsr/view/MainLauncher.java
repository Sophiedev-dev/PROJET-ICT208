package org.ruxlsr.view;

import org.ruxlsr.model.Utilisateur;
import org.ruxlsr.service.AuthService;
import org.ruxlsr.view.admin.AdminFrame;
import org.ruxlsr.view.eleve.EleveFrame;
import org.ruxlsr.view.enseignant.EnseignantFrame;

import javax.swing.*;
import java.awt.*;

public class MainLauncher {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Gestion Scolaire");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 150);
            frame.setLocationRelativeTo(null);

            JButton btnConnexion = new JButton("Se connecter");
            btnConnexion.addActionListener(e -> lancerConnexion(frame));

            frame.setLayout(new GridBagLayout());
            frame.add(btnConnexion);
            frame.setVisible(true);
        });
    }

    private static void lancerConnexion(Component parent) {
        AuthService auth = new AuthService();
        String[] roles = { "ADMINISTRATEUR", "ENSEIGNANT", "ELEVE" };
        String choix = (String) JOptionPane.showInputDialog(parent, "Choisir un rôle à simuler :", "Launcher",
                JOptionPane.QUESTION_MESSAGE, null, roles, roles[0]);

        if (choix == null)
            return;

        switch (choix) {
            case "ADMINISTRATEUR" -> {
                String identifiant = JOptionPane.showInputDialog(parent, "Identifiant :");
                String mdp = JOptionPane.showInputDialog(parent, "Mot de passe :");
                Utilisateur user = auth.authentifier(identifiant, mdp);
                if (user != null && "ADMINISTRATEUR".equals(user.getRole())) {
                    new AdminFrame().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(parent, "Identifiants invalides.");
                }
            }

            case "ENSEIGNANT" -> {
                String id = JOptionPane.showInputDialog(parent, "ID de l'enseignant :");
                String mdp = JOptionPane.showInputDialog(parent, "Mot de passe :");
                Utilisateur user = auth.authentifierParIdEnseignant(Integer.parseInt(id), mdp);
                if (user != null && "ENSEIGNANT".equals(user.getRole())) {
                    new EnseignantFrame(user.getEnseignantId()).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(parent, "Identifiants invalides.");
                }
            }

            case "ELEVE" -> {
                String id = JOptionPane.showInputDialog(parent, "ID de l'élève :");
                new EleveFrame(Integer.parseInt(id)).setVisible(true);
            }
        }
    }
}
