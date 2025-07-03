package org.ruxlsr.view.admin;

import org.ruxlsr.model.Enseignant;
import org.ruxlsr.service.AdminService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelEnseignants extends JPanel {
    private JTable table;
    private final DefaultTableModel model;
    private final AdminService service = new AdminService();

    public PanelEnseignants() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel();
        JTextField nom = new JTextField(20);
        JButton ajouter = new JButton("Ajouter"), refresh = new JButton("Rafraîchir"), supprimer = new JButton("Supprimer");

        form.add(new JLabel("Nom :")); form.add(nom);
        form.add(ajouter); form.add(refresh); form.add(supprimer);
        add(form, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Nom"}, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 1;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        ajouter.addActionListener(e -> {
            try {
                if( nom.getText().isBlank()){
                    JOptionPane.showMessageDialog(this, "entrée incorrecte");
                    return;
                }
                service.creerEnseignant(nom.getText());
                nom.setText("");
                chargerTable();
            } catch (Exception ex) {
                //JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
        refresh.addActionListener(e -> chargerTable());

        supprimer.addActionListener(e -> {
            String input = JOptionPane.showInputDialog("Entrer l'ID de l'enseignant à supprimer :");
            if (input != null) {
                try {
                    service.supprimerEnseignantParId(Integer.parseInt(input));
                    chargerTable();
                } catch (Exception ex) {
                    //JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        table.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 1) {
                int row = e.getFirstRow();
                int id = (int) model.getValueAt(row, 0);
                String nouveauNom = (String) model.getValueAt(row, 1);
                service.modifierNomEnseignant(id, nouveauNom);
            }
        });

        chargerTable();
    }

    private void chargerTable() {
        model.setRowCount(0);
        try {
            List<Enseignant> list = service.listerEnseignants();
            for (Enseignant e : list) {
                model.addRow(new Object[]{e.getId(), e.getNom()});
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


