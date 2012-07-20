package eu.fluidforms.processing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import processing.core.PApplet;
import processing.core.PMatrix;
import processing.core.PMatrix3D;

public class Navigation implements MouseWheelListener {

	private PApplet p5;

	private PMatrix3D camera;
	private PMatrix origionalMatrix;
	private float deltaX = 0;
	private float deltaY = 0;
	private float deltaZoom = 0;
	private float deltaPanH = 0;
	private float deltaPanV = 0;

	private float translationX = 0;
	private float translationY = 0;
	private float translationZ = 0;
	
	private float panSensitivity = 1;
	private float rotationSensitivity = 1;
	private float zoomSensitivity = 1;

	private float pmouseX = 0;
	private float pmouseY = 0;
		
	private boolean centered = false;

	private boolean gotHardDriveAccess = true;

	public void setup(PApplet processingSketch){
		this.p5 = processingSketch;
		// The Firefox Java Plugin caches FluidForms in the JRE after a dispose(), 
		// so I am explicitly resetting the values.
		if(!processingSketch.getClass().getName().equals("eu.fluidforms.groocessing.Stage")){
			deltaX = 0;
			deltaY = 0;
			deltaZoom = 0;
			deltaPanH = 0;
			deltaPanV = 0;
			translationX = 0;
			translationY = 0;
			translationZ = 0;
		}

		if(FF.is3D){
			origionalMatrix = p5.getMatrix();
		}
		camera = new PMatrix3D();

		p5.registerMouseEvent(this);

		if(PUtils.isRunningInBrowser(processingSketch)){
			p5.addMouseWheelListener(this);
		}

		center();

	}
		
	/**
	 * Sets how sensitive the rotation with the mouse is. The default value is 1, a 
	 * higher value makes it rotate faster.
	 * @param val The value to set the sensitivity to.
	 */
	public void setRotationSensitivity(float val) {
		rotationSensitivity = val;
	}
	/**
	 * Sets how sensitive the zoom with the mouse is. The default value is 1, a 
	 * higher value makes it zoom faster.
	 * @param val The value to set the sensitivity to.
	 */
	public void setZoomSensitivity(float val) {
		zoomSensitivity = val;
	}
	/**
	 * Sets how sensitive the pan with the mouse is. The default value is 1, a 
	 * higher value makes it pan faster.
	 * @param val The value to set the sensitivity to.
	 */
	public void setPanSensitivity(float val) {
		panSensitivity = val;
	}

	/**
	 * Sets what could be considered the camera zoom. This is useful if you are 
	 * creating an object in cm or inches and want to fit it on the screen.
	 * @param val
	 */
	public void zoom(float val) {
		translationZ = val;
	}

	/**
	 * This can be called in your draw method to provide mouse navigation. 
	 * If useFluidFormsDefaults is set to the default value of true this 
	 * will be called automatically.
	 */
	public void navigate() {
		translate(deltaPanH * panSensitivity, deltaPanV * panSensitivity,
				deltaZoom * zoomSensitivity);
		
		deltaPanH = 0;
		deltaPanV = 0;
		deltaZoom = 0;
		
		rotate(deltaY * rotationSensitivity, deltaX * rotationSensitivity, 0);

		deltaX = 0;
		deltaY = 0;
	}
	
	public void translate(float x, float y, float z){
		translationX += x;
		translationY += y;
		translationZ += z;
		if(FF.is3D){
			p5.translate(translationX, translationY, translationZ);
		}else{
			p5.translate(translationX, translationY);
		}
	}
	public void resetMatrix(){
		if(FF.is3D){
			p5.setMatrix(origionalMatrix);
		}
	}
	private void resetTranslation(){
		if(FF.is3D){
			camera.translate(translationX, translationY, translationZ);
		}else{
			camera.translate(translationX, translationY);
		}
	}
	
	public void setRotation(float x, float y, float z){
		camera = new PMatrix3D();
		resetTranslation();
		setRawRotation(camera, x, y, z);
	}
	private void setRawRotation(PMatrix3D matrix, float x, float y, float z){
		if(!FF.is3D)return;
		
		PMatrix3D camtmp = new PMatrix3D();

		camtmp.rotateX(x);
		matrix.apply(camtmp);
		camtmp.reset();
		
		camtmp.rotateY(y);
		matrix.apply(camtmp);
		camtmp.reset();
		
		camtmp.rotateZ(z);
		matrix.apply(camtmp);

		p5.applyMatrix(matrix);
	}
	public void rotate(float x, float y, float z){
		rawRotate(camera, x, y, z);
	}
	
	private void rawRotate(PMatrix3D matrix, float x, float y, float z){
		if(!FF.is3D)return;
		
		PMatrix3D camtmp = new PMatrix3D();

		camtmp.rotateX(x);
		matrix.preApply(camtmp);
		camtmp.reset();
		
		camtmp.rotateY(y);
		matrix.preApply(camtmp);
		camtmp.reset();
		
		camtmp.rotateZ(z);
		matrix.preApply(camtmp);
		
		p5.applyMatrix(matrix);
	}
	/**
	 * Moves to the center of the screen or in other words ... translate(width / 2, height / 2)
	 */
	public void center() {
		
		if(!centered){
			translate(p5.width / 2, p5.height / 2, 0);
			centered = true;
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		translationZ -= e.getWheelRotation() * 30;
	}
	/**
	 * Called by processing when a mouse event occurs.
	 * @param event
	 */
	public void mouseEvent(MouseEvent event) {
		switch (event.getID()) {
			case MouseEvent.MOUSE_DRAGGED :
				if (p5.mouseButton == PApplet.LEFT) {
					deltaX -= (pmouseX - event.getX()) / p5.width
							* PApplet.TWO_PI;
					deltaY += (pmouseY - event.getY()) / p5.height
							* PApplet.TWO_PI;
				} else {
					if (event.isShiftDown() || event.isControlDown() || event.isAltDown()) {
						deltaPanH += event.getX() - pmouseX;
						deltaPanV += event.getY() - pmouseY;
					} else {
						deltaZoom += event.getY() - pmouseY;
					}
				}
		}
		pmouseX = event.getX();
		pmouseY = event.getY();
	}
	public void dispose(){
		p5.unregisterMouseEvent(this);
	}


}
