package org.braintrust;

import java.io.FileReader;
import java.io.File;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class InputProcessor {

    private static JSONParser parser = new JSONParser();
    
    public double[] getRandomGestureData(GestureStore.Gesture gesture) {
        String currentDirectory = new File("").getAbsolutePath();
        File folder = new File(currentDirectory + "/data/training/" + getGestureName(gesture));
        
        File[] listOfFiles = getDataFiles(folder.listFiles());
        
        int randomFileIndex = (int)Math.round(Math.random() * (listOfFiles.length-1));
        File file = listOfFiles[randomFileIndex];
        String filename = file.getName();
        System.out.println("File picked: " + file.getName());
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
            // JSONArray rotationArray = (JSONArray)jsonData.get("rotation");

            JSONArray accelerometerArray = (JSONArray)jsonData.get("acc");
            trimDataArray(450, accelerometerArray);

            System.out.println("Accelerometer data length: " + accelerometerArray.size());
            System.out.println("Accelerometer data extracted: " + extractValues(accelerometerArray).length);

            System.out.println("");

            return extractValues(accelerometerArray);
        }
        return null;
    }
    
    private void trimDataArray(int trimTo, JSONArray dataArray) {
        boolean removeFromFront = true;
        while (dataArray.size() > trimTo) {
            if (removeFromFront) {
                dataArray.remove(0);
            } else {
                dataArray.remove(dataArray.size() - 1);
            }
            removeFromFront = !removeFromFront;
        }
    } 
    
    private File[] getDataFiles(File[] files) {
        File[] dataFiles = new File[files.length-1];
        int index = 0;
        
        for (File f : files) {
            if (!f.getName().equals(".DS_Store")) {
                dataFiles[index] = f;
                index++;
            }
        }
        return dataFiles;
    }
    
    private double[] extractValues(JSONArray dataArray) {
        double[] values = new double[dataArray.size()*3];
        
        for (int i = 0; i < dataArray.size(); i++) {
            values[i*3] = (double)((JSONObject)(dataArray.get(i))).get("x");
            values[i*3 + 1] = (double)((JSONObject)(dataArray.get(i))).get("y");
            values[i*3 + 2] = (double)((JSONObject)(dataArray.get(i))).get("z");
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