import eu.fluidforms.utils.*;
import eu.fluidforms.processing.*;
import eu.fluidforms.geom.*;
import eu.fluidforms.io.*;
import eu.fluidforms.parser.*;

void setup(){
  Log.debug("debug");
  println("----------------------------");
  Log.setLogLevel(Log.INFO);
  Log.debug("debug");
  Log.info("info");
  println("----------------------------");
  Log.setLogLevel(Log.WARN);
  Log.debug("debug");
  Log.info("info");
  Log.warn("warn");
  println("----------------------------");
  Log.setLogLevel(Log.ERROR);
  Log.debug("debug");
  Log.info("info");
  Log.warn("warn");
  Log.error("error");
  println("----------------------------");
  Log.setLogLevel(Log.FATAL);
  Log.debug("debug");
  Log.info("info");
  Log.warn("warn");
  Log.error("error");
  Log.fatal("fatal");
}
