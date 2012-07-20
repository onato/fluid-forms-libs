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

/*
 * Created on 16-Apr-2005
 *
 * 
 * 
 */
package eu.fluidforms.io;

import java.io.IOException;

import eu.fluidforms.geom.FScene;
import eu.fluidforms.geom.FTriangle;
import eu.fluidforms.geom.FVertex;

/**
 * @author williams
 * 
 * 
 * 
 */
public class STLWriter extends Exporter3DAbstract {
	public boolean writeNow = true;
	private boolean writeHeaderLater = true;
	private int triCount = 0;
	private int numTriangles = 0;// prediction of how many there will be

	public STLWriter() {
		super();
	}
	public STLWriter(String fileName, boolean binary) {
		super(fileName, true);
	}

	public STLWriter(String fileName) {
		this(fileName, true);
	}

	public STLWriter(boolean binary) {
		super("fluidforms.stl", binary);
	}

	public void init(){
		super.init();
		writeHeaderLater = isBinary() && writeNow && numTriangles==0;
		header(getComment(), scene);
	}

	public void addTriangle(FTriangle tri) {
		if (!writeNow) {
			super.addTriangle(tri);
		} else {

			try {
				writeTriangle(tri);
			} catch (IOException e) {
				e.printStackTrace();
				writeNow = false;
			}
		}
		triCount++;
	}

	public void write() {
		if (getTriangles().size() == 0) {
			endShape();
		}
		try {
			if(!writeNow){
				mesh(scene);
			}
			footer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		closeWriter();
		
		// overwrite the header with the correct triangle count
		if (writeHeaderLater) {
			openReadWrite();
			header(getComment(), scene);
			closeWriter();
		}
	}

	void header(String comment, FScene scene) {
		if (isBinary()) {
			byte[] name = new byte[80];
			byte[] bName = comment.getBytes();
			for (int i = 0; i < 80 && i < 80; i++) {
				if (i < bName.length) {
					name[i] = bName[i];
				} else {
					name[i] = " ".getBytes()[0];
				}
			}
			write(name, 0, 80);
			int triCount = getTriangleCount();
			if(triCount==0){
				triCount = numTriangles;
			}
			byte[] triCountByte4 = new byte[4];
			triCountByte4[3] = (byte) (triCount >> 24);
			triCountByte4[2] = (byte) ((triCount << 8) >> 24);
			triCountByte4[1] = (byte) ((triCount << 16) >> 24);
			triCountByte4[0] = (byte) ((triCount << 24) >> 24);

			write(triCountByte4, 0, 4);// num facits
		} else {
			println("solid " + comment);
		}
	}

	void footer() throws IOException {
		if (isBinary()) {
		} else {
			print("endsolid");
		}
	}
	private void write4Bites(float val){
		float v = val;//(Math.round(val*Math.pow(10, 3)) / Math.pow(10, 3));
		write(floatToBytes4(v));
	}
	
	private void writeTriangle(FTriangle tri) throws IOException {
		FVertex v1 = tri.getV1();
		FVertex v2 = tri.getV2();
		FVertex v3 = tri.getV3();
		FVertex norm = tri.getNorm();

		// the processing x axis seems to point in the other
		// other direction to the standard
		if (isBinary()) {
			write4Bites(norm.x);
			write4Bites(norm.y);
			write4Bites(norm.z);

			write4Bites(v1.x);
			write4Bites(v1.y);
			write4Bites(v1.z);

			write4Bites(v2.x);
			write4Bites(v2.y);
			write4Bites(v2.z);

			write4Bites(v3.x);
			write4Bites(v3.y);
			write4Bites(v3.z);

			write(new byte[2]);
		} else {
			// text.add(" facet normal 0.0 0.0 0.0");

			println("\tfacet normal " + norm.x + " " + norm.y + " " + norm.z);
			println("\t\touter loop");
			println("\t\t\tvertex " + v1.x + " " + v1.y + " " + v1.z);
			println("\t\t\tvertex " + v2.x + " " + v2.y + " " + v2.z);
			println("\t\t\tvertex " + v3.x + " " + v3.y + " " + v3.z);
			println("\t\tendloop");
			println("\tendfacet");
			// }
		}

	}

	private int getTriangleCount() {
		if (getTriangles().size() > 0) {
			return getTriangles().size();
		}
		return triCount;
	}

	void mesh(FScene scene) throws IOException {
		for (int i = 0; !writeNow && i < getTriangles().size(); i++) {
			// if(i % (getTriangles().size()/10) == 0){
			// System.out.println((int)(100f*i/getTriangles().size())+1+"%");
			// }
			FTriangle tri = getTriangles().get(i);
			writeTriangle(tri);
		}
	}
	public void setNumTriangles(int numTriangles) {
		this.numTriangles = numTriangles;
		this.writeHeaderLater = false;
	}

}