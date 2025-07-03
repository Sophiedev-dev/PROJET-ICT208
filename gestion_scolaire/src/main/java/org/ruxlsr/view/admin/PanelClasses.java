package org.ruxlsr.view.admin;

import org.ruxlsr.model.Classe;
import org.ruxlsr.service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelClasses extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private AdminService service = new AdminService();

    public PanelClasses() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel();
        JTextField nom = new JTextField(10);
        JTextField niveauId = new JTextField(5);
        JButton ajouter = new JButton("Ajouter"), refresh = new JButton("Rafraîchir");

        form.add(new JLabel("Classe :")); form.add(nom);
        form.add(new JLabel("ID Niveau :")); form.add(niveauId);
        form.add(ajouter); form.add(refresh);
        add(form, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Nom", "Niveau"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        ajouter.addActionListener(e -> {
            try {
                service.creerClasse(nom.getText(), Integer.parseInt(niveauId.getText()));
                nom.setText(""); niveauId.setText("");
                chargerTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        refresh.addActionListener(e -> chargerTable());

        chargerTable();
    }

    private void chargerTable() {
        model.setRowCount(0);
        try {
            List<Classe> list = service.listerClasses();
            for (Classe c : list) {
                model.addRow(new Object[]{c.getId(), c.getNom(), c.getNiveauId()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
