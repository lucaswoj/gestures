package org.braintrust;

public enum GestureType {
  CIRCLE(0, "circle"),
  TRIANGLE(1, "triangle"),
  FLICK_OUT(2, "flickOut"),
  FLICK_IN(3, "flickIn"),
  FLICK_FORWARD(4, "flickForward"),
  FLICK_BACKWARD(5, "flickBackward");

  public final int value;
  public final String name;

  private GestureType(int value, String name) {
    this.value = value;
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
