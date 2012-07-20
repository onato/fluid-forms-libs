import processing.opengl.*;

import eu.fluidforms.utils.*;
import eu.fluidforms.processing.*;
import eu.fluidforms.geom.*;
import eu.fluidforms.io.*;
import eu.fluidforms.parser.*;

public int paramHeight = 50;
public int paramWidth = 50;
public int paramDepth = 50;

void setup(){
  size(500, 800, OPENGL);
  FluidFormsLibs.setup(this);
}
void draw(){
  Sphere uvSphere = new Sphere();
  //Cylinder uvSphere = new Cylinder();

  uvSphere.height = paramHeight;
  uvSphere.width = paramWidth;
  uvSphere.depth = paramDepth;

  FluidFormsLibs.draw.uv(uvSphere);
}




