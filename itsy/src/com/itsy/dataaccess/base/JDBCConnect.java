package com.itsy.dataaccess.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.itsy.config.XMLConfig;
import com.itsy.exception.SmartException;
import com.itsy.exception.SystemException;
import com.itsy.util.TextUtil;

public class JDBCConnect {
	
	public static Connection getItsyConnection() throws SmartException {
		String url = XMLConfig.getProperty("PROPERTY.data_sources.itsy_ds.jdbcurl");
		String user = XMLConfig.getProperty("PROPERTY.data_sources.itsy_ds.username");
		String pass = XMLConfig.getProperty("PROPERTY.data_sources.itsy_ds.password");
		Connection conn = null;
		try {
			Properties connectionProperties = new Properties();
			connectionProperties.setProperty("user", user);
			if (!TextUtil.isEmpty(pass))
				connectionProperties.setProperty("password", pass);
			
			Class.forName("org.sqlite.JDBC");
			
			conn = DriverManager.getConnection(url, connectionProperties);
		} catch (ClassNotFoundException e) {
			throw new SystemException(e, "The jdbc drivers are not in the CLASSPATH.");
		} catch (SQLException e) {
			throw new SystemException(e, "Could not connect to database.  The database appears to be off-line.");
		}
		return conn;
	}
}