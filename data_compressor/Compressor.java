import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

public class Compressor {

	private ArrayList<HashMap<String, String>> dataCodeCommuneNomVilleEtPopulation;

	private HashMap<String, double[]> mapAgeEtPopParCode;

	private HashMap<String, Double> mapDensite;

	private HashMap<String, Double> mapChomage;

	private HashMap<String, Double> sommePrixM2ParCode;

	HashMap<String, Integer> nombreVentesParCode;

	public Compressor() {

		this.sommePrixM2ParCode = new HashMap<>();
		this.nombreVentesParCode = new HashMap<>();

		System.out.println("Chargement des communes littorales...");
		this.dataCodeCommuneNomVilleEtPopulation = Reader.lireCSV(3, new int[]{4, 8}, ';', "files/communes_littorales_2019.csv");

		System.out.println("Chargement des données d'âge...");
		this.mapAgeEtPopParCode = Reader.chargerDonneesAge("files/age-insee-2020.csv");

		System.out.println("Chargement de la densité...");
		this.mapDensite = Reader.chargerDensite("files/insee_rp_hist_1968.csv");

		System.out.println("Chargement du chômage...");
		this.mapChomage = Reader.chargerChomage("files/chomage.csv");

		System.out.println("Chargement des valeurs foncières et de la taille des villes...");
		traiterDvfEtCalculerMoyenne("files/valeurfonciere2024.txt");

		calculerDonneesEtEnvoyerAuWritter();
	}

	private void traiterDvfEtCalculerMoyenne(String cheminDvf) {
		Set<String> codesLittoraux = dataCodeCommuneNomVilleEtPopulation.get(0).keySet();
		System.out.println("Filtre actif sur " + codesLittoraux.size() + " communes littorales.");

		int COL_NATURE = 9;   // Nature mutation (Vente)
		int COL_VALEUR = 10;  // Valeur foncière
		int COL_COMMUNE = 17; // Nom de la commune
		int COL_DEPT = 18;    // Code département (ex: 01)
		int COL_CODE_COM = 19;// Code commune (ex: 76)
		int COL_SURFACE = 38; // Surface reelle bati

		try (BufferedReader br = new BufferedReader(new FileReader(cheminDvf))) {
			String line;
			int countLignes = 0;

			while ((line = br.readLine()) != null) {
				countLignes++;

				String[] row = line.split(Pattern.quote("|"), -1);

				if (row.length <= COL_SURFACE) continue;

				if (!row[COL_NATURE].equals("Vente")) continue;

				String dept = row[COL_DEPT];
				String com = row[COL_CODE_COM];

				String codeInsee = genererCodeInsee(dept, com);

				if (!codesLittoraux.contains(codeInsee)) {
					continue;
				}

				String strPrix = row[COL_VALEUR];
				String strSurface = row[COL_SURFACE];

				try {
					if (strPrix.isEmpty() || strSurface.isEmpty()) continue;

					double prix = Double.parseDouble(strPrix.replace(",", "."));
					double surface = Double.parseDouble(strSurface.replace(",", "."));

					if (surface > 0 && prix > 1000) {
						double prixAuM2 = prix / surface;

						sommePrixM2ParCode.put(codeInsee, sommePrixM2ParCode.getOrDefault(codeInsee, 0.0) + prixAuM2);
						nombreVentesParCode.put(codeInsee, nombreVentesParCode.getOrDefault(codeInsee, 0) + 1);
					}
				} catch (NumberFormatException e) {
				}
			}
			System.out.println("Lecture DVF terminée. " + countLignes + " lignes analysées.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void calculerDonneesEtEnvoyerAuWritter()
	{
		ArrayList<VilleResultat> listeAInserer = new ArrayList<>();

		System.out.println("--- PRIX MOYEN AU M² (LITTORAL) ---");

		for (String codeInsee : sommePrixM2ParCode.keySet()) {
			double totalPrixM2 = sommePrixM2ParCode.get(codeInsee);
			int nombreVentes = nombreVentesParCode.get(codeInsee);
			double prixMoyen = totalPrixM2 / nombreVentes;
			double densite = mapDensite.getOrDefault(codeInsee, 0.0);
			double chomeurs = mapChomage.getOrDefault(codeInsee, 0.0);
			String nomVille = dataCodeCommuneNomVilleEtPopulation.get(0).get(codeInsee);

			double ageMoyen = -1;
			double population = 0;
			if (mapAgeEtPopParCode.containsKey(codeInsee)) {
				double[] stats = mapAgeEtPopParCode.get(codeInsee);
				ageMoyen = stats[0];
				population = stats[1];
			}

			listeAInserer.add(new VilleResultat(codeInsee, nomVille, prixMoyen, ageMoyen, population, densite, chomeurs));
		}

		System.out.println("Nombre de communes prêtes : " + listeAInserer.size());
		CustomWriter.saveListToDatabase(listeAInserer);
	}


	private String genererCodeInsee(String dept, String commune) {
		if (dept == null || commune == null) return "";

		dept = dept.trim();
		commune = commune.trim();

		if (commune.length() == 1) commune = "00" + commune;
		else if (commune.length() == 2) commune = "0" + commune;

		return dept + commune;
	}

	public static void main(String[] args) {
		new Compressor();
	}
}