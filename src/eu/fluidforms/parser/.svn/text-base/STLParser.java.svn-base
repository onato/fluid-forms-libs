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

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;

public class STLParser extends antlr.LLkParser       implements STLParserTokenTypes
 {

	public Vector solids  = new Vector();
	private FSolid tmpSolid;
	private FTriangle tri;
	
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
        System.err.println("Usage: java STLParser <file name>");

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
             f.getName().substring(f.getName().length()-4).equals(".stl")) {
      System.err.println("-------------------------------------------");
      System.err.println(f.getAbsolutePath());
      parseFile(new FileInputStream(f));
    }
  }

  // Here's where we do the real work...
  public static void parseFile(InputStream s) throws Exception {
    try {
      // Create a scanner that reads from the input stream passed to us
      STLLexer lexer = new STLLexer(s);

      // Create a parser that reads from the scanner
      STLParser parser = new STLParser(lexer);

      // start parsing at the compilationUnit rule
      parser.parse();
      System.out.println(parser.solids.size());
    }
    catch (Exception e) {
      System.err.println("parser exception: "+e);
      e.printStackTrace();   // so we can get stack trace		
    }
  }

protected STLParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public STLParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected STLParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public STLParser(TokenStream lexer) {
  this(lexer,1);
}

public STLParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
}

	public final void parse() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			{
			int _cnt3=0;
			_loop3:
			do {
				if ((LA(1)==LITERAL_solid)) {
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
			match(LITERAL_solid);
			}
			
						tmpSolid = new FSolid();
					
			{
			switch ( LA(1)) {
			case IDENT:
			{
				n = LT(1);
				match(IDENT);
				
								tmpSolid.setName(n.getText());
							
				break;
			}
			case LITERAL_facet:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			int _cnt8=0;
			_loop8:
			do {
				if ((LA(1)==LITERAL_facet)) {
					facet();
				}
				else {
					if ( _cnt8>=1 ) { break _loop8; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt8++;
			} while (true);
			}
			{
			switch ( LA(1)) {
			case LITERAL_endsolid:
			{
				match(LITERAL_endsolid);
				break;
			}
			case LITERAL_end:
			{
				match(LITERAL_end);
				match(LITERAL_solid);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			
				solids.insertElementAt(tmpSolid, solids.size());
					
			{
			switch ( LA(1)) {
			case IDENT:
			{
				match(IDENT);
				break;
			}
			case EOF:
			case LITERAL_solid:
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
			recover(ex,_tokenSet_1);
		}
	}
	
	public final void facet() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LITERAL_facet);
			
					tri = new FTriangle();
				
			{
			_loop16:
			do {
				if ((LA(1)==LITERAL_normal)) {
					normal();
				}
				else {
					break _loop16;
				}
				
			} while (true);
			}
			{
			int _cnt18=0;
			_loop18:
			do {
				if ((LA(1)==LITERAL_outer)) {
					triangle();
				}
				else {
					if ( _cnt18>=1 ) { break _loop18; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt18++;
			} while (true);
			}
			match(LITERAL_endfacet);
			
					tmpSolid.addTriangle(tri);
				
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_2);
		}
	}
	
	public final void vertex() throws RecognitionException, TokenStreamException {
		
		Token  x = null;
		Token  y = null;
		Token  z = null;
		
		try {      // for error handling
			match(LITERAL_vertex);
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
					tri.add(v);
				
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
	}
	
	public final void normal() throws RecognitionException, TokenStreamException {
		
		Token  x = null;
		Token  y = null;
		Token  z = null;
		
		try {      // for error handling
			match(LITERAL_normal);
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
			recover(ex,_tokenSet_4);
		}
	}
	
	public final void triangle() throws RecognitionException, TokenStreamException {
		
		
		try {      // for error handling
			match(LITERAL_outer);
			match(LITERAL_loop);
			vertex();
			vertex();
			vertex();
			match(LITERAL_endloop);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_5);
		}
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"solid\"",
		"IDENT",
		"\"endsolid\"",
		"\"end\"",
		"\"vertex\"",
		"an integer value",
		"\"normal\"",
		"\"outer\"",
		"\"loop\"",
		"\"endloop\"",
		"\"facet\"",
		"\"endfacet\"",
		"DIGIT",
		"WS"
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
		long[] data = { 16576L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 8448L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 3072L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 34816L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	
	}
