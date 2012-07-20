import eu.fluidforms.utils.*;
import eu.fluidforms.processing.*;
import eu.fluidforms.geom.*;
import eu.fluidforms.io.*;
import eu.fluidforms.parser.*;

Vector triangles;

void setup(){
  size(500, 800, P3D);
  FluidFormsLibs.setup(this);
  frameRate(20);
  noStroke();
  triangles = FluidFormsLibs.load("Bottle.stl");
}
void draw(){
  rotateX(PI/2);
  rotateZ(PI/2+ (float)frameCount/100);
  FluidFormsLibs.draw.triangles(triangles);
  FluidFormsLibs.draw.singleEdges(triangles);
}
