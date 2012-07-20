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
 
// $ANTLR 2.7.6 (2005-12-22): "STL.g" -> "STLParser.java"$

package eu.fluidforms.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.Vector;
	
import eu.fluidforms.geom.FSolid;
import eu.fluidforms.geom.FTriangle;
import eu.fluidforms.geom.FVertex;

public interface STLParserTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int LITERAL_solid = 4;
	int IDENT = 5;
	int LITERAL_endsolid = 6;
	int LITERAL_end = 7;
	int LITERAL_vertex = 8;
	int FLOAT = 9;
	int LITERAL_normal = 10;
	int LITERAL_outer = 11;
	int LITERAL_loop = 12;
	int LITERAL_endloop = 13;
	int LITERAL_facet = 14;
	int LITERAL_endfacet = 15;
	int DIGIT = 16;
	int WS = 17;
}
