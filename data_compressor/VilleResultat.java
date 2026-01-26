public class VilleResultat {
	String code;
	String nom;
	double prixMoyen;
	double ageMoyen;
	double population;
	double densite;
	double chomeurs;

	public VilleResultat(String code, String nom, double prixMoyen, double ageMoyen, double population, double densite, double chomeurs) {
		this.code = code;
		this.nom = nom;
		this.prixMoyen = prixMoyen;
		this.ageMoyen = ageMoyen;
		this.population = population;
		this.densite = densite;
		this.chomeurs = chomeurs;
	}
}