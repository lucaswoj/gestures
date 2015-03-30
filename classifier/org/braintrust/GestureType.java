package org.braintrust;

public enum GestureType {

  CIRCLE("circle"),
  TRIANGLE("triangle"),
  CHECK("check"),
  ARROW("arrow"),
  PIGTAIL("pigtail"),
  STAR("star");
  
  public final String name;
  
  public static int size() {
    return GestureType.values().length;
  }

  private GestureType(String name) {
    this.name = name;
  }

  public static GestureType random() {
    return Utilities.random(GestureType.values());      
  }
  
  public static GestureType fromOutput(double[] output) {
    assert(output.length == GestureType.values().length);    
    return GestureType.values()[Utilities.maxIndex(output)];
  }
}
