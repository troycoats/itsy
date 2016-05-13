package com.itsy.config;

import java.util.Properties;

/**
 * XMLConfig is a simple class that loads a global configuration file
 **/
public class XMLConfig {
	private static Properties properties;
	private static String propsFile = "/usr/local/WebSphere61/AppServer/customlib/properties/Itsy_Properties.xml";
	static {
		try {
			XMLProperties xmlLoader = new XMLProperties();
			properties = xmlLoader.getPropertiesObject(propsFile);
			xmlLoader = null;
		} catch (Exception e) {
			System.err.println("Serious system error!  Couldn't load properties in file " + propsFile);
			properties = new Properties();
		}
	}
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}
	public static String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
	public static void main(String[] args) {
		System.out.println("Properties file: " + propsFile);
		System.out.println("Properties: " + properties);
	}
}