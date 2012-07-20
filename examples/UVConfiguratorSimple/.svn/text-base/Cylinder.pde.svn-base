import eu.fluidforms.geom.*;
import processing.core.PApplet;

class Cylinder implements FGeometryUV {
  public float height = 100;
  public float width = 100;
  public float depth = 100;
  
  public float getX(float u, float v){
    return cos(u) * width;
  }
  public float getY(float u, float v){
    return sin(u) * height;
  }
  public float getZ(float u, float v){
    return v * depth;
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
    return 0;
  }
  public float getVMax(){
    return PI;
  }

  public float getHeight(){
    return 1;
  }
  public float getScale(){
    return 1;
  }
}





