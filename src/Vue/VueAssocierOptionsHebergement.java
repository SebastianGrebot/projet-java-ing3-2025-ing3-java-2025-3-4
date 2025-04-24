package Vue;

import Modele.Hebergement;
import Modele.Option;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class VueAssocierOptionsHebergement extends JFrame {
    private JComboBox<Hebergement> comboBoxHebergements;
    private JPanel panelOptions;
    private JButton boutonValider;
    private JButton boutonRetour;
    private List<JCheckBox> checkBoxesOptions = new ArrayList<>();

    public VueAssocierOptionsHebergement() {
        setTitle("Associer des options à un hébergement");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        comboBoxHebergements = new JComboBox<>();
        panelOptions = new JPanel();
        panelOptions.setLayout(new BoxLayout(panelOptions, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(panelOptions);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        boutonValider = new JButton("Valider l'association");
        boutonRetour = new JButton("Retour");

        boutonValider.setActionCommand("VALIDER_ASSOCIATION");
        boutonRetour.setActionCommand("RETOUR_ACCUEIL");

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.add(comboBoxHebergements, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        JPanel panelBoutons = new JPanel();
        panelBoutons.add(boutonValider);
        panelBoutons.add(boutonRetour);
        panelPrincipal.add(panelBoutons, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    public void setHebergements(List<Hebergement> hebergements) {
        comboBoxHebergements.removeAllItems();
        for (Hebergement h : hebergements) {
            comboBoxHebergements.addItem(h);
        }
    }

    public void setOptions(List<Option> options, List<Option> optionsAssociees) {
        panelOptions.removeAll();  // Supprime toutes les anciennes options
        checkBoxesOptions.clear();  // Vider la liste des cases à cocher

        // Pour chaque option, ajouter une JCheckBox et définir son état (sélectionnée ou non)
        for (Option opt : options) {
            JCheckBox checkBox = new JCheckBox(opt.getNomOption());

            // Si l'option est dans la liste des options associées, elle sera pré-sélectionnée
            checkBox.setSelected(optionsAssociees.contains(opt));

            // Ajouter l'option à la case à cocher
            checkBox.putClientProperty("option", opt);
            checkBoxesOptions.add(checkBox);
            panelOptions.add(checkBox);
        }

        panelOptions.revalidate();  // Revalider l'affichage
        panelOptions.repaint();     // Repeindre l'interface pour appliquer les changements
    }


    public Hebergement getHebergementSelectionne() {
        return (Hebergement) comboBoxHebergements.getSelectedItem();
    }

    public List<Option> getOptionsSelectionnees() {
        List<Option> selectionnees = new ArrayList<>();
        for (JCheckBox cb : checkBoxesOptions) {
            if (cb.isSelected()) {
                selectionnees.add((Option) cb.getClientProperty("option"));
            }
        }
        return selectionnees;
    }

    public void ajouterEcouteur(ActionListener listener) {
        boutonValider.addActionListener(listener);
        boutonRetour.addActionListener(listener);
        comboBoxHebergements.addActionListener(listener);
    }

    public void afficherMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public JComboBox<Hebergement> getComboBoxHebergements() {
        return comboBoxHebergements;
    }

}
