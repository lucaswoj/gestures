package org.braintrust;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GestureStore {

  private static JSONParser parser = new JSONParser();

  public static final int SENSORS = 3;
  public static final int SENSOR_LENGTH = 450;

  public static final int INPUT_NEURONS = SENSORS * SENSOR_LENGTH;
  public static final int OUTPUT_NEURONS = GestureType.values().length;

  public static enum GestureType {
    CIRCLE(0, "circle"),
    TRIANGLE(1, "triangle"),
    FLICK_OUT(2, "flickOut"),
    FLICK_IN(3, "flickIn"),
    FLICK_FORWARD(4, "flickForward"),
    FLICK_BACKWARD(5, "flickBackward")
    ;
    

    private final int value;
    private final String name;

    private GestureType(int value, String name) {
      this.value = value;
      this.name = name;
    }

    public static GestureType random() {
      return Utilities.random(GestureType.values());      
    }
  }

  public static enum DataType {
    TRAINING("training"),
    TESTING("test");

    private final String name;

    private DataType(String name) {
      this.name = name;
    }
  }

  public static Tuple<double[], double[]> getRandomTraining() {
    return getRandom(DataType.TRAINING);
  }

  public static Tuple<double[], double[]> getRandomTesting() {
    return getRandom(DataType.TESTING);
  }

  public static Tuple<double[], double[]> getRandom(DataType data) {
    return getRandom(data, GestureType.random());
  }

  public static Tuple<double[], double[]> getRandom(DataType type, GestureType gesture) {
    File directory = Paths.get("data", type.name, gesture.name).toFile();
    File[] files = directory.listFiles((File dir, String name) -> !name.equals(".DS_STORE"));
    File file = Utilities.random(files);
    
    double[] input = new double[INPUT_NEURONS];
    
    try {
      JSONObject data = (JSONObject) parser.parse(new FileReader(file));
      
      JSONArray dataAcceleration = (JSONArray) data.get("acc");
      // JSONArray dataOrientation = (JSONArray) data.get("orientation");
      // JSONArray dataGyro = (JSONArray) data.get("gyroscope");
      // JSONArray dataRotation = (JSONArray) data.get("rotation");
      
      int start = (dataAcceleration.size() - SENSOR_LENGTH) / 2;
      for (int i = 0; i < SENSOR_LENGTH; i++ ) {
        input[i * 3] =     (double) ((JSONObject) dataAcceleration.get(i + start)).get("x");
        input[i * 3 + 1] = (double) ((JSONObject) dataAcceleration.get(i + start)).get("y");
        input[i * 3 + 2] = (double) ((JSONObject) dataAcceleration.get(i + start)).get("z");
      }

    } catch (IOException | ParseException ex) {
      Logger.getLogger(GestureStore.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    double[] output = new double[OUTPUT_NEURONS];
    output[gesture.value] = 1.0;
    
    return new Tuple(input, output);
    
  }
}