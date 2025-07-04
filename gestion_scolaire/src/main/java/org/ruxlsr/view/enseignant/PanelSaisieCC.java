package org.ruxlsr.view.enseignant;

import org.ruxlsr.model.Cours;
import org.ruxlsr.model.Eleve;
import org.ruxlsr.model.Classe;
import org.ruxlsr.service.EnseignantService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class PanelSaisieCC extends JPanel {
    private JComboBox<Cours> coursCombo;
    private JComboBox<Classe> classeCombo;
    private JComboBox<String> trimestreCombo;
    private JTable table;
    private DefaultTableModel model;
    private JButton enregistrerBtn;
    private final EnseignantService service = new EnseignantService();

    public PanelSaisieCC(int enseignantId) {
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        // Récupère la liste des classes de l'enseignant
        List<Classe> classes = service.getClassesByEnseignantV2(enseignantId);
        classeCombo = new JComboBox<>(classes.toArray(new Classe[0]));
        top.add(new JLabel("Classe :"));
        top.add(classeCombo);

        coursCombo = new JComboBox<>(service.getCoursByEnseignant(enseignantId).toArray(new Cours[0]));
        coursCombo = new JComboBox<>(service.getCoursByEnseignant(enseignantId).toArray(new Cours[0]));
        trimestreCombo = new JComboBox<>(new String[] { "1", "2", "3" });
        JButton chargerBtn = new JButton("Charger");

        top.add(new JLabel("Cours :"));
        top.add(coursCombo);
        top.add(new JLabel("Classe :"));
        top.add(classeCombo);
        top.add(new JLabel("Trimestre :"));
        top.add(trimestreCombo);
        top.add(chargerBtn);
        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] { "ID", "Nom", "Note CC" }, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        table = new JTable(model) {
            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                if (column == 2) { // colonne "Note CC"
                    JTextField field = new JTextField();
                    ((javax.swing.text.AbstractDocument) field.getDocument())
                            .setDocumentFilter(new javax.swing.text.DocumentFilter() {
                                @Override
                                public void insertString(FilterBypass fb, int offset, String string,
                                        javax.swing.text.AttributeSet attr)
                                        throws javax.swing.text.BadLocationException {
                                    String text = fb.getDocument().getText(0, fb.getDocument().getLength());
                                    text = text.substring(0, offset) + string + text.substring(offset);
                                    if (text.matches("^\\d{0,2}(\\.\\d{0,2})?$")) { // max 2 chiffres avant/après la
                                                                                    // virgule
                                        super.insertString(fb, offset, string, attr);
                                    }
                                }

                                @Override
                                public void replace(FilterBypass fb, int offset, int length, String string,
                                        javax.swing.text.AttributeSet attr)
                                        throws javax.swing.text.BadLocationException {
                                    String text = fb.getDocument().getText(0, fb.getDocument().getLength());
                                    text = text.substring(0, offset) + string + text.substring(offset + length);
                                    if (text.matches("^\\d{0,2}(\\.\\d{0,2})?$")) {
                                        super.replace(fb, offset, length, string, attr);
                                    }
                                }
                            });
                    return new DefaultCellEditor(field);
                }
                return super.getCellEditor(row, column);
            }
        };
        add(new JScrollPane(table), BorderLayout.CENTER);

        enregistrerBtn = new JButton("Enregistrer Notes");
        add(enregistrerBtn, BorderLayout.SOUTH);

        chargerBtn.addActionListener(e -> chargerEleves());
        enregistrerBtn.addActionListener(e -> enregistrerNotes());
    }

    private void chargerEleves() {
        model.setRowCount(0);
        Cours cours = (Cours) coursCombo.getSelectedItem();
        Classe selectedClasse = (Classe) classeCombo.getSelectedItem();
        int classeId = selectedClasse.getId();
        int trimestre = trimestreCombo.getSelectedIndex() + 1;

        List<Eleve> eleves = null;
        try {
            eleves = service.getElevesByClasse(classeId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Eleve el : eleves) {
            // Récupérer la note CC existante pour cet élève, ce cours et ce trimestre
            Float noteCC = service.getNoteCC(el.getId(), cours.getId(), trimestre); // À implémenter dans
                                                                                    // EnseignantService
            model.addRow(new Object[] {
                    el.getId(),
                    el.getNom(),
                    noteCC != null ? noteCC : ""
            });
        }
    }

    private void enregistrerNotes() {
        // Force la validation de la cellule en cours d'édition
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        Cours cours = (Cours) coursCombo.getSelectedItem();
        int trimestre = trimestreCombo.getSelectedIndex() + 1;
        for (int i = 0; i < model.getRowCount(); i++) {
            int eleveId = (int) model.getValueAt(i, 0);
            String val = model.getValueAt(i, 2).toString();
            if (!val.isEmpty()) {
                float note = Float.parseFloat(val);
                if (note < 0 || note > 20) {
                    JOptionPane.showMessageDialog(this, "La note doit être comprise entre 0 et 20 !");
                    return; // Arrête la saisie si une note est invalide
                }
                service.saisirNoteCC(eleveId, cours.getId(), trimestre, note);
            }
        }
        JOptionPane.showMessageDialog(this, "Notes CC enregistrées.");
    }

}
