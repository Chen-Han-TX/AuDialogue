package com.example.audialogue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
    private Button[] buttons = new Button[5];
    private Map<String, JSONArray> data;

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

        // Load the data
        data = loadAndSortData();

        // Assign the buttons
        buttons[0] = findViewById(R.id.button1);
        buttons[1] = findViewById(R.id.button2);
        buttons[2] = findViewById(R.id.button3);
        buttons[3] = findViewById(R.id.button4);
        buttons[4] = findViewById(R.id.button5);

        // Assign the text and the listeners to the buttons
        assignTextAndListeners();

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

    private void assignTextAndListeners() {
        List<String> keys = new ArrayList<>(data.keySet());

        for (int i = 0; i < buttons.length; i++) {
            int index = keys.size() - i - 1;
            if (index >= 0) {
                String key = keys.get(index);
                buttons[i].setText(String.format("Record %d (%s)", i + 1, key));
                buttons[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        displayDialogues(key);
                    }
                });
            } else {
                buttons[i].setVisibility(View.GONE);
            }
        }
    }

    private void displayDialogues(String key) {
        // Convert the JSONArray into a string to display
        JSONArray dialogues = data.get(key);
        StringBuilder dialoguesString = new StringBuilder();
        for (int i = 0; i < dialogues.length(); i++) {
            try {
                dialoguesString.append(dialogues.getJSONObject(i).toString()).append("\n");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Display the dialogues in a toast for now
        Toast.makeText(this, dialoguesString.toString(), Toast.LENGTH_LONG).show();
    }
}