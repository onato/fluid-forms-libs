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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class IOUtils {
	public IOUtils(){

	}

	/**
	 * Adds a trailing File.seperatorChar if the path does not have one. 
	 * @param path
	 * @return The string with, if needed, a trailing slash appended to it.
	 */
	public static String trailingSlash(String path){
		char lastChar = path.charAt(path.length() - 1);
		if(lastChar != '/' || lastChar != '\\'){
			return path + File.separatorChar;
		}
		return path;
	}

	
	/**
	 * Simplified method to open a Java InputStream.
	 * <P>
	 * This method is useful if you want to use the facilities provided by
	 * PApplet to easily open things from the data folder or from a URL, but
	 * want an InputStream object so that you can use other Java methods to take
	 * more control of how the stream is read.
	 * <P>
	 * If the requested item doesn't exist, null is returned. (Prior to 0096,
	 * die() would be called, killing the applet)
	 * <P>
	 * For 0096+, the "data" folder is exported intact with subfolders, and
	 * openStream() properly handles subdirectories from the data folder
	 * <P>
	 * If not online, this will also check to see if the user is asking for a
	 * file whose name isn't properly capitalized. This helps prevent issues
	 * when a sketch is exported to the web, where case sensitivity matters, as
	 * opposed to Windows and the Mac OS default where case sensitivity is
	 * preserved but ignored.
	 * <P>
	 * It is strongly recommended that libraries use this method to open data
	 * files, so that the loading sequence is handled in the same way as
	 * functions like loadBytes(), loadImage(), etc.
	 * <P>
	 * The filename passed in can be:
	 * <UL>
	 * <LI>A URL, for instance openStream("http://processing.org/");
	 * <LI>A file in the sketch's data folder
	 * <LI>Another file to be opened locally (when running as an application)
	 * </UL>
	 */
	public static InputStream openStream(String filename) {
		String sketchPath = System.getProperty("user.dir");

		InputStream stream = null;

		if (filename == null)
			return null;

		if (filename.length() == 0) {
			// an error will be called by the parent function
			// System.err.println("The filename passed to openStream() was
			// empty.");
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
			// Java 1.5 likes to throw this when URL not available. (fix for
			// 0119)
			// http://dev.processing.org/bugs/show_bug.cgi?id=403

		} catch (IOException e) {
			// changed for 0117, shouldn't be throwing exception
			e.printStackTrace();
			// System.err.println("Error downloading from URL " + filename);
			return null;
			// throw new RuntimeException("Error downloading from URL " +
			// filename);
		}

		// using getClassLoader() prevents java from converting dots
		// to slashes or requiring a slash at the beginning.
		// (a slash as a prefix means that it'll load from the root of
		// the jar, rather than trying to dig into the package location)
		ClassLoader cl = IOUtils.class.getClassLoader();

		// by default, data files are exported to the root path of the jar.
		// (not the data folder) so check there first.
		stream = cl.getResourceAsStream("data/" + filename);
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

		// when used with an online script, also need to check without the
		// data folder, in case it's not in a subfolder called 'data'
		// http://dev.processing.org/bugs/show_bug.cgi?id=389
		stream = cl.getResourceAsStream(filename);
		if (stream != null) {
			String cn = stream.getClass().getName();
			if (!cn.equals("sun.plugin.cache.EmptyInputStream")) {
				return stream;
			}
		}

		// handle case sensitivity check
		try {
			// first see if it's in a data folder
			File file = new File(dataPath(filename));
			if (!file.exists()) {
				// next see if it's just in this folder
				file = new File(sketchPath, filename);
			}
			if (file.exists()) {
				try {
					String filePath = file.getCanonicalPath();
					String filenameActual = new File(filePath).getName();
					// make sure there isn't a subfolder prepended to the name
					String filenameShort = new File(filename).getName();
					// if the actual filename is the same, but capitalized
					// differently, warn the user.
					// if (filenameActual.equalsIgnoreCase(filenameShort) &&
					// !filenameActual.equals(filenameShort)) {
					if (!filenameActual.equals(filenameShort)) {
						throw new RuntimeException("This file is named "
								+ filenameActual + " not " + filename
								+ ". Re-name it " + "or change your code.");
					}
				} catch (IOException e) {
				}
			}

			// if this file is ok, may as well just load it
			stream = new FileInputStream(file);
			if (stream != null)
				return stream;

			// have to break these out because a general Exception might
			// catch the RuntimeException being thrown above
		} catch (IOException ioe) {
		} catch (SecurityException se) {
		}

		try {
			// attempt to load from a local file, used when running as
			// an application, or as a signed applet
			try { // first try to catch any security exceptions
				try {
					stream = new FileInputStream(dataPath(filename));
					if (stream != null)
						return stream;
				} catch (IOException e2) {
				}

				try {
					stream = new FileInputStream(sketchPath(filename));
					if (stream != null)
						return stream;
				} catch (Exception e) {
				} // ignored

				try {
					stream = new FileInputStream(filename);
					if (stream != null)
						return stream;
				} catch (IOException e1) {
				}

			} catch (SecurityException se) {
			} // online, whups

		} catch (Exception e) {
			// die(e.getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Return a full path to an item in the data folder.
	 * <p>
	 * In this method, the data path is defined not as the applet's actual data
	 * path, but a folder titled "data" in the sketch's working directory. This
	 * is because in an application, the "data" folder is exported as part of
	 * the jar file, and it's not as though you're gonna write into the jar file
	 * itself. If you need to get things out of the jar file, you should use
	 * openStream().
	 */
	public static String dataPath(String where) {
		String sketchPath = System.getProperty("user.dir");

		// isAbsolute() could throw an access exception, but so will writing
		// to the local disk using the sketch path, so this is safe here.
		if (new File(where).isAbsolute())
			return where;

		return sketchPath + File.separator + "data" + File.separator + where;
	}

	/**
	 * Prepend the sketch folder path to the filename (or path) that is passed
	 * in. External libraries should use this function to save to the sketch
	 * folder. <p/> Note that when running as an applet inside a web browser,
	 * the sketchPath will be set to null, because security restrictions prevent
	 * applets from accessing that information. <p/> This will also cause an
	 * error if the sketch is not inited properly, meaning that init() was never
	 * called on the PApplet when hosted my some other main() or by other code.
	 * For proper use of init(), see the examples in the main description text
	 * for PApplet.
	 */
	public static String sketchPath(String where) {
		String sketchPath = System.getProperty("user.dir");
		if (sketchPath == null) {
			throw new RuntimeException("The applet was not inited properly, "
					+ "or security restrictions prevented "
					+ "it from determining its path.");
		}
		// isAbsolute() could throw an access exception, but so will writing
		// to the local disk using the sketch path, so this is safe here.
		// for 0120, added a try/catch anyways.
		try {
			if (new File(where).isAbsolute())
				return where;
		} catch (Exception e) {
		}

		return sketchPath + File.separator + where;
	}

}
