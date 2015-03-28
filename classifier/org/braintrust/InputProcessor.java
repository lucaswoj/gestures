package org.braintrust;

import java.io.FileReader;
import java.io.File;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class InputProcessor {

    private static JSONParser parser = new JSONParser();

    public double[] getGestureData(GestureStore.Gesture gesture) {

        String currentDirectory = new File("").getAbsolutePath();
        File folder = new File(currentDirectory + "/data/training/" + getGestureName(gesture));
        File[] listOfFiles = folder.listFiles();

        for (File listOfFile : listOfFiles) {
            String filename = listOfFile.getName();
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
                System.out.println("Accelerometer data X: " + getXValues(accelerometerArray).length);

                System.out.println("");

                return getXValues(accelerometerArray);
            }
        }
        return null;
    }

    public static double[] getXValues(JSONArray dataArray) {
        double[] values = new double[dataArray.size()];

        for (int i = 0; i < dataArray.size(); i++) {
            values[i] = (double)((JSONObject)(dataArray.get(i))).get("x");
        }

        return values;
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