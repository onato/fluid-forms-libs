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
 
package eu.fluidforms.utils;
/*
 to compile so that java 1.1 can play the applet use...
 		javac -target 1.1 -source 1.2 -Xlint:deprecation CheckJavaVersion.java
 
 */

import java.applet.Applet;
import java.awt.Color;
import java.awt.Label;
import java.net.URL;
import java.net.URLEncoder;

/**
 * A little utility applet that reads the version and vendor of the JRE running it 
 * and then calling the supplied URL with the results.
 * 
 * @author Stephen Williams
 *
 */
public class CheckJavaVersion extends Applet {
	 static final long serialVersionUID = 84931890341890L;

	public void init() {
        setBackground(Color.white);
        Color fontColour = new Color(240, 240, 240);
        Label tfVersionVendor = new Label("Java Version: " + System.getProperty("java.version") + "  from " + System.getProperty("java.vendor"));
        tfVersionVendor.setForeground(fontColour);
        add(tfVersionVendor);
        
		try {
			String redirect_url = getParameter("url");
			String params = getParameter("params");
			if(redirect_url!=null && !redirect_url.equals("")){
				params += "&version="+System.getProperty("java.version");
				params += "&vendor="+URLEncoder.encode(System.getProperty("java.vendor"), "UTF-8");
				URL url = new URL(redirect_url+"?"+params);
				url.getContent();
			} else {
				System.err.println("You must provide a url parameter in order to have a script called."); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
