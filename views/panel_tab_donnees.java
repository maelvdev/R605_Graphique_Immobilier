package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import models.DonneeImmobiliere;

public class panel_tab_donnees extends JPanel
{
	private JTable table;
	private DefaultTableModel model;

	public panel_tab_donnees()
	{
		setLayout(new BorderLayout());

		// Définition des colonnes
		String[] colonnes = { "Année", "Prix (€)", "Surface (m²)", "Nb Pièces" };
		model = new DefaultTableModel(colonnes, 0);

		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);

		add(scrollPane, BorderLayout.CENTER);
	}

	public void mettreAJourDonnees(List<DonneeImmobiliere> donnees)
	{
		model.setRowCount(0); // Vider le tableau
		for (DonneeImmobiliere d : donnees)
		{
			model.addRow(new Object[] { d.getAnnee(), String.format("%.2f", d.getPrix()),
					String.format("%.2f", d.getSurface()), d.getNbPieces() });
		}
	}
}