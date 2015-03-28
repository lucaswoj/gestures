import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class InputPreparer {
	
	public static void main(String[] args) {

		String gestureName = "circle";
		if (args.length > 0) {
			gestureName = args[0];
		}

		JSONParser parser = new JSONParser();

		File folder = new File(new File("").getAbsolutePath() + "/training/" + gestureName);
		File[] listOfFiles = folder.listFiles();

	    for (int i = 0; i < listOfFiles.length; i++) {
	    	String filename = listOfFiles[i].getName();

	    	if (filename != ".DS_Store") {
	    		Object object = null; 
	    		String filePath = folder + "/0328100956.txt";
	    		try {
	    			object = parser.parse(new FileReader(filePath));
	    		} catch(Exception e) {
	    			System.out.println(e);
	    			return;
	    		}

	    		JSONObject jsonData = (JSONObject)object;

	    		JSONArray orientationArray = (JSONArray)jsonData.get("orientation");
	    		JSONArray gyroArray = (JSONArray)jsonData.get("gyroscope");
	    		JSONArray accelerometerArray = (JSONArray)jsonData.get("acc");
	    		JSONArray rotationArray = (JSONArray)jsonData.get("rotation");

	    		System.out.println("Stats on " + filename);
	    		System.out.println("Orientation data length: " + orientationArray.size());
	    		System.out.println("Gyro data length: " + gyroArray.size());
	    		System.out.println("Accelerometer data length: " + accelerometerArray.size());
	    		System.out.println("Rotation data length: " + rotationArray.size());
	    		System.out.println("");
	    	}
	    }

		
		
	}
}