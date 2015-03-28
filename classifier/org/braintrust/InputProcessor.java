package org.braintrust;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class InputProcessor {

    private static JSONParser parser = new JSONParser();
    
    public static void main(String[] args) {
        getGestureData(GestureStore.Gesture.CIRCLE);     
    }

    public static void getGestureData(GestureStore.Gesture gesture) {

        File folder = new File(new File("").getAbsolutePath() + "/training/" + getGestureName(gesture));
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            String filename = listOfFiles[i].getName();

            if (!filename.equals(".DS_Store")) {
                Object object = null; 
                String filePath = folder + "/" + filename;
                try {
                    object = parser.parse(new FileReader(filePath));
                } catch(Exception e) {
                    System.out.println(e);
                    System.exit(1);
                }

                JSONObject jsonData = (JSONObject)object;

                // JSONArray orientationArray = (JSONArray)jsonData.get("orientation");
                // JSONArray gyroArray = (JSONArray)jsonData.get("gyroscope");
                JSONArray accelerometerArray = (JSONArray)jsonData.get("acc");
                // JSONArray rotationArray = (JSONArray)jsonData.get("rotation");

                // System.out.println("Stats on " + filename);
                // System.out.println("Orientation data length: " + orientationArray.size());
                // System.out.println("Gyro data length: " + gyroArray.size());
                // System.out.println("Accelerometer data length: " + accelerometerArray.size());
                // System.out.println("Rotation data length: " + rotationArray.size());

                boolean removeFromFront = true;
                while (accelerometerArray.size() > 450) {
                    if (removeFromFront) {
                        accelerometerArray.remove(0);
                    } else {
                        accelerometerArray.remove(accelerometerArray.size()-1);
                    }
                    removeFromFront = !removeFromFront;
                }
                System.out.println("Accelerometer data length: " + accelerometerArray.size());
                System.out.println("");



            }
        }
    }

    public static String getGestureName(GestureStore.Gesture gesture) {
    	switch(gesture) {
    		case CIRCLE: 
    			return "circle";
        		case TRIANGLE:
    			return "triangle";
    		default:
    			return "";
    	}
    }
}