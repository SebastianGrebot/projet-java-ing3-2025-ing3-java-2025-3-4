package Vue;

import Modele.Hebergement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
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

    private JButton boutonAccueil;
    private JButton boutonMesReservations;
    private JButton boutonDeconnexion;

    private ActionListener actionListener;

    private ArrayList<Hebergement> hebergementsAffiches;
    private Hebergement hebergementSelectionne;

    public VueAccueil() {
        setTitle("Accueil - Rechercher un Hébergement");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- Barre de navigation en haut ---
        JPanel barreNavigation = new JPanel(new FlowLayout(FlowLayout.LEFT));
        boutonAccueil = new JButton("Accueil");
        boutonAccueil.setActionCommand("NAV_ACCUEIL");

        boutonMesReservations = new JButton("Mes Réservations");
        boutonMesReservations.setActionCommand("NAV_MES_RESERVATIONS");

        boutonDeconnexion = new JButton("Déconnexion");
        boutonDeconnexion.setActionCommand("DECONNEXION");

        barreNavigation.add(boutonAccueil);
        barreNavigation.add(boutonMesReservations);
        barreNavigation.add(Box.createHorizontalStrut(20)); // espacement
        barreNavigation.add(boutonDeconnexion);

        // --- Barre de recherche ---
        JPanel panelRecherche = new JPanel(new GridLayout(2, 5, 10, 10));
        panelRecherche.setBorder(BorderFactory.createTitledBorder("Rechercher un hébergement"));

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
        panelRecherche.add(new JLabel("")); // vide

        panelRecherche.add(champLieu);
        panelRecherche.add(spinnerPersonnes);
        panelRecherche.add(champDateDebut);
        panelRecherche.add(champDateFin);
        panelRecherche.add(boutonRecherche);

        // --- Tableau des hébergements ---
        tableModel = new DefaultTableModel(
                new Object[]{"Nom", "Ville", "Pays", "Catégorie", "Description", "Prix (€)", "Réserver"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Seule la colonne bouton est éditable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 6 ? JButton.class : String.class;
            }
        };

        tableHebergements = new JTable(tableModel);
        tableHebergements.setRowHeight(40);
        JScrollPane scrollPane = new JScrollPane(tableHebergements);

        tableHebergements.getColumn("Réserver").setCellRenderer(new ButtonRenderer());
        tableHebergements.getColumn("Réserver").setCellEditor(new ButtonEditor(new JCheckBox()));

        // --- Layout global ---
        JPanel panelHaut = new JPanel(new BorderLayout());
        panelHaut.add(barreNavigation, BorderLayout.NORTH);
        panelHaut.add(panelRecherche, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(panelHaut, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void afficherListeHebergements(ArrayList<Hebergement> hebergements) {
        this.hebergementsAffiches = hebergements;
        tableModel.setRowCount(0);

        for (Hebergement h : hebergements) {
            tableModel.addRow(new Object[]{
                    h.getNom(),
                    h.getVille(),
                    h.getPays(),
                    h.getCategorie(),
                    h.getDescription(),
                    h.getPrixParNuit() + " €",
                    "Réserver"
            });
        }

        tableHebergements.getColumn("Réserver").setCellRenderer(new ButtonRenderer());
        tableHebergements.getColumn("Réserver").setCellEditor(new ButtonEditor(new JCheckBox()));
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
        if (this.actionListener == null) {
            boutonRecherche.addActionListener(listener);
            boutonDeconnexion.addActionListener(listener);
            boutonAccueil.addActionListener(listener);
            boutonMesReservations.addActionListener(listener);
            this.actionListener = listener;
        }
    }

    public void afficherMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public Hebergement getHebergementSelectionne() {
        return hebergementSelectionne;
    }

    // Renderer pour afficher un bouton dans une cellule
    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText((value == null) ? "Réserver" : value.toString());
            return this;
        }
    }

    // Editor pour gérer le clic sur le bouton dans une cellule
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    int row = tableHebergements.getSelectedRow();
                    if (row >= 0 && row < hebergementsAffiches.size()) {
                        hebergementSelectionne = hebergementsAffiches.get(row);
                        if (actionListener != null) {
                            actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "RESERVER"));
                        }
                    }
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "Réserver" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            isPushed = false;
            return label;
        }
    }
}
