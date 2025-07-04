package org.ruxlsr.view.admin;

import org.ruxlsr.model.Classe;
import org.ruxlsr.model.Niveau;
import org.ruxlsr.service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelClasses extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private AdminService service = new AdminService();
    private JComboBox<Niveau> niveauBox; // Ajouté comme attribut

    public PanelClasses() {
        setLayout(new BorderLayout());

        JPanel form = new JPanel();
        JTextField nomField = new JTextField(10);
        niveauBox = new JComboBox<>(); // Utilise l'attribut
        JButton ajouter = new JButton("Ajouter"), refresh = new JButton("Rafraîchir"),
                supprimer = new JButton("Supprimer");

        form.add(new JLabel("Classe :"));
        form.add(nomField);
        form.add(new JLabel("Niveau :"));
        form.add(niveauBox);
        form.add(ajouter);
        form.add(refresh);
        form.add(supprimer);
        add(form, BorderLayout.NORTH);

        rafraichirNiveaux(); // Chargement initial

        model = new DefaultTableModel(new String[] { "ID", "Nom", "Niveau" }, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 1;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        ajouter.addActionListener(e -> {
            try {
                if (nomField.getText().isBlank()) {
                    JOptionPane.showMessageDialog(this, "entrée incorrecte");
                    return;
                }
                Niveau niveau = (Niveau) niveauBox.getSelectedItem();
                assert niveau != null;
                service.creerClasse(nomField.getText(), niveau.getId());
                nomField.setText("");
                chargerTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        refresh.addActionListener(e -> {
            chargerTable();
            rafraichirNiveaux();
        });

        supprimer.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("ID de la classe à supprimer :");
            if (input != null) {
                try {
                    service.supprimerClasseParId(Integer.parseInt(input));
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
                service.modifierNomClasse(id, nom);
            }
        });

        chargerTable();
    }

    private void rafraichirNiveaux() {
        niveauBox.removeAllItems();
        try {
            for (Niveau n : service.listerNiveaux()) {
                niveauBox.addItem(n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chargerTable() {
        model.setRowCount(0);
        try {
            List<Classe> list = service.listerClasses();
            for (Classe c : list) {
                String niveauNom = service.getNomNiveauById(c.getNiveauId());
                model.addRow(new Object[] { c.getId(), c.getNom(), niveauNom });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}