package org.ruxlsr.view.admin;

import org.ruxlsr.model.Cours;
import org.ruxlsr.model.Enseignant;
import org.ruxlsr.service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
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
        JComboBox<Enseignant> enseignantBox = new JComboBox<>();
        JButton ajouter = new JButton("Ajouter"), refresh = new JButton("Rafraîchir"), supprimer = new JButton("Supprimer");

        form.add(new JLabel("Nom :")); form.add(nom);
        form.add(new JLabel("Coef :")); form.add(coef);
        form.add(new JLabel("Enseignant :"));

        try {
            for (Enseignant e : service.listerEnseignants()) {
                enseignantBox.addItem(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        form.add(enseignantBox);
        form.add(ajouter); form.add(refresh); form.add(supprimer);
        add(form, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Nom", "Coef", "Enseignant"}, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 1 || col == 2 ;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        ajouter.addActionListener(e -> {
            try {
                Cours c = new Cours(0, nom.getText(), Integer.parseInt(coef.getText()), ((Enseignant) enseignantBox.getSelectedItem()).getId());
                service.creerCours(nom.getText(), Integer.parseInt(coef.getText()), ((Enseignant) enseignantBox.getSelectedItem()).getId());
                nom.setText(""); coef.setText("");
                JOptionPane.showMessageDialog(this, "insertion reussie");
                chargerTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
        refresh.addActionListener(e -> {
            try {
                chargerTable();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        supprimer.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("ID du cours à supprimer :");
            if (input != null) {
                try {
                    service.supprimerCoursParId(Integer.parseInt(input));
                    chargerTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        table.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int id = (int) model.getValueAt(row, 0);
            String nomCours = (String) model.getValueAt(row, 1);
            int coefficient = Integer.parseInt(model.getValueAt(row, 2).toString());
            String enseignantNom = (String) model.getValueAt(row, 3);
            int enseignantId = service.getIdEnseignantByNom(enseignantNom);
            service.modifierCours(id, nomCours, coefficient, enseignantId);
        });

        try {
            chargerTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void chargerTable() throws SQLException {
        model.setRowCount(0);

            List<Cours> list = service.listerCours();
            for (Cours c : list) {
                String enseignantNom = service.getNomEnseignantById(c.getEnseignantId());
                model.addRow(new Object[]{c.getId(), c.getNom(), c.getCoefficient(), enseignantNom});
            }

    }
}

