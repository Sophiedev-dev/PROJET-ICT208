package org.ruxlsr.view.admin;

import org.ruxlsr.model.Niveau;
import org.ruxlsr.service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelNiveaux extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private AdminService service = new AdminService();

    public PanelNiveaux() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel();
        JTextField niveauField = new JTextField(20);
        JButton ajouter = new JButton("Ajouter"), refresh = new JButton("Rafraîchir"), supprimer = new JButton("Supprimer");

        form.add(new JLabel("Niveau :")); form.add(niveauField);
        form.add(ajouter); form.add(refresh); form.add(supprimer);
        add(form, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Nom"}, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 1;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        ajouter.addActionListener(e -> {
            try {
                service.creerNiveau(niveauField.getText());
                niveauField.setText("");
                chargerTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
        refresh.addActionListener(e -> chargerTable());

        supprimer.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Entrer l'ID du niveau à supprimer :");
            if (input != null) {
                try {
                    service.supprimerNiveauParIdCascade(Integer.parseInt(input));
                    chargerTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        table.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 1) {
                int row = e.getFirstRow();
                int id = (int) model.getValueAt(row, 0);
                String nom = (String) model.getValueAt(row, 1);
                service.modifierNomNiveau(id, nom);
            }
        });

        chargerTable();
    }

    private void chargerTable() {
        model.setRowCount(0);
        try {
            List<Niveau> list = service.listerNiveaux();
            for (Niveau n : list) {
                model.addRow(new Object[]{n.getId(), n.getNom()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}