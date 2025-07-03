package org.ruxlsr.view.admin;

import org.ruxlsr.model.Classe;
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
        JComboBox<Classe> classeBox = new JComboBox<>();
        JButton ajouter = new JButton("Ajouter"), refresh = new JButton("Rafraîchir"), supprimer = new JButton("Supprimer");

        form.add(new JLabel("Nom :")); form.add(nom);
        form.add(new JLabel("Classe :"));

        try {
            for (Classe c : service.listerClasses()) {
                classeBox.addItem(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        form.add(classeBox);
        form.add(ajouter); form.add(refresh); form.add(supprimer);
        add(form, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Nom", "Classe ID", "Anonymat"}, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 1 || col == 2;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        ajouter.addActionListener(e -> {
            try {
                Classe selected = (Classe) classeBox.getSelectedItem();
                service.creerEleveAvecAnonymat(nom.getText(), selected.getId());
                nom.setText("");
                chargerTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getStackTrace());
            }
        });
        refresh.addActionListener(e -> chargerTable());
        supprimer.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("ID de l'élève à supprimer :");
            if (input != null) {
                try {
                    service.supprimerEleveParId(Integer.parseInt(input));
                    chargerTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getStackTrace());
                }
            }
        });

        table.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int id = (int) model.getValueAt(row, 0);
            String nomEleve = (String) model.getValueAt(row, 1);
            int classeId = Integer.parseInt(model.getValueAt(row, 2).toString());
            service.modifierEleve(id, nomEleve, classeId);
        });

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

