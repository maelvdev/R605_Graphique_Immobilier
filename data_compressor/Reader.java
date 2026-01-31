import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Reader {

	public static ArrayList<HashMap<String, String>> lireCSV(int colCle, int[] colData, char separateur, String file) {

		ArrayList<HashMap<String,String>> res = new ArrayList<>();
		for (int k = 0; k < colData.length; k++) {
			res.add(new HashMap<>());
		}

		try {
			Scanner sc = new Scanner(new File(file));
			int compteurLigne = 0;

			while(sc.hasNext())
			{
				String ligne = sc.nextLine();
				if (ligne.trim().isEmpty()) continue;

				String[] tabDatas = ligne.split(Pattern.quote(String.valueOf(separateur)));

				if (colCle == -1 || tabDatas.length > colCle) {
					
					String cle;
					if (colCle == -1) {
						cle = String.valueOf(compteurLigne++);
					} else {
						cle = tabDatas[colCle];
					}

					for(int j = 0 ; j < colData.length ; j++)
					{
						if(colData[j] < tabDatas.length) {
							res.get(j).put(cle, tabDatas[colData[j]]);
						}
					}
				}
			}
			sc.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return res;
	}

	public static HashMap<String, Double> chargerChomage(String path) {
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


	public static HashMap<String, Double> chargerDensite(String path) {
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

	public static HashMap<String, double[]> chargerDonneesAge(String path) {
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

						double popTranche = popF + popH;

						totalPopulation += popTranche;

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
}