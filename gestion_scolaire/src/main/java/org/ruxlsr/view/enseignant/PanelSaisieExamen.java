package org.ruxlsr.view.enseignant;

import org.ruxlsr.model.Cours;
import org.ruxlsr.model.Eleve;
import org.ruxlsr.model.Classe;
import org.ruxlsr.service.EnseignantService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelSaisieExamen extends JPanel {
    private JComboBox<Cours> coursCombo;
    private JComboBox<String> trimestreCombo;
    private JComboBox<Classe> classeCombo;
    private JTable table;
    private DefaultTableModel model;
    private JButton enregistrerBtn;
    private final EnseignantService service = new EnseignantService();

    public PanelSaisieExamen(int enseignantId) {
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        coursCombo = new JComboBox<>(service.getCoursByEnseignant(enseignantId).toArray(new Cours[0]));
        trimestreCombo = new JComboBox<>(new String[]{"1", "2", "3"});
        List<Classe> classes = service.getClassesByEnseignantV2(enseignantId);
        classeCombo = new JComboBox<>(classes.toArray(new Classe[0]));
        JButton chargerBtn = new JButton("Charger");
        top.add(new JLabel("Cours :")); top.add(coursCombo);
        top.add(new JLabel("Trimestre :")); top.add(trimestreCombo);
        top.add(new JLabel("Classe :")); top.add(classeCombo);
        top.add(chargerBtn);
        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Anonymat", "Note Examen"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        enregistrerBtn = new JButton("Enregistrer Notes");
        add(enregistrerBtn, BorderLayout.SOUTH);

        chargerBtn.addActionListener(e -> chargerEleves());
        enregistrerBtn.addActionListener(e -> enregistrerNotes());
    }

    private void chargerEleves() {
        model.setRowCount(0);
        Cours cours = (Cours) coursCombo.getSelectedItem();
        Classe classe = (Classe) classeCombo.getSelectedItem();
        int trimestre = trimestreCombo.getSelectedIndex() + 1;
        List<Eleve> eleves = service.getElevesByClasse(classe.getId());
        for (Eleve el : eleves) {
            // Récupérer la note d'examen existante pour cet anonymat, ce cours et ce trimestre
            Float noteExamen = service.getNoteExamen(el.getIdAnonymat(), cours.getId(), trimestre); // À implémenter dans EnseignantService
            model.addRow(new Object[]{
                el.getIdAnonymat(),
                noteExamen != null ? noteExamen : ""
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
        JOptionPane.showMessageDialog(this, "Notes examen enregistrées.");
    }
}

