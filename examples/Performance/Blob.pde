import eu.fluidforms.geom.*;
import processing.core.PApplet;

class Blob implements FGeometryUV {
  int resolution = 40;
  public float getScale(){
    return 150;
  }

  public float getResolutionU(){
    return resolution;
  }
  public float getResolutionV(){
    return resolution;
  }
  public float getUMin(){
    return 0;
  }
  public float getUMax(){
    return TWO_PI;
  }
  public float getVMin(){
    return -PI/2;
  }
  public float getVMax(){
    return PI/2;
  }

  public float getX(float u, float v){
    return cos(u) * cos(v);
  }
  public float getY(float u, float v){
    return sin(u) * cos(v);
  }
  public float getZ(float u, float v){
    return sin(v) + cos(u);
  }
  public float getHeight(){
    return 1;
  }
}





