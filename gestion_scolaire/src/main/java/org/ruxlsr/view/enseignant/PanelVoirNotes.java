package org.ruxlsr.view.enseignant;

import org.ruxlsr.model.Cours;
import org.ruxlsr.service.EnseignantService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelVoirNotes extends JPanel {
    private JComboBox<Integer> classeCombo;
    private JComboBox<Cours> coursCombo;
    private JComboBox<String> trimestreCombo;
    private JTable table;
    private DefaultTableModel model;
    private final EnseignantService service = new EnseignantService();

    public PanelVoirNotes(int enseignantId) {
        setLayout(new BorderLayout());
        JPanel top = new JPanel();

        classeCombo = new JComboBox<>(service.getClasseIdsByEnseignant(enseignantId).toArray(new Integer[0]));
        coursCombo = new JComboBox<>(service.getCoursByEnseignant(enseignantId).toArray(new Cours[0]));
        trimestreCombo = new JComboBox<>(new String[] { "1", "2", "3" });
        JButton afficher = new JButton("Afficher");

        top.add(new JLabel("Classe :"));
        top.add(classeCombo);
        top.add(new JLabel("Cours :"));
        top.add(coursCombo);
        top.add(new JLabel("Trimestre :"));
        top.add(trimestreCombo);
        top.add(afficher);
        add(top, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] { "Nom", "Note CC", "Note Examen", "Moyenne" }, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        afficher.addActionListener(e -> chargerNotes());
    }

    private void chargerNotes() {
        model.setRowCount(0);
        int classeId = (int) classeCombo.getSelectedItem();
        int coursId = ((Cours) coursCombo.getSelectedItem()).getId();
        int trimestre = trimestreCombo.getSelectedIndex() + 1;
        new EnseignantService().getNoteParclasseEtCours(model, classeId, coursId, trimestre);

    }
}