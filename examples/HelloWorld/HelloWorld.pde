import eu.fluidforms.utils.*;
import eu.fluidforms.processing.*;
import eu.fluidforms.geom.*;
import eu.fluidforms.io.*;
import eu.fluidforms.parser.*;

//press ctrl-e to export an STL file.

void setup(){
  size(500, 500, P3D);
  FluidFormsLibs.setup(this);
}

void draw(){
  box(100);  
}
