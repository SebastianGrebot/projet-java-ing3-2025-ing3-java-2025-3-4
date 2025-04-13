package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VueConnexion extends JFrame {
    private JTextField champEmail;
    private JPasswordField champMdp;
    private JButton boutonConnexion, boutonInscription;

    public VueConnexion() {
        setTitle("Connexion");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

        champEmail = new JTextField();
        champMdp = new JPasswordField();

        boutonConnexion = new JButton("Se connecter");
        boutonConnexion.setActionCommand("CONNEXION_PAGE");

        boutonInscription = new JButton("Je n'ai pas de compte");
        boutonInscription.setActionCommand("INSCRIPTION_PAGE");

        panel.add(new JLabel("Email :"));
        panel.add(champEmail);
        panel.add(new JLabel("Mot de passe :"));
        panel.add(champMdp);
        panel.add(boutonConnexion);
        panel.add(boutonInscription);

        add(panel);
        setVisible(true);
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
    }

    public void afficherMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
