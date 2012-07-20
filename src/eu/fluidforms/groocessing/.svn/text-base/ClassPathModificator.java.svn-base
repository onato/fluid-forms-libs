package eu.fluidforms.groocessing;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassPathModificator {
	private static final Class[] urlParams = new Class[] { URL.class };

	public static void appendPath(URL newURL) {
			URLClassLoader classLoader = (URLClassLoader) ClassLoader
					.getSystemClassLoader();
			try {
				Method meth = URLClassLoader.class.getDeclaredMethod("addURL",
						urlParams);
				meth.setAccessible(true);
				meth.invoke(classLoader, new Object[] { newURL });
			} catch (Exception err) {
				System.err.println("Couldn't find " + newURL);
			}
	}

	public static void appendPath(String newPath) {
		appendPath(new File(newPath));
	}

	public static void appendPath(File fileObj) {
		try {
			appendPath(fileObj.toURL());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addDir(String s)  {
		try {
			// This enables the java.library.path to be modified at runtime
			// From a Sun engineer at http://forums.sun.com/thread.jspa?threadID=707176
			//
			Field field = ClassLoader.class.getDeclaredField("usr_paths");
			field.setAccessible(true);
			String[] paths = (String[])field.get(null);
			for (int i = 0; i < paths.length; i++) {
				if (s.equals(paths[i])) {
					return;
				}
			}
			String[] tmp = new String[paths.length+1];
			System.arraycopy(paths,0,tmp,0,paths.length);
			tmp[paths.length] = s;
			field.set(null,tmp);
			System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + s);
		} catch (IllegalAccessException e) {
			System.err.println("Failed to get permissions to set library path");
		} catch (NoSuchFieldException e) {
			System.err.println("Failed to get field handle to set library path");
		} catch (Exception err) {
			System.err.println("Couldn't find " + s);
		}
	}
}