import eu.fluidforms.utils.*;
import eu.fluidforms.processing.*;
import eu.fluidforms.geom.*;
import eu.fluidforms.io.*;
import eu.fluidforms.parser.*;

Blob blob = new Blob();

void setup(){
  size(500, 800, P3D);
  FluidForms.setup(this);
  FluidForms.navigation.rotate(-PI/2, 0, 0);
  noStroke();
  FluidForms.calculateNormals(false);
  noLoop();
  mouseReleased();
}
void draw(){
  FluidForms.draw.uv(blob);
}


void mousePressed(){
  blob.resolution = 20;
}
void mouseReleased(){
  blob.resolution = 100;
  redraw();
}
void mouseDragged(){redraw();}
