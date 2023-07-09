package com.example.audialogue;

import android.content.Context;

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

    public static void writeJsonFile(Context context, String key, JSONObject newData) {
        try {
            // Read the existing file
            JSONObject existingData = readJsonFile(context);

            // If the file doesn't exist or is empty, initialize it
            if (existingData == null) {
                existingData = new JSONObject();
            }

            // Add the new data
            if (!existingData.has(key)) {
                existingData.put(key, newData);
            }

            // Write the modified JSON back to the file
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(existingData.toString());
            osw.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}