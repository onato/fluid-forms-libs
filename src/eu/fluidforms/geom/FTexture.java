package eu.fluidforms.geom;

import processing.core.PImage;
import processing.core.PMatrix3D;

public class FTexture {
	PMatrix3D matrix;
	PMatrix3D matrixInv;
	PImage image;
	int direction=1;
	
	public FTexture(PImage image){
		this.matrix = new PMatrix3D();
		this.image = image;
	}
	public void scale(float val){
		matrix.scale(val);
		matrixInv = matrix;
		matrixInv.invert();
	}
	public void rotateX(float val){
		matrix.rotateX(val);
	}
	public void rotateY(float val){
		matrix.rotateY(val);
	}
	public void rotateZ(float val){
		matrix.rotateZ(val);
	}
	
	public int getU(FVertex vertex){
		// apply the transformation matrix to a copy of the vertex
		FVertex v = new FVertex(vertex);
		v.apply(matrix);
		
		int u = (int)(Math.abs(v.x ) / 100 * 256 / 40);
		if(u>image.width){
			u = image.width;
		}
		return u;
	}
	public int getV(FVertex vertex){
		// apply the transformation matrix to a copy of the vertex
		FVertex vert = new FVertex(vertex);
		vert.apply(matrix);

		int v = (int)(Math.abs(vert.z ) / 7 );
		if(v>image.height){
			v = image.height;
		}
		return v;
	}
	public PImage getImage() {
		return image;
	}
	protected void setImage(PImage image) {
		this.image = image;
	}
}
