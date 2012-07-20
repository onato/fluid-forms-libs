def script = '''
import processing.core.*


class GPApplet{
  def p5 = {}





  def methodMissing(String method, Object params) {
  println "invoking method :" + method
    if(method=="setup"){
        //p5.setup()
        return true
    }
    if(method=="draw"){
        //p5.draw()
        return true
    }
        //println "You must implement setup() and draw()"
        //return
        //}
    def floatParams = []
    params.each{ 
        if(it.class == Double || it.class == java.math.BigDecimal){
            floatParams.add(new Float(it))
        }else{
            floatParams.add(it)
        }
    }
    try{
    if(floatParams.size()){
        p5.invokeMethod(method, floatParams);
    }else{
        p5.invokeMethod(method, params);
    }
    }catch(e){
        if(e.class.name != "processing.core.PApplet\$RendererChangeException"){
            e.printStackTrace()
        }
    }
  }
  

  def getProperty(String property){
    if(property != "p5"){
        def cls = p5.getClass();
        def field = cls.getField(property);
        def retVal = field.get(p5);
        return retVal
    }
  }

  def draw(){
      println "draw2"
  }
}

'''

GroovyClassLoader loader = new GroovyClassLoader();
Class groovyClass = loader.parseClass(script);
newGroovyObject = (GroovyObject) groovyClass.newInstance();
newGroovyObject.invokeMethod("dra2w", null);

            