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

import eu.fluidforms.io.Exporter3D;
import eu.fluidforms.io.VelocityExporter;

import processing.core.PGraphics3D;


public class RawWriter extends PGraphics3D {
	protected Exporter3D exporter;
	protected boolean toggle;

	/**
	 * Constructor for Processing version 1.0 and greater
	 */
	public RawWriter() {
		super();
	}

	/**
	 * Sets the path to the file we want to export.
	 * 
	 * @param path The path, either relative or absolute, to when we want to save the file.
	 */
	public void setPath(String path){
		init(path);
	}

	protected void init(String path) {
	}

	/**
	 * Called to signify the begining of a shape. 
	 * See the processing documentation for more details.
	 * 
	 * @param kind The type of shape we are defining.
	 */
	public void beginShape(int kind) {
		super.beginShape(kind);
		toggle = false;
		if(exporter != null){
			exporter.beginShape();
		}
	}

	public void endShape(int mode) {
		super.endShape(mode);
		if(exporter != null){
			exporter.endShape();
		}
	}

	public void endDraw() {
		super.endDraw();
		if(exporter != null){
			exporter.write();
		}
	}


	/**
	 * Specifies the name to prepend to object names within the file.
	 * 
	 * @param valueToPrepend The value that gets prepended.
	 */
	public void setNamePrepend(String valueToPrepend) {
		((VelocityExporter)exporter).setNamePrepend(valueToPrepend);
	}



	public void dispose() {
		if(exporter != null){
			exporter.dispose();
		}
	}


}
