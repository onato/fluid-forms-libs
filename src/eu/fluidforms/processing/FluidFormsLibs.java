package eu.fluidforms.processing;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Vector;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

import netscape.javascript.JSObject;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import eu.fluidforms.geom.FGeometryUV;
import eu.fluidforms.geom.FSolid;
import eu.fluidforms.geom.FTexture;
import eu.fluidforms.geom.FTriangle;
import eu.fluidforms.geom.FVertex;
import eu.fluidforms.utils.Triangulate;

/**
 * This class provides some useful feature to processing for creating fluid forms, 
 * such as file exports upon pressing ctrl-e or methods for drawing a Vector of triangles 
 * or a FGeometryUV object.
 * @author Stephen Williams
 *
 */
public class FluidFormsLibs {
	private static Preferences preferences;
	
	private static final String VERSION = "0.2.02";
	public static final String RECORDER = "eu.fluidforms.processing.FGraphics";
	
	public static boolean online = false;

	private static FluidFormsLibs myInstance;
	public static PApplet p5;
	private static FGraphics fGraphics;
	public static Navigation navigation;
	public static Draw draw;
	private static boolean useFluidFormsDefaults = true;
	private static boolean useDefaultLights = true;
	private static boolean doExport = false;
	private static boolean exportEnabled = true;
	protected static boolean is3D = true;
	
	/**
	 * Disables exporting on ctrl-e.
	 * @param exportEnabled
	 */
	public static void setExportEnabled(boolean exportEnabled) {
		FluidFormsLibs.exportEnabled = exportEnabled;
	}
	
	private static boolean exporting = false;
	private static boolean exitOnExport = false;
	private static boolean finished = false;
	private static boolean gotHardDriveAccess = true;
	private static String exportFileName = null;
	private static String exportFileFormat = "stl";
	private static ParameterChanger parameterChanger;
	public static ParameterChanger getParameterChanger() {
		return parameterChanger;
	}
	private static boolean showParameterChanger = true;
	
	public static void setShowParameterChanger(boolean showParameterChanger) {
		FluidFormsLibs.showParameterChanger = showParameterChanger;
	}

	private static boolean disableRegisterMethods = false;
	public static void disableRegisterMethods(Boolean val){
		disableRegisterMethods = val;
	}
	public static boolean useFluidFormsDefaults(){
		return useFluidFormsDefaults;
	}
	private static boolean applyMatrixToExport = true;
	public static void disableMatrixForExport(){
		applyMatrixToExport = false;
	}

	protected FluidFormsLibs() {
	}
	public static FluidFormsLibs getInstance(){
		return myInstance;
	}
	
	/**
	 * This method must be called after the size() method in your setup() method.
	 * @param processingSketch Your processing sketch passed in as FluidForms.setup(this).
	 */
	public static void setup(PApplet processingSketch) {
		setup(processingSketch, true);
	}
	/**
	 * This method must be called after the size() method in your setup() method.
	 * @param processingSketch Your processing sketch passed in as FluidForms.setup(this).
	 * @param useFluidFormsDefaults Your useful default settings. 
	 * For 2D work you might want to turn this off.
	 */
	public static void setup(PApplet processingSketch, Boolean useFluidFormsDefaults) {
		
		FluidFormsLibs.useFluidFormsDefaults = useFluidFormsDefaults;
		
		finished = false;
		if(p5!=null){
			return;
		}
		System.out.println("Loading Fluid-Forms-Libs Version " + VERSION);
		p5 = processingSketch;

		is3D = !p5.g.getClass().getName().equals(PApplet.JAVA2D) && !p5.g.getClass().getName().equals(PApplet.P2D);

		setupIfRunningInBrowser();

		

		myInstance = new FluidFormsLibs();
		draw = new Draw(p5);

		if(useFluidFormsDefaults){
			navigation = new Navigation();
			navigation.setup(processingSketch);
			if(!disableRegisterMethods){
				// a hack to check that we are not rendering from a servlet.
				// Processing should just throw an error without stopping so I can decide how bad this is.
				p5.registerSize(myInstance);
				p5.registerPre(myInstance);
				p5.registerPost(myInstance);
				p5.registerKeyEvent(myInstance);
				p5.registerDispose(myInstance);
			}
			if(showParameterChanger){
				showParameterChangerIfParamsExist();
			}
		}
		if(!online){
			preferences = Preferences.userNodeForPackage(p5.getClass());
		}
		
	}
	private static void setupIfRunningInBrowser() {
		try{
			new File(p5.sketchPath);
		}catch(Exception e){
			gotHardDriveAccess = false;
			online = true;
		}
	}
	
	/**
	 * Sets whether or not to use default setting that used useful and often used. You control
	 * freaks ;-) out there may want to turn it off.
	 * @param val
	 */
	public static void useFluidFormsDefaults(boolean val) {
		useFluidFormsDefaults = val;
	}

	/**
	 * Loads the geometry contained with in the file you pass in.
	 * @param fileName The name of the file to be read in.
	 * @return A Vector of FTriangles.
	 */
	public static Vector<FTriangle> load(String fileName) {
		if (p5 != null) {
			fileName = p5.dataPath(fileName);
		}
		return Loader.load(fileName);
	}
	
	/**
	 * Called by processing when the user presses a key.
	 * @param event
	 */
	public void keyEvent(KeyEvent event) {
		if ((event.isMetaDown() || event.isControlDown()) && event.getKeyCode() == 69) {
			//doExport = !p5.online;
			if(exportEnabled){
				doExport = gotHardDriveAccess;// hack 
			}
		}
	}

	public static void export() {
		FluidFormsLibs.doExport = true;
	}
	
	/**
	 * Called by processing after you call the size() method in your sketch. 
	 * It is the closest we have to getting into the setup method. 
	 * @param width
	 * @param height
	 */
	public void size(int width, int height) {
		p5.noStroke();
	}
	/**
	 * This is called by processing before the draw() method to allow us to start exporting, set the background and lights.
	 */
	public void pre() {
		if (useFluidFormsDefaults) {
			p5.background(255);
			if(useDefaultLights && is3D){
				lights();
			}
			
			p5.noStroke();
			if (doExport) {
				System.out.println("FluidFormsLib: Exporting...");
				
				String fileName = exportFileName;
				if(!is3D || ",PNG,JPG".indexOf(exportFileFormat.toUpperCase())>0){
					if(exportFileFormat.equals("stl")){ // we are not in 3D
						setExportFileFormat("png");
					}
					if(!exportFileFormat.equals("pdf")){
						fileName = p5.getClass().getName()+"."+exportFileFormat;
					}else{
						fileName = p5.getClass().getName()+".pdf";
						try{
							p5.beginRecord(PApplet.PDF, fileName);
						}catch(Exception e){
							exportFileFormat = "PNG";
							fileName = p5.getClass().getName()+".png";
						}
					}
				}else{
					if(fileName==null){
						fileName = p5.getClass().getName()+"."+exportFileFormat;
					}
					if(fileName.indexOf('/')<0 || fileName.indexOf('\\')>0){
						fileName = p5.sketchPath + "/" + fileName;
					}
					if(exportFileFormat.toUpperCase().equals("DXF")){
					    p5.beginRaw(PApplet.DXF, fileName);
					}else{
						fGraphics = (FGraphics)p5.beginRecord(RECORDER, fileName);
						if(is3D){
							fGraphics.setApplyTransformMatrix(applyMatrixToExport);
						}
					}
				}
				
				System.out.println("Saving " + fileName + "...");
				doExport = false;
				exporting = true;
			}
		}
		navigation.navigate();
	}
	
	/**
	 * Called by processing after the draw() method to allow us to export our geometry.
	 */
	public void post() {
		if(exporting){
			if(exportFileFormat.toUpperCase().equals("PNG")){
				p5.saveFrame(p5.getClass().getName()+".png");
			}else if(exportFileFormat.toUpperCase().equals("DXF")){
				p5.endRaw();
			}else{
				p5.endRecord();
			}
			System.out.println("FluidFormsLib: Done!");
			beep();
			exporting = false;
			if (exitOnExport) {
				p5.exit();
			}
		}
	}
	

	/**
	 * This is called by processing before the draw() method to allow us to start exporting, set the background and lights.
	 */
	public void dispose() {
		p5.noLoop();
		p5.unregisterSize(myInstance);
		p5.unregisterPre(myInstance);
		p5.unregisterPost(myInstance);
		p5.unregisterKeyEvent(myInstance);
		p5.unregisterDispose(myInstance);

		finished = true;
		p5 = null;
		fGraphics = null;
		exportFileName = null;
		exportFileFormat = null;
		parameterChanger = null;
		myInstance = null;
		
		navigation.dispose();
		navigation = null;
		//System.out.println("Destroyed Fluid-Forms-Libs :-(");
		
	}
	
	public static FGraphics record(){
		fGraphics = (FGraphics)p5.beginRecord(RECORDER, "");  
		return fGraphics;
	}
	  public void lights() {
		if(finished)return;
	    // need to make sure colorMode is RGB 255 here
	    int colorModeSaved = p5.g.colorMode;
	    p5.g.colorMode = PApplet.RGB;

	    p5.g.lightFalloff(1, 0, 0);
	    p5.g.lightSpecular(0, 0, 0);

	    p5.g.ambientLight(p5.g.colorModeX * 0.5f,
	    		p5.g.colorModeY * 0.5f,
	    		p5.g.colorModeZ * 0.5f);
	    p5.g.directionalLight(p5.g.colorModeX * 0.5f,
	    		p5.g.colorModeY * 0.5f,
	    		p5.g.colorModeZ * 0.5f,
	                     1, 1, -1);

	    p5.g.colorMode = colorModeSaved;

	  }

	public static void showParameterChangerIfParamsExist(){
		parameterChanger = new ParameterChanger();
		parameterChanger.setup(p5);
	}
	public static void showParameterChanger(){
		parameterChanger = new ParameterChanger();
		parameterChanger.forceShow = true;
		parameterChanger.setup(p5);
	}
	public static void hideParameterChanger(){
		parameterChanger.hide();
	}
	/**
	 * Sets the name of the file that is exported.
	 * @param exportFileName The path and name of the file to be exported.
	 */
	public static void setExportFileName(String exportFileName) {
		FluidFormsLibs.exportFileName = exportFileName;
	}
	/**
	 * The format of the exported file. The file name is the name of the sketch.
	 * @param exportFileFormat The extension of the file to be exported without the dot.
	 */
	public static void setExportFileFormat(String exportFileFormat) {
		FluidFormsLibs.exportFileFormat = exportFileFormat;
	}
	/**
	 * Returns whether or not we are currently exporting a file.
	 * @return A boolean value whether we are currently exporting or not.
	 */
	public static boolean isExporting(){
		return exporting;
	}
	
	
	public static FSolid getLastShape(){
		return fGraphics.getCurrentShape();
	}
	public static FGraphics getGraphics() {
		return fGraphics;
	}
	
	/**
	 * Sends the eventName to the the javascript event handler fluidforms.widgetAction(eventName).
	 * @param eventName The name of the event being fired.
	 */
	public static void sendEvent(String eventName){
		js("fluidforms", "widgetAction", new Object[]{ eventName});
	}
	/**
	 * Sends the parameters to the fluidforms.addToCart() javascript function.
	 * @param params An Object array of parameters to be passed.
	 */
	public static void addToCart(Object[]params){
		js("fluidforms", "addToCart", params);
	}
	/**
	 * Sends the parameters to the fluidforms.addToMyDesigns() javascript function.
	 * @param params An Object array of parameters to be passed.
	 */
	public static void addToMyDesigns(Object[]params){
		js("fluidforms", "addToMyDesigns", params);
	}

	/**
	 * Evaluates and calls the javascirpt you pass in without using JSObject. This should
	 * only be used for simple scripts. Passing JSON for example may not work on all browsers.
	 * @param script
	 */
	public static void js(String script){
		if(online){
			p5.link("javascript:void(" + script + ")");
		}else{
			System.err.println("javascript:void(" + script + ")");
		}
	}
	/**
	 * Calls the specified function on the specified object passing the specified parameters.
	 * @param object The name of the javascript object to call the function on.
	 * @param function The name of the javascript function to call.
	 * @param params The the Object[] of parameters to pass to the javascript function.
	 */
	public static void js(String object, String function, Object[]params){
		JSObject window = JSObject.getWindow(p5);
		try{
			JSObject fluidforms = (JSObject) window.getMember(object);
			fluidforms.call(function, params);
		}catch(java.lang.ClassCastException e){}
	}

	/**
	 * Sets whether we should use commonly used lighting settings or not.
	 * @param useDefaultLights
	 */
	public static void useDefaultLights(boolean useDefaultLights) {
		FluidFormsLibs.useDefaultLights = useDefaultLights;
	}
	/**
	 * Sets whether or not to calculate the vertex normals when rendering a FGeometryUV for example.
	 * @param calculateNormals
	 */
	public static void calculateNormals(boolean calculateNormals) {
		Draw.calculateNormals = calculateNormals;
	}
	
	public static void beep(){
		Toolkit.getDefaultToolkit().beep();
	}
	
	public static String enterString(String title, String def){
		String s = (String)JOptionPane.showInputDialog(
                p5.frame,
                "",
                title,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                def);

		//If a string was returned, say so.
		if (s == null || s.length() <= 0) {
			return null;
		}
		return s;
	}
	
	public static Vector<FTriangle> triangulate(Vector<FVertex> vertices){
		return Triangulate.triangulate(vertices);
	}

	public static String getPreference(String key, String def){
		return preferences.get(key, def);
	}
	public static void setPreference(String key, String value){
		preferences.put(key, value);
	}

	private static void youHaveToCallSetup(){
		System.err.println("You have to call FluidForms.setup(this) just after you call size(...) in your setup().");
		System.exit(1);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Draws all of the FTriangles contained with in the Vector.
	 * @param triangles A Vector of FTriangle objects to be drawn.
	 */
	public static void drawTriangles(Vector<FTriangle> triangles) {
		sendDrawingDepreciationWarning("drawTriangles", "triangles");
		draw.triangles(triangles);
	}

	/**
	 * Draws all of the FTriangles contained with in the Vector using 
	 * the PGraphics directly.
	 * @param triangles A Vector of FTriangle objects to be drawn.
	 */
	public static void drawTriangles(PGraphics g, Vector<FTriangle> triangles) {
		sendDrawingDepreciationWarning("drawTriangles", "triangles");
		draw.triangles(g, triangles);
	}
	
	/**
	 * Draws all of the FTriangles contained with in the Vector mapping the supplied
	 * texture to them.
	 * @param triangles A Vector of FTriangle objects to be drawn.
	 * @param texture
	 */
	public static void drawTriangles(Vector<FTriangle> triangles, PImage texture) {
		sendDrawingDepreciationWarning("drawTriangles", "triangles");
		draw.triangles(triangles, texture);
	}
	
	/**
	 * Draws all of the FTriangles contained with in the Vector mapping the supplied
	 * FTexture object.
	 * @param triangles A Vector of FTriangle objects to be drawn.
	 * @param texture A FTexture object contains and image and describes the texture mapping method.
	 */
	public static void drawTriangles(Vector<FTriangle> triangles,
			FTexture texture) {
		sendDrawingDepreciationWarning("drawTriangles", "triangles");
		draw.triangles(triangles, texture);
	}
	
	/**
	 * Creates a texture containing 4 squares that can be useful for testing purposes.
	 * @return The PImage that you can use as a texture.
	 */
	public static PImage createTestTexture() {
		sendDrawingDepreciationWarning("createTestTexture", "createTestTexture");
		return draw.createTestTexture();
	}

	/**
	 * Draws the GeometryUV object.
	 * @param shape The shape to be drawn.
	 */
	public static void drawUV(FGeometryUV shape) {
		if(finished)return;
		if(p5==null){
			youHaveToCallSetup();
			return;
		}
		sendDrawingDepreciationWarning("drawUV", "uv");
		draw.uv(shape);
	}

	/**
	 * Draws lonely  edges which could cause problems in Rapid Manufacturing.
	 * @param triangles
	 */
	public static void drawSingleEdges(Vector<FTriangle> triangles) {
		sendDrawingDepreciationWarning("drawSingleEdges", "singleEdges");
		draw.singleEdges(triangles);
	}
	
	/**
	 * Draws a plane starting from (0,0).
	 * @param width The width of the plane to be drawn.
	 * @param height The width of the plane to be drawn.
	 */
	public static void plane(float width, float height) {
		sendDrawingDepreciationWarning("plane", "plane");
		draw.plane(width, height);
	}

	
	
	
	
	
	
	/**
	 * Sets how sensitive the rotation with the mouse is. The default value is 1, a 
	 * higher value makes it rotate faster.
	 * @param val The value to set the sensitivity to.
	 */
	public static void setRotationSensitivity(float val) {
		sendMouseDepreciationWarning("setRotationSensitivity");
		navigation.setRotationSensitivity(val);
	}

	/**
	 * Sets how sensitive the zoom with the mouse is. The default value is 1, a 
	 * higher value makes it zoom faster.
	 * @param val The value to set the sensitivity to.
	 */
	public static void setZoomSensitivity(float val) {
		sendMouseDepreciationWarning("setZoomSensitivity");
		navigation.setZoomSensitivity(val);
	}

	/**
	 * Sets how sensitive the pan with the mouse is. The default value is 1, a 
	 * higher value makes it pan faster.
	 * @param val The value to set the sensitivity to.
	 */
	public static void setPanSensitivity(float val) {
		sendMouseDepreciationWarning("setPanSensitivity");
		navigation.setPanSensitivity(val);
	}

	/**
	 * Sets what could be considered the camera zoom. This is useful if you are 
	 * creating an object in cm or inches and want to fit it on the screen.
	 * @param val
	 */
	public static void zoom(float val) {
		sendMouseDepreciationWarning("zoom");
		navigation.zoom(val);
	}

	/**
	 * This can be called in your draw method to provide mouse navigation. 
	 * If useFluidFormsDefaults is set to the default value of true this 
	 * will be called automatically.
	 */
	public static void navigate() {
		sendMouseDepreciationWarning("navigate");
		navigation.navigate();
	}
	
	public static void translate(float x, float y, float z){
		sendMouseDepreciationWarning("translate");
		navigation.translate(x, y, z);
	}
	public static void resetMatrix(){
		sendMouseDepreciationWarning("resetMatrix");
		navigation.resetMatrix();
	}
	
	public static void setRotation(float x, float y, float z){
		sendMouseDepreciationWarning("setRotation");
		navigation.setRotation(x, y, z);
	}
	public static void rotate(float x, float y, float z){
		sendMouseDepreciationWarning("rotate");
		navigation.rotate(x, y, z);
	}

	
	private static boolean givenMouseDepreciationWarning = false;
	private static void sendMouseDepreciationWarning(String functionName){
		if(!givenMouseDepreciationWarning){
			System.err.println("The function FluidFormsLibs."+functionName + "() is depreciated. Use FluidFormsLibs.navigation."+functionName + "() instead." );
			givenMouseDepreciationWarning = true;
		}
	}
	private static boolean givenDrawingDepreciationWarning = false;
	private static void sendDrawingDepreciationWarning(String functionName, String newFunctionName){
		if(!givenDrawingDepreciationWarning){
			System.err.println("The function FluidFormsLibs."+functionName + "() is depreciated. Use FluidFormsLibs.draw." + newFunctionName + "() instead." );
			givenDrawingDepreciationWarning = true;
		}
	}
}
