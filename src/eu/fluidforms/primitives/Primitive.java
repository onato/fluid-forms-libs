package eu.fluidforms.primitives;

import java.util.Vector;

import eu.fluidforms.geom.FTriangle;

public interface Primitive {
	public Vector<FTriangle> getTriangles();
	public float getHeight();
	public float getWidth();
	public float getDepth();
}
