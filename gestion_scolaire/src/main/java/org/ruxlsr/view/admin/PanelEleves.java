package org.ruxlsr.view.admin;

import org.ruxlsr.model.Eleve;
import org.ruxlsr.service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelEleves extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private AdminService service = new AdminService();

    public PanelEleves() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel();
        JTextField nom = new JTextField(10);
        JTextField classeId = new JTextField(5);
        JButton ajouter = new JButton("Ajouter"), refresh = new JButton("Rafraîchir");

        form.add(new JLabel("Nom :")); form.add(nom);
        form.add(new JLabel("Classe ID :")); form.add(classeId);
        form.add(ajouter); form.add(refresh);
        add(form, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Nom", "Classe", "Anonymat"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        ajouter.addActionListener(e -> {
            try {
                service.creerEleve(nom.getText(), Integer.parseInt(classeId.getText()));
                nom.setText(""); classeId.setText("");
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
            List<Eleve> list = service.listerEleves();
            for (Eleve e : list) {
                model.addRow(new Object[]{e.getId(), e.getNom(), e.getClasseId(), e.getIdAnonymat()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
