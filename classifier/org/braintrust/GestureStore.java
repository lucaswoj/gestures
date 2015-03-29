package org.braintrust;
 
public class GestureStore {

  public static enum Gesture {
    CIRCLE(0, "circle"),
    TRIANGLE(1, "triangle"),
    FLICK_OUT(2, "flickOut"),
    FLICK_IN(3, "flickIn"),
    FLICK_FORWARD(4, "flickForward"),
    FLICK_BACKWARD(5, "flickBackward")
    ;
    

    private final int value;
    private final String name;

    private Gesture(int value, String name) {
      this.value = value;
      this.name = name;
    }
    
    public int getValue() {
      return this.value;
    }
    
    public String getName() {
      return this.name;
    }
    
    public static Gesture fromValue(int value) {
      if (value == 0) return Gesture.CIRCLE;
      else            return Gesture.TRIANGLE;
    }
  }
  
  private InputProcessor inputProcessor = new InputProcessor();
 
  public final int GESTURE_CIRCLE = 0;
  public final int GESTURE_SQUARE = 1;
  
  public final int INPUT_NEURONS = 999999999;
  public final int OUTPUT_NEURONS = 2;
  
  // Returns a tuple of (input[INPUT_NEURONS], {GESTURE_CIRCLE, GESTURE_CIRCLE})
  public Tuple<double[], Integer> getRandomTrainingData(Gesture gesture) {
    double[] inputs = inputProcessor.getRandomTrainingData(gesture);
    return new Tuple(inputs, Gesture.CIRCLE.value);
  }
  
  public Tuple<double[], Integer> getRandomTestData(Gesture gesture) {
    double[] inputs = inputProcessor.getRandomTestData(gesture);
    return new Tuple(inputs, Gesture.CIRCLE.value);
  }
  
}