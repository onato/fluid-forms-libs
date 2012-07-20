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

import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;




public class VelocityExporter extends Exporter3DAbstract {

	public VelocityExporter(String fileName) {
		super(fileName, false);
	}
	public void init(){}
	
	public void write() {
		String FileExtension = getFileExtension().toLowerCase();
		String templateFile = "templates/" + FileExtension + ".vm";
		try {

			Properties p = new Properties();
			p.setProperty( "resource.loader", "class" );
			p.setProperty( "class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
			Velocity.init(p);
			
			
			VelocityContext context = new VelocityContext();
			context.put("NamePrepend", namePrepend);
			context.put("solids", scene.members);
			Template template = null;
			try {
				template = Velocity.getTemplate(templateFile);
			} catch (ResourceNotFoundException rnfe) {
				System.out.println("Error : cannot find template "
						+ templateFile);
			} catch (ParseErrorException pee) {
				System.out.println("Error : Syntax error in template "
						+ templateFile + ":" + pee);
			}

			// BufferedWriter writer = new BufferedWriter(
			// new OutputStreamWriter(System.out));

			if (template != null) {
				template.merge(context, getWriter());
			}

			closeWriter();
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
