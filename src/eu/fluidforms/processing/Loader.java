package eu.fluidforms.processing;

import java.io.InputStream;
import java.util.Vector;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import eu.fluidforms.geom.FSolid;
import eu.fluidforms.geom.FTriangle;
import eu.fluidforms.io.IOUtils;
import eu.fluidforms.io.STLReaderFast;
import eu.fluidforms.parser.OBJLexer;
import eu.fluidforms.parser.OBJParser;
import eu.fluidforms.utils.Utils;

public class Loader {

	public static Vector<FTriangle> load(String fileName){
		if(fileName.indexOf("null")==0){
			fileName = fileName.substring(5);
		}
		String ext = Utils.getFileExtension(fileName);
		if (ext.equalsIgnoreCase("stl")) {
			System.out.println("reading "+fileName);
			return STLReaderFast.parse(fileName);
		} else if (ext.equalsIgnoreCase("obj")) {
			InputStream inputStream = null;
			inputStream = IOUtils.openStream(fileName);
			OBJLexer lexer = new OBJLexer(inputStream);
			OBJParser parser = new OBJParser(lexer);
			try {
				parser.parse();
			} catch (RecognitionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TokenStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Vector<FTriangle> triangles = new Vector<FTriangle>();
			for (int i = parser.solids.size() - 1; i >= 0; i--) {
				FSolid solid = (FSolid) parser.solids.get(0);
				System.out.println("Writing Triangles: "
						+ solid.getTriangles().size() + "...");

				triangles.addAll(solid.getTriangles());
			}
			return triangles;
		}
		return new Vector<FTriangle>();
	}
}
