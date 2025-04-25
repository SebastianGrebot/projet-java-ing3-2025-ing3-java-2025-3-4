package Vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VueConnexion extends JFrame {
    // Constantes pour les couleurs (cohérentes avec VueAccueil)
    private static final Color COULEUR_PRIMAIRE = new Color(41, 128, 185);
    private static final Color COULEUR_ACCENT = new Color(52, 152, 219);
    private static final Color COULEUR_FOND = new Color(245, 245, 245);
    private static final Color COULEUR_TEXTE = new Color(44, 62, 80);
    private static final Color COULEUR_BOUTON = new Color(52, 152, 219);
    private static final Color COULEUR_BOUTON_HOVER = new Color(41, 128, 185);
    
    private JTextField champEmail;
    private JPasswordField champMdp;
    private JButton boutonConnexion, boutonInscription, boutonConnexionAdmin;
    
    public VueConnexion() {
        setTitle("Connexion - Holiday Homes");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COULEUR_FOND);
        
        // Application du look and feel global
        applyGlobalStyle();
        
        // Conteneur principal avec BorderLayout et marges
        setLayout(new BorderLayout());
        
        // Panel du logo et titre en haut
        JPanel panelHaut = creerPanelTitre();
        
        // Panel principal au centre (formulaire de connexion)
        JPanel panelCentre = creerPanelFormulaire();
        
        // Panel pour le bouton admin en bas
        JPanel panelBas = creerPanelBas();
        
        // Ajout des panneaux au conteneur principal
        add(panelHaut, BorderLayout.NORTH);
        add(panelCentre, BorderLayout.CENTER);
        add(panelBas, BorderLayout.SOUTH);
    }
    
    private void applyGlobalStyle() {
        // Police et couleurs par défaut
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("PasswordField.font", new Font("Segoe UI", Font.PLAIN, 14));
        
        UIManager.put("Panel.background", COULEUR_FOND);
        UIManager.put("Label.foreground", COULEUR_TEXTE);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", COULEUR_TEXTE);
        UIManager.put("PasswordField.background", Color.WHITE);
        UIManager.put("PasswordField.foreground", COULEUR_TEXTE);
    }
    
    private JPanel creerPanelTitre() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COULEUR_PRIMAIRE);
        panel.setBorder(new EmptyBorder(30, 0, 30, 0));
        
        // Logo "HOLIDAY HOMES"
        JLabel logo = new JLabel("HOLIDAY HOMES");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Sous-titre
        JLabel sousTitre = new JLabel("Connectez-vous pour accéder à vos réservations");
        sousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sousTitre.setForeground(new Color(236, 240, 241));
        sousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(logo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(sousTitre);
        
        return panel;
    }
    
    private JPanel creerChampAvecLabel(String labelText, JComponent champ, String placeholder) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COULEUR_FOND);
        panel.setBorder(new EmptyBorder(5, 0, 15, 0));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        champ.setAlignmentX(Component.LEFT_ALIGNMENT);
        champ.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        // Ajouter une bordure et un effet de focus au champ
        if (champ instanceof JTextField) {
            JTextField textField = (JTextField) champ;
            textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            
            // Gestion du placeholder si applicable
            if (placeholder != null && !placeholder.isEmpty()) {
                textField.setText(placeholder);
                textField.setForeground(Color.GRAY);
                
                textField.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if (textField.getText().equals(placeholder)) {
                            textField.setText("");
                            textField.setForeground(COULEUR_TEXTE);
                        }
                    }
                    
                    @Override
                    public void focusLost(FocusEvent e) {
                        if (textField.getText().isEmpty()) {
                            textField.setText(placeholder);
                            textField.setForeground(Color.GRAY);
                        }
                    }
                });
            }
        }
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(champ);
        
        return panel;
    }
    
    private JPanel creerPanelFormulaire() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COULEUR_FOND);
        panel.setBorder(new EmptyBorder(30, 50, 20, 50));
        
        // Titre du formulaire
        JLabel titreFormulaire = new JLabel("Connexion");
        titreFormulaire.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titreFormulaire.setForeground(COULEUR_TEXTE);
        titreFormulaire.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Initialisation des champs
        champEmail = new JTextField();
        champMdp = new JPasswordField();
        
        // Créer des panneaux pour chaque champ avec son label
        JPanel panelEmail = creerChampAvecLabel("Adresse email", champEmail, "Entrez votre email");
        JPanel panelMdp = creerChampAvecLabel("Mot de passe", champMdp, null);
        
        // Bouton de connexion
        boutonConnexion = new JButton("Se connecter");
        boutonConnexion.setActionCommand("CONNEXION_PAGE");
        boutonConnexion.setBackground(COULEUR_BOUTON);
        boutonConnexion.setForeground(Color.WHITE);
        boutonConnexion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boutonConnexion.setBorderPainted(false);
        boutonConnexion.setFocusPainted(false);
        boutonConnexion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonConnexion.setAlignmentX(Component.LEFT_ALIGNMENT);
        boutonConnexion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        // Effet de survol pour le bouton de connexion
        ajouterEffetSurvol(boutonConnexion, COULEUR_BOUTON, COULEUR_BOUTON_HOVER);
        
        // Lien d'inscription
        boutonInscription = new JButton("Pas encore inscrit ? Créer un compte");
        boutonInscription.setActionCommand("INSCRIPTION_PAGE");
        boutonInscription.setBorderPainted(false);
        boutonInscription.setContentAreaFilled(false);
        boutonInscription.setForeground(COULEUR_ACCENT);
        boutonInscription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        boutonInscription.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boutonInscription.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Ajout des composants au panneau
        panel.add(titreFormulaire);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(panelEmail);
        panel.add(panelMdp);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(boutonConnexion);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(boutonInscription);
        
        return panel;
    }
    
    private JPanel creerPanelBas() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(COULEUR_FOND);
        panel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        boutonConnexionAdmin = new JButton("Accès Administrateur");
        boutonConnexionAdmin.setActionCommand("ADMIN_CONNEXION");
        boutonConnexionAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        boutonConnexionAdmin.setBorderPainted(false);
        boutonConnexionAdmin.setContentAreaFilled(false);
        boutonConnexionAdmin.setForeground(new Color(127, 140, 141));
        boutonConnexionAdmin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        panel.add(boutonConnexionAdmin);
        
        return panel;
    }
    
    private void ajouterEffetSurvol(JButton bouton, Color couleurNormale, Color couleurSurvol) {
        bouton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bouton.setBackground(couleurSurvol);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                bouton.setBackground(couleurNormale);
            }
        });
    }
    
    public String getEmail() {
        String email = champEmail.getText();
        // Ne pas retourner le placeholder comme valeur
        return email.equals("Entrez votre email") ? "" : email;
    }
    
    public String getMotDePasse() {
        return new String(champMdp.getPassword());
    }
    
    public void ajouterEcouteur(ActionListener listener) {
        boutonConnexion.addActionListener(listener);
        boutonInscription.addActionListener(listener);
        boutonConnexionAdmin.addActionListener(listener);
    }
    
    public void afficherMessage(String message) {
        UIManager.put("OptionPane.background", COULEUR_FOND);
        UIManager.put("Panel.background", COULEUR_FOND);
        UIManager.put("OptionPane.messageForeground", COULEUR_TEXTE);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 14));
        
        JOptionPane.showMessageDialog(this, message);
    }
}
