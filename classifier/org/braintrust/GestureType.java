package org.braintrust;

public enum GestureType {

  CIRCLE("circle"),
  TRIANGLE("triangle"),
  FLICK_OUT("flickOut"),
  FLICK_IN("flickIn"),
  FLICK_FORWARD("flickForward"),
  FLICK_BACKWARD("flickBackward"),
  CHECK("check"),
  ARROW("arrow"),
  PIGTAIL("pigtail"),
  STAR("star");
  
  public static int LENGTH = 10;

  public final String name;

  private GestureType(String name) {
    this.name = name;
  }

  public static GestureType random() {
    assert(LENGTH == GestureType.values().length);
    return Utilities.random(GestureType.values());      
  }
  
  public static GestureType fromOutput(double[] output) {
    assert(output.length == GestureType.values().length);    
    return GestureType.values()[Utilities.maxIndex(output)];
  }
}
