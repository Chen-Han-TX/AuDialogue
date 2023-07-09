package com.example.audialogue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HistoryActivity extends AppCompatActivity {

    private ImageButton backButton;
    private Button button1;
    private Button clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(HistoryActivity.this, MainActivity.class);
                if (newIntent != null)
                {
                    startActivity(newIntent);
                    finish();
                }
            }
        });

        // Assign the buttons
        button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Read the JSON file
                JSONObject jsonFile = FileHelper.readJsonFile(HistoryActivity.this);
                if (jsonFile != null) {
                    Log.d("JSONFileContent", jsonFile.toString());
                }
            }
        });


        // Clear the internal storage
        clearButton = findViewById(R.id.clear);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    private Map<String, JSONArray> loadAndSortData() {
        // Read the JSON file
        JSONObject jsonFile = FileHelper.readJsonFile(this);
        if (jsonFile == null) {
            return new TreeMap<>();
        }

        // Put the data into a sorted map
        Map<String, JSONArray> sortedData = new TreeMap<>();
        Iterator<String> keys = jsonFile.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                sortedData.put(key, jsonFile.getJSONArray(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Return the sorted data
        return sortedData;
    }


}