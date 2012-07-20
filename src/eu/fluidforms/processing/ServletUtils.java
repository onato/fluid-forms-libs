package eu.fluidforms.processing;

import eu.fluidforms.processing.FGraphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;

import javax.servlet.http.HttpServletResponse;

import processing.core.PApplet;
import processing.core.PGraphics;

import com.sun.jimi.core.Jimi;
import com.sun.jimi.core.JimiWriter;

public class ServletUtils {

	public static void renderImage(BufferedImage img, String format, HttpServletResponse response){
		response.setContentType("image/" + format);
		
		// there is repeated code here!!!!!
		try{
			OutputStream sout = response.getOutputStream();
			JimiWriter writer = Jimi.createJimiWriter("image/" + format, sout);
			writer.setSource(img);
			writer.putImage(sout);
		}catch(IOException e){
			// this can happen if the client sends two requests at the same time.
		}catch(Exception e){
			e.printStackTrace();
		}
	}
//	public static void renderP5Image(PApplet app, HttpServletResponse response, String fileName){
//		System.setProperty("java.awt.headless", "true"); 
//		
//   		String format = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
//   		FluidForms.disableRegisterMethods(true);
//   		
//   		app.noLoop();
//		app.g = app.createGraphics(100, 100, FluidForms.RECORDER);
//		try{
//			app.setup();
//		}catch(RendererChangeException e){
//			app.setup();
//			// ignore
//		}
//		app.g.beginDraw();
//		app.draw();
//		app.g.endDraw();
//
//		Graphics2D graphics;
//		BufferedImage bufferedImage = new BufferedImage(app.g.width, app.g.height, BufferedImage.TYPE_INT_RGB);
//		graphics = bufferedImage.createGraphics();
//		graphics.drawImage(app.g.image, 0, 0, null);
//
//		try{
//	  		response.setContentType("image/" + format);
//			OutputStream out = response.getOutputStream();
//			JimiWriter writer = Jimi.createJimiWriter("image/" + format, out);
//			writer.setSource(bufferedImage);
//			writer.putImage(out);
//			out.close();
//		}catch(IOException e){
//			// this can happen if the client sends two requests at the same time.
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//
//	}

	public static FGraphics createExportGraphics(String fileName, HttpServletResponse response) throws IOException{
		return createExportGraphics(fileName, response, 100, 100);
	}
	public static FGraphics createExportGraphics(String fileName, HttpServletResponse response, int width, int height) throws IOException{
		return createExportGraphics(fileName, response, 100, 100, 0);
	}
	public static FGraphics createExportGraphics(String fileName, HttpServletResponse response, int width, int height, int numTriangles) throws IOException{
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		String ext = fileName.substring(fileName.lastIndexOf('.')+1);
		if(ext.toUpperCase().equals("STL")){
	    	response.setContentType("application/stl");
		}else{
			response.setContentType("application/gcode");
		}

    	FGraphics g = new FGraphics();
    	g.setServerSide(true);
    	g.setNumTriangles(numTriangles);

		g.init(fileName, response.getOutputStream());
		g.setSize(width, height);
		return g;
	}
	
	public static PGraphics createGraphics(int width, int height){
		return createGraphics(width, height, PApplet.P2D, null);
	}
	public static PGraphics createGraphics(int width, int height, String renderer){
		return createGraphics(width, height, renderer, null);
	}
	public static PGraphics createGraphics(int width, int height, String renderer, String path){
		
		try {
			Class<?> rendererClass = Thread.currentThread().getContextClassLoader().loadClass(renderer);

			Constructor<?> constructor = rendererClass.getConstructor();
			PGraphics pg = (PGraphics) constructor.newInstance();
			
			if (path != null) pg.setPath(path);
			pg.setSize(width, height);
			
			// everything worked, return it
			return pg;
		} catch (Exception e) {
			System.err.println("Could not create graphics: " + renderer);
			e.printStackTrace();
		}
		return null;
	}

}
