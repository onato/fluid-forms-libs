package eu.fluidforms.processing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PGraphics3D;
import processing.core.PImage;
import processing.core.PMatrix3D;
import eu.fluidforms.geom.FEdge;
import eu.fluidforms.geom.FGeometryUV;
import eu.fluidforms.geom.FTexture;
import eu.fluidforms.geom.FTriangle;
import eu.fluidforms.geom.FVertex;

public class Draw {
	private PApplet p5;
	protected static boolean calculateNormals = false;
	
	public Draw(PApplet p5){
		this.p5 = p5;
	}

	/**
	 * Draws all of the FTriangles contained with in the Vector.
	 * @param triangles A Vector of FTriangle objects to be drawn.
	 */
	public void triangles(Vector<FTriangle> triangles) {
		p5.beginShape(PApplet.TRIANGLES);
		justDrawTrinagles(triangles);
		p5.endShape();

	}

	/**
	 * Draws all of the FTriangles contained with in the Vector using 
	 * the PGraphics directly.
	 * @param triangles A Vector of FTriangle objects to be drawn.
	 */
	public void triangles(PGraphics g, Vector<FTriangle> triangles) {
		g.beginShape(PApplet.TRIANGLES);
		justDrawTrinagles(g, triangles);
		g.endShape();

	}
	
	/**
	 * Draws all of the FTriangles contained with in the Vector mapping the supplied
	 * texture to them.
	 * @param triangles A Vector of FTriangle objects to be drawn.
	 * @param texture
	 */
	public void triangles(Vector<FTriangle> triangles, PImage texture) {
		FTexture fTexture = new FTexture(texture);
		triangles(triangles, fTexture);
	}
	
	/**
	 * Draws all of the FTriangles contained with in the Vector mapping the supplied
	 * FTexture object.
	 * @param triangles A Vector of FTriangle objects to be drawn.
	 * @param texture A FTexture object contains and image and describes the texture mapping method.
	 */
	public void triangles(Vector<FTriangle> triangles,
			FTexture texture) {
		p5.beginShape(PApplet.TRIANGLES);
		p5.texture(texture.getImage());
		for (int i = 0; i < triangles.size(); i++) {
			FTriangle tri = triangles.elementAt(i);

			p5.normal(tri.v1.normal.x, tri.v1.normal.y,
					tri.v1.normal.z);
			p5.vertex(tri.getV1().x, tri.getV1().y,
					tri.getV1().z, texture.getU(tri.getV1()),
					texture.getV(tri.getV1()));
			p5.normal(tri.v2.normal.x, tri.v2.normal.y,
					tri.v2.normal.z);
			p5.vertex(tri.getV2().x, tri.getV2().y,
					tri.getV2().z, texture.getU(tri.getV2()),
					texture.getV(tri.getV2()));
			p5.normal(tri.v3.normal.x, tri.v3.normal.y,
					tri.v3.normal.z);
			p5.vertex(tri.getV3().x, tri.getV3().y,
					tri.getV3().z, texture.getU(tri.getV3()),
					texture.getV(tri.getV3()));
		}
		p5.endShape();
	}
	
	/**
	 * Draws all of the FTriangles contained with in the Vector without calling the 
	 * beginShape and endShape methods.
	 * @param triangles A Vector of FTriangle objects to be drawn.
	 */
	private void justDrawTrinagles(Vector<FTriangle> triangles) {
		for (int i = 0; i < triangles.size(); i++) {
			
			FTriangle tri = triangles.elementAt(i);
			p5.vertex(tri.getV1().x, tri.getV1().y,
					 tri.getV1().z);
			p5.vertex(tri.getV2().x, tri.getV2().y,
					tri.getV2().z);
			p5.vertex(tri.getV3().x, tri.getV3().y,
					tri.getV3().z);
		}
	}

	/**
	 * Draws all of the FTriangles contained with in the Vector using the Graphics 
	 * class directly without calling the beginShape and endShape methods.
	 * @param triangles A Vector of FTriangle objects to be drawn.
	 */
	private void justDrawTrinagles(PGraphics g, Vector<FTriangle> triangles) {
		for (int i = 0; i < triangles.size(); i++) {
			FTriangle tri = triangles.elementAt(i);
			g.vertex(tri.getV1().x, tri.getV1().y,
					tri.getV1().z);
			g.vertex(tri.getV2().x, tri.getV2().y,
					tri.getV2().z);
			g.vertex(tri.getV3().x, tri.getV3().y,
					tri.getV3().z);
		}
	}

	/**
	 * Creates a texture containing 4 squares that can be useful for testing purposes.
	 * @return The PImage that you can use as a texture.
	 */
	public PImage createTestTexture() {
		PGraphics textureG = p5.createGraphics(256, 256, PApplet.P3D);
		textureG.beginDraw();
		textureG.background(255);
		textureG.fill(255, 100, 0);
		textureG.noStroke();
		textureG.rect(0, 0, 128, 128);
		textureG.fill(0, 100, 255);
		textureG.rect(128, 128, 128, 128);
		return textureG;
	}

	/**
	 * Draws the GeometryUV object.
	 * @param shape The shape to be drawn.
	 */
	public void uv(FGeometryUV shape) {
		try{
		  //FSolid currentShape = new FSolid();
		  FVertex[][] vertexArray = new FVertex[(int)shape.getResolutionV()+1][(int)shape.getResolutionU()+1];
			for (int y = 0; y < vertexArray.length; y++) { // creates
				float v1 = shape.getVMin() + y
						* (shape.getVMax() - shape.getVMin())
						/ shape.getResolutionV();
				p5.beginShape(PApplet.TRIANGLE_STRIP);
				for (int x = 0; x < vertexArray[0].length; x++) {
					float u = shape.getUMin() + x
							* (shape.getUMax() - shape.getUMin())
							/ shape.getResolutionU();

					float x1 = shape.getX(u, v1) * shape.getScale();
					float y1 = shape.getY(u, v1) * shape.getScale();
					float z1 = shape.getZ(u, v1) * shape.getScale();
					
					FVertex v = new FVertex(x1, y1, z1);
					vertexArray[y][x] = v;
				}
				p5.endShape();
//				if(fGraphics!=null){
//					currentShape = fGraphics.getCurrentShape();
//				}
			}
			for (int y = 0; calculateNormals && y < vertexArray[0].length; y++) {
				for (int x = 0; x < vertexArray.length; x++) {
					FVertex it = vertexArray[x][y];
					FVertex left;
					if(x>0){
						left = vertexArray[x-1][y];
					}else{
						left = vertexArray[vertexArray.length-1][y];
					}

					FVertex right;
					if(x < vertexArray.length - 1){
						right = vertexArray[x+1][y];
					}else{
						right = vertexArray[0][y];
					}

					FVertex top;
					if(y>0){
						top = vertexArray[x][y-1];
					}else{
						top = vertexArray[x][vertexArray[0].length - 1];
					}

					FVertex bottom;;
					if(y < vertexArray[0].length - 1){
						bottom = vertexArray[x][y+1];
					}else{
						bottom = vertexArray[x][0];
					}

					FVertex v1 = new FVertex(right.x - left.x, right.y - left.y, right.z - left.z);
					FVertex v2 = new FVertex(bottom.x - top.x, bottom.y - top.y, bottom.z - top.z);
					it.setNormal(v1.cross(v2));
					it.normal.normalise();
				}
			}




			// for (float v = shape.getVMin(); v <= shape.getVMax(); v += incV) { //
			// creates
			for (int y = 0; y < vertexArray[0].length; y++) {
				// a
				// half
				// circle
				p5.beginShape(PApplet.TRIANGLES);
				// for (float u = shape.getUMin(); u <= shape.getUMax() + incU; u +=
				// incU) {
				for (int x = 0; x < vertexArray.length; x++) {
					FVertex fv1 = vertexArray[x][y];
					FVertex fv2;
					FVertex fv3;
					FVertex fv4;
					if(x < vertexArray.length - 1){
						fv2 = vertexArray[x+1][y];
						if(y < vertexArray[0].length - 1){
							fv4 = vertexArray[x+1][y+1];
						}else{
							fv4 = vertexArray[x+1][0];
						}
					}else{
						fv2 = vertexArray[0][y];
						if(y < vertexArray[0].length - 1){
							fv4 = vertexArray[0][y+1];
						}else{
							fv4 = vertexArray[0][0];
						}
					}
					if(y < vertexArray[0].length - 1){
						fv3 = vertexArray[x][y+1];
					}else{
						fv3 = vertexArray[x][0];
					}

					if(calculateNormals)p5.normal(fv1.normal.x, fv1.normal.y, fv1.normal.z);
					p5.vertex(fv1.x, fv1.y, fv1.z);
					if(calculateNormals)p5.normal(fv2.normal.x, fv2.normal.y, fv2.normal.z);
					p5.vertex(fv2.x, fv2.y, fv2.z);
					if(calculateNormals)p5.normal(fv3.normal.x, fv3.normal.y, fv3.normal.z);
					p5.vertex(fv3.x, fv3.y, fv3.z);

					if(calculateNormals)p5.normal(fv2.normal.x, fv2.normal.y, fv2.normal.z);
					p5.vertex(fv2.x, fv2.y, fv2.z);
					if(calculateNormals)p5.normal(fv4.normal.x, fv4.normal.y, fv4.normal.z);
					p5.vertex(fv4.x, fv4.y, fv4.z);
					if(calculateNormals)p5.normal(fv3.normal.x, fv3.normal.y, fv3.normal.z);
					p5.vertex(fv3.x, fv3.y, fv3.z);
				}
				p5.endShape();
			}
		}catch(NullPointerException npe){
			// p5 is null when the applet is shutting down.
		}
	}

	/**
	 * Draws lonely  edges which could cause problems in Rapid Manufacturing.
	 * @param triangles
	 */
	public void singleEdges(Vector<FTriangle> triangles) {
		HashMap<FEdge, Integer> edgeMap = new HashMap<FEdge, Integer>();
		for (Iterator<FTriangle> iterator = triangles.iterator(); iterator
				.hasNext();) {
			FTriangle tri = iterator.next();

			for (int i = 0; i <= 2; i++) {
				Integer edgeCount = edgeMap.get(tri.getE(i));
				if (!edgeMap.containsKey(tri.getE(i))) {
					edgeMap.put(tri.getE(i), new Integer(1));
				} else {
					edgeMap.put(tri.getE(i), Integer.valueOf(1 + edgeCount
							.intValue()));
				}
			}
		}

		PGraphics3D g2 = (PGraphics3D) p5.createGraphics(p5.width, p5.height,
				PApplet.P3D);
		g2.beginDraw();
		g2.colorMode(PApplet.HSB, 100);
		g2.stroke(p5.frameCount % 100, 100, 100);
		g2.strokeWeight(1.5f);

		PMatrix3D m = ((PGraphics3D) p5.g).modelview;
		g2.setMatrix(m);

		for (Iterator<Entry<FEdge, Integer>> iterator = edgeMap.entrySet()
				.iterator(); iterator.hasNext();) {

			Entry<FEdge, Integer> keyValue = iterator.next();
			int triCount = (keyValue.getValue()).intValue();
			if (triCount == 1) {
				FEdge edge = keyValue.getKey();
				FVertex v1 = edge.getV1();
				FVertex v2 = edge.getV2();
				g2.line(v1.x, v1.y, v1.z, v2.x,
						v2.y, v2.z);
			}
		}
		g2.endDraw();
		p5.blend(g2, 0, 0, p5.width, p5.height, 0, 0, p5.width, p5.height,
				PApplet.ADD);

	}
	
	/**
	 * Draws a plane starting from (0,0).
	 * @param width The width of the plane to be drawn.
	 * @param height The width of the plane to be drawn.
	 */
	public void plane(float width, float height) {
		p5.beginShape();
		p5.vertex(0, 0);
		p5.vertex(width, 0);
		p5.vertex(width, height);
		p5.vertex(0, height);
		p5.endShape(PApplet.CLOSE);
	}


}
