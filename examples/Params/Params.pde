// Displays a popup to modify the parameters

import eu.fluidforms.utils.*;
import eu.fluidforms.processing.*;
import eu.fluidforms.geom.*;

public float paramWidth = 50; // notice that the public is required!
public int[] paramWidthRange = {-200, 100};
public int paramHeight = 10;
public int paramDepth = 50;
public float paramHorizontalRotation;
public boolean paramDarkBackground = false;

void setup() {
  size(500, 500, P3D);
  FluidFormsLibs.setup(this);
}

void draw() {
  background(paramDarkBackground?0:255);
  rotateX(paramHorizontalRotation/10);
  box(paramWidth, paramHeight, paramDepth);
}

