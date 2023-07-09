package com.example.audialogue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

public class ConvoHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convo_history);
        LinearLayout conversationLayout = findViewById(R.id.conversationLayout);

        Map<String, JSONArray> sortedData = loadAndSortData();

        for (Map.Entry<String, JSONArray> entry : sortedData.entrySet()) {
            String timestamp = entry.getKey();
            JSONArray messages = entry.getValue();

            // Create CardView
            CardView cardView = new CardView(this);
            // Set up CardView properties here...

            // Create LinearLayout for CardView content
            LinearLayout cardContentLayout = new LinearLayout(this);
            cardContentLayout.setOrientation(LinearLayout.VERTICAL);

            // Create title TextView
            TextView titleTextView = new TextView(this);
            titleTextView.setText(convertTimestampToDate(timestamp));
            titleTextView.setTextSize(24); // Set the text size larger

            // Create LinearLayout for messages
            final LinearLayout messagesLayout = new LinearLayout(this);
            messagesLayout.setOrientation(LinearLayout.VERTICAL); // Align the messages vertically
            messagesLayout.setVisibility(View.GONE);

            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Toggle visibility of messagesLayout
                    if (messagesLayout.getVisibility() == View.GONE) {
                        messagesLayout.setVisibility(View.VISIBLE);
                    } else {
                        messagesLayout.setVisibility(View.GONE);
                    }
                }
            });

            // Add titleTextView to cardContentLayout
            cardContentLayout.addView(titleTextView);

            // Create TextViews for messages
            for (int i = 0; i < messages.length(); i++) {
                try {
                    JSONObject message = (JSONObject) messages.get(i);
                    if (message.has("Portrait")) {
                        TextView messageTextView = new TextView(this);
                        messageTextView.setText(message.getString("Portrait"));
                        messageTextView.setBackgroundColor(Color.BLUE);
                        messagesLayout.addView(messageTextView);
                    } else if (message.has("Reversed")) {
                        TextView messageTextView = new TextView(this);
                        messageTextView.setText(message.getString("Reversed"));
                        messageTextView.setBackgroundColor(Color.GREEN);
                        messagesLayout.addView(messageTextView);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // Add messagesLayout to cardContentLayout
            cardContentLayout.addView(messagesLayout);

            // Add cardContentLayout to CardView
            cardView.addView(cardContentLayout);

            // Add CardView to conversationLayout
            conversationLayout.addView(cardView);
        }
    }


    private String convertTimestampToDate(String timestamp) {
        long unixSeconds = Long.parseLong(timestamp);
        Date date = new Date(unixSeconds*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }


    private Map<String, JSONArray> loadAndSortData() {
        // Read the JSON file
        JSONObject jsonFile = FileHelper.readJsonFile(this);
        if (jsonFile == null) {
            return new TreeMap<>();
        }

        // Put the data into a sorted map
        Map<String, JSONArray> sortedData = new TreeMap<>(Collections.reverseOrder());
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