vpackage Vue;

import Dao.HebergementDAOImpl;
import Dao.OptionDAOImpl;
import Modele.Chambre;
import Modele.Hebergement;
import Modele.Option;
import Modele.Reduction;
import Controleur.Inscription;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class VueAccueil extends JFrame {

    // Composants de la vue
    private JTable tableHebergements;
    private DefaultTableModel tableModel;
    private JTextField champLieu, champDateDebut, champDateFin;
    private JSpinner spinnerPersonnes;
    private JButton boutonRecherche, boutonAccueil, boutonMesReservations, boutonDeconnexion;
    private JPanel panelOptions;
    private List<JCheckBox> checkBoxesOptions;
    private ActionListener actionListener;

    // Données des hébergements
    private ArrayList<Hebergement> hebergementsAffiches;
    private Hebergement hebergementSelectionne;

    // DAO
    private HebergementDAOImpl hebergementDAO;
    private OptionDAOImpl optionDAO;

    // Constructeur
    public VueAccueil(HebergementDAOImpl hebergementDAO, OptionDAOImpl optionDAO) {
        this.hebergementDAO = hebergementDAO;
        this.optionDAO = optionDAO;
        this.checkBoxesOptions = new ArrayList<>();
        
        initialiserFenetre();
        initialiserComposants();
        organiserLayout();
        chargerOptionsDisponibles();
    }

    // Initialiser la fenêtre de l'application
    private void initialiserFenetre() {
        setTitle("Accueil - Rechercher un Hébergement");
        setSize(1300, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // Initialisation des composants de la vue
    private void initialiserComposants() {
        // Initialisation des boutons de navigation
        boutonAccueil = new JButton("Accueil");
        boutonMesReservations = new JButton("Mes Réservations");
        boutonDeconnexion = new JButton("Déconnexion");
        boutonRecherche = new JButton("Rechercher");

        // Action des boutons
        boutonAccueil.setActionCommand("NAV_ACCUEIL");
        boutonMesReservations.setActionCommand("NAV_MES_RESERVATIONS");
        boutonDeconnexion.setActionCommand("DECONNEXION");
        boutonRecherche.setActionCommand("RECHERCHER");

        // Initialisation des champs de recherche
        champLieu = new JTextField();
        spinnerPersonnes = new JSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        champDateDebut = new JTextField("JJ/MM/AAAA");
        champDateFin = new JTextField("JJ/MM/AAAA");

        // Panel de filtrage des options
        panelOptions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelOptions.setBorder(BorderFactory.createTitledBorder("Filtrer par options"));
    }

    // Organiser le layout de la fenêtre principale
    private void organiserLayout() {
        // --- Barre de navigation ---
        JPanel barreNavigation = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barreNavigation.add(boutonAccueil);
        barreNavigation.add(boutonMesReservations);
        barreNavigation.add(Box.createHorizontalStrut(20));
        barreNavigation.add(boutonDeconnexion);

        // --- Barre de recherche ---
        JPanel panelRecherche = new JPanel(new GridLayout(2, 5, 10, 10));
        panelRecherche.setBorder(BorderFactory.createTitledBorder("Rechercher un hébergement"));
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
        tableModel = new DefaultTableModel(new Object[]{
                "Photo", "Nom", "Ville", "Pays", "Catégorie", "Description", "Prix (€)", "Note Moyenne", "Étoiles", "Réduction", "Réserver"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 10; // "Réserver" est cliquable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return ImageIcon.class;
                if (columnIndex == 9 || columnIndex == 10) return JButton.class;
                return String.class;
            }
        };

        tableHebergements = new JTable(tableModel);
        tableHebergements.setRowHeight(80);
        JScrollPane scrollPaneTable = new JScrollPane(tableHebergements);

        tableHebergements.getColumn("Réserver").setCellRenderer(new ButtonRenderer());
        tableHebergements.getColumn("Réserver").setCellEditor(new ButtonEditor(new JCheckBox()));

        // --- Layout global ---
        JPanel panelHaut = new JPanel(new BorderLayout());
        panelHaut.add(barreNavigation, BorderLayout.NORTH);
        panelHaut.add(panelRecherche, BorderLayout.SOUTH);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JScrollPane scrollPaneOptions = new JScrollPane(panelOptions);
        scrollPaneOptions.setPreferredSize(new Dimension(1400, 80));
        panelPrincipal.add(scrollPaneOptions, BorderLayout.NORTH);
        panelPrincipal.add(scrollPaneTable, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(panelHaut, BorderLayout.NORTH);
        add(panelPrincipal, BorderLayout.CENTER);
    }

    // Charger les options disponibles depuis la base de données
    private void chargerOptionsDisponibles() {
        List<Option> options = optionDAO.getAll();
        for (Option opt : options) {
            JCheckBox checkBox = new JCheckBox(opt.getNomOption());
            checkBox.putClientProperty("option", opt);
            panelOptions.add(checkBox);
            checkBoxesOptions.add(checkBox);
        }
    }

    // Retourner les options sélectionnées par l'utilisateur
    public List<Option> getOptionsSelectionnees() {
        List<Option> optionsSelectionnees = new ArrayList<>();
        for (JCheckBox cb : checkBoxesOptions) {
            if (cb.isSelected()) {
                optionsSelectionnees.add((Option) cb.getClientProperty("option"));
            }
        }
        return optionsSelectionnees;
    }

    // Afficher la liste des hébergements dans la table
    public void afficherListeHebergements(ArrayList<Hebergement> hebergements) {
        this.hebergementsAffiches = hebergements;
        tableModel.setRowCount(0);

        int nbPersonnes = (int) spinnerPersonnes.getValue();

        for (Hebergement h : hebergements) {
            if (peutAccueillir(h, nbPersonnes)) {
                afficherHebergement(h);
            }
        }

        tableHebergements.getColumn("Réserver").setCellRenderer(new ButtonRenderer());
        tableHebergements.getColumn("Réserver").setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    // Vérifier si l'hébergement peut accueillir le nombre de personnes
    private boolean peutAccueillir(Hebergement h, int nbPersonnes) {
        boolean peutAccueillir = false;
        if (h.getCategorie().equalsIgnoreCase("hotel")) {
            List<Chambre> chambresDisponibles = hebergementDAO.getChambresDisponibles(h.getId());
            int capaciteTotale = 0;
            for (Chambre chambre : chambresDisponibles) {
                capaciteTotale += chambre.getPlaceMax();
            }
            if (capaciteTotale >= nbPersonnes) {
                peutAccueillir = true;
            }
        } else if (h.getPlace() >= nbPersonnes) {
            peutAccueillir = true;
        }
        return peutAccueillir;
    }

    // Afficher un hébergement spécifique dans la table
    private void afficherHebergement(Hebergement h) {
        double moyenne = hebergementDAO.calculerMoyenneNotes(h.getId());
        int etoiles = (int) Math.round(moyenne);

        h.setNoteMoyenne(moyenne);
        h.setEtoiles(etoiles);
        hebergementDAO.mettreAJourNoteEtEtoiles(h.getId(), moyenne, etoiles);

        String noteStr = (moyenne == 0) ? "Aucune note" : String.format("%.1f / 5", moyenne);
        String reductionStr = "Pas de réduction";
        if (Inscription.getUtilisateurConnecte() != null && "ancien".equals(Inscription.getUtilisateurConnecte().getTypeUtilisateur())) {
            Reduction reduction = hebergementDAO.getReductionParHebergement(h.getId());
            if (reduction != null) {
                reductionStr = reduction.getPourcentage() + "% de réduction";
            }
        }

        ImageIcon image = chargerImageHebergement(h);

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
                reductionStr,
                "Réserver"
        });
    }

    // Charger l'image de l'hébergement
    private ImageIcon chargerImageHebergement(Hebergement h) {
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
        return image;
    }

    // Générer une chaîne d'étoiles basée sur le nombre d'étoiles
    private String genererEtoiles(int etoilesPleines) {
        StringBuilder etoiles = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            etoiles.append(i < etoilesPleines ? "★" : "☆");
        }
        return etoiles.toString();
    }

    // Ajouter des écouteurs pour les actions
    public void ajouterEcouteur(ActionListener listener) {
        if (this.actionListener == null) {
            boutonRecherche.addActionListener(listener);
            boutonDeconnexion.addActionListener(listener);
            boutonAccueil.addActionListener(listener);
            boutonMesReservations.addActionListener(listener);
            this.actionListener = listener;
        }
    }

    // Afficher un message à l'utilisateur
    public void afficherMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    // Récupérer l'hébergement sélectionné
    public Hebergement getHebergementSelectionne() {
        return hebergementSelectionne;
    }
}
