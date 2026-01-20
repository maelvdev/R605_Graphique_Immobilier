import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class CustomWriter {

	public CustomWriter(HashMap<String, String> dataVilles, HashMap<String, String> dataPrix, HashMap<String, String> dataSurface) {
		saveToDatabase(dataVilles, dataPrix, dataSurface);
	}

	private void saveToDatabase(HashMap<String, String> dataVilles, HashMap<String, String> dataPrix, HashMap<String, String> dataSurface) {
		String url = "jdbc:mysql://localhost:3306/ta_base_de_donnee";
		String user = "root";
		String password = "password";

		String sql = "INSERT INTO Ville (code_commune, nom_ville, prix_m2, surface_m2) VALUES (?, ?, ?, ?)";

		try (Connection conn = DriverManager.getConnection(url, user, password);
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {

			conn.setAutoCommit(false);

			for (String codeCommune : dataVilles.keySet()) {

				String nomVille = dataVilles.get(codeCommune);
				String strPrix = dataPrix.get(codeCommune);
				String strSurface = dataSurface.get(codeCommune);

				pstmt.setString(1, codeCommune);
				pstmt.setString(2, nomVille);

				if (strPrix != null && !strPrix.isEmpty()) {
					try {
						double prixVal = Double.parseDouble(strPrix.replace(",", "."));
						pstmt.setDouble(3, prixVal);
					} catch (NumberFormatException e) {
						pstmt.setObject(3, null);
					}
				} else {
					pstmt.setObject(3, null);
				}

				if (strSurface != null && !strSurface.isEmpty()) {
					try {
						double surfaceVal = Double.parseDouble(strSurface.replace(",", "."));
						pstmt.setDouble(4, surfaceVal);
					} catch (NumberFormatException e) {
						pstmt.setObject(4, null);
					}
				} else {
					pstmt.setObject(4, null);
				}

				pstmt.addBatch();
			}
			pstmt.executeBatch();
			conn.commit();

			System.out.println("Importation terminée. " + dataVilles.size() + " lignes traitées.");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}