package Vue;

import Dao.HebergementDAOImpl;
import Modele.Hebergement;
import Modele.Reduction;
import Controleur.Inscription;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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

    private HebergementDAOImpl hebergementDAO;

    // Couleurs et polices pour un style cohérent
    private final Color couleurPrincipale = new Color(60, 141, 188);
    private final Color couleurSecondaire = new Color(245, 245, 245);
    private final Color couleurBordure = new Color(220, 220, 220);
    private final Font policeNormale = new Font("Arial", Font.PLAIN, 14);
    private final Font policeTitre = new Font("Arial", Font.BOLD, 16);

    public VueAccueil(HebergementDAOImpl hebergementDAO) {
        this.hebergementDAO = hebergementDAO;
        setTitle("Accueil - Rechercher un Hébergement");
        setSize(1300, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal avec une marge
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(panelPrincipal);

        // --- Barre de navigation en haut avec style amélioré ---
        JPanel barreNavigation = new JPanel();
        barreNavigation.setLayout(new BoxLayout(barreNavigation, BoxLayout.X_AXIS));
        barreNavigation.setBackground(couleurPrincipale);
        barreNavigation.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        boutonAccueil = createNavButton("Accueil", "NAV_ACCUEIL");
        boutonMesReservations = createNavButton("Mes Réservations", "NAV_MES_RESERVATIONS");
        boutonDeconnexion = createNavButton("Déconnexion", "DECONNEXION");

        barreNavigation.add(boutonAccueil);
        barreNavigation.add(Box.createHorizontalStrut(15));
        barreNavigation.add(boutonMesReservations);
        barreNavigation.add(Box.createHorizontalGlue());
        barreNavigation.add(boutonDeconnexion);

        // --- Barre de recherche avec style amélioré ---
        JPanel panelRecherche = new JPanel(new GridBagLayout());
        panelRecherche.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(couleurBordure, 1),
                        "Rechercher un hébergement",
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION,
                        policeTitre,
                        couleurPrincipale
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panelRecherche.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 10);

        // Labels
        JLabel lblLieu = createLabel("Lieu :");
        JLabel lblPersonnes = createLabel("Personnes :");
        JLabel lblDateDebut = createLabel("Date d'arrivée :");
        JLabel lblDateFin = createLabel("Date de départ :");

        // Champs de saisie
        champLieu = createTextField();

        spinnerPersonnes = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        spinnerPersonnes.setFont(policeNormale);
        JComponent editor = spinnerPersonnes.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor)editor).getTextField().setFont(policeNormale);
        }

        champDateDebut = createTextField();
        champDateDebut.setText("JJ/MM/AAAA");

        champDateFin = createTextField();
        champDateFin.setText("JJ/MM/AAAA");

        boutonRecherche = new JButton("Rechercher");
        boutonRecherche.setFont(policeNormale);
        boutonRecherche.setBackground(couleurPrincipale);
        boutonRecherche.setForeground(Color.WHITE);
        boutonRecherche.setFocusPainted(false);
        boutonRecherche.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonRecherche.setActionCommand("RECHERCHER");

        // Placement des composants dans la grille
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        panelRecherche.add(lblLieu, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.3;
        panelRecherche.add(champLieu, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.2;
        panelRecherche.add(lblPersonnes, gbc);

        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.3;
        panelRecherche.add(spinnerPersonnes, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelRecherche.add(lblDateDebut, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        panelRecherche.add(champDateDebut, gbc);

        gbc.gridx = 2; gbc.gridy = 1;
        panelRecherche.add(lblDateFin, gbc);

        gbc.gridx = 3; gbc.gridy = 1;
        panelRecherche.add(champDateFin, gbc);

        gbc.gridx = 4; gbc.gridy = 0; gbc.gridheight = 2; gbc.weightx = 0.2;
        gbc.fill = GridBagConstraints.BOTH;
        panelRecherche.add(boutonRecherche, gbc);

        // --- Tableau des hébergements avec style amélioré ---
        tableModel = new DefaultTableModel(
                new Object[]{"Photo", "Nom", "Ville", "Pays", "Catégorie", "Description", "Prix (€)", "Note Moyenne", "Étoiles", "Réduction", "Réserver"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 10;  // "Réserver" est cliquable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return ImageIcon.class;
                if (columnIndex == 10) return JButton.class;
                return String.class;
            }
        };

        tableHebergements = new JTable(tableModel);
        tableHebergements.setRowHeight(80);
        tableHebergements.setFont(policeNormale);
        tableHebergements.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableHebergements.getTableHeader().setBackground(couleurSecondaire);
        tableHebergements.setSelectionBackground(new Color(230, 240, 250));
        tableHebergements.setRowMargin(5);
        tableHebergements.setIntercellSpacing(new Dimension(10, 5));

        JScrollPane scrollPane = new JScrollPane(tableHebergements);
        scrollPane.setBorder(BorderFactory.createLineBorder(couleurBordure));
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Configuration du rendu des boutons
        ButtonRenderer buttonRenderer = new ButtonRenderer();
        buttonRenderer.setBackground(couleurPrincipale);
        buttonRenderer.setForeground(Color.WHITE);
        buttonRenderer.setFont(policeNormale);
        tableHebergements.getColumn("Réserver").setCellRenderer(buttonRenderer);
        tableHebergements.getColumn("Réserver").setCellEditor(new ButtonEditor(new JCheckBox()));

        // --- Layout global ---
        JPanel panelHaut = new JPanel(new BorderLayout(0, 10));
        panelHaut.add(barreNavigation, BorderLayout.NORTH);
        panelHaut.add(panelRecherche, BorderLayout.CENTER);

        panelPrincipal.add(panelHaut, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
    }

    // Méthode utilitaire pour créer des boutons de navigation
    private JButton createNavButton(String text, String action) {
        JButton button = new JButton(text);
        button.setFont(policeNormale);
        button.setActionCommand(action);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBackground(couleurPrincipale);
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Méthode utilitaire pour créer des labels
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(policeNormale);
        return label;
    }

    // Méthode utilitaire pour créer des champs texte
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(policeNormale);
        textField.setMargin(new Insets(5, 5, 5, 5));
        return textField;
    }

    public void afficherListeHebergements(ArrayList<Hebergement> hebergements) {
        this.hebergementsAffiches = hebergements;
        tableModel.setRowCount(0);

        // Vérification du type d'utilisateur connecté
        boolean utilisateurAncien = Inscription.getUtilisateurConnecte().getTypeUtilisateur().equals("ancien");

        // Récupérer toutes les réductions uniquement si l'utilisateur est "ancien"
        List<Reduction> reductions = new ArrayList<>();
        if (utilisateurAncien) {
            reductions = hebergementDAO.getAllReductions();
        }

        for (Hebergement h : hebergements) {
            double moyenne = hebergementDAO.calculerMoyenneNotes(h.getId());
            int etoiles = (int) Math.round(moyenne);

            h.setNoteMoyenne(moyenne);
            h.setEtoiles(etoiles);
            hebergementDAO.mettreAJourNoteEtEtoiles(h.getId(), moyenne, etoiles);

            String noteStr = (moyenne == 0) ? "Aucune note" : String.format("%.1f / 5", moyenne);

            ImageIcon image = null;
            try {
                if (h.getPhoto() != null && !h.getPhoto().isEmpty()) {
                    ImageIcon icon = new ImageIcon(h.getPhoto());
                    Image scaled = icon.getImage().getScaledInstance(100, 75, Image.SCALE_SMOOTH);
                    image = new ImageIcon(scaled);
                }
            } catch (Exception e) {
                System.out.println("Erreur de chargement de l'image pour : " + h.getNom());
            }

            // Vérifier s'il y a une réduction pour cet hébergement
            String reductionStr = "Pas de réduction";
            for (Reduction reduction : reductions) {
                if (reduction.getHebergementId() == h.getId()) {
                    reductionStr = reduction.getPourcentage() + "% - " + reduction.getDescription();
                    break;
                }
            }

            tableModel.addRow(new Object[]{
                    image,
                    h.getNom(),
                    h.getVille(),
                    h.getPays(),
                    h.getCategorie(),
                    h.getDescription(),
                    h.getPrixParNuit() + " €",
                    noteStr,
                    genererEtoiles(h.getEtoiles()),
                    reductionStr,  // Affichage de la réduction
                    "Réserver"
            });
        }

        // Réappliquer les renderers après mise à jour du modèle
        ButtonRenderer buttonRenderer = new ButtonRenderer();
        buttonRenderer.setBackground(couleurPrincipale);
        buttonRenderer.setForeground(Color.WHITE);
        buttonRenderer.setFont(policeNormale);
        tableHebergements.getColumn("Réserver").setCellRenderer(buttonRenderer);
        tableHebergements.getColumn("Réserver").setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private String genererEtoiles(int etoilesPleines) {
        StringBuilder etoiles = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            etoiles.append(i < etoilesPleines ? "★" : "☆");
        }
        return etoiles.toString();
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

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(couleurPrincipale);
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(true);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFont(policeNormale);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText((value == null) ? "Réserver" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(couleurPrincipale);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorderPainted(true);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setFont(policeNormale);

            button.addActionListener(e -> {
                fireEditingStopped();
                int row = tableHebergements.getSelectedRow();
                if (row >= 0 && row < hebergementsAffiches.size()) {
                    hebergementSelectionne = hebergementsAffiches.get(row);
                    if (actionListener != null) {
                        actionListener.actionPerformed(new ActionEvent(VueAccueil.this, ActionEvent.ACTION_PERFORMED, "RESERVER"));
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
