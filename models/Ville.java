package models;

public class Ville
{
	private final int code;
	private final String nom;
	private final boolean littoral;
	private final boolean montagnes;
	private final int population;
	private final double densite;
	private final int ageMoyen;
	private final double chomage;
	private final double prixM2;

	public Ville(int code, String nom, boolean littoral, boolean montagnes, int population, double densite,
			int ageMoyen, double chomage, double prixM2)
	{
		this.code = code;
		this.nom = nom;
		this.littoral = littoral;
		this.montagnes = montagnes;
		this.population = population;
		this.densite = densite;
		this.ageMoyen = ageMoyen;
		this.chomage = chomage;
		this.prixM2 = prixM2;
	}

	public int getCode()
	{
		return code;
	}

	public String getNom()
	{
		return nom;
	}

	public boolean isLittoral()
	{
		return littoral;
	}

	public boolean isMontagnes()
	{
		return montagnes;
	}

	public int getPopulation()
	{
		return population;
	}

	public double getDensite()
	{
		return densite;
	}

	public int getAgeMoyen()
	{
		return ageMoyen;
	}

	public double getChomage()
	{
		return chomage;
	}

	public double getPrixM2()
	{
		return prixM2;
	}

	public double getValeurParNom(String nomChamp)
	{
		switch (nomChamp)
		{
		case "Code":
			return code;
		case "Population":
			return population;
		case "Densité":
			return densite;
		case "Âge moyen":
			return ageMoyen;
		case "Chômage":
			return chomage;
		case "Prix m²":
			return prixM2;
		default:
			return 0;
		}
	}
}
