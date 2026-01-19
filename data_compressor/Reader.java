import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern; // Import nécessaire

public class Reader {

	public static ArrayList<HashMap<String, String>> lireCSV(int colCle, int[] colData, char separateur, String file) {

		ArrayList<HashMap<String,String>> res = new ArrayList<>();
		for (int k = 0; k < colData.length; k++) {
			res.add(new HashMap<>());
		}

		try {
			Scanner sc = new Scanner(new File(file));

			while(sc.hasNext())
			{
				String ligne = sc.nextLine();
				if (ligne.trim().isEmpty()) continue;

				String[] tabDatas = ligne.split(Pattern.quote(String.valueOf(separateur)));

				if(tabDatas.length > colCle) {
					String cle = tabDatas[colCle];

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
}