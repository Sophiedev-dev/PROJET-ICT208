package org.ruxlsr.view.admin;

import org.ruxlsr.model.Classe;
import org.ruxlsr.model.Cours;
import org.ruxlsr.model.Enseignant;
import org.ruxlsr.service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PanelCours extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private final AdminService service = new AdminService();
    private JComboBox<Enseignant> enseignantBox;
    private JComboBox<Classe> classeBox;

    public PanelCours() {
        setLayout(new BorderLayout());

        JPanel form = new JPanel();
        JTextField nom = new JTextField(10);
        JTextField coef = new JTextField(5);
        enseignantBox = new JComboBox<>();
        classeBox = new JComboBox<>();

        JButton ajouter = new JButton("Ajouter");
        JButton refresh = new JButton("Rafraîchir");
        JButton supprimer = new JButton("Supprimer");

        // Chargement initial des listes déroulantes
        rafraichirListesDeroulantes();

        // Formulaire
        form.add(new JLabel("Nom :"));
        form.add(nom);
        form.add(new JLabel("Coef :"));
        form.add(coef);
        form.add(new JLabel("Enseignant :"));
        form.add(enseignantBox);
        form.add(new JLabel("Classe :"));
        form.add(classeBox);
        form.add(ajouter);
        form.add(refresh);
        form.add(supprimer);

        add(form, BorderLayout.NORTH);

        // Tableau
        model = new DefaultTableModel(new String[] { "ID", "Nom", "Coef", "Enseignant", "Classe" }, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 1 || col == 2; // nom et coef modifiables
            }
        };
        table = new JTable(model) {
            // Pour empêcher la saisie de texte non entier dans la colonne coefficient
            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                if (column == 2) { // colonne coefficient
                    JTextField field = new JTextField();
                    ((AbstractDocument) field.getDocument()).setDocumentFilter(new javax.swing.text.DocumentFilter() {
                        @Override
                        public void insertString(FilterBypass fb, int offset, String string,
                                javax.swing.text.AttributeSet attr)
                                throws javax.swing.text.BadLocationException {
                            if (string.matches("\\d+")) {
                                super.insertString(fb, offset, string, attr);
                            }
                        }

                        @Override
                        public void replace(FilterBypass fb, int offset, int length, String text,
                                javax.swing.text.AttributeSet attrs)
                                throws javax.swing.text.BadLocationException {
                            if (text.matches("\\d*")) {
                                super.replace(fb, offset, length, text, attrs);
                            }
                        }
                    });
                    return new DefaultCellEditor(field);
                }
                return super.getCellEditor(row, column);
            }
        };
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Double-clic sur la colonne Enseignant pour changer l'enseignant associé
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (evt.getClickCount() == 2 && col == 3 && row != -1) { // colonne "Enseignant"
                    int coursId = (int) model.getValueAt(row, 0);

                    JComboBox<Enseignant> combo = new JComboBox<>();
                    try {
                        for (Enseignant e : service.listerEnseignants()) {
                            combo.addItem(e);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Sélectionne l'enseignant actuel
                    String currentNom = (String) model.getValueAt(row, 3);
                    for (int i = 0; i < combo.getItemCount(); i++) {
                        if (combo.getItemAt(i).toString().equals(currentNom)) {
                            combo.setSelectedIndex(i);
                            break;
                        }
                    }

                    int result = JOptionPane.showConfirmDialog(
                            PanelCours.this,
                            combo,
                            "Sélectionnez le nouvel enseignant",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        Enseignant selected = (Enseignant) combo.getSelectedItem();
                        if (selected != null) {
                            String nomCours = (String) model.getValueAt(row, 1);
                            int coefficient = Integer.parseInt(model.getValueAt(row, 2).toString());
                            service.modifierCours(coursId, nomCours, coefficient, selected.getId());
                            model.setValueAt(selected.toString(), row, 3);
                        }
                    }
                }
            }
        });

        // Bouton ajouter
        ajouter.addActionListener(e -> {
            try {
                String nomCours = nom.getText().trim();
                if (nomCours.isBlank() || coef.getText().isBlank()) {
                    JOptionPane.showMessageDialog(this, "entrée incorrecte");
                    return;
                }

                int coefficient = Integer.parseInt(coef.getText());
                if (coefficient <= 0) {
                    JOptionPane.showMessageDialog(this, "entrée du coeficient incorecte");
                    return;
                }

                int enseignantId = ((Enseignant) enseignantBox.getSelectedItem()).getId();
                int classeId = ((Classe) classeBox.getSelectedItem()).getId();

                int coursId = service.creerCoursEtRetournerId(nomCours, coefficient, enseignantId);
                service.associerCoursAClasse(coursId, classeId);

                nom.setText("");
                coef.setText("");
                JOptionPane.showMessageDialog(this, "Cours ajouté avec succès.");
                chargerTable();
            } catch (Exception ex) {
                // JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            }
        });

        // Bouton refresh
        refresh.addActionListener(e -> {
            chargerTable();
            rafraichirListesDeroulantes();
        });

        // Bouton supprimer
        supprimer.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("ID du cours à supprimer :");
            if (input != null) {
                try {
                    service.supprimerCoursParId(Integer.parseInt(input));
                    chargerTable();
                } catch (Exception ex) {
                    // JOptionPane.showMessageDialog(this, ex.getMessage());

                }
            }
        });

        // Modifications en ligne (nom + coef)
        table.getModel().addTableModelListener(e -> {
            int row = e.getFirstRow();
            int id = (int) model.getValueAt(row, 0);
            String nomCours = (String) model.getValueAt(row, 1);
            int coefficient = Integer.parseInt(model.getValueAt(row, 2).toString());
            String enseignantNom = (String) model.getValueAt(row, 3);
            int enseignantId = service.getIdEnseignantByNom(enseignantNom);
            service.modifierCours(id, nomCours, coefficient, enseignantId);
        });

        chargerTable();
    }

    private void rafraichirListesDeroulantes() {
        enseignantBox.removeAllItems();
        classeBox.removeAllItems();
        try {
            for (Enseignant e : service.listerEnseignants()) {
                enseignantBox.addItem(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        List<Cours> list = null;
        try {
            list = service.listerCours();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Cours c : list) {
            String enseignantNom = service.getNomEnseignantById(c.getEnseignantId());
            String classeNom = service.getNomClasseByCoursId(c.getId()); // attention à cette méthode
            model.addRow(new Object[] { c.getId(), c.getNom(), c.getCoefficient(), enseignantNom, classeNom });
        }
    }

}
