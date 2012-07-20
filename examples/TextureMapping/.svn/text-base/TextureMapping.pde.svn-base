import eu.fluidforms.utils.*;
import eu.fluidforms.processing.*;
import eu.fluidforms.geom.*;
import eu.fluidforms.io.*;
import eu.fluidforms.parser.*;
import eu.fluidforms.primitives.*;

Vector triangles = new Vector();
PImage texture;

void setup(){
  size(1000, 1000, P3D);
  FluidFormsLibs.setup(this);

  String rows[] = loadStrings("elevations.txt");

  Vector verticies = new Vector();
  for (int y=0; y < rows.length; y++) {
    String[] cols = rows[y].split(",");
    for (int x=0; x < cols.length; x++) {
      verticies.add(new FVertex((x-20)*100, (y-20)*100, Integer.valueOf(cols[x])));
    }
  }
  triangles = Triangulate.triangulate(verticies);
  noStroke();
  texture = loadImage("stripe.png");

  FluidForms.navigation.zoom(-5000);
}

void draw(){
  rotateX(1);
  FTexture fTexture = new FTexture(texture);
  fTexture.rotateX((float)frameCount/100);
  fTexture.rotateZ((float)frameCount/100);
  fTexture.scale(10);
  Sphere sphere = new Sphere(3);
  sphere.scale(1000);
  FluidFormsLibs.draw.triangles(sphere.getTriangles(), fTexture);
}

