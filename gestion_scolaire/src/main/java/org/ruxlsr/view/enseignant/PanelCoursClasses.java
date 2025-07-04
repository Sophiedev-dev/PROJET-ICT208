package org.ruxlsr.view.enseignant;

import org.ruxlsr.service.EnseignantService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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
        service.getCoursClasseNomsById(enseignantId, model);
    }


}
