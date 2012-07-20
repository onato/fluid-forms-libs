/*
 * Press (ctrl/cmd)-e to export the file.
 */

import eu.fluidforms.utils.*;
import eu.fluidforms.processing.*;
import eu.fluidforms.geom.*;
import eu.fluidforms.io.*;
import eu.fluidforms.parser.*;

PImage img;
int res = 50;
int BAR_WIDTH = 10;
int SPACING = 10;

void setup(){
  size(1000, 1000, P3D);
  FluidFormsLibs.setup(this);
  FluidFormsLibs.navigation.zoom(350);
  img = loadImage("freek.jpg");
  noStroke();
}

void draw(){
  background(255);
  if(frameCount < 20){
    //FluidForms sends us by default to the center of the screen.
    image(img, -300, -300);
  }
  else{
    translate(-res/2*SPACING,-res/2*SPACING,0);
    for(float i = 0; i<res;  i++){
      for(float j = 0; j<res;  j++){
        pushMatrix();
        color c = img.get((int)(i/res*img.width), (int)(j/res*img.height));
        float r = (float)red(c)/2;
        translate(i*SPACING, j*SPACING, r);
        translate(BAR_WIDTH/2, BAR_WIDTH/2, -50);
        box(BAR_WIDTH,BAR_WIDTH,100);
        popMatrix();
      }

    }  
  }
}

