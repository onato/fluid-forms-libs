import eu.fluidforms.geom.*;
import processing.core.PApplet;

class Sphere implements FGeometryUV {
  public float height = 100;
  public float width = 100;
  public float depth = 100;
  
  public float getX(float u, float v){
    return cos(u) * sin(v) * width;
  }
  public float getY(float u, float v){
    return sin(u) * sin(v) * height;
  }
  public float getZ(float u, float v){
    return cos(v) * depth;
  }

  

  public float getResolutionU(){
    return 140;
  }
  public float getResolutionV(){
    return 140;
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





