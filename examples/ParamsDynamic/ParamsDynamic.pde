// Displays a pop-up to modify the parameters.
// Press and hold a button to save the current values.

import eu.fluidforms.utils.*;
import eu.fluidforms.processing.*;
import eu.fluidforms.geom.*;

public boolean paramGreenColor = true;
ParameterChanger params;

void setup() {
  size(500, 500, P3D);
  FluidFormsLibs.setup(this);
  params = FluidFormsLibs.getParameterChanger();
  params.add("Blue Color", true);
  params.add("Red Color", 0f);
}

void draw() {
  background(255);
  fill(100);
  int g = 0;

  if(paramGreenColor){
	  g = 255;
  }
  if(params.getBoolean("Blue Color")){
    background((int)(params.getInt("Red Color")*2.55), g, 255);
  }else{
    background((int)(params.getInt("Red Color")*2.55), g, 0);
  }
}
