package singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import java.util.Properties;

public class DBConnection {
	private static final String JDBC_URL = "jdbc:mysql://localhost:3306/alimentosKM0";
	private static final String USER = "root";
	private static final String PASS = "1234";
	private static Connection instance = null;

	private DBConnection() {
	}

	public static Connection getConnection() throws SQLException {

		if (instance == null) {
			/*Properties props = new Properties();
			props.put("user", "root");
			props.put("password", "1234");*/
			
			try {
				instance = DriverManager.getConnection(JDBC_URL, USER, PASS);
			} catch (SQLException ex) {
				System.out.println("Error al abrir la conexion");
				ex.printStackTrace();
			} catch (Exception ex) {
				System.out.println("Error al cargar el driver");
				ex.printStackTrace();
			}
		}
		return instance;
	}
}
