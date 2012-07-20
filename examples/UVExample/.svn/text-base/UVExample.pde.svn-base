import eu.fluidforms.utils.*;
import eu.fluidforms.processing.*;
import eu.fluidforms.geom.*;
import eu.fluidforms.io.*;
import eu.fluidforms.parser.*;




void setup(){
  size(500, 800, P3D);
  background(255);
  String format = "stl";  
  FGraphics gExport = (FGraphics)beginRecord(FluidFormsLibs.RECORDER, "UVExample." + format);  
  gExport.setApplyTransformMatrix(false);
  //gExport.setAscii(true);
  smooth();
  lights();
  fill(200);
  noStroke();
  translate(width/2, height/2);
  rotateX(PI/2);
  rotateZ(PI/2);
  int scale = 170;
  int resolution = 50;
  float inc = (TWO_PI)/resolution;
  for(float v=-PI/2; v<PI/2; v+=inc){ // creates a half circle
    beginShape(TRIANGLE_STRIP);
    for(float u=0; u<=TWO_PI+inc; u+=inc){  // rotates the half circle
      float x1 = getX(u, v);
      float y1 = getY(u, v);
      float z1 = getZ(u, v);

      float x2 = getX(u, v+inc);
      float y2 = getY(u, v+inc);
      float z2 = getZ(u, v+inc);

          vertex(x1*scale,y1*scale,z1*scale);
          vertex(x2*scale,y2*scale,z2*scale);
    }
    endShape();
  }

  endRecord();
}


float getX(float u, float v){
  return cos(u) * cos(v);
}
float getY(float u, float v){
  return sin(u) * cos(v);
}
float getZ(float u, float v){
  return sin(v) + cos(u);
}

