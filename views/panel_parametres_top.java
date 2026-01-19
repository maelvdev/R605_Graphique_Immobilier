package views;

import java.awt.*;
import javax.swing.*;

public class panel_parametres_top extends JPanel
{
	private JComboBox<String> comboAbscisse;
	private JComboBox<String> comboOrdonnee;
	private JComboBox<String> comboEchelleX;
	private JComboBox<String> comboEchelleY;
	private JButton btnAppliquer;
	private JButton btnReinitialiser;
	private JCheckBox checkAfficherLegende;
	private JCheckBox checkAfficherGrille;
	private JLabel labelAbscisse;
	private JLabel labelOrdonnee;
	private JLabel labelEchelleX;
	private JLabel labelEchelleY;

	public panel_parametres_top()
	{
		initComponents();
	}

	private void initComponents()
	{
		// --- Création des composants ---

		// Ligne 1 : Les axes
		labelAbscisse = new JLabel("Axe X (Abscisse):");
		comboAbscisse = new JComboBox<>(new String[] { "Année", "Prix", "Surface", "Nombre de pièces" });

		labelOrdonnee = new JLabel("Axe Y (Ordonnée):");
		comboOrdonnee = new JComboBox<>(new String[] { "Prix", "Surface", "Nombre de pièces", "Année" });

		// Ligne 2 : Échelles et Options
		labelEchelleX = new JLabel("Éch. X:");
		comboEchelleX = new JComboBox<>(new String[] { "Linéaire", "Logarithmique" });

		labelEchelleY = new JLabel("Éch. Y:");
		comboEchelleY = new JComboBox<>(new String[] { "Linéaire", "Logarithmique" });

		checkAfficherLegende = new JCheckBox("Légende");
		checkAfficherLegende.setSelected(true);

		checkAfficherGrille = new JCheckBox("Grille");
		checkAfficherGrille.setSelected(true);

		btnAppliquer = new JButton("Appliquer");
		// On met le bouton en gras pour montrer qu'il est important
		btnAppliquer.setFont(btnAppliquer.getFont().deriveFont(Font.BOLD));

		btnReinitialiser = new JButton("Reset");

		// --- Mise en page (Layout) ---

		// On veut 2 lignes l'une sur l'autre
		setLayout(new GridLayout(2, 1));

		// Création de deux sous-panneaux pour organiser les lignes
		JPanel ligne1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
		JPanel ligne2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

		// Ajout des éléments à la Ligne 1
		ligne1.add(labelAbscisse);
		ligne1.add(comboAbscisse);
		ligne1.add(Box.createHorizontalStrut(20)); // Espace vide de séparation
		ligne1.add(labelOrdonnee);
		ligne1.add(comboOrdonnee);

		// Ajout des éléments à la Ligne 2
		ligne2.add(labelEchelleX);
		ligne2.add(comboEchelleX);
		ligne2.add(labelEchelleY);
		ligne2.add(comboEchelleY);
		ligne2.add(new JSeparator(SwingConstants.VERTICAL)); // Barre verticale
																// déco
		ligne2.add(checkAfficherLegende);
		ligne2.add(checkAfficherGrille);
		ligne2.add(Box.createHorizontalStrut(10)); // Espace
		ligne2.add(btnAppliquer);
		ligne2.add(btnReinitialiser);

		// Ajout des deux lignes au panneau principal
		add(ligne1);
		add(ligne2);
	}

	// --- Getters (Inchangés) ---

	public String getAbscisse()
	{
		return (String) comboAbscisse.getSelectedItem();
	}

	public String getOrdonnee()
	{
		return (String) comboOrdonnee.getSelectedItem();
	}

	public String getEchelleX()
	{
		return (String) comboEchelleX.getSelectedItem();
	}

	public String getEchelleY()
	{
		return (String) comboEchelleY.getSelectedItem();
	}

	public boolean isLegendVisible()
	{
		return checkAfficherLegende.isSelected();
	}

	public boolean isGridVisible()
	{
		return checkAfficherGrille.isSelected();
	}

	public JButton getBtnAppliquer()
	{
		return btnAppliquer;
	}

	public JButton getBtnReinitialiser()
	{
		return btnReinitialiser;
	}
}