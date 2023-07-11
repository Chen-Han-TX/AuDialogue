package com.chancechenhan.audialogue;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileHelper {

    private static final String FILE_NAME = "mydata.json";

    public static JSONObject readJsonFile(Context context) {
        String jsonString = "";
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[100];
            int charRead;
            while ((charRead = isr.read(inputBuffer)) > 0) {
                String readString = String.copyValueOf(inputBuffer, 0, charRead);
                jsonString += readString;
            }
            isr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new JSONObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addMessage(Context context, String key, JSONObject newData) {
        try {
            // Read the existing file
            JSONObject existingData = readJsonFile(context);

            // If the file doesn't exist or is empty, initialize it
            if (existingData == null) {
                existingData = new JSONObject();
            }

            JSONArray dataForGivenKey;
            // Check if data already exists for the given key
            if (existingData.has(key)) {
                // If yes, get the existing JSONArray
                dataForGivenKey = existingData.getJSONArray(key);
            } else {
                // If not, create a new JSONArray
                dataForGivenKey = new JSONArray();
            }

            // Add the new data to the JSONArray
            dataForGivenKey.put(newData);

            // Put the JSONArray back into the JSONObject
            existingData.put(key, dataForGivenKey);

            // Write the modified JSON back to the file
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(existingData.toString());
            osw.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    // Write the entire JSON file - replacing the old one
    public static void writeJsonFile(Context context, JSONObject jsonObject) {
        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(jsonObject.toString());
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteSpecificData(Context context, String keyToDelete) {
        // Read the file into a JSONObject
        JSONObject jsonFile = FileHelper.readJsonFile(context);

        // Remove the specific data
        jsonFile.remove(keyToDelete);

        // Write the updated JSONObject back to the file
        FileHelper.writeJsonFile(context, jsonFile);
    }

}




