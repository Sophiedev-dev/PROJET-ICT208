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
    private final DefaultTableModel model;
    private final AdminService service = new AdminService();
    private JComboBox<Classe> classeBox; // Déclaré comme attribut

    public PanelEleves() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel();
        JTextField nom = new JTextField(10);
        classeBox = new JComboBox<>();
        JButton ajouter = new JButton("Ajouter"), refresh = new JButton("Rafraîchir"),
                supprimer = new JButton("Supprimer");

        form.add(new JLabel("Nom :"));
        form.add(nom);
        form.add(new JLabel("Classe :"));

        rafraichirClasses(); // Chargement initial

        form.add(classeBox);
        form.add(ajouter);
        form.add(refresh);
        form.add(supprimer);
        add(form, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] { "ID", "Nom", "Classe ID", "Anonymat" }, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 1;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Ajout du MouseListener pour double-clic sur la colonne "Classe ID"
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (evt.getClickCount() == 2 && col == 2 && row != -1) { // colonne "Classe ID"
                    int eleveId = (int) model.getValueAt(row, 0);

                    // Création d'une liste déroulante avec les classes
                    JComboBox<Classe> combo = new JComboBox<>();
                    try {
                        for (Classe c : service.listerClasses()) {
                            combo.addItem(c);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Sélectionne la classe actuelle
                    int currentClasseId = Integer.parseInt(model.getValueAt(row, 2).toString());
                    for (int i = 0; i < combo.getItemCount(); i++) {
                        if (combo.getItemAt(i).getId() == currentClasseId) {
                            combo.setSelectedIndex(i);
                            break;
                        }
                    }

                    int result = JOptionPane.showConfirmDialog(
                            PanelEleves.this,
                            combo,
                            "Sélectionnez la nouvelle classe",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        Classe selectedClasse = (Classe) combo.getSelectedItem();
                        if (selectedClasse != null) {
                            // Mise à jour côté service et table
                            String nomEleve = (String) model.getValueAt(row, 1);
                            service.modifierEleve(eleveId, nomEleve, selectedClasse.getId());
                            model.setValueAt(selectedClasse.getId(), row, 2);
                        }
                    }
                }
            }
        });

        ajouter.addActionListener(e -> {
            try {
                if (nom.getText().isBlank()) {
                    JOptionPane.showMessageDialog(this, "entrée incorrecte");
                    return;
                }
                Classe selected = (Classe) classeBox.getSelectedItem();
                service.creerEleveAvecAnonymat(nom.getText(), selected.getId());
                nom.setText("");
                chargerTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        refresh.addActionListener(e -> {
            chargerTable();
            rafraichirClasses();
        });

        supprimer.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("ID de l'élève à supprimer :");
            if (input != null) {
                try {
                    service.supprimerEleveParId(Integer.parseInt(input));
                    JOptionPane.showMessageDialog(this, "insertion reussie");
                    chargerTable();
                } catch (Exception ex) {
                    // JOptionPane.showMessageDialog(this, ex.getMessage());
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

    private void rafraichirClasses() {
        classeBox.removeAllItems();
        try {
            for (Classe c : service.listerClasses()) {
                classeBox.addItem(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chargerTable() {
        model.setRowCount(0);
        try {
            List<Eleve> list = service.listerEleves();
            for (Eleve e : list) {
                model.addRow(new Object[] { e.getId(), e.getNom(), e.getClasseId(), e.getIdAnonymat() });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
