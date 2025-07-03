package org.ruxlsr.view.eleve;

import org.ruxlsr.service.EleveService;
import org.ruxlsr.utils.DatabaseConnection;
import org.ruxlsr.view.JTextAreaOutputStream;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class EleveFrame extends JFrame {

    private JComboBox<String> trimestreCombo;
    private JButton afficherButton;
    private JTable table;
    private JLabel moyenneLabel, mentionLabel, rangLabel;
    private DefaultTableModel tableModel;

    private final int eleveId;

    public EleveFrame(int eleveId) {
        this.eleveId = eleveId;

        String nomEleve = getNomEleve();
        setTitle("Bulletin de " + nomEleve);
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new FlowLayout());

        trimestreCombo = new JComboBox<>(new String[]{"1", "2", "3"});
        afficherButton = new JButton("Afficher bulletin");

        topPanel.add(new JLabel("Trimestre :"));
        topPanel.add(trimestreCombo);
        topPanel.add(afficherButton);

        add(topPanel, BorderLayout.NORTH);

        // Tableau
        String[] columns = {"Matière", "Coefficient", "Note CC", "Note Examen", "Moyenne"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Pied de page
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3));
        moyenneLabel = new JLabel("Moyenne : ");
        mentionLabel = new JLabel("Mention : ");
        rangLabel = new JLabel("Rang : ");
        bottomPanel.add(moyenneLabel);
        bottomPanel.add(mentionLabel);
        bottomPanel.add(rangLabel);

        add(bottomPanel, BorderLayout.SOUTH);

        afficherButton.addActionListener(e -> afficherBulletin());
    }

    private void afficherBulletin() {
        int trimestre = trimestreCombo.getSelectedIndex() + 1;
        try (Connection conn = DatabaseConnection.getConnection()) {

            // Effacer le tableau
            tableModel.setRowCount(0);

            String sql = """
                SELECT c.nom AS cours, c.coefficient, n.note_cc, n.note_examen, n.moyenne
                FROM notes n
                JOIN cours c ON c.id = n.cours_id
                WHERE n.eleve_id = ? AND n.trimestre = ?
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, eleveId);
            stmt.setInt(2, trimestre);
            ResultSet rs = stmt.executeQuery();

            float totalCoef = 0;
            float totalPonderee = 0;

            while (rs.next()) {
                String cours = rs.getString("cours");
                int coef = rs.getInt("coefficient");
                float cc = rs.getFloat("note_cc");
                float examen = rs.getFloat("note_examen");
                float moy = rs.getFloat("moyenne");

                tableModel.addRow(new Object[]{cours, coef, cc, examen, moy});
                totalCoef += coef;
                totalPonderee += moy * coef;
            }

            float moyenne = totalCoef > 0 ? totalPonderee / totalCoef : 0;
            moyenneLabel.setText("Moyenne : " + String.format("%.2f", moyenne));
            mentionLabel.setText("Mention : " + getMention(moyenne));
            rangLabel.setText("Rang : " + getRang(trimestre));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    private String getNomEleve() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT nom FROM eleves WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, eleveId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("nom");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Élève inconnu";
    }

    private String getMention(float moyenne) {
        if (moyenne >= 16) return "Très Bien";
        if (moyenne >= 14) return "Bien";
        if (moyenne >= 12) return "Assez Bien";
        if (moyenne >= 10) return "Passable";
        return "Insuffisant";
    }

    private String getRang(int trimestre) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = """
                SELECT e.id, SUM(n.moyenne * c.coefficient) / SUM(c.coefficient) AS moy
                FROM notes n
                JOIN eleves e ON e.id = n.eleve_id
                JOIN cours c ON c.id = n.cours_id
                WHERE n.trimestre = ? AND e.classe_id = (
                    SELECT classe_id FROM eleves WHERE id = ?
                )
                GROUP BY e.id
                ORDER BY moy DESC
            """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, trimestre);
            stmt.setInt(2, eleveId);
            ResultSet rs = stmt.executeQuery();
            int rang = 1;
            while (rs.next()) {
                if (rs.getInt("id") == eleveId) return String.valueOf(rang);
                rang++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "-";
    }
}

