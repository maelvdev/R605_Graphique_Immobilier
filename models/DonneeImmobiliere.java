package models;

public class DonneeImmobiliere
{
	private int annee;
	private double prix;
	private double surface;
	private int nbPieces;

	public DonneeImmobiliere(int annee, double prix, double surface, int nbPieces)
	{
		this.annee = annee;
		this.prix = prix;
		this.surface = surface;
		this.nbPieces = nbPieces;
	}

	public int getAnnee()
	{
		return annee;
	}

	public double getPrix()
	{
		return prix;
	}

	public double getSurface()
	{
		return surface;
	}

	public int getNbPieces()
	{
		return nbPieces;
	}

	// Méthode utilitaire pour récupérer une valeur selon le nom du champ (pour
	// le graph)
	public double getValeurParNom(String nom)
	{
		switch (nom)
		{
		case "Année":
			return annee;
		case "Prix":
			return prix;
		case "Surface":
			return surface;
		case "Nombre de pièces":
			return nbPieces;
		default:
			return 0;
		}
	}
}