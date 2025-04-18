package Vue;

import Modele.Hebergement;
import Modele.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VuePaiement extends JFrame {

    private JLabel labelNomUser;
    private JLabel labelNomHebergement;
    private JLabel labelDates;
    private JLabel labelPrixTotal;

    private JButton boutonPayer;

    public VuePaiement(User user, Hebergement hebergement, Date dateDebut, Date dateFin, double prixTotal) {
        setTitle("Paiement");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        labelNomUser = new JLabel("Nom : " + user.getPrenom() + " " + user.getNom());
        labelNomHebergement = new JLabel("Hébergement : " + hebergement.getNom());
        labelDates = new JLabel("Du " + sdf.format(dateDebut) + " au " + sdf.format(dateFin));
        labelPrixTotal = new JLabel("Total à payer : " + prixTotal + " €");

        boutonPayer = new JButton("Payer");
        boutonPayer.setActionCommand("PAYER");

        labelNomUser.setFont(new Font("SansSerif", Font.PLAIN, 16));
        labelNomHebergement.setFont(new Font("SansSerif", Font.PLAIN, 16));
        labelDates.setFont(new Font("SansSerif", Font.PLAIN, 16));
        labelPrixTotal.setFont(new Font("SansSerif", Font.BOLD, 18));
        boutonPayer.setFont(new Font("SansSerif", Font.BOLD, 16));

        panel.add(labelNomUser);
        panel.add(labelNomHebergement);
        panel.add(labelDates);
        panel.add(labelPrixTotal);
        panel.add(boutonPayer);

        add(panel);
    }

    public void ajouterEcouteur(ActionListener listener) {
        boutonPayer.addActionListener(listener);
    }

    public JButton getBoutonPayer() {
        return boutonPayer;
    }
}
