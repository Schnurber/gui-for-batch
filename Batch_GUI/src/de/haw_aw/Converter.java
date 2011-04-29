package de.haw_aw;
import java.io.FileInputStream;
import java.util.Properties;
import javax.swing.JOptionPane;


public class Converter {

	/**
	 * Batch-converter
	 * @Author Dieter Meiller 2011
	 * For Eclipse: Export->Runnable JAR-File
	 */
	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Batch-GUI");
	    new Converter().convert();
	}
	public void convert(){
		Properties configFile = new Properties();
	    try {
	    	String pName=System.getProperty("user.dir")+System.getProperty("file.separator")+"converter.properties";
	    	String os=System.getProperty("os.name").toLowerCase();
	    	//Mac OS X
	    	configFile.load(new FileInputStream(pName));
	    	
			String sFiles=(os.indexOf( "windows" ) < 0) ? configFile.getProperty("MAC") : configFile.getProperty("WIN");
			String[] files=sFiles.split(";");
			//String bar = configFile.getProperty("MAC");
			boolean nat=configFile.getProperty("NATIVE").equalsIgnoreCase("true");
			//After loading 
			new ConverterFrame(System.getProperty("os.name"),files,nat);
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Config-file could not be loaded!");
			e.printStackTrace();
		}
	}
}
