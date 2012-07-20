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
import java.util.Stack;
import java.util.Vector;

import eu.fluidforms.geom.FScene;
import eu.fluidforms.geom.FSolid;
import eu.fluidforms.geom.FTriangle;

/**
 * Base class for 3D exporter classes. It provides a method by which 
 * to automatically fill the AScene with ASolids. This enables you to 
 * create different solids or groups in your files by wrapping them in 
 * beginShape() and endShape() functions.  
 * 
 * @author Stephen Williams stephen@algodes.com
 * @see eu.fluidforms.geom.FSolid
 * @see eu.fluidforms.geom.FScene
 *
 */
public abstract class Exporter3DAbstract extends BinOrAsciiWriter
		implements
			Exporter3D {

	/**
	 * This is a comment that can be writen to some file formats. You could
	 * include your name and copyright in here, for example.
	 */
	private String comment = "Fluid Forms Libs library for processing";

	/**
	 * A list of solids stored untill they are added to the scene.
	 */
	protected Stack<FSolid> tempSolids;

	protected FScene scene;

	public FSolid getSolid(int index){
		return scene.getSolid(index);
	}

	/**
	 * Class constructor specifying to use when you want to specify 
	 * this outputStream yourself.
	 * 
	 */
	public Exporter3DAbstract() {
		super();
	}

	/**
	 * Class constructor specifying the path to the file you wish to save and
	 * whether it is a binary or ASCII file.
	 * 
	 * @param path
	 *            the path to the file that you want to save.
	 * @param binary
	 *            if true the file is writen in a binary format.
	 */
	public Exporter3DAbstract(String path, boolean binary) {
		super(path, binary);
	}
	
	public void init(){
		scene = new FScene();
		tempSolids = new Stack<FSolid>();
	}

	/**
	 * Adds a triangle to the last created ASolid, which is created every 
	 * time beginShape() is called.
	 * 
	 * @see #beginShape()
	 */
	public void addTriangle(FTriangle tri) {
		if(tempSolids.size() == 0){
			beginShape();
		}

		FSolid solid = tempSolids.peek();
		solid.addTriangle(tri);
	}
	public Vector<FTriangle> getTriangles(){
		Vector<FTriangle> triangles = new Vector<FTriangle>();
		for (int i = 0; i < scene.solidCount(); i++) {
			FSolid solid = scene.getSolid(i);
			triangles.addAll(solid.getTriangles());
		}
		return triangles;
	}
	/**
	 * Creates a new ASolid object to which future triangles will be added.
	 */
	public void beginShape() {
		tempSolids.push(new FSolid());
	}
	/**
	 * Adds the current ASolid object to the AScene.
	 * 
	 * @see eu.fluidforms.geom.FSolid
	 * @see eu.fluidforms.geom.FScene
	 */
	public void endShape() {
		if(tempSolids.size()>0){
			scene.addMember(tempSolids.pop());
		}
	}


	protected String getComment() {
		return comment;
	}


	protected void setComment(String comment) {
		this.comment = comment;
	}

}