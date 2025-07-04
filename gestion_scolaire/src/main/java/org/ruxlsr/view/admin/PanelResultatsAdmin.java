package org.ruxlsr.view.admin;

import org.ruxlsr.model.*;
import org.ruxlsr.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

public class PanelResultatsAdmin extends JPanel {
    private final JComboBox<Classe> classeCombo = new JComboBox<>();
    private final JComboBox<Cours> coursCombo = new JComboBox<>();
    private final JComboBox<String> trimestreCombo = new JComboBox<>(new String[] { "1", "2", "3" });
    private final DefaultTableModel model = new DefaultTableModel(
            new String[] { "#", "Nom", "Anonymat", "Note CC", "Note Examen", "Moyenne", "Mention", "Rang" }, 0) {
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable table = new JTable(model);
    private final AdminService adminService = new AdminService();
    private final NoteService noteService = new NoteService();

    public PanelResultatsAdmin() {
        setLayout(new BorderLayout());

        // Panel supérieur (filtres)
        JPanel top = new JPanel();
        top.add(new JLabel("Classe :"));
        top.add(classeCombo);
        top.add(new JLabel("Cours :"));
        top.add(coursCombo);
        top.add(new JLabel("Trimestre :"));
        top.add(trimestreCombo);
        JButton afficher = new JButton("Afficher");
        JButton refresh = new JButton("Rafraîchir");
        top.add(afficher);
        top.add(refresh);
        add(top, BorderLayout.NORTH);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Chargement initial des listes déroulantes
        rafraichirListesDeroulantes();

        // Charger cours quand une classe est sélectionnée
        classeCombo.addActionListener(e -> {
            rafraichirCours();
        });

        // Afficher les résultats
        afficher.addActionListener(e -> chargerResultats());

        // Rafraîchir les listes déroulantes et vider le tableau
        refresh.addActionListener(e -> {
            rafraichirListesDeroulantes();
            model.setRowCount(0);
        });
    }

    private void rafraichirListesDeroulantes() {
        classeCombo.removeAllItems();
        try {
            for (Classe c : adminService.listerClasses()) {
                classeCombo.addItem(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        rafraichirCours();
    }

    private void rafraichirCours() {
        coursCombo.removeAllItems();
        Classe selectedClasse = (Classe) classeCombo.getSelectedItem();
        if (selectedClasse != null) {
            List<Cours> coursList = adminService.listerCoursParClasse(selectedClasse.getId());
            for (Cours c : coursList) {
                coursCombo.addItem(c);
            }
        }
    }

    private void chargerResultats() {
        model.setRowCount(0);
        Classe classe = (Classe) classeCombo.getSelectedItem();
        Cours cours = (Cours) coursCombo.getSelectedItem();
        int trimestre = trimestreCombo.getSelectedIndex() + 1;

        if (classe == null || cours == null)
            return;

        List<Eleve> eleves = null;
        try {
            eleves = adminService.getElevesByClasse(classe.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<Vector<Object>> lignes = new ArrayList<>();
        for (Eleve e : eleves) {
            float noteCC = noteService.getNoteCC(e.getId(), cours.getId(), trimestre);
            float noteEx = noteService.getNoteExamen(e.getId(), cours.getId(), trimestre);
            float moyenne = (noteCC + noteEx) / 2;
            String mention = getMention(moyenne);

            Vector<Object> row = new Vector<>();
            row.add(0); // placeholder #
            row.add(e.getNom());
            row.add(e.getIdAnonymat());
            row.add(noteCC);
            row.add(noteEx);
            row.add(moyenne);
            row.add(mention);
            row.add(0); // placeholder rang
            lignes.add(row);
        }

        // Tri par moyenne décroissante
        lignes.sort(Comparator.comparing(o -> -(float) o.get(5)));

        // Ajout au tableau avec rangs
        for (int i = 0; i < lignes.size(); i++) {
            lignes.get(i).set(0, i + 1); // #
            lignes.get(i).set(7, i + 1); // Rang
            model.addRow(lignes.get(i));
        }
    }

    private String getMention(float moyenne) {
        if (moyenne >= 16)
            return "Très Bien";
        if (moyenne >= 14)
            return "Bien";
        if (moyenne >= 12)
            return "Assez Bien";
        if (moyenne >= 10)
            return "Passable";
        return "Insuffisant";
    }
}
