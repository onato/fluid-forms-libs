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
 
package eu.fluidforms.primitives;

import java.util.Vector;

import eu.fluidforms.geom.*;

public class Sphere {
	private Vector<FVertex> verticies = new Vector<FVertex>();
	private Vector<FTriangle> triangles = new Vector<FTriangle>();
	private static final int OCT = 1;
	private static final int ISO = 2;
	private static final int To4 = 3;
	private static final int To6 = 4;
	
	private int base = ISO;
	private int subdivMode = To6;

	public Sphere() {
		this(6);
	}
	public Sphere(int subdivisions) {
		if(base == OCT){
			createOctagon();
		}else{
			createIsohedron();
		}
		for (int i = 0; i < subdivisions; i++) {
			if(subdivMode == To4){
				subdivide1to4();
			}else{
				subdivide1to6();
			}
		}
	}
	
	public void scale(float scale){
//		for (int i = 0; i < triangles.size(); i++) {
//			FTriangle tri = (FTriangle)triangles.elementAt(i);
//			tri.scale(scale);
//		}
		for (int i = 0; i < verticies.size(); i++) {
			FVertex v = verticies.elementAt(i);
			v.scale(scale);
		}

	}

	private void createOctagon() {
		verticies.removeAllElements();
		triangles.removeAllElements();
		FVertex[] v = new FVertex[6];
		v[0] = new FVertex(0, 1, 0);// top
		float h = (float)Math.sqrt(0.5);
		v[1] = new FVertex(h, 0, h);
		v[2] = new FVertex(h, 0, -h);
		v[3] = new FVertex(-h, 0, -h);
		v[4] = new FVertex(-h, 0, h);

		v[5] = new FVertex(0, -1, 0);// bottom

		for (int i = 0; i < 4; i++) {
			verticies.add(v[i]);
			int offset = (1 + i) % 4;
			triangles.add(new FTriangle(v[0], v[1 + i], v[1 + offset]));
			triangles.add(new FTriangle(v[5], v[4 - i], v[4 - offset]));
		}
	}

	private void createIsohedron() {
		verticies.removeAllElements();
		triangles.removeAllElements();
		FVertex[] v = new FVertex[12];
		float phi = (float)(1 + Math.sqrt(5)) / 2;
		float a = 0.5f;
		float b = 1 / (2 * phi);

		v[0] = new FVertex(0, b, -a);
		v[1] = new FVertex(b, a, 0);
		v[2] = new FVertex(-b, a, 0);
		v[3] = new FVertex(0, b, a);
		v[4] = new FVertex(0, -b, a);
		v[5] = new FVertex(a, 0, b);
		v[6] = new FVertex(-a, 0, b);
		v[7] = new FVertex(0, -b, -a);
		v[8] = new FVertex(a, 0, -b);
		v[9] = new FVertex(-a, 0, -b);
		v[10] = new FVertex(b, -a, 0);
		v[11] = new FVertex(-b, -a, 0);

		for (int i = 0; i < v.length; i++) {
			v[i].normalise();
			verticies.add(v[i]);
		}

		triangles.add(new FTriangle(v[0], v[1], v[2]));
		triangles.add(new FTriangle(v[3], v[2], v[1]));
		triangles.add(new FTriangle(v[3], v[4], v[6]));
		triangles.add(new FTriangle(v[3], v[5], v[4]));
		triangles.add(new FTriangle(v[0], v[7], v[8]));
		triangles.add(new FTriangle(v[0], v[9], v[7]));
		triangles.add(new FTriangle(v[4], v[10], v[11]));
		triangles.add(new FTriangle(v[7], v[11], v[10]));
		triangles.add(new FTriangle(v[2], v[6], v[9]));
		triangles.add(new FTriangle(v[11], v[9], v[6]));
		triangles.add(new FTriangle(v[1], v[8], v[5]));
		triangles.add(new FTriangle(v[10], v[5], v[8]));
		triangles.add(new FTriangle(v[3], v[6], v[2]));
		triangles.add(new FTriangle(v[3], v[1], v[5]));
		triangles.add(new FTriangle(v[0], v[2], v[9]));
		triangles.add(new FTriangle(v[0], v[8], v[1]));
		triangles.add(new FTriangle(v[7], v[9], v[11]));
		triangles.add(new FTriangle(v[7], v[10], v[8]));
		triangles.add(new FTriangle(v[4], v[11], v[6]));
		triangles.add(new FTriangle(v[4], v[5], v[10]));
	}

	private void subdivide1to4() {
		Vector<FTriangle> newTriangles = new Vector<FTriangle>();
		for (int i = 0; i < triangles.size(); i++) {
			FTriangle tri = triangles.elementAt(i);
			FVertex v1 = tri.getV1();
			FVertex v2 = tri.getV2();
			FVertex v3 = tri.getV3();

			FVertex v4 = getMiddle(v1, v2);
			FVertex v5 = getMiddle(v2, v3);
			FVertex v6 = getMiddle(v3, v1);

			newTriangles.add(new FTriangle(v1, v4, v6));
			newTriangles.add(new FTriangle(v6, v5, v3));
			newTriangles.add(new FTriangle(v4, v2, v5));
			newTriangles.add(new FTriangle(v6, v4, v5));
		}
		triangles.removeAllElements();
		triangles.addAll(newTriangles);
	}
	
	private void subdivide1to6() {
		Vector<FTriangle> newTriangles = new Vector<FTriangle>();
		for (int i = 0; i < triangles.size(); i++) {
			FTriangle tri = triangles.elementAt(i);
			FVertex v1 = tri.getV1();
			FVertex v2 = tri.getV2();
			FVertex v3 = tri.getV3();

			FVertex v4 = getMiddle(v1, v2);
			FVertex v5 = getMiddle(v2, v3);
			FVertex v6 = getMiddle(v3, v1);
			FVertex v7 = getMiddle(v1, v2, v3);

			newTriangles.add(new FTriangle(v7, v1, v4));
			newTriangles.add(new FTriangle(v7, v4, v2));
			newTriangles.add(new FTriangle(v7, v2, v5));
			newTriangles.add(new FTriangle(v7, v5, v3));
			newTriangles.add(new FTriangle(v7, v3, v6));
			newTriangles.add(new FTriangle(v7, v6, v1));
		}
		triangles.removeAllElements();
		triangles.addAll(newTriangles);
	}

	public FVertex getMiddle(FVertex v1, FVertex v2) {
		float x = (v1.x + v2.x) / 2;
		float y = (v1.y + v2.y) / 2;
		float z = (v1.z + v2.z) / 2;
		FVertex vNew = new FVertex(x, y, z);
		verticies.add(vNew);
		vNew.normalise();
		return vNew;
	}
	public FVertex getMiddle(FVertex v1, FVertex v2, FVertex v3) {
		float x = (v1.x + v2.x + v3.x) / 3;
		float y = (v1.y + v2.y + v3.y) / 3;
		float z = (v1.z + v2.z + v3.z) / 3;
		FVertex vNew = new FVertex(x, y, z);
		verticies.add(vNew);
		vNew.normalise();
		return vNew;
	}

	public Vector<FTriangle> getTriangles() {
		return triangles;
	}
	public void setTriangles(Vector<FTriangle> triangles) {
		this.triangles = triangles;
	}
	public Vector<FVertex> getVerticies() {
		return verticies;
	}
	public void setVerticies(Vector<FVertex> verticies) {
		this.verticies = verticies;
	}
	
	
	public int getBase() {
		return base;
	}
	public void setBase(int base) {
		this.base = base;
	}
	public int getSubdivMode() {
		return subdivMode;
	}
	public void setSubdivMode(int subdivMode) {
		this.subdivMode = subdivMode;
	}

}
