package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VueConnexion extends JFrame {
    private JTextField champEmail;
    private JPasswordField champMdp;
    private JButton boutonConnexion, boutonInscription, boutonConnexionAdmin;

    public VueConnexion() {
        setTitle("Connexion");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Conteneur principal en BorderLayout
        setLayout(new BorderLayout(10, 10));

        // Panel principal au centre (formulaire de connexion)
        JPanel panelCentre = new JPanel(new GridLayout(3, 1, 10, 10));

        champEmail = new JTextField();
        champMdp = new JPasswordField();

        boutonConnexion = new JButton("Se connecter");
        boutonConnexion.setActionCommand("CONNEXION_PAGE");

        boutonInscription = new JButton("Je n'ai pas de compte");
        boutonInscription.setActionCommand("INSCRIPTION_PAGE");

        panelCentre.add(new JLabel("Email :"));
        panelCentre.add(champEmail);
        panelCentre.add(new JLabel("Mot de passe :"));
        panelCentre.add(champMdp);
        panelCentre.add(boutonConnexion);
        panelCentre.add(boutonInscription);

        // Panel pour le bouton admin en bas
        JPanel panelBas = new JPanel(new FlowLayout(FlowLayout.CENTER));
        boutonConnexionAdmin = new JButton("Connexion Admin");
        boutonConnexionAdmin.setActionCommand("ADMIN_CONNEXION");
        boutonConnexionAdmin.setFont(new Font("Arial", Font.PLAIN, 10));
        panelBas.add(boutonConnexionAdmin);

        // Ajout au conteneur principal
        add(panelCentre, BorderLayout.CENTER);
        add(panelBas, BorderLayout.SOUTH);
    }

    public String getEmail() {
        return champEmail.getText();
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
        JOptionPane.showMessageDialog(this, message);
    }
}
