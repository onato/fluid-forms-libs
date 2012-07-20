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
 
package eu.fluidforms.geom;

import eu.fluidforms.utils.*;

/**
 * @author williams
 * 
 * 
 * 
 */
public class FTriangle implements Cloneable {
	public FVertex v1, v2, v3;

	private FEdge e1, e2, e3;

	private FVertex norm;
	
	private int numberOfVerticies = 0;

	/**
	 * A Copy Constructor to create a copy of the past ATriangle.
	 * 
	 * @param tri
	 *            The ATriangle to be copied.
	 */
	public FTriangle(FTriangle tri) {
		this.v1 = new FVertex(tri.v1);
		this.v2 = new FVertex(tri.v2);
		this.v3 = new FVertex(tri.v3);

		if(tri.e1 != null && tri.e2 != null && tri.e3 != null){
			this.e1 = new FEdge(tri.e1);
			this.e2 = new FEdge(tri.e2);
			this.e3 = new FEdge(tri.e3);
		}

		recalculateNormal();

	}
	
	public FTriangle(FEdge e1, FEdge e2, FEdge e3) {
		this.e1 = e1;
		this.e2 = e2;
		this.e3 = e3;
		e1.addTriagle(this);
		e2.addTriagle(this);
		e3.addTriagle(this);

		this.v1 = e1.getV1();
		this.v2 = e1.getV2();
		if (e2.getV1() != v1 && e2.getV1() != v2)
			this.v3 = e2.getV1();
		else
			this.v3 = e2.getV2();

		init(v1, v2, v3);
	}

	public FTriangle(){}
	
	public FTriangle(FVertex v1, FVertex v2, FVertex v3) {
		init(v1, v2, v3);
	}

	private void init(FVertex v1, FVertex v2, FVertex v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.e1 = new FEdge(v1, v2);
		this.e2 = new FEdge(v2, v3);
		this.e3 = new FEdge(v3, v1);

		recalculateNormal();
		
		v1.vTriangles.insertElementAt(this, v1.vTriangles.size());
		v2.vTriangles.insertElementAt(this, v2.vTriangles.size());
		v3.vTriangles.insertElementAt(this, v3.vTriangles.size());
	}
	
	public void scale(float scale){
		v1.scale(scale);
		v2.scale(scale);
		v3.scale(scale);
	}

	private void recalculateNormal(){
		FVertex fV1 = new FVertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
		FVertex fV2 = new FVertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
		norm = Utils.normalise(Utils.crossProduct(fV1, fV2));
	}
	
	/**
	 * Swaps the direction of the normal by switching two vertices.
	 * 
	 */
	public void switchDirection() {
		FVertex vTmp = v1;
		v1 = v2;
		v2 = vTmp;
	}

//	public Object clone() {
//		ATriangle tmpTri;
//		try {
//			tmpTri = (ATriangle) super.clone();
//			tmpTri.v1 = (AVertex) v1.clone();
//			tmpTri.v2 = (AVertex) v2.clone();
//			tmpTri.v3 = (AVertex) v3.clone();
//			return tmpTri;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		tmpTri = new ATriangle((AVertex) v1.clone(), (AVertex) v2.clone(),
//				(AVertex) v3.clone());
//		return tmpTri;
//	}

	public boolean sharesVertex(FTriangle other) {
		return v1 == other.v1 || v1 == other.v2 || v1 == other.v3
				|| v2 == other.v1 || v2 == other.v2 || v2 == other.v3
				|| v3 == other.v1 || v3 == other.v2 || v3 == other.v3;
	}
	public FVertex getNormal(int i){
		switch (i) {
		case 1:
			return v1.getNormal();
		case 2:
			return v2.getNormal();
		case 3:
			return v3.getNormal();

		default:
			break;
		}
		return v1.getNormal();
	}

	public FEdge getE(int i) {
		switch (i) {
		case 0:
			return e1;
		case 1:
			return e2;
		case 2:
			return e3;

		default:
			break;
		}
		System.err.println("Valid values: 0, 1 or 2");
		return e1;
	}

	public FEdge getE1() {
		return e1;
	}

	public void setE1(FEdge e1) {
		this.e1 = e1;
	}

	public FEdge getE2() {
		return e2;
	}

	public void setE2(FEdge e2) {
		this.e2 = e2;
	}

	public FEdge getE3() {
		return e3;
	}

	public void setE3(FEdge e3) {
		this.e3 = e3;
	}

	public FVertex getV(int i) {
		switch (i) {
		case 0:
			return v1;
		case 1:
			return v2;
		case 2:
			return v3;

		default:
			break;
		}
		System.err.println("Valid values: 0, 1 or 2");
		return v1;
	}

	public FVertex getV1() {
		return v1;
	}

	public void setV1(FVertex v1) {
		this.v1 = v1;
	}

	public FVertex getV2() {
		return v2;
	}

	public void setV2(FVertex v2) {
		this.v2 = v2;
	}

	public FVertex getV3() {
		return v3;
	}

	public void setV3(FVertex v3) {
		this.v3 = v3;
	}

	public FVertex getNorm() {
		return norm;
	}

	public void setNorm(FVertex norm) {
		this.norm = norm;
	}
	
	public void add(FVertex v){
		if(this.v1 == null){
			this.v1 = v;
			numberOfVerticies = 1;
			return;
		}
		if(this.v2 == null){
			this.v2 = v;
			numberOfVerticies = 2;
			return;
		}
		if(this.v3 == null){
			this.v3 = v;
			numberOfVerticies = 3;
		}
	}

	public int getNumberOfVerticies() {
		return numberOfVerticies;
	}

	public void setNumberOfVerticies(int numberOfVerticies) {
		this.numberOfVerticies = numberOfVerticies;
	}
}
