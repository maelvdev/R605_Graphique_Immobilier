package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateurDonnees
{
	public static List<DonneeImmobiliere> genererDonneesAleatoires(int quantite)
	{
		List<DonneeImmobiliere> liste = new ArrayList<>();
		Random rand = new Random();

		for (int i = 0; i < quantite; i++)
		{
			int annee = 2000 + rand.nextInt(24); // Entre 2000 et 2024
			double surface = 20 + rand.nextDouble() * 130; // Entre 20 et 150 m2
			int pieces = (int) (surface / 25) + 1; // Estimation grossière des
													// pièces
			// Le prix dépend un peu de la surface pour que le graph soit
			// cohérent
			double prix = (surface * 3000) + (rand.nextDouble() * 50000);

			liste.add(new DonneeImmobiliere(annee, prix, surface, pieces));
		}
		return liste;
	}
}