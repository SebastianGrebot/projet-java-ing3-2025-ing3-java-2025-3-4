package Vue;

import Modele.Hebergement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class VueAccueil extends JFrame {
    private JTable tableHebergements;
    private DefaultTableModel tableModel;

    private JTextField champLieu;
    private JSpinner spinnerPersonnes;
    private JTextField champDateDebut;
    private JTextField champDateFin;
    private JButton boutonRecherche;
    private JButton boutonDeconnexion;

    public VueAccueil() {
        setTitle("Accueil - Hébergements");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Barre de recherche ---
        JPanel panelRecherche = new JPanel(new GridLayout(2, 5, 10, 10));

        champLieu = new JTextField();
        spinnerPersonnes = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        champDateDebut = new JTextField("JJ/MM/AAAA");
        champDateFin = new JTextField("JJ/MM/AAAA");
        boutonRecherche = new JButton("Rechercher");
        boutonRecherche.setActionCommand("RECHERCHER");

        panelRecherche.add(new JLabel("Lieu :"));
        panelRecherche.add(new JLabel("Personnes :"));
        panelRecherche.add(new JLabel("Date d'arrivée :"));
        panelRecherche.add(new JLabel("Date de départ :"));
        panelRecherche.add(new JLabel(""));

        panelRecherche.add(champLieu);
        panelRecherche.add(spinnerPersonnes);
        panelRecherche.add(champDateDebut);
        panelRecherche.add(champDateFin);
        panelRecherche.add(boutonRecherche);

        // --- Tableau des hébergements ---
        tableModel = new DefaultTableModel(new Object[]{"Nom", "Prix", "Options"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) return Boolean.class; // Colonne options = checkbox
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Seule la colonne options est modifiable
            }
        };

        tableHebergements = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableHebergements);

        // --- Bouton Déconnexion ---
        boutonDeconnexion = new JButton("Déconnexion");
        boutonDeconnexion.setActionCommand("DECONNEXION");

        JPanel panelBas = new JPanel();
        panelBas.add(boutonDeconnexion);

        // --- Composition principale ---
        setLayout(new BorderLayout());
        add(panelRecherche, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBas, BorderLayout.SOUTH);
    }

    public void afficherListeHebergements(ArrayList<Hebergement> hebergements) {
        tableModel.setRowCount(0); // Vide le tableau
        for (Hebergement h : hebergements) {
            // TODO: options réelles à parser, ici on met un simple checkbox décoché
            tableModel.addRow(new Object[]{h.getNom(), h.getPrixParNuit(), false});
        }
    }

    public String getLieuRecherche() {
        return champLieu.getText();
    }

    public int getNbPersonnes() {
        return (Integer) spinnerPersonnes.getValue();
    }

    public String getDateDebut() {
        return champDateDebut.getText();
    }

    public String getDateFin() {
        return champDateFin.getText();
    }

    public void ajouterEcouteur(ActionListener listener) {
        boutonRecherche.addActionListener(listener);
        boutonDeconnexion.addActionListener(listener);
    }

    public void afficherMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
