package eu.fluidforms.processing;

import java.io.File;

import processing.core.PApplet;
import processing.core.PImage;

public class PUtils {
	private static boolean gotHardDriveAccess = true;
	public static boolean isRunningInBrowser(PApplet p5){
		try{
			new File(p5.sketchPath);
		}catch(Exception e){
			gotHardDriveAccess = false;
		}
		return gotHardDriveAccess;
	}
	
	public static PImage blur(PImage img){
		  float v = 1.0f/9.0f;
		  float[][] kernel = { { v, v, v },
		                     { v, v, v },
		                     { v, v, v } };

		  // Create an opaque image of the same size as the original
		  PImage edgeImg = FF.p5.createImage(img.width, img.height, PApplet.RGB);

		  // Loop through every pixel in the image.
		  for (int y = 1; y < img.height-1; y++) { // Skip top and bottom edges
		    for (int x = 1; x < img.width-1; x++) { // Skip left and right edges
		      float sum = 0; // Kernel sum for this pixel
		      for (int ky = -1; ky <= 1; ky++) {
		        for (int kx = -1; kx <= 1; kx++) {
		          // Calculate the adjacent pixel for this kernel point
		          int pos = (y + ky)*img.width + (x + kx);
		          // Image is grayscale, red/green/blue are identical
		          float val = FF.p5.red(img.pixels[pos]);
		          // Multiply adjacent pixels based on the kernel values
		          sum += kernel[ky+1][kx+1] * val;
		        }
		      }
		      // For this pixel in the new image, set the gray value
		      // based on the sum from the kernel
		      edgeImg.pixels[y*img.width + x] = FF.p5.color(sum);
		    }
		  }
		  // State that there are changes to edgeImg.pixels[]
		  edgeImg.updatePixels();
		  
		  return edgeImg;
		}
}
