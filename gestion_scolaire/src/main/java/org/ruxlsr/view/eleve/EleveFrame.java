package org.ruxlsr.view.eleve;

import org.ruxlsr.service.EleveService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class EleveFrame extends JFrame {

    private JComboBox<String> trimestreCombo;
    private JButton afficherButton;
    private JTable table;
    private JLabel moyenneLabel, mentionLabel, rangLabel;
    private DefaultTableModel tableModel;

    private final int eleveId;
    private final EleveService service = new EleveService();

    public EleveFrame(int eleveId) {
        this.eleveId = eleveId;
        String nomEleve = service.getNomEleve(eleveId);
        setTitle("Bulletin de " + nomEleve);
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new FlowLayout());
        trimestreCombo = new JComboBox<>(new String[]{"1", "2", "3"});
        afficherButton = new JButton("Afficher bulletin");
        topPanel.add(new JLabel("Trimestre :"));
        topPanel.add(trimestreCombo);
        topPanel.add(afficherButton);
        add(topPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Matière", "Coefficient", "Note CC", "Note Examen", "Moyenne"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 3));
        moyenneLabel = new JLabel("Moyenne : ");
        mentionLabel = new JLabel("Mention : ");
        rangLabel = new JLabel("Rang : ");
        bottomPanel.add(moyenneLabel);
        bottomPanel.add(mentionLabel);
        bottomPanel.add(rangLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        afficherButton.addActionListener(e -> afficherBulletin());
    }

    private void afficherBulletin() {
        int trimestre = trimestreCombo.getSelectedIndex() + 1;
        try {
            tableModel.setRowCount(0);
            float moyenne = service.remplirBulletin(eleveId, trimestre, tableModel);
            moyenneLabel.setText("Moyenne : " + String.format("%.2f", moyenne));
            mentionLabel.setText("Mention : " + service.getMention(moyenne));
            rangLabel.setText("Rang : " + service.getRang(eleveId, trimestre));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }
}
