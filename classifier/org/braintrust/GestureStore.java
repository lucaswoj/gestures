package org.braintrust;
 
public class GestureStore {

  public static enum Gesture{
    CIRCLE(0),
    TRIANGLE(1)
    ;

    private final int value;

    private Gesture(int value) {
      this.value = value;
    }
  }
  
  private InputProcessor inputProcessor = new InputProcessor();
 
  public final int GESTURE_CIRCLE = 0;
  public final int GESTURE_SQUARE = 1;
  
  public final int INPUT_NEURONS = 999999999;
  public final int OUTPUT_NEURONS = 2;
  
  // Returns a tuple of (input[INPUT_NEURONS], {GESTURE_CIRCLE, GESTURE_CIRCLE})
  public Tuple<double[], Integer> getRandom(Gesture gesture) {
    double[] inputs = inputProcessor.getRandomGestureData(gesture);
    return new Tuple(inputs, Gesture.CIRCLE.value);
  }
  
}