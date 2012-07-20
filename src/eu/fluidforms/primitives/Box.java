package eu.fluidforms.primitives;

import java.util.Vector;

import eu.fluidforms.geom.FTriangle;
import eu.fluidforms.geom.FVertex;

public class Box implements Primitive{
	Vector<FVertex> verticies = new Vector<FVertex>();
	Vector<FTriangle> triangles = new Vector<FTriangle>();
	float w;
	float h;
	float d;
	
	public Box(float w, float h, float d){
		this.w = w;
		this.h = h;
		this.d = d;
		
	    float x1 = -w/2f; float x2 = w/2f;
	    float y1 = -h/2f; float y2 = h/2f;
	    float z1 = -d/2f; float z2 = d/2f;

	    FVertex v1 = new FVertex(x1, y1, z1);verticies.add(v1);
	    FVertex v2 = new FVertex(x2, y1, z1);verticies.add(v2);
	    FVertex v3 = new FVertex(x2, y2, z1);verticies.add(v3);
	    FVertex v4 = new FVertex(x1, y2, z1);verticies.add(v4);

	    FVertex v5 = new FVertex(x2, y1, z2);verticies.add(v5);
	    FVertex v6 = new FVertex(x1, y1, z2);verticies.add(v6);
	    FVertex v7 = new FVertex(x1, y2, z2);verticies.add(v7);
	    FVertex v8 = new FVertex(x2, y2, z2);verticies.add(v8);


	    // front
	    FTriangle t1 = new FTriangle(v1, v2, v3);triangles.add(t1);
	    FTriangle t2 = new FTriangle(v1, v3, v4);triangles.add(t2);

	    // right
	    FTriangle t3 = new FTriangle(v2, v5, v8);triangles.add(t3);
	    FTriangle t4 = new FTriangle(v2, v8, v3);triangles.add(t4);

	    // back
	    FTriangle t5 = new FTriangle(v5, v6, v7);triangles.add(t5);
	    FTriangle t6 = new FTriangle(v5, v7, v8);triangles.add(t6);

	    // left
	    FTriangle t7 = new FTriangle(v6, v1, v4);triangles.add(t7);
	    FTriangle t8 = new FTriangle(v6, v4, v7);triangles.add(t8);

	    // top
	    FTriangle t9 = new FTriangle(v6, v5, v2);triangles.add(t9);
	    FTriangle t10 = new FTriangle(v6, v2, v1);triangles.add(t10);

	    // bottom
	    FTriangle t11 = new FTriangle(v4, v3, v8);triangles.add(t11);
	    FTriangle t12 = new FTriangle(v4, v8, v7);triangles.add(t12);

	}

	public Vector<FTriangle> getTriangles() {
		return triangles;
	}
	
	public float getWidth(){
		return w;
	}
	public float getHeight(){
		return h;
	}
	public float getDepth(){
		return d;
	}
}
