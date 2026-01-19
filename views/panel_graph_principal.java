package views;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import models.DonneeImmobiliere;

public class panel_graph_principal extends JPanel
{
	private List<DonneeImmobiliere> donnees;
	private String nomAxeX = "Surface";
	private String nomAxeY = "Prix";
	private boolean afficherGrille = true;

	public panel_graph_principal()
	{
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
	}

	public void setParametres(List<DonneeImmobiliere> donnees, String x, String y, boolean grille)
	{
		this.donnees = donnees;
		this.nomAxeX = x;
		this.nomAxeY = y;
		this.afficherGrille = grille;
		repaint(); // Redessine le composant
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (donnees == null || donnees.isEmpty())
			return;

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int padding = 50;
		int width = getWidth();
		int height = getHeight();

		// 1. Trouver les Min et Max pour l'échelle
		double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;

		for (DonneeImmobiliere d : donnees)
		{
			double valX = d.getValeurParNom(nomAxeX);
			double valY = d.getValeurParNom(nomAxeY);
			if (valX < minX)
				minX = valX;
			if (valX > maxX)
				maxX = valX;
			if (valY < minY)
				minY = valY;
			if (valY > maxY)
				maxY = valY;
		}

		// Ajouter une petite marge aux axes
		maxX *= 1.1;
		maxY *= 1.1;
		if (minX > 0)
			minX *= 0.9;
		else
			minX = 0;
		if (minY > 0)
			minY *= 0.9;
		else
			minY = 0;

		// 2. Dessiner les axes
		g2.setColor(Color.BLACK);
		g2.drawLine(padding, height - padding, width - padding, height - padding); // Axe
																					// X
		g2.drawLine(padding, height - padding, padding, padding); // Axe Y

		g2.drawString(nomAxeX, width - padding - 20, height - padding + 20);
		g2.drawString(nomAxeY, padding - 20, padding - 10);

		// 3. Dessiner la grille (optionnel)
		if (afficherGrille)
		{
			g2.setColor(new Color(220, 220, 220));
			for (int i = 1; i <= 5; i++)
			{
				int y = height - padding - (i * (height - 2 * padding) / 5);
				int x = padding + (i * (width - 2 * padding) / 5);
				g2.drawLine(padding, y, width - padding, y); // Lignes
																// horizontales
				g2.drawLine(x, padding, x, height - padding); // Lignes
																// verticales
			}
		}

		// 4. Dessiner les points
		g2.setColor(Color.BLUE);
		double echelleX = (width - 2 * padding) / (maxX - minX);
		double echelleY = (height - 2 * padding) / (maxY - minY);

		for (DonneeImmobiliere d : donnees)
		{
			double valX = d.getValeurParNom(nomAxeX);
			double valY = d.getValeurParNom(nomAxeY);

			int x = padding + (int) ((valX - minX) * echelleX);
			int y = height - padding - (int) ((valY - minY) * echelleY);

			g2.fillOval(x - 3, y - 3, 6, 6);
		}
	}
}