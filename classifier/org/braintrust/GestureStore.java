package org.braintrust;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GestureStore {

  public static enum DataType {
    TRAINING("training"),
    TESTING("test");

    private final String name;

    private DataType(String name) {
      this.name = name;
    }
  }
  
  public static final GestureStore instance = new GestureStore();
  private final JSONParser parser = new JSONParser();
 
  public final Map<DataType, ArrayList<Gesture>> gestures = new EnumMap<>(DataType.class);  

  public static final int TRIM_LENGTH = 450;
  
  public static final int INPUT_NEURONS = 3 * TRIM_LENGTH;
  public static final int OUTPUT_NEURONS = 6;
  
  private double[] readGestureFile(File file) {
    double[] input = new double[INPUT_NEURONS];
    
    JSONObject json = null;
    try {
      json = (JSONObject) parser.parse(new FileReader(file));
    } catch (IOException | ParseException ex) {
      Logger.getLogger(GestureStore.class.getName()).log(Level.SEVERE, null, ex);
      System.exit(1);
    }

    JSONArray jsonAcceleration = (JSONArray) json.get("acc");
    // JSONArray dataOrientation = (JSONArray) gestures.get("orientation");
    // JSONArray dataGyro = (JSONArray) gestures.get("gyroscope");
    // JSONArray dataRotation = (JSONArray) gestures.get("rotation");

    int start = (jsonAcceleration.size() - TRIM_LENGTH) / 2;
    for (int i = 0; i < TRIM_LENGTH; i++) {
      input[i * 3] =     (double) ((JSONObject) jsonAcceleration.get(i + start)).get("x");
      input[i * 3 + 1] = (double) ((JSONObject) jsonAcceleration.get(i + start)).get("y");
      input[i * 3 + 2] = (double) ((JSONObject) jsonAcceleration.get(i + start)).get("z");
    }
    
    return input;
  }
    
  private GestureStore() {
    assert(OUTPUT_NEURONS == GestureType.values().length);
    
    for (DataType dataType : DataType.values()) {
      this.gestures.put(dataType, new ArrayList<>());
      
      for (GestureType gestureType : GestureType.values()) {
        File directory = Paths.get(new File("").getAbsolutePath(), "data", dataType.name, gestureType.name).toFile();
        File[] files = directory.listFiles((File dir, String name) -> !name.equals(".DS_STORE"));
        
        for (File file : files) {
          this.gestures.get(dataType).add(new Gesture(readGestureFile(file), gestureType));
        }
      }
    }
  }

  public Gesture getRandomTrainingGesture() {
    return Utilities.random(gestures.get(DataType.TRAINING));
  }

  public Gesture getRandomTestingGesture() {
    return Utilities.random(gestures.get(DataType.TESTING));
  }
  
  public ArrayList<Gesture> getAllTestingGestures() {
    return gestures.get(DataType.TESTING);
  }
  
  public ArrayList<Gesture> getAllTrainingGestures() {
    return gestures.get(DataType.TRAINING);
  }

}