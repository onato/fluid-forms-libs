import eu.fluidforms.geom.*;
import processing.core.PApplet;

class Blob implements FGeometryUV {
  public float getScale(){
    return 150;
  }

  public float getResolutionU(){
    return 40;
  }
  public float getResolutionV(){
    return 40;
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





