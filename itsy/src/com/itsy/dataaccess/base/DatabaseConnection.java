package com.itsy.dataaccess.base;import java.sql.Connection;/** * This classes handles getting a connection to the database */public class DatabaseConnection {		private DatabaseConnection() {		super();	}			public static Connection getItsyConnection() throws Exception {		return JDBCConnect.getItsyConnection();	}	}