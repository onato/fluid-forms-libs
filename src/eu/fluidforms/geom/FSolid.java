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

import java.util.Collection;
import java.util.Vector;


public class FSolid {
	
	public FSolid(){
		triangles = new Vector<FTriangle>();
	}
	
	public FSolid(FSolid solid){
		triangles = new Vector<FTriangle>();
		for (int i = 0; i < solid.triangles.size(); i++) {
			FTriangle tri = solid.getTriangle(i);
			triangles.add(new FTriangle(tri));
		}
	}
	
	/**
	 * The name of the solid
	 */
	private String name;
	
	/**
	 * A list of triangles from which the solid is comprised.
	 */
	private Vector<FTriangle> triangles;
	
//	/**
//	 * A list of verticies from which the solid is comprised.
//	 */
//	private Vector verticies;

	/**
	 * 
	 * @return The name of the solid
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the solid
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the ATriangle at the specified index.
	 * @param index
	 * @return the triangle at the specified index.
	 */
	public FTriangle getTriangle(int index) {
		return triangles.elementAt(index);
	}
	
	/**
	 * Adds an triangle to the solid.
	 * @param triangle
	 */
	public void addTriangle(FTriangle triangle) {
		triangle.setE1(new FEdge(triangle.getV1(), triangle.getV2()));
		triangle.setE2(new FEdge(triangle.getV2(), triangle.getV3()));
		triangle.setE3(new FEdge(triangle.getV3(), triangle.getV1()));
		this.triangles.insertElementAt(triangle, triangles.size());
	}
	
	/**
	 * Adds triangles to the solid.
	 * @param triangles
	 */
	public void addTriangles(Collection<FTriangle> triangles) {
		this.triangles.addAll(triangles);
	}

	public Vector<FTriangle> getTriangles() {
		return triangles;
	}

	
	
	/**
	 * returns the number of triangles in the solid.
	 *
	 */
	public int size(){
		return this.triangles.size();
	}
}
