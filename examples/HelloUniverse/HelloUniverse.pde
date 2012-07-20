import eu.fluidforms.utils.*;
import eu.fluidforms.processing.*;
import eu.fluidforms.geom.*;
import eu.fluidforms.io.*;
import eu.fluidforms.parser.*;

void setup(){
  size(500, 500, P3D);
  String format = "stl";
  beginRecord(FluidFormsLibs.RECORDER, "HelloUniverse." + format);  
  int i = 100;
  translate(width/2, height/2, 400);
  while(i-->0){
    pushMatrix();
    translate(random(20)-10, random(20)-10, random(20)-10);
    box(random(10), random(10), random(10));
    popMatrix();
  }
  endRecord();
}
