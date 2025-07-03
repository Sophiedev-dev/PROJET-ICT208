package org.ruxlsr.view.enseignant;

import org.ruxlsr.model.Cours;
import org.ruxlsr.model.Eleve;
import org.ruxlsr.service.EnseignantService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelSaisieExamen extends JPanel {
    private JComboBox<Cours> coursCombo;
    private JComboBox<String> trimestreCombo;
    private JTable table;
    private DefaultTableModel model;
    private JButton enregistrerBtn;
    private final EnseignantService service = new EnseignantService();

    public PanelSaisieExamen(int enseignantId) {
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        coursCombo = new JComboBox<>(service.getCoursByEnseignant(enseignantId).toArray(new Cours[0]));
        trimestreCombo = new JComboBox<>(new String[]{"1", "2", "3"});
        JButton chargerBtn = new JButton("Charger");
        top.add(new JLabel("Cours :")); top.add(coursCombo);
        top.add(new JLabel("Trimestre :")); top.add(trimestreCombo);
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
        int classeId = new EnseignantService().getClasseIdForCours(cours.getId());
        List<Eleve> eleves = service.getElevesByClasse(classeId);
        for (Eleve el : eleves) {
            model.addRow(new Object[]{el.getIdAnonymat(), ""});
        }
    }

    private void enregistrerNotes() {
        Cours cours = (Cours) coursCombo.getSelectedItem();
        int trimestre = trimestreCombo.getSelectedIndex() + 1;
        for (int i = 0; i < model.getRowCount(); i++) {
            String anonymat = model.getValueAt(i, 0).toString();
            String val = model.getValueAt(i, 1).toString();
            if (!val.isEmpty()) {
                float note = Float.parseFloat(val);
                assert cours != null;
                service.saisirNoteExamen(anonymat, cours.getId(), trimestre, note);
            }
        }
        JOptionPane.showMessageDialog(this, "Notes examen enregistrées.");
    }
}

