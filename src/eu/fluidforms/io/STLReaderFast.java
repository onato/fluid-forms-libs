/*
  (c) copyright
  
  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
 */
 
package eu.fluidforms.io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import eu.fluidforms.geom.FTriangle;
import eu.fluidforms.geom.FVertex;
import eu.fluidforms.utils.Utils;

public class STLReaderFast {
	static String sFloatPattern = "(-?[0-9]+[\\.]?[0-9]*e?E?-?\\+[0-9]*)";// require the last part if we find an 'e'

	public static Vector<FTriangle> parseBinarySTL(String stlFile) {
		Vector<FTriangle> triangles = new Vector<FTriangle>();
		
		
		try {
			DataInputStream data_in    = new DataInputStream (createInput(stlFile) );
			
			byte[] bComment = new byte[80];
			data_in.read(bComment);
			String comment = new String(bComment);
			System.out.println(comment);
			byte[] bNumberOfTriangles = new byte[4];
			data_in.read(bNumberOfTriangles);
			int numberOfTriangles = BitUtils.toInt(bNumberOfTriangles);
			System.out.println("Number of Triangles: " + numberOfTriangles);
			for (int i = 0; i < numberOfTriangles; i++) {
				FVertex norm = new FVertex();
				FVertex v1 = new FVertex();
				FVertex v2 = new FVertex();
				FVertex v3 = new FVertex();

				norm.x = bytesToFloat(data_in);
				norm.y = bytesToFloat(data_in);
				norm.z = bytesToFloat(data_in);

				int places = 5;
				v1.x = Utils.round(bytesToFloat(data_in), places);
				v1.y = Utils.round(bytesToFloat(data_in), places);
				v1.z = Utils.round(bytesToFloat(data_in), places);

				v2.x = Utils.round(bytesToFloat(data_in), places);
				v2.y = Utils.round(bytesToFloat(data_in), places);
				v2.z = Utils.round(bytesToFloat(data_in), places);

				v3.x = Utils.round(bytesToFloat(data_in), places);
				v3.y = Utils.round(bytesToFloat(data_in), places);
				v3.z = Utils.round(bytesToFloat(data_in), places);
				data_in.read(new byte[2]);
				
				FTriangle tri = new FTriangle(v1, v2, v3);
				tri.setNorm(norm);
				triangles.add(tri);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
		
		return triangles;
	}
	private static float bytesToFloat(DataInputStream data_in) throws IOException{
		byte[] numBuff = new byte[4];
		data_in.read(numBuff);
        return BitUtils.toFloat(numBuff); 
	}
	public static Vector<FTriangle> parse(String stlFile) {
		Vector<FTriangle> triangles = new Vector<FTriangle>();
		triangles = tryToParseAsAscii(createReader(stlFile));
		if(triangles.size()==0){
			return parseBinarySTL(stlFile);
		}
		return triangles;
	}
	
	public static Vector<FTriangle> tryToParseAsAscii(BufferedReader reader) {
		Pattern vertexPattern = Pattern.compile("\\s*vertex\\s*+" + sFloatPattern + "\\s*+" + sFloatPattern + "\\s*+" + sFloatPattern + "\\s*");
		Pattern triangleStartPattern = Pattern.compile("\\s*outer\\s*+loop\\s*");
		Pattern triangleEndPattern = Pattern.compile("\\s*endloop\\s*");
		Pattern firstLinePattern = Pattern.compile("\\s*solid\\s*(.*)");

		Vector<FTriangle> triangles = new Vector<FTriangle>();
		try{

			
			boolean inTri = false;
			int vertexCount = 0;
			int lineNumber = 0;
			FVertex[] verts = new FVertex[3];		
			String line = null;
			int vertCount = 0;
			while ((line = reader.readLine()) != null) {
				lineNumber++;
				if(lineNumber==1){
					if(!firstLinePattern.matcher(line).matches()){
						System.out.println("Appears to be a binary STL file.");
						break;
					}
				}
				if(inTri && vertexCount<=2){
					Matcher matcher = vertexPattern.matcher(line);
					if(matcher.matches()){
						line = line.substring(line.indexOf("vertex"));
						verts[vertexCount++%3] = new FVertex(
								Float.parseFloat(matcher.group(1)),
								Float.parseFloat(matcher.group(2)),
								Float.parseFloat(matcher.group(3))
								);
						vertCount++;
					}else{
						System.out.println(line);
					}
				}else if(triangleStartPattern.matcher(line).matches()){
					inTri = true;
				}else if(triangleEndPattern.matcher(line).matches()){
					if(vertexCount!=3){
						System.err.println("ERROR :: line " + lineNumber + " :: Triangle was missing a vertex or it was incorrectly formated.");
					}else{
						triangles.add(new FTriangle(verts[0],verts[1],verts[2]));
					}
					vertexCount = 0;
					inTri = false;
				}
			}
			reader.close();
			
			System.out.println(triangles.size());
		}catch(Exception e){
			e.printStackTrace();
		}
		return triangles;
	}
	
	public static void main(String args[]){
		STLReaderFast.parse("c:/__cassius/CASSIUS~12-29-08--18-04-59_small.stl");
		Pattern floatPattern = Pattern.compile(sFloatPattern);
		String[] stringsToPass = {"1", "-1", "1.2", "-1.2", "0.1", "1.2e006", "1.2e-6"};
		String[] stringsToFail = {".0", "00.1", "1.", "1.2e", "1.2e-", "--1"};

		for (int i = 0; i < stringsToPass.length; i++) {
			String floatStr = stringsToPass[i];
			if(!floatPattern.matcher(floatStr).matches() && javaSaysOK(floatStr)){
				System.err.println(floatStr + " should have passed");
			}else{
				System.out.println(floatStr + " passed");
			}
		}
		for (int i = 0; i < stringsToFail.length; i++) {
			String floatStr = stringsToFail[i];
			if(floatPattern.matcher(floatStr).matches() && !javaSaysOK(floatStr)){
				System.err.println(floatStr + " should have failed");
			}else{
				System.out.println(floatStr + " failed");
			}
		}
		
		

	}
	private static boolean javaSaysOK(String floatStr){
		try{
			Float.valueOf(floatStr);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	  /**
	   * Copied from PApplet. If it was static I wouldn't have to copy it :-(
	   * I want to read lines from a file. I have RSI from typing these
	   * eight lines of code so many times.
	   */
	  public static BufferedReader createReader(String filename) {
	    try {
	      InputStream is = createInput(filename);
	      if (is == null) {
	        System.err.println(filename + " does not exist or could not be read");
	        return null;
	      }
	      return createReader(is);

	    } catch (Exception e) {
	      if (filename == null) {
	        System.err.println("Filename passed to reader() was null");
	      } else {
	        System.err.println("Couldn't create a reader for " + filename);
	      }
	    }
	    return null;
	  }
	  
	  /**
	   * I want to read lines from a stream. If I have to type the
	   * following lines any more I'm gonna send Sun my medical bills.
	   */
	  static public BufferedReader createReader(InputStream input) {
	    InputStreamReader isr = null;
	    try {
	      isr = new InputStreamReader(input, "UTF-8");
	    } catch (UnsupportedEncodingException e) { }  // not gonna happen
	    return new BufferedReader(isr);
	  }

	  /**
	   * Simplified method to open a Java InputStream.
	   * <P>
	   * This method is useful if you want to use the facilities provided
	   * by PApplet to easily open things from the data folder or from a URL,
	   * but want an InputStream object so that you can use other Java
	   * methods to take more control of how the stream is read.
	   * <P>
	   * If the requested item doesn't exist, null is returned.
	   * (Prior to 0096, die() would be called, killing the applet)
	   * <P>
	   * For 0096+, the "data" folder is exported intact with subfolders,
	   * and openStream() properly handles subdirectories from the data folder
	   * <P>
	   * If not online, this will also check to see if the user is asking
	   * for a file whose name isn't properly capitalized. This helps prevent
	   * issues when a sketch is exported to the web, where case sensitivity
	   * matters, as opposed to Windows and the Mac OS default where
	   * case sensitivity is preserved but ignored.
	   * <P>
	   * It is strongly recommended that libraries use this method to open
	   * data files, so that the loading sequence is handled in the same way
	   * as functions like loadBytes(), loadImage(), etc.
	   * <P>
	   * The filename passed in can be:
	   * <UL>
	   * <LI>A URL, for instance openStream("http://processing.org/");
	   * <LI>A file in the sketch's data folder
	   * <LI>Another file to be opened locally (when running as an application)
	   * </UL>
	   */
	  public static InputStream createInput(String filename) {
		  Loader loader = new Loader();
	    InputStream input = loader.createInputRaw(filename);
	    if ((input != null) && filename.toLowerCase().endsWith(".gz")) {
	      try {
	        return new GZIPInputStream(input);
	      } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	      }
	    }
	    return input;
	  }
}
	  
	  class Loader{
	  /**
	   * Call openStream() without automatic gzip decompression.
	   */
	  public InputStream createInputRaw(String filename) {
	    InputStream stream = null;

	    if (filename == null) return null;

	    if (filename.length() == 0) {
	      // an error will be called by the parent function
	      //System.err.println("The filename passed to openStream() was empty.");
	      return null;
	    }

	    // safe to check for this as a url first. this will prevent online
	    // access logs from being spammed with GET /sketchfolder/http://blahblah
	    try {
	      URL url = new URL(filename);
	      stream = url.openStream();
	      return stream;

	    } catch (MalformedURLException mfue) {
	      // not a url, that's fine

	    } catch (FileNotFoundException fnfe) {
	      // Java 1.5 likes to throw this when URL not available. (fix for 0119)
	      // http://dev.processing.org/bugs/show_bug.cgi?id=403

	    } catch (IOException e) {
	      // changed for 0117, shouldn't be throwing exception
	      e.printStackTrace();
	      //System.err.println("Error downloading from URL " + filename);
	      return null;
	      //throw new RuntimeException("Error downloading from URL " + filename);
	    }

	    // Moved this earlier than the getResourceAsStream() checks, because
	    // calling getResourceAsStream() on a directory lists its contents.
	    // http://dev.processing.org/bugs/show_bug.cgi?id=716
	    try {
	      // First see if it's in a data folder. This may fail by throwing
	      // a SecurityException. If so, this whole block will be skipped.
	      File file = new File(filename);
	      if (file.isDirectory()) {
	        return null;
	      }
	      if (file.exists()) {
	        try {
	          // handle case sensitivity check
	          String filePath = file.getCanonicalPath();
	          String filenameActual = new File(filePath).getName();
	          // make sure there isn't a subfolder prepended to the name
	          String filenameShort = new File(filename).getName();
	          // if the actual filename is the same, but capitalized
	          // differently, warn the user.
	          //if (filenameActual.equalsIgnoreCase(filenameShort) &&
	          //!filenameActual.equals(filenameShort)) {
	          if (!filenameActual.equals(filenameShort)) {
	            throw new RuntimeException("This file is named " +
	                                       filenameActual + " not " +
	                                       filename + ". Rename the file " +
	            "or change your code.");
	          }
	        } catch (IOException e) { }
	      }

	      // if this file is ok, may as well just load it
	      stream = new FileInputStream(file);
	      if (stream != null) return stream;

	      // have to break these out because a general Exception might
	      // catch the RuntimeException being thrown above
	    } catch (IOException ioe) {
	    } catch (SecurityException se) { }

	    
   System.out.println("try the class loader stuuf");
	    // Using getClassLoader() prevents java from converting dots
	    // to slashes or requiring a slash at the beginning.
	    // (a slash as a prefix means that it'll load from the root of
	    // the jar, rather than trying to dig into the package location)
	    ClassLoader cl = getClass().getClassLoader();
	    filename = filename.replace('\\', '/');
	    
	    // by default, data files are exported to the root path of the jar.
	    // (not the data folder) so check there first.
	    System.out.println("getResourceAsStream('"+filename+"')");
	    stream = cl.getResourceAsStream(filename);
	    if (stream != null) {
	      String cn = stream.getClass().getName();
	      // this is an irritation of sun's java plug-in, which will return
	      // a non-null stream for an object that doesn't exist. like all good
	      // things, this is probably introduced in java 1.5. awesome!
	      // http://dev.processing.org/bugs/show_bug.cgi?id=359
	      if (!cn.equals("sun.plugin.cache.EmptyInputStream")) {
	        return stream;
	      }
	    }

	    // When used with an online script, also need to check without the
	    // data folder, in case it's not in a subfolder called 'data'.
	    // http://dev.processing.org/bugs/show_bug.cgi?id=389
	    stream = cl.getResourceAsStream(filename);
	    if (stream != null) {
	      String cn = stream.getClass().getName();
	      if (!cn.equals("sun.plugin.cache.EmptyInputStream")) {
	        return stream;
	      }
	    }

	    try {
	      // attempt to load from a local file, used when running as
	      // an application, or as a signed applet
	      try {  // first try to catch any security exceptions
	        try {
	          stream = new FileInputStream(filename);
	          if (stream != null) return stream;
	        } catch (IOException e2) { }

	        try {
	          stream = new FileInputStream(filename);
	          if (stream != null) return stream;
	        } catch (Exception e) { }  // ignored

	        try {
	          stream = new FileInputStream(filename);
	          if (stream != null) return stream;
	        } catch (IOException e1) { }

	      } catch (SecurityException se) { }  // online, whups

	    } catch (Exception e) {
	      //die(e.getMessage(), e);
	      e.printStackTrace();
	    }
	    return null;
	  }
	  }
	  
