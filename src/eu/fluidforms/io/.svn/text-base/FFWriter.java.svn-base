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

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import eu.fluidforms.geom.FTriangle;
import eu.fluidforms.geom.FVertex;


/**
 * @author Stephen Williams (stephen.williams@algodes.com)
 * 
 * 
 *  
 */
public class FFWriter {

    OutputStream os;
    BufferedWriter bw;

    //ArrayList text;

    protected boolean binary;

    String fileName;

    Vector<FTriangle> triangles;
    Vector<FVertex>verticies;

    String comment, fileType;

    public FFWriter(){
    	this(true);
    }
    
    public FFWriter(boolean binary) {
        this.binary = binary;
    }

    public void write(String fileName, Vector<FTriangle> vTriangles, String comment)
            throws IOException, FileNotFoundException {
        this.fileName = fileName;
        this.triangles = vTriangles;
        this.comment = comment;
        beforeWrite();
        writeMesh();
        afterWrite();

    }

    public void write(String fileName, Vector<FTriangle> vTriangles, Vector<FVertex> vVerticies, String comment)
            throws IOException, FileNotFoundException {
        this.verticies = vVerticies;
        write(fileName, vTriangles, comment);
    }

    protected void beforeWrite() throws IOException, FileNotFoundException {
        if (binary) {
            os = new BufferedOutputStream(new FileOutputStream(fileName), 32768);
        } else {
            bw = new BufferedWriter(new FileWriter(fileName));
            //text = new java.util.ArrayList(100);
        }

    }

    protected void writeMesh() throws IOException, FileNotFoundException {
    };

    protected void afterWrite() throws IOException {
        if (!binary)
//            p5.saveStrings(fileName, (String[]) text
//                    .toArray(new String[0]));
            bw.close();
        else
            os.close();

        System.out.println(fileType+" file \"" + fileName + "\" written with "
                + triangles.size() + " triangles.");

    }
    protected void writeln(String str) throws IOException {
        bw.write(str+"\n");
    } 
    public static byte[] toBytes4(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n);
        n >>>= 8;
        b[1] = (byte) (n);
        n >>>= 8;
        b[2] = (byte) (n);
        n >>>= 8;
        b[3] = (byte) (n);

        return b;
    }

    /**
     * @return Returns the binary.
     */
    public boolean isBinary() {
        return binary;
    }
    /**
     * @param binary The binary to set.
     */
    public void setBinary(boolean binary) {
        this.binary = binary;
    }
}