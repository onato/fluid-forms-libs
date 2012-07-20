/*
  FluidIO is a simple framework for importing and exporting different file formats.
  
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
 
package eu.fluidforms.parser;

import java.util.Vector;



import eu.fluidforms.geom.FSolid;
import eu.fluidforms.io.IOUtils;

public class ObjLoader {
	OBJParser parser;

	public ObjLoader() {

	}

	public void load(String objPath) {
		IOUtils ioUtils = new IOUtils();

		try {
			// Create a scanner that reads from the input stream passed to us
			OBJLexer lexer = new OBJLexer(ioUtils.openStream(objPath));
			// Create a parser that reads from the scanner
			parser = new OBJParser(lexer);

			// start parsing at the compilationUnit rule
			parser.parse();

		} catch (Exception e) {
			System.err.println("parser exception: " + e);
			e.printStackTrace(); // so we can get stack trace
		}
	}

	public Vector getSolids(){
		if(parser!=null){
			return parser.solids;
		}
		System.out.println("you must call the load() function to load a file " +
				"before calling getSolids().");
		return new Vector(); 
	}
	
	public int getNumberOfSolids(){
		return parser.solids.size();
	}
	
	public FSolid getSolid(int i){
		return (FSolid) parser.solids.elementAt(i);
	}
}
