package org.ruxlsr.view.admin;

import org.ruxlsr.model.Cours;
import org.ruxlsr.service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelCours extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private AdminService service = new AdminService();

    public PanelCours() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel();
        JTextField nom = new JTextField(10);
        JTextField coef = new JTextField(5);
        JTextField idEns = new JTextField(5);
        JButton ajouter = new JButton("Ajouter"), refresh = new JButton("Rafraîchir");

        form.add(new JLabel("Nom :")); form.add(nom);
        form.add(new JLabel("Coef :")); form.add(coef);
        form.add(new JLabel("ID Enseignant :")); form.add(idEns);
        form.add(ajouter); form.add(refresh);
        add(form, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Nom", "Coef", "Enseignant"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        ajouter.addActionListener(e -> {
            try {
                service.creerCours(nom.getText(), Integer.parseInt(coef.getText()), Integer.parseInt(idEns.getText()));
                nom.setText(""); coef.setText(""); idEns.setText("");
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
            List<Cours> list = service.listerCours();
            for (Cours c : list) {
                model.addRow(new Object[]{c.getId(), c.getNom(), c.getCoefficient(), c.getEnseignantId()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
