package Vue;

import Dao.HebergementDAOImpl;
import Modele.Hebergement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class VueAccueil extends JFrame {
    // Constantes pour les couleurs
    private static final Color COULEUR_PRIMAIRE = new Color(41, 128, 185);
    private static final Color COULEUR_ACCENT = new Color(52, 152, 219);
    private static final Color COULEUR_FOND = new Color(245, 245, 245);
    private static final Color COULEUR_TEXTE = new Color(44, 62, 80);
    private static final Color COULEUR_BOUTON = new Color(52, 152, 219);
    private static final Color COULEUR_BOUTON_HOVER = new Color(41, 128, 185);
    
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

    public VueAccueil(HebergementDAOImpl hebergementDAO) {
        this.hebergementDAO = hebergementDAO;
        setTitle("Accueil - Rechercher un Hébergement");
        setSize(1300, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COULEUR_FOND);
        
        // Application du look and feel global
        applyGlobalStyle();

        // --- Barre de navigation en haut ---
        JPanel barreNavigation = creerBarreNavigation();

        // --- Barre de recherche ---
        JPanel panelRecherche = creerPanelRecherche();

        // --- Tableau des hébergements ---
        JScrollPane scrollPane = creerTableauHebergements();

        // --- Layout global ---
        JPanel panelHaut = new JPanel(new BorderLayout());
        panelHaut.setBackground(COULEUR_FOND);
        panelHaut.add(barreNavigation, BorderLayout.NORTH);
        panelHaut.add(panelRecherche, BorderLayout.SOUTH);

        JPanel contentPanel = new JPanel(new BorderLayout(0, 15));
        contentPanel.setBackground(COULEUR_FOND);
        contentPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        contentPanel.add(panelHaut, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
    }

    private void applyGlobalStyle() {
        // Police et couleurs par défaut
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Spinner.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 14));
        
        UIManager.put("Panel.background", COULEUR_FOND);
        UIManager.put("Label.foreground", COULEUR_TEXTE);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", COULEUR_TEXTE);
    }

    private JPanel creerBarreNavigation() {
        JPanel barreNavigation = new JPanel();
        barreNavigation.setLayout(new BoxLayout(barreNavigation, BoxLayout.X_AXIS));
        barreNavigation.setBackground(COULEUR_PRIMAIRE);
        barreNavigation.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Titre de l'application
        JLabel logo = new JLabel("HOLIDAY HOMES");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logo.setForeground(Color.WHITE);
        
        // Boutons de navigation
        boutonAccueil = creerBoutonNavigation("Accueil", "NAV_ACCUEIL", true);
        boutonMesReservations = creerBoutonNavigation("Mes Réservations", "NAV_MES_RESERVATIONS", false);
        boutonDeconnexion = creerBoutonNavigation("Déconnexion", "DECONNEXION", false);

        // Ajout des boutons à la barre de navigation
        barreNavigation.add(logo);
        barreNavigation.add(Box.createHorizontalGlue());
        barreNavigation.add(boutonAccueil);
        barreNavigation.add(Box.createRigidArea(new Dimension(15, 0)));
        barreNavigation.add(boutonMesReservations);
        barreNavigation.add(Box.createRigidArea(new Dimension(15, 0)));
        barreNavigation.add(boutonDeconnexion);

        return barreNavigation;
    }

    private JButton creerBoutonNavigation(String texte, String commande, boolean actif) {
        JButton bouton = new JButton(texte);
        bouton.setActionCommand(commande);
        bouton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bouton.setForeground(Color.WHITE);
        bouton.setBackground(actif ? COULEUR_ACCENT : COULEUR_PRIMAIRE);
        bouton.setBorderPainted(false);
        bouton.setFocusPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Ajouter des effets de survol
        bouton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bouton.setBackground(COULEUR_ACCENT);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!actif) {
                    bouton.setBackground(COULEUR_PRIMAIRE);
                }
            }
        });
        
        return bouton;
    }

    private JPanel creerPanelRecherche() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 10));
        panelPrincipal.setBackground(COULEUR_FOND);
        panelPrincipal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));

        JLabel titreRecherche = new JLabel("Trouvez l'hébergement idéal pour vos vacances");
        titreRecherche.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titreRecherche.setForeground(COULEUR_TEXTE);
        titreRecherche.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel panelChamps = new JPanel(new GridLayout(1, 5, 10, 0));
        panelChamps.setBackground(COULEUR_FOND);

        // Champ lieu
        JPanel panelLieu = creerPanelChamp("Destination", "Où souhaitez-vous aller?");
        champLieu = (JTextField) panelLieu.getComponent(1);

        // Champ nombre de personnes
        JPanel panelPersonnes = new JPanel(new BorderLayout());
        panelPersonnes.setBackground(COULEUR_FOND);
        JLabel labelPersonnes = new JLabel("Voyageurs");
        labelPersonnes.setFont(new Font("Segoe UI", Font.BOLD, 14));
        spinnerPersonnes = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        spinnerPersonnes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ((JSpinner.DefaultEditor) spinnerPersonnes.getEditor()).getTextField().setBackground(Color.WHITE);
        panelPersonnes.add(labelPersonnes, BorderLayout.NORTH);
        panelPersonnes.add(spinnerPersonnes, BorderLayout.CENTER);

        // Champ date début
        JPanel panelDateDebut = creerPanelChamp("Arrivée", "JJ/MM/AAAA");
        champDateDebut = (JTextField) panelDateDebut.getComponent(1);

        // Champ date fin
        JPanel panelDateFin = creerPanelChamp("Départ", "JJ/MM/AAAA");
        champDateFin = (JTextField) panelDateFin.getComponent(1);

        // Bouton recherche
        boutonRecherche = new JButton("Rechercher");
        boutonRecherche.setActionCommand("RECHERCHER");
        boutonRecherche.setBackground(COULEUR_BOUTON);
        boutonRecherche.setForeground(Color.WHITE);
        boutonRecherche.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boutonRecherche.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonRecherche.setFocusPainted(false);
        
        // Effet de survol pour le bouton
        boutonRecherche.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boutonRecherche.setBackground(COULEUR_BOUTON_HOVER);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                boutonRecherche.setBackground(COULEUR_BOUTON);
            }
        });

        // Ajout des champs au panneau principal
        panelChamps.add(panelLieu);
        panelChamps.add(panelPersonnes);
        panelChamps.add(panelDateDebut);
        panelChamps.add(panelDateFin);
        
        JPanel panelBouton = new JPanel(new GridBagLayout());
        panelBouton.setBackground(COULEUR_FOND);
        panelBouton.add(boutonRecherche);

        panelChamps.add(panelBouton);

        panelPrincipal.add(titreRecherche, BorderLayout.NORTH);
        panelPrincipal.add(panelChamps, BorderLayout.CENTER);

        return panelPrincipal;
    }

    private JPanel creerPanelChamp(String label, String placeholder) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COULEUR_FOND);
        
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        JTextField textField = new JTextField(placeholder);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setForeground(Color.GRAY);
        textField.setBackground(Color.WHITE);
        
        // Gestionnaire de placeholder
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(COULEUR_TEXTE);
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
        
        panel.add(jLabel, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        
        return panel;
    }

    private JScrollPane creerTableauHebergements() {
        tableModel = new DefaultTableModel(
                new Object[]{"Photo", "Nom", "Ville", "Pays", "Catégorie", "Description", "Prix (€)", "Note", "Étoiles", ""}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9; // Seule la colonne réserver est éditable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return ImageIcon.class;
                if (columnIndex == 9) return JButton.class;
                return String.class;
            }
        };

        tableHebergements = new JTable(tableModel);
        tableHebergements.setRowHeight(100);
        tableHebergements.setShowGrid(false);
        tableHebergements.setIntercellSpacing(new Dimension(0, 10));
        tableHebergements.setBackground(Color.WHITE);
        tableHebergements.setForeground(COULEUR_TEXTE);
        tableHebergements.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableHebergements.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableHebergements.getTableHeader().setBackground(COULEUR_FOND);
        tableHebergements.getTableHeader().setForeground(COULEUR_TEXTE);
        tableHebergements.setSelectionBackground(new Color(230, 240, 250));
        
        // Personnaliser le rendu des cellules du tableau
        TableCellRenderer headerRenderer = tableHebergements.getTableHeader().getDefaultRenderer();
        if (headerRenderer instanceof JLabel) {
            ((JLabel) headerRenderer).setHorizontalAlignment(JLabel.CENTER);
        }
        
        // Aligner et formater les colonnes
        for (int i = 0; i < tableHebergements.getColumnCount(); i++) {
            tableHebergements.getColumnModel().getColumn(i).setCellRenderer(new CellulePersonnaliseeRenderer());
        }

        // Ajuster les tailles de colonnes
        tableHebergements.getColumnModel().getColumn(0).setPreferredWidth(120);  // Photo
        tableHebergements.getColumnModel().getColumn(1).setPreferredWidth(150);  // Nom
        tableHebergements.getColumnModel().getColumn(2).setPreferredWidth(100);  // Ville
        tableHebergements.getColumnModel().getColumn(3).setPreferredWidth(100);  // Pays
        tableHebergements.getColumnModel().getColumn(4).setPreferredWidth(120);  // Catégorie
        tableHebergements.getColumnModel().getColumn(5).setPreferredWidth(250);  // Description
        tableHebergements.getColumnModel().getColumn(6).setPreferredWidth(80);   // Prix
        tableHebergements.getColumnModel().getColumn(7).setPreferredWidth(80);   // Note
        tableHebergements.getColumnModel().getColumn(8).setPreferredWidth(100);  // Étoiles
        tableHebergements.getColumnModel().getColumn(9).setPreferredWidth(100);  // Réserver

        // Configurer le bouton réserver dans la dernière colonne
        tableHebergements.getColumn("").setCellRenderer(new BoutonReserverRenderer());
        tableHebergements.getColumn("").setCellEditor(new BoutonReserverEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(tableHebergements);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }

    public void afficherListeHebergements(ArrayList<Hebergement> hebergements) {
        this.hebergementsAffiches = hebergements;
        tableModel.setRowCount(0);

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
                    Image scaled = icon.getImage().getScaledInstance(120, 90, Image.SCALE_SMOOTH);
                    image = new ImageIcon(scaled);
                }
            } catch (Exception e) {
                System.out.println("Erreur de chargement de l'image pour : " + h.getNom());
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
                    "Réserver"
            });
        }
    }

    private String genererEtoiles(int etoilesPleines) {
        StringBuilder etoiles = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            etoiles.append(i < etoilesPleines ? "★" : "☆");
        }
        return etoiles.toString();
    }

    public String getLieuRecherche() {
        String texte = champLieu.getText();
        return texte.equals("Où souhaitez-vous aller?") ? "" : texte;
    }

    public int getNbPersonnes() {
        return (Integer) spinnerPersonnes.getValue();
    }

    public String getDateDebut() {
        String texte = champDateDebut.getText();
        return texte.equals("JJ/MM/AAAA") ? "" : texte;
    }

    public String getDateFin() {
        String texte = champDateFin.getText();
        return texte.equals("JJ/MM/AAAA") ? "" : texte;
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
        UIManager.put("OptionPane.background", COULEUR_FOND);
        UIManager.put("Panel.background", COULEUR_FOND);
        UIManager.put("OptionPane.messageForeground", COULEUR_TEXTE);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 14));
        
        JOptionPane.showMessageDialog(this, message);
    }

    public Hebergement getHebergementSelectionne() {
        return hebergementSelectionne;
    }

    // Renderer personnalisé pour les cellules du tableau
    class CellulePersonnaliseeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value instanceof ImageIcon) {
                setIcon((ImageIcon) value);
                setText("");
                setHorizontalAlignment(JLabel.CENTER);
            } else {
                setIcon(null);
                if (column == 5) { // Description
                    setHorizontalAlignment(JLabel.LEFT);
                } else if (column == 6) { // Prix
                    setHorizontalAlignment(JLabel.RIGHT);
                    setFont(new Font("Segoe UI", Font.BOLD, 14));
                    setForeground(new Color(231, 76, 60)); // Rouge pour le prix
                } else if (column == 7) { // Note
                    setHorizontalAlignment(JLabel.CENTER);
                } else if (column == 8) { // Étoiles
                    setHorizontalAlignment(JLabel.CENTER);
                    setForeground(new Color(243, 156, 18)); // Orange pour les étoiles
                } else {
                    setHorizontalAlignment(JLabel.CENTER);
                }
            }

            // Ajouter un padding
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            // Ajouter des couleurs alternées pour les lignes
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 249, 249));
            }
            
            return c;
        }
    }

    // Renderer personnalisé pour le bouton "Réserver"
    class BoutonReserverRenderer extends JButton implements TableCellRenderer {
        public BoutonReserverRenderer() {
            setOpaque(true);
            setBackground(new Color(46, 204, 113)); // Vert
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText("Réserver");
            return this;
        }
    }

    // Editor personnalisé pour le bouton "Réserver"
    class BoutonReserverEditor extends DefaultCellEditor {
        private JButton button;
        private String label;
        private boolean isPushed;

        public BoutonReserverEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(46, 204, 113)); // Vert
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 14));
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            
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
            
            // Effet de survol
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(new Color(39, 174, 96)); // Vert plus foncé
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(new Color(46, 204, 113)); // Vert original
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = "Réserver";
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
