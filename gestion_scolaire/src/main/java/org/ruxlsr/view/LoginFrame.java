package org.ruxlsr.view;

import org.ruxlsr.dao.UtilisateurDAO;
import org.ruxlsr.model.Utilisateur;
import org.ruxlsr.view.eleve.EleveFrame;
import org.ruxlsr.view.enseignant.EnseignantFrame;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField identifiantField;
    private JPasswordField motDePasseField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Connexion");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        identifiantField = new JTextField();
        motDePasseField = new JPasswordField();
        loginButton = new JButton("Se connecter");

        panel.add(new JLabel("Identifiant :"));
        panel.add(identifiantField);
        panel.add(new JLabel("Mot de passe :"));
        panel.add(motDePasseField);
        panel.add(new JLabel());
        panel.add(loginButton);

        add(panel);

        loginButton.addActionListener(e -> {
            String id = identifiantField.getText();
            String mdp = new String(motDePasseField.getPassword());
            UtilisateurDAO dao = new UtilisateurDAO();
            Utilisateur user = dao.login(id, mdp);

            if (user == null) {
                JOptionPane.showMessageDialog(this, "Identifiants invalides", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                this.dispose();
                switch (user.getRole()) {
                    case "ELEVE" -> new EleveFrame(user.getEleveId()).setVisible(true);
                    case "ENSEIGNANT" -> new EnseignantFrame(user.getEnseignantId()).setVisible(true);
                    case "ADMINISTRATEUR" -> JOptionPane.showMessageDialog(null, "Interface admin non implémentée.");
                    default -> JOptionPane.showMessageDialog(null, "Rôle inconnu.");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
