package org.ruxlsr.view.admin;

import org.ruxlsr.model.Enseignant;
import org.ruxlsr.service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelEnseignants extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private AdminService service = new AdminService();

    public PanelEnseignants() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel();
        JTextField nom = new JTextField(20);
        JButton ajouter = new JButton("Ajouter"), refresh = new JButton("Rafraîchir");

        form.add(new JLabel("Nom :")); form.add(nom);
        form.add(ajouter); form.add(refresh);
        add(form, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Nom"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        ajouter.addActionListener(e -> {
            try {
                service.creerEnseignant(nom.getText());
                nom.setText("");
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
            List<Enseignant> list = service.listerEnseignants();
            for (Enseignant e : list) {
                model.addRow(new Object[]{e.getId(), e.getNom()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

