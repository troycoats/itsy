package com.itsy.config;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * The XMLProperties class parses XML properties files into
 * java.util.Properties objects. This is a simple XMLParser, attributes are not parsed.
 * Properties are stored by their node names separated by a '.'
 * <ol>
 *    <li>
 *    The XMLProperties object has the capability to include the properties
 *    stored in separate XML files into a given properties file.  This allows
 *    you to share configuration details for your important external systems
 *    in only one file, which can be used by any number of applications.  When
 *    you have ten to thirty separate projects running, and each of them has
 *    to have its configuration settings stored somewhere, it can become a
 *    real hassle to keep these settings synchronized between projects.
 *    </li>
 * </ol>
 **/
public class XMLProperties
{
	/**
	 * Internal XML file parser provided by IBM.
	 **/
	//private DOMParser parser;

	/**
	 * Internal TXDocument DOM Object.
	 **/
	private org.w3c.dom.Document doc;

	/**
	 * The Properties object to be built.
	 **/
	private Properties properties = new Properties();



	/**
	 * Name of the XML properties file to load.
	 **/
	private String fileName;

	/**
	 * Full path to the XML properties file.
	 **/
	private String fullPath;

	/**
	 * Constructs an XMLProperties parser object with the given context.  The
	 * context is necessary, because it contains information about the
	 * running application's base directory.  The XML Properties file loaded
	 * will most likely be in the conf directory of the running application.
	 * If not, it can also reside in the library's conf directory.
	 **/
//	public XMLProperties()
//	{
//		properties = new Properties();
//	}
	/**
	 * Returns a Properties object which is constructed as the sum of
	 * both of the given Properties objects.
	 **/
	public Properties addProperties(Properties base, Properties toAdd)
	{
		Properties all = (Properties) base.clone();

		Enumeration en = toAdd.keys();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String value = toAdd.getProperty(key);

			all.put(key, value);
		}
		en = null;

		return all;
	}
	/**
	 * Returns a Properties object that is constructed of the two given
	 * Properties objects, with each key in the second Properties object
	 * being prepended by the given String, and a period character.  This
	 * method is used with recursing through nested SET tags.
	 **/
	public Properties addProperties(Properties base, Properties toAdd, String addPrefix)
	{
		Properties all = (Properties) base.clone();

		Enumeration en = toAdd.keys();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String value = toAdd.getProperty(key);

			all.put(addPrefix+"."+key, value);
		}
		en = null;

		return all;
	}
	/**
	 * Returns the full path to given filename, including the filename
	 * itself.  If the specified filename is not found in the path that
	 * is given, then the file will be looked for in the library's conf
	 * directory.  If not found in either, this method returns null.
	 **/
	public String getPathOf(String xmlFile) {
		String path = null;

		File f = new File(xmlFile);
		if (f.isAbsolute())	{
			if (f.exists())	{
				path = f.getAbsolutePath();
			} else {
				System.err.println("Cannot locate XML Properties file: "+xmlFile);
			}
		} else {

			if (f.exists())	{
				path = f.getAbsolutePath();
			} else {
				System.err.println("Can't locate Properties file: "+xmlFile);
			}

		}
		f = null;
		
		return path;
	}
	/**
	 * Parse the given properties XML file into a Properties object for the
	 * PRODUCTION runMode.  This method wraps getProperties(String, String).
	 **/
	public Properties getPropertiesObject(String fileName)
		throws Exception
	{
		this.fileName = fileName;
		this.fullPath = getPathOf(fileName);
		if (fullPath == null) {
			throw new Exception("Could not find the XML Properties file: "+fileName);
		}

		try{
			File xmlFile = new File( fullPath );
			DataInputStream in = new DataInputStream( new BufferedInputStream( new java.io.FileInputStream( xmlFile ) ) );
			byte[] documentBytes = new byte[ in.available() ];
			in.readFully( documentBytes );
			String xmlString = new String(documentBytes);
			InputSource inputXML = new InputSource( new StringReader( xmlString ) );
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			org.w3c.dom.Document domTree = docBuilder.parse ( inputXML );
			traverse(domTree,"");
			
			xmlFile = null;
			in = null;
			documentBytes = null;
			inputXML = null;
			docBuilderFactory = null;
			docBuilder = null;
			domTree = null;
			
			return properties;
		} catch( Exception e ){
			Exception newException = new Exception( "Exception encountered while creating DOM document from XML: " + e );
			newException.fillInStackTrace();
			throw newException;
		}

	}
/**
	 * The main method of XMLProperties is helpful for debugging your
	 * XML Properties files.  You can pass it a file name, or list of
	 * file names, and it will display what each of the files would
	 * be parsed into in each of the three standard run modes.  If you
	 * also pass in the '-check' option, the files will only be checked
	 * for structural validity, and the key,value pairs for the file(s)
	 * will not be output.
	 **/
public static void main(String[] args) {
	if (args.length == 0) {
		System.out.println("Usage: java XMLProperties (-check) [file list]");
		System.exit(0);
	}

	int i = 0;
	boolean verbose = true;

	if (args[0].equals("-check")) {
		i = 1;
		verbose = false;
	}

	for (; i < args.length; i++) {

		String fileName = args[i];

		if (verbose) {
			System.out.println("Parsing properties file: " + fileName);
		}

		try {
			XMLProperties xml = new XMLProperties();
			Properties props = xml.getPropertiesObject(fileName);

			if (verbose) {
				System.out.println(" SET {");
				String[] list = propertiesToSortedArray(props);
				for (int j = 0; j < list.length; j++) {
					System.out.println("  " + list[j]);
				}
				System.out.println("}\n");
				System.out.println("");
			}

			System.err.println("File " + fileName + " is valid.");

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("File: " + fileName + " contains errors.");
			System.exit(1);
		}
	}
}
	/**
	 * Returns an array of Strings that have been sorted in alphabetical
	 * order, constructed from the given Properties object.  Each
	 * key and value is described in 'key=value' format.
	 **/
	private static String[] propertiesToSortedArray(Properties props)
	{
		String[] list = new String[props.size()];
		int i = 0;

		Enumeration en = props.keys();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String value = props.getProperty(key);

			list[i++] = key+"="+value;
		}
		en = null;

// 		NO NO JDK 1.1.8		Arrays.sort(list);

		return list;
	}
	private void traverse (Node node, String name) {
      int type = node.getNodeType();
      if (type == Node.ELEMENT_NODE) {
         name = name + "." + node.getNodeName();
         //System.out.println (node.getNodeName()+" name to this point:"+name);
      }
      NodeList children = node.getChildNodes();
      if (children != null && children.getLength() > 0) {
         for (int i=0; i< children.getLength(); i++) 
            traverse (children.item(i),name);  
      }
      else{
	  	if(node.getNodeValue() != null && node.getNodeValue().trim().length() > 0){
		  	name = name.substring(1);
		  	properties.put(name,node.getNodeValue().trim());
	  	}
	  }
      children = null;
   }
}
