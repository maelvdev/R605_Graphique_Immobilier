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
		// --- Liste des champs disponibles dans Ville.java ---
		String[] champsDisponibles = new String[] { "Population", "Prix m²", "Densité", "Chômage", "Âge moyen",
				"Code" };

		// Ligne 1 : Les axes
		labelAbscisse = new JLabel("Axe X (Abscisse):");
		comboAbscisse = new JComboBox<>(champsDisponibles);
		comboAbscisse.setSelectedItem("Population"); // Défaut

		labelOrdonnee = new JLabel("Axe Y (Ordonnée):");
		comboOrdonnee = new JComboBox<>(champsDisponibles);
		comboOrdonnee.setSelectedItem("Prix m²"); // Défaut

		// Ligne 2 : Échelles et Options
		labelEchelleX = new JLabel("Éch. X:");
		comboEchelleX = new JComboBox<>(new String[] { "Linéaire", "Logarithmique" });

		labelEchelleY = new JLabel("Éch. Y:");
		comboEchelleY = new JComboBox<>(new String[] { "Linéaire", "Logarithmique" });

		checkAfficherGrille = new JCheckBox("Grille");
		checkAfficherGrille.setSelected(true);

		btnAppliquer = new JButton("Appliquer");
		btnAppliquer.setFont(btnAppliquer.getFont().deriveFont(Font.BOLD));

		btnReinitialiser = new JButton("Reset");

		// --- Layout ---
		setLayout(new GridLayout(2, 1));

		JPanel ligne1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
		JPanel ligne2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

		ligne1.add(labelAbscisse);
		ligne1.add(comboAbscisse);
		ligne1.add(Box.createHorizontalStrut(20));
		ligne1.add(labelOrdonnee);
		ligne1.add(comboOrdonnee);

		ligne2.add(labelEchelleX);
		ligne2.add(comboEchelleX);
		ligne2.add(labelEchelleY);
		ligne2.add(comboEchelleY);
		ligne2.add(new JSeparator(SwingConstants.VERTICAL));
		ligne2.add(checkAfficherGrille);
		ligne2.add(Box.createHorizontalStrut(10));
		ligne2.add(btnAppliquer);
		ligne2.add(btnReinitialiser);

		add(ligne1);
		add(ligne2);
	}

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