package eu.fluidforms.groocessing;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.awt.Insets;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.codehaus.groovy.runtime.InvokerInvocationException;

import processing.core.PApplet;

public class Stage extends PApplet {
	GroovyObject groovyObject;
	boolean needsRefresh = false;
	int autoRefreshCounter = 0;
	long lastModified = 0;

	File groovyCodeFile;
	String groovyCode, header, footer;
	boolean error = false;

	public Stage(File file) {
		this.groovyCodeFile = file;
//		String[] jars = Base.librariesClassPath.split(File.pathSeparator);
//		for (int i = 0; i < jars.length; i++) {
//			ClassPathModificator.appendPath(jars[i]);
//			//System.out.println(jars[i]);
//		}

		InputStream is = getClass().getResourceAsStream("/data/groovy.header");
		header = getTextFromFile(is);
		is = getClass().getResourceAsStream("/data/groovy.footer");
		footer = getTextFromFile(is);
	}
	protected void resizeRenderer(int iwidth, int iheight){
        Insets insets = frame.getInsets();
        int w = iwidth + insets.left + insets.right;
        int h = iheight + insets.top + insets.bottom;
        frame.setSize(w,h);

		super.resizeRenderer(iwidth, iheight);
	}
	
	

	public void setup() {
		reloadScript(groovyCodeFile);
		groovyObject.invokeMethod("setup", null);
	}

	public void draw() {
		  try{
			    if(autoRefreshCounter--<=0){
			      if(groovyCodeFile.lastModified() != lastModified){
			    	//System.out.println("reloading");
			        lastModified = groovyCodeFile.lastModified();
			        error = false;
			        reloadScript(groovyCodeFile);
			        //groovyObject.invokeMethod("run", null);
			        groovyObject.invokeMethod("setup", null);
			      }
			    }
//			    if(!error){
			      groovyObject.invokeMethod("draw", null);
//			    }
			  }
			  catch(InvokerInvocationException e){
			    e.printStackTrace();
			    //reloadScript(groovyCode);
			  }
	}

	public void reloadScript(File file) {
		reloadScript(getGroovyCode(file));
	}
	public void reloadScript(String groovyCode) {
		loop();
		if(groovyCode.indexOf("setup()") < 0){
			groovyCode = "def setup(){\nsize(600,600)\n" + groovyCode + "\n}";
		}
		if(groovyCode.indexOf("draw()") < 0){
			groovyCode += "\ndef draw(){}";
		}
		String includes = "";
		String main = "";
		String[] line = groovyCode.split("\n");
		for (int i = 0; i < line.length; i++) {
			if(line[i].indexOf("import ")==0){ // TODO: replace this with a more robust REGEX
				includes+=line[i]+"\n";
			}else{
				main+=line[i]+"\n";
			}
		}
		String headerAndIncludes = header.replace("###IMPORTS###", includes);
		
		try {
			ClassLoader parent = this.getClass().getClassLoader();
			GroovyClassLoader loader = new GroovyClassLoader(parent);
//			loader.addClasspath(Base.librariesClassPath);

			GroovyObject newGroovyObject;
			String script = headerAndIncludes + main + footer;
			//System.out.println(script);
			Class groovyClass = loader.parseClass(script);
			newGroovyObject = (GroovyObject) groovyClass.newInstance();
			newGroovyObject.setProperty("p5", this);
			groovyObject = newGroovyObject;
			this.groovyCode = groovyCode;
		} catch (Exception e) {
			error = true;
			System.err.println(e.getMessage());
			return;
		}
	}
	private String getGroovyCode(File file){
		String text = "";
		try {
			FileInputStream is = new FileInputStream(file);
			text = getTextFromFile(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}

	public String getTextFromFile(InputStream is) {
		String text = "";
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				text += line+"\n";
			}
			br.close();
			isr.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

}
