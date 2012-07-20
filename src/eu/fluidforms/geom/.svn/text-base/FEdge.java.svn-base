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
 * Created on 14-Jun-2005
 *
 * 
 * 
 */
package eu.fluidforms.geom;

import java.util.Vector;

/**
 * This class represents an edge of a polygon.
 * 
 * @author Stephen Williams ~ Fluidforms
 * 
 * 
 * 
 */
public class FEdge {

	/**
	 * The first vertex of the edge.
	 */
	private FVertex v1;

	/**
	 * The second vertex of the edge.
	 */
	private FVertex v2;

	/**
	 * A list of trinagle that this edge is part of.
	 */
	private Vector<FTriangle> triangles;

	/**
	 * Creates an Edge.
	 * 
	 * @param vFrom
	 *            The first vertex of the edge.
	 * @param vTo
	 *            The second vertex of the edge.
	 */
	public FEdge(final FVertex vFrom, final FVertex vTo) {
		this.v1 = vFrom;
		this.v2 = vTo;
		triangles = new Vector<FTriangle>();
	}

	/**
	 * A Copy Constructor to create a copy of the past AEdge.
	 * 
	 * @param edge
	 *            The AEdge to be copied.
	 */
	public FEdge(final FEdge edge) {
		this.v1 = new FVertex(edge.getV1());
		this.v2 = new FVertex(edge.getV2());
		triangles = new Vector<FTriangle>();
	}
	
	public float length(){
		return (float)Math.sqrt(
				Math.pow(v1.x - v2.x, 2) +
				Math.pow(v1.y - v2.y, 2) +
				Math.pow(v1.z - v2.z, 2)
				);
	}

	/**
	 * @return Returns the number of triangles for this edge.
	 */
	public final int getNumberOfTriangles() {
		return triangles.size();
	}

	/**
	 * @return Returns the first vertex.
	 */
	public final FVertex getV1() {
		return v1;
	}

	/**
	 * @param v
	 *            Sets the first vertex.
	 */
	public final void setV1(final FVertex v) {
		this.v1 = v;
	}

	/**
	 * @return Returns the second vertex.
	 */
	public final FVertex getV2() {
		return v2;
	}

	/**
	 * @param v
	 *            The vertex to set. Sets the second vertex.
	 */
	public final void setV2(final FVertex v) {
		this.v2 = v;
	}

	/**
	 * Addes a Trinagle to the list of triangles that this edge is part of.
	 * 
	 * @param t
	 *            The trinagle to add to the list.
	 */
	public final void addTriagle(final FTriangle t) {
		triangles.insertElementAt(t, triangles.size());
	}

	public String toString(){
		return v1.toString() + " -> " + v2.toString();
	}

	@Override
	public int hashCode() {
		//final int prime = 31;
		int result = 1;
		result += ((v1 == null) ? 0 : v1.hashCode());//prime * result + 
		result += ((v2 == null) ? 0 : v2.hashCode());//prime * result + 
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FEdge other = (FEdge) obj;
		if (v1 == null) {
			if (other.v1 != null){
				return false;
			}
		} else if (!v1.equals(other.v1) && !v1.equals(other.v2)){
			return false;
		}

		if (v2 == null) {
			if (other.v2 != null){
				return false;
			}
		} else if (!v2.equals(other.v2) && !v2.equals(other.v1)){
			return false;
		}

		return true;
	}

}