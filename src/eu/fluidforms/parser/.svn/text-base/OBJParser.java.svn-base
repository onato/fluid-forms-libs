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
 
// $ANTLR 2.7.6 (2005-12-22): "OBJ.g" -> "OBJParser.java"$

package eu.fluidforms.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.util.Vector;
	
import eu.fluidforms.geom.FSolid;
import eu.fluidforms.geom.FTriangle;
import eu.fluidforms.geom.FVertex;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;

public class OBJParser extends antlr.LLkParser       implements OBJParserTokenTypes
 {

	public Vector solids  = new Vector();
	public Vector verticies  = new Vector();
	private FSolid tmpSolid;
	private FTriangle tri;
	private String textureName;
	private Vector tmpVerticies = new Vector();
	
  // Define a main
  public static void main(String[] args) {
    // Use a try/catch block for parser exceptions
    try {
      // if we have at least one command-line argument
      if (args.length > 0 ) {
        System.err.println("Parsing...");

        // for each directory/file specified on the command line
        for(int i=0; i< args.length;i++){
          doFile(new File(args[i])); // parse it
        }
      }
      else
        System.err.println("Usage: java OBJParser <file name>");

    }
    catch(Exception e) {
      System.err.println("exception: "+e);
      e.printStackTrace(System.err);   // so we can get stack trace
    }
  }


  // This method decides what action to take based on the type of
  //   file we are looking at
  public static void doFile(File f) throws Exception {
    // If this is a directory, walk each file/dir in that directory
    if (f.isDirectory()) {
      String files[] = f.list();
      for(int i=0; i < files.length; i++)
        doFile(new File(f, files[i]));
    }

    // otherwise, if this is a java file, parse it!
    else if ((f.getName().length()>5) &&
             f.getName().substring(f.getName().length()-4).equals(".obj")) {
      System.err.println("-------------------------------------------");
      System.err.println(f.getAbsolutePath());
      parseFile(new FileInputStream(f));
    }
  }

  // Here's where we do the real work...
  public static void parseFile(InputStream s) throws Exception {
    try {
      // Create a scanner that reads from the input stream passed to us
      OBJLexer lexer = new OBJLexer(s);

      // Create a parser that reads from the scanner
      OBJParser parser = new OBJParser(lexer);

      // start parsing at the compilationUnit rule
      parser.parse();
      System.out.println(parser.solids.size());
    }
    catch (Exception e) {
      System.err.println("parser exception: "+e);
      e.printStackTrace();   // so we can get stack trace		
    }
  }

protected OBJParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public OBJParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected OBJParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public OBJParser(TokenStream lexer) {
  this(lexer,2);
}

public OBJParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
}

	public final void parse() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			{
			int _cnt3=0;
			_loop3:
			do {
				if ((LA(1)==LITERAL_g)) {
					solid();
				}
				else {
					if ( _cnt3>=1 ) { break _loop3; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt3++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
	}
	
	public final void solid() throws RecognitionException, TokenStreamException {
		
		Token  n = null;
		
		try {      // for error handling
			{
			match(LITERAL_g);
			}
			
						tmpSolid = new FSolid();
					
			{
			_loop7:
			do {
				if ((LA(1)==IDENT)) {
					n = LT(1);
					match(IDENT);
					
									tmpSolid.setName(n.getText());
								
				}
				else {
					break _loop7;
				}
				
			} while (true);
			}
			{
			_loop9:
			do {
				if ((LA(1)==LITERAL_mtllib)) {
					materialLib();
				}
				else {
					break _loop9;
				}
				
			} while (true);
			}
			{
			_loop11:
			do {
				if ((LA(1)==LITERAL_usemtl)) {
					material();
				}
				else {
					break _loop11;
				}
				
			} while (true);
			}
			{
			int _cnt13=0;
			_loop13:
			do {
				if ((LA(1)==LITERAL_v)) {
					vertex();
				}
				else {
					if ( _cnt13>=1 ) { break _loop13; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt13++;
			} while (true);
			}
			{
			_loop15:
			do {
				if ((LA(1)==LITERAL_vt)) {
					textureMapping();
				}
				else {
					break _loop15;
				}
				
			} while (true);
			}
			{
			int _cnt17=0;
			_loop17:
			do {
				if ((LA(1)==LITERAL_f)) {
					face();
				}
				else {
					if ( _cnt17>=1 ) { break _loop17; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt17++;
			} while (true);
			}
			
						solids.insertElementAt(tmpSolid, solids.size());
					
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_1);
		}
	}
	
	public final void materialLib() throws RecognitionException, TokenStreamException {
		
		Token  t = null;
		
		try {      // for error handling
			match(LITERAL_mtllib);
			{
			t = LT(1);
			match(IDENT);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
	}
	
	public final void material() throws RecognitionException, TokenStreamException {
		
		Token  t = null;
		
		try {      // for error handling
			match(LITERAL_usemtl);
			{
			switch ( LA(1)) {
			case IDENT:
			{
				t = LT(1);
				match(IDENT);
				
								textureName = t.getText();
							
				break;
			}
			case LITERAL_usemtl:
			case LITERAL_v:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
	}
	
	public final void vertex() throws RecognitionException, TokenStreamException {
		
		Token  x = null;
		Token  y = null;
		Token  z = null;
		
		try {      // for error handling
			match(LITERAL_v);
			x = LT(1);
			match(FLOAT);
			y = LT(1);
			match(FLOAT);
			z = LT(1);
			match(FLOAT);
			
					float dx = Float.valueOf(x.getText()).floatValue();
					float dy = Float.valueOf(y.getText()).floatValue();
					float dz = Float.valueOf(z.getText()).floatValue();
					FVertex v = new FVertex(dx, dy, dz);
					verticies.insertElementAt(v, verticies.size());
				
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_4);
		}
	}
	
	public final void textureMapping() throws RecognitionException, TokenStreamException {
		
		Token  u = null;
		Token  v = null;
		Token  w = null;
		
		try {      // for error handling
			match(LITERAL_vt);
			u = LT(1);
			match(FLOAT);
			v = LT(1);
			match(FLOAT);
			w = LT(1);
			match(FLOAT);
			
				//System.out.println("vt:"+u.getText());
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
	}
	
	public final void face() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LITERAL_f);
			{
			_loop27:
			do {
				if ((LA(1)==FLOAT)) {
					faceVector();
				}
				else {
					break _loop27;
				}
				
			} while (true);
			}
			
			/*
					tri = new ATriangle();
					int i1 = Integer.valueOf(x.getText()).intValue();
					int i2 = Integer.valueOf(y.getText()).intValue();
					int i3 = Integer.valueOf(z.getText()).intValue();
					AVertex v1 = (AVertex) verticies.elementAt(i1 - 1);
					AVertex v2 = (AVertex) verticies.elementAt(i2 - 1);
					AVertex v3 = (AVertex) verticies.elementAt(i3 - 1);
					tri.add(v1);
					tri.add(v2);
					tri.add(v3);
					tmpSolid.addTriangle(tri);
			*/
					//Vector triangulated = Triangulate.triangulate(tmpVerticies);
					//tmpSolid.addTriangles(triangulated);
					//System.out.println(triangulated.size());
					//System.out.println("v="+triangulated.size());
					
					tri = new FTriangle();
					tri.add((FVertex)tmpVerticies.elementAt(0));
					tri.add((FVertex)tmpVerticies.elementAt(1));
					tri.add((FVertex)tmpVerticies.elementAt(2));
					tmpSolid.addTriangle(tri);
			
					if(tmpVerticies.size()==4){
						tri = new FTriangle();
						tri.add((FVertex)tmpVerticies.elementAt(0));
						tri.add((FVertex)tmpVerticies.elementAt(2));
						tri.add((FVertex)tmpVerticies.elementAt(3));
						tmpSolid.addTriangle(tri);
					}
			
			
					tmpVerticies.removeAllElements();
				
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
	}
	
	public final void normal() throws RecognitionException, TokenStreamException {
		
		Token  x = null;
		Token  y = null;
		Token  z = null;
		
		try {      // for error handling
			match(LITERAL_n);
			x = LT(1);
			match(FLOAT);
			y = LT(1);
			match(FLOAT);
			z = LT(1);
			match(FLOAT);
			
					float dx = Float.valueOf(x.getText()).floatValue();
					float dy = Float.valueOf(y.getText()).floatValue();
					float dz = Float.valueOf(z.getText()).floatValue();
					FVertex v = new FVertex(dx, dy, dz);
					tri.setNorm(v);
				
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_0);
		}
	}
	
	public final void faceVector() throws RecognitionException, TokenStreamException {
		
		Token  x = null;
		Token  t = null;
		Token  n = null;
		
		try {      // for error handling
			x = LT(1);
			match(FLOAT);
			{
			switch ( LA(1)) {
			case SLASH:
			{
				match(SLASH);
				t = LT(1);
				match(FLOAT);
				{
				switch ( LA(1)) {
				case SLASH:
				{
					match(SLASH);
					n = LT(1);
					match(FLOAT);
					break;
				}
				case EOF:
				case LITERAL_g:
				case FLOAT:
				case LITERAL_f:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case EOF:
			case LITERAL_g:
			case FLOAT:
			case LITERAL_f:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			
					int i = Integer.valueOf(x.getText()).intValue();
					FVertex v = (FVertex) verticies.elementAt(i - 1);
					tmpVerticies.insertElementAt(v, tmpVerticies.size());
				
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"g\"",
		"IDENT",
		"\"mtllib\"",
		"\"usemtl\"",
		"\"v\"",
		"FLOAT",
		"\"vt\"",
		"\"n\"",
		"\"f\"",
		"SLASH",
		"DIGIT",
		"WS",
		"COMMENT"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 18L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 448L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 384L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 5376L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 5120L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 4114L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 4626L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	
	}
