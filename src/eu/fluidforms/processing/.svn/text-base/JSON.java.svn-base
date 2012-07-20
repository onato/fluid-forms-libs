package eu.fluidforms.processing;

import java.lang.reflect.Field;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import processing.core.PApplet;

public class JSON {
	/**
	 * Parses the JSON input and returns the JSONObject object. If the input starts with http://
	 * then the text at that URL will be used as the JSON input.
	 * @param input The JSON input, either a JSON string or a URL containing JSON data.
	 * @param p5 Your sketch, or in other words "this".
	 * @return The JSONObject representing the input.
	 */
	public static JSONObject get(String input, PApplet p5) {
		if (input.indexOf("http://") == 0) {
			if(p5==null){
				System.err.println("No handle on PApplet. Use  get(\"jsonInput\", this) instead of call FluidForms.setup(this) beforhand.");
			}
			input = PApplet.join(p5.loadStrings(input), "");
		}
		try {
			return new JSONObject(input);
		} catch (JSONException e) {
			System.err.println("There was an error parsing the JSONObject.");
			e.printStackTrace();
		};
		return null;
	}
	/**
	 * This is a short cut for the get(String input, PApplet p5) function that only works if FluidForms 
	 * has already been set up.
	 * @param input  The JSON input, either a JSON string of a URL containing JSON data.
	 * @return The JSONObject representing the input.
	 */
	public static JSONObject get(String input) {
		return get(input, FF.p5);
	}

	public static JSONObject get(JSONObject jsonObj, String key) {
		try {
			return new JSONObject(jsonObj.getString(key));
		} catch (JSONException e) {
			System.err.println("There was an error parsing the JSONObject.");
			e.printStackTrace();
		};
		return null;
	}

	public static JSONArray getArray(JSONObject jsonObj, String key) {
		try {
			return new JSONArray(jsonObj.getString(key));
		} catch (JSONException e) {
			System.err.println("There was an error parsing the JSONObject.");
			e.printStackTrace();
		};
		return null;
	}
	public static float getFloat(JSONObject jsonObj, String key) {
		try {
			return (float)jsonObj.getDouble(key);
		} catch (JSONException e) {
			System.err.println("There was an error parsing the JSONObject.");
			e.printStackTrace();
		};
		return 0;
	}

	public static int getInt(JSONObject jsonObj, String key) {
		try {
			return (int)jsonObj.getDouble(key);
		} catch (JSONException e) {
			System.err.println("There was an error parsing the JSONObject.");
			e.printStackTrace();
		};
		return 0;
	}

	public static boolean getBoolean(JSONObject jsonObj, String key) {
		try {
			return jsonObj.getBoolean(key);
		} catch (JSONException e) {
			System.err.println("There was an error parsing the JSONObject.");
			e.printStackTrace();
		};
		return false;
	}

	public static String getString(JSONObject jsonObj, String key) {
		try {
			return jsonObj.getString(key);
		} catch (JSONException e) {
			System.err.println("There was an error parsing the JSONObject.");
			e.printStackTrace();
		};
		return null;
	}

	public static JSONObject get(JSONArray jsonArray, int i) {
		try {
			return (JSONObject)jsonArray.get(i);
		} catch (JSONException e) {
			System.err.println("There was an error parsing the JSONObject.");
			e.printStackTrace();
		};
		return null;
	}
	
	public static JSONArray getArray(JSONArray jsonArray, int i) {
		try {
			return (JSONArray)jsonArray.get(i);
		} catch (JSONException e) {
			System.err.println("There was an error parsing the JSONObject.");
			e.printStackTrace();
		};
		return null;
	}
	public void destroy(){
	}

	/**
	 * Converts a bean containing String and numeric attributes to JSON. 
	 * It is filters the attribute "resolution". 
	 * @param clazz The instance that should be converted to JSON.
	 * @return The JSON String
	 */
	public static String toJSON(Object clazz){
		Field[] flds = clazz.getClass().getFields();
	    String jsonString = "{";
	    Field f;
	    for(int i=0; i<flds.length; i++){
	      f = flds[i];
	      if(f.getName().toLowerCase().indexOf("resolution")<0){
	        try{
	          if(f.getType().getName().equals("java.lang.String")){
	            jsonString += "\"" + f.getName() + "\":\"" + f.get(clazz) + "\"";
	          }else{
	            jsonString += "\"" + f.getName() + "\":" + f.getFloat(clazz);
	          }
	         jsonString += ",";
	        } catch (Exception e) {e.printStackTrace();}

	      }
	    }
	    jsonString = jsonString.substring(0, jsonString.length()-1);
	    jsonString += "}";
	    return jsonString;
	}

}
