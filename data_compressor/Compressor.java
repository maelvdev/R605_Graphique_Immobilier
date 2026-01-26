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

	public Compressor() {
		System.out.println("Chargement des communes littorales...");
		this.dataCodeCommuneNomVilleEtPopulation = Reader.lireCSV(3, new int[]{4, 8}, ';', "files/communes_littorales_2019.csv");

		System.out.println("Chargement des données d'âge...");
		this.mapAgeEtPopParCode = chargerDonneesAge("files/age-insee-2020.csv");

		System.out.println("Chargement de la densité...");
		this.mapDensite = chargerDensite("files/insee_rp_hist_1968.csv");

		System.out.println("Chargement du chômage...");
		this.mapChomage = chargerChomage("files/chomage.csv");

		traiterDvfEtCalculerMoyenne("files/valeurfonciere2024.txt");
	}

	private HashMap<String, Double> chargerChomage(String path) {
		HashMap<String, Double> res = new HashMap<>();

		try (BufferedReader br = new BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(path), java.nio.charset.StandardCharsets.UTF_8))) {

			for(int i = 0; i < 12; i++) br.readLine();

			String line;
			while ((line = br.readLine()) != null) {
				String[] row = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

				if (row.length < 16) continue;

				String code = row[4].trim();
				if (code.length() == 4) code = "0" + code;

				String valStr = row[15];

				try {
					valStr = valStr.replace(",", ".");
					valStr = valStr.replaceAll("[^0-9.]", "");

					if (!valStr.isEmpty()) {
						double val = Double.parseDouble(valStr);
						res.put(code, val);
					}
				} catch (NumberFormatException e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}


	private HashMap<String, Double> chargerDensite(String path) {
		HashMap<String, Double> res = new HashMap<>();
		HashMap<String, Integer> mapAnnee = new HashMap<>();

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			for (int i = 0; i < 5; i++) br.readLine();

			String line;
			while ((line = br.readLine()) != null) {
				String[] row = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

				if (row.length < 4) continue;

				String code = row[0].replace("\"", "").trim();

				try {
					int annee = Integer.parseInt(row[2].trim());

					String valStr = row[3].replace("\"", "").replace(",", ".");
					double densite = Double.parseDouble(valStr);

					if (!mapAnnee.containsKey(code) || annee > mapAnnee.get(code)) {
						mapAnnee.put(code, annee);
						res.put(code, densite);
					}

				} catch (NumberFormatException e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private HashMap<String, double[]> chargerDonneesAge(String path) {
		HashMap<String, double[]> resultats = new HashMap<>();

		double[] poidsAge = {1, 4, 8, 14, 21, 32, 47, 59.5, 72, 85};
		int[] colsF = {5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
		int[] colsH = {16, 17, 18, 19, 20, 21, 22, 23, 24, 25};

		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line = br.readLine();

			while ((line = br.readLine()) != null) {
				String[] row = line.split(Pattern.quote(","), -1);

				if (row.length < 26) continue;

				String codeInsee = row[0].replace("\"", "").trim();
				if (codeInsee.length() == 4) codeInsee = "0" + codeInsee;

				double totalPopulation = 0;
				double sommePonderee = 0;

				for (int i = 0; i < poidsAge.length; i++) {
					try {
						double popF = row[colsF[i]].isEmpty() ? 0 : Double.parseDouble(row[colsF[i]]);
						double popH = row[colsH[i]].isEmpty() ? 0 : Double.parseDouble(row[colsH[i]]);

						// Calcul de la population pour cette tranche
						double popTranche = popF + popH;

						// On additionne pour la population totale
						totalPopulation += popTranche;
						// On additionne pour la moyenne d'âge
						sommePonderee += (popTranche * poidsAge[i]);

					} catch (NumberFormatException e) {
					}
				}

				if (totalPopulation > 0) {
					double ageMoyen = sommePonderee / totalPopulation;
					// On stocke les deux valeurs : [0] = Age, [1] = Population
					resultats.put(codeInsee, new double[]{ageMoyen, totalPopulation});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultats;
	}

	private void traiterDvfEtCalculerMoyenne(String cheminDvf) {
		HashMap<String, Double> sommePrixM2ParCode = new HashMap<>();
		HashMap<String, Integer> nombreVentesParCode = new HashMap<>();

		ArrayList<VilleResultat> listeAInserer = new ArrayList<>();

		Set<String> codesLittoraux = dataCodeCommuneNomVilleEtPopulation.get(0).keySet();
		System.out.println("Filtre actif sur " + codesLittoraux.size() + " communes littorales.");

		// INDICES DVF (Basé sur votre header fourni)
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

		// Nettoyage
		dept = dept.trim();
		commune = commune.trim();

		// Cas classique : Dept (2 chars) + Commune (3 chars)
		// Si la commune est "76", il faut la transformer en "076"
		if (commune.length() == 1) commune = "00" + commune;
		else if (commune.length() == 2) commune = "0" + commune;

		// Cas DOM-TOM (Dept 3 chars ex: 974)
		return dept + commune;
	}

	public static void main(String[] args) {
		new Compressor();
	}
}