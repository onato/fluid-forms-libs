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
 
package eu.fluidforms.processing;

import java.io.File;
import java.io.OutputStream;
import java.util.Stack;
import java.util.Vector;

import processing.core.PMatrix3D;
import eu.fluidforms.geom.FSolid;
import eu.fluidforms.geom.FTriangle;
import eu.fluidforms.geom.FVertex;
import eu.fluidforms.io.STLWriter;
import eu.fluidforms.io.VelocityExporter;
import eu.fluidforms.utils.Triangulate;

/**
 * This is the main class for interacting with the exporter of Fluid Forms Libs library.
 * 
 * To export a file, supply a supported format like STL or OBJ and do as follows...
 *    String format = "stl";
 *    beginRaw("eu.fluidforms.processing.FGraphics", "Box." + format);
 *      box(100);
 *    endRaw();
 * 
 * @author Stephen Williams (Fluid Forms)
 * 
 */

public class FGraphics extends RawWriter {
	private boolean applyTransformMatrix = true;
	private Stack<FVertex> tmpVertexList = new Stack<FVertex>();
	private FSolid currentShape;
	private int numTriangles = 0;
	private boolean serverSide = false;

	/**
	 * This is called by the constructors and sets up the right exporter for the 
	 * format that we supplied in the file name.
	 * 
	 * @param path The path, either relative or absolute, to when we want to save the file.
	 */
	protected void init(String pathToFile) {
		File file = new File(pathToFile);
		if(pathToFile!=null && !pathToFile.equals("") && !file.isDirectory()){
			String ext = pathToFile.substring(pathToFile.lastIndexOf('.')+1);
			if(ext.equalsIgnoreCase("STL")){
				exporter = new STLWriter(pathToFile, true);
				exporter.init();
			}else{
				exporter = new VelocityExporter(pathToFile);
			}
		}else{
			// this mean we are just doing visual stuff.
			setApplyTransformMatrix(false);
		}
	}
	/**
	 * Sets up the exporter for use with the STL exporter with the supplied output stream. 
	 * This is used for streaming an STL file from a webserver.
	 * 
	 * @param path The path, either relative or absolute, to when we want to save the file.
	 */
	protected void init(String pathToFile, OutputStream binaryWriter) {
		String ext = pathToFile.substring(pathToFile.lastIndexOf('.')+1);
		if(ext.equalsIgnoreCase("STL")){
			STLWriter stlExporter = new STLWriter();
			stlExporter.setBinaryWriter(binaryWriter);
			if(serverSide && numTriangles==0){
				stlExporter.writeNow = false; // you cant stream and "writeNow"
			}
			stlExporter.setNumTriangles(numTriangles);
			stlExporter.init();
			exporter = stlExporter;
		}else{
			System.err.println("The file format "+ ext + " is not supported yet when using your own OutputStream.");
		}
	}
	
	/**
	 * Sets whether or not we should export in an ASCII file format. 
	 * STL ASCII and binary are supported.
	 * @param val
	 */
	public void setAscii(boolean val){
		String ext = exporter.getFileExtension();
		if(ext.equalsIgnoreCase("STL")){
			//exporter = new STLWriter(exporter.getFileName(), false);
			exporter = new VelocityExporter(exporter.getFileName());
		}
	}

	private void addTriangle(FTriangle tri){
		if(exporter == null){
			currentShape.addTriangle(tri);
		}else{
			exporter.addTriangle(tri); // TODO : get rid of this. It is floatd.
		}
	}

	/**
	 * Adds a vertex to the current shape using the supplied x and y coordinates.
	 */
	public void vertex(float x, float y) {
		vertex(x, y, 0);
	}

	/**
	 * Adds a vertex to the current shape using the supplied x, y and z coordinates.
	 */
	public void vertex(float x, float y, float z) {
		super.vertex(x, y, z);
		
		if(applyTransformMatrix){
			
			float w = 1;
			PMatrix3D matrix = modelview;
			
			float newX = matrix.m00 * x + matrix.m01 * y
			+ matrix.m02 * z + matrix.m03 * w;
			float newY = matrix.m10 * x + matrix.m11 * y
					+ matrix.m12 * z + matrix.m13 * w;
			float newZ = matrix.m20 * x + matrix.m21 * y
					+ matrix.m22 * z + matrix.m23 * w;
	
			
			x = newX;
			y = newY;
			z = newZ;
		}
		
		FVertex v = new FVertex(x, y, z);
		tmpVertexList.add(v);
		int numberOfVerticies = tmpVertexList.size();
		
		int numVertsNeeded = 3;
		if(shape==QUADS || shape==QUAD_STRIP){
			numVertsNeeded = 4;
		}
		
		if (numberOfVerticies == numVertsNeeded) {
			if(shape==TRIANGLES || shape==TRIANGLE){
				FVertex v3 = tmpVertexList.pop();
				FVertex v2 = tmpVertexList.pop();
				FVertex v1 = tmpVertexList.pop();
				addTriangle(new FTriangle(v1, v3, v2));
			}else if(shape==TRIANGLE_FAN){
				FVertex v3 = tmpVertexList.pop();
				FVertex v2 = tmpVertexList.pop();
				FVertex v1 = tmpVertexList.pop();
				addTriangle(new FTriangle(v1, v3, v2));
				tmpVertexList.add(v1);
				tmpVertexList.add(v3);
			}else if(shape==TRIANGLE_STRIP){
				FVertex v3 = tmpVertexList.pop();
				FVertex v2 = tmpVertexList.pop();
				FVertex v1 = tmpVertexList.pop();
				if(toggle){
					addTriangle(new FTriangle(v1, v2, v3));
				}else{
					addTriangle(new FTriangle(v1, v3, v2));
				}
				toggle = !toggle;
				tmpVertexList.add(v2);
				tmpVertexList.add(v3);
			}else if(shape==QUAD || shape==QUADS){
				FVertex v4 = tmpVertexList.pop();
				FVertex v3 = tmpVertexList.pop();
				FVertex v2 = tmpVertexList.pop();
				FVertex v1 = tmpVertexList.pop();
				addTriangle(new FTriangle(v1, v3, v2));
				addTriangle(new FTriangle(v1, v4, v3));
			}else if(shape==QUAD_STRIP){
				FVertex v4 = tmpVertexList.pop();
				FVertex v3 = tmpVertexList.pop();
				FVertex v2 = tmpVertexList.pop();
				FVertex v1 = tmpVertexList.pop();
				addTriangle(new FTriangle(v1, v3, v2));
				addTriangle(new FTriangle(v2, v3, v4));
				tmpVertexList.add(v3);
				tmpVertexList.add(v4);
			}else if(shape==POLYGON){
				//System.out.println("polygons are not implemented yet");
				// gets triangulated in endShape()
			}
		}
	}
	
	public void beginShape(int kind) {
		super.beginShape(kind);
		currentShape = new FSolid();
		tmpVertexList.clear();
	}

	public void endShape(int kind) {

		if(tmpVertexList.size()>0){
			if(shape==POLYGON){
				triangulate();
			}else if((shape==TRIANGLE_STRIP || shape==TRIANGLE_FAN || shape==QUAD_STRIP) && tmpVertexList.size()==2){
				// ignore
			}else{
				System.err.println(tmpVertexList.size() + " verticies left over! This may be cause by a bug, please consider reporting it: " + shape);
			}
		}
		super.endShape(kind);
		tmpVertexList.clear();
	}
	public void endShape() {
		super.endShape();
	}
	
	private void triangulate(){
		Vector<FTriangle> triangles = Triangulate.triangulate(tmpVertexList);
		for (int i = 0; i < triangles.size(); i++) {
			addTriangle(triangles.elementAt(i));
		}
	}
//	public Vector getTriangles(){
//		return triangles;
//	}

	
	/**
	 * This defines whether or not to apply processings translations and rotations
	 * to the geometry of not. If you are generating your own mesh you might like
	 * to set it to false to gain more control.
	 * 
	 * @param applyTransformMatrix if false the coordinates in your code will appear
	 * in the resulting file, otherwise they will be adjusted by processings
	 * transformation matrix. 
	 */
	public void setApplyTransformMatrix(boolean applyTransformMatrix) {
		this.applyTransformMatrix = applyTransformMatrix;
	}
	
	/**
	 * Returns the FSolid object representing the last drawn shape.
	 * @return FSolid representing the last drawn shape.
	 */
	public FSolid getCurrentShape() {
		return new FSolid(currentShape);
	}

	/**
	 * Sets the FSolid object representing the last drawn shape. Useful for composite objects.
	 */
	public void setCurrentShape( FSolid shape ) {
		currentShape = new FSolid(shape);
	}
	
	/**
	 * Set the number of triangles that will be exported in an STL file.
	 * @param numTriangles
	 */
	public void setNumTriangles(int numTriangles) {
		this.numTriangles = numTriangles;
	}
	
	/**
	 * Set whether or not we are using this code on a webserver or not.
	 * @param serverSide
	 */
	public void setServerSide(boolean serverSide) {
		this.serverSide = serverSide;
	}
}



