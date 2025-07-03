package org.ruxlsr.view.enseignant;

import org.ruxlsr.service.EnseignantService;
import org.ruxlsr.utils.DatabaseConnection;
import org.ruxlsr.view.JTextAreaOutputStream;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.PrintStream;
import java.sql.*;

public class PanelCoursClasses extends JPanel {
    private final EnseignantService service = new EnseignantService();
    private final JTable table = new JTable();
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID Cours", "Nom Cours", "Classe"}, 0);

    public PanelCoursClasses(int enseignantId) {
        setLayout(new BorderLayout());

        table.setModel(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Rafraîchir");
        refreshBtn.addActionListener(e -> chargerDonnees(enseignantId));
        add(refreshBtn, BorderLayout.NORTH);

        chargerDonnees(enseignantId);
    }

    private void chargerDonnees(int enseignantId) {
        model.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = """
                SELECT cours.id, cours.nom AS cours_nom, classes.nom AS classe_nom
                FROM cours
                JOIN cours_classes ON cours.id = cours_classes.cours_id
                JOIN classes ON cours_classes.classe_id = classes.id
                WHERE cours.enseignant_id = ?
            """;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, enseignantId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    model.addRow(new Object[]{rs.getInt("id"), rs.getString("cours_nom"), rs.getString("classe_nom")});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
