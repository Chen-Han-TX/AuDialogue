package com.example.audialogue;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

        // Back button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(ConvoHistoryActivity.this, RealTimeTextingActivity.class);
                if (newIntent != null){
                    startActivity(newIntent);
                    finish();
                }
            }
        });


        LinearLayout conversationLayout = findViewById(R.id.conversationLayout);

        Map<String, JSONArray> sortedData = loadAndSortData();

        LayoutInflater inflater = LayoutInflater.from(this);

        // If no entry yet
        if (sortedData.isEmpty())
        {
            //Add textview shows: No conversation history yet
            TextView noDataTextView = new TextView(this);
            noDataTextView.setText("No conversation history yet");
            noDataTextView.setTextSize(24);  // or another size
            noDataTextView.setTextColor(Color.BLACK);  // or another color
            noDataTextView.setGravity(Gravity.CENTER);  // centered text

            Typeface typeface = ResourcesCompat.getFont(this, R.font.dekko);
            noDataTextView.setTypeface(typeface);

            // add the TextView to your conversationLayout
            conversationLayout.addView(noDataTextView);
        }

        for (Map.Entry<String, JSONArray> entry : sortedData.entrySet()) {
            String timestamp = entry.getKey();
            JSONArray messages = entry.getValue();

            // Inflate CardView from XML layout
            View cardViewLayout = inflater.inflate(R.layout.card_view_layout, null, false);

            CardView cardView = cardViewLayout.findViewById(R.id.cardView);
            TextView titleTextView = cardViewLayout.findViewById(R.id.titleTextView);
            LinearLayout messagesLayout = cardViewLayout.findViewById(R.id.messagesLayout);

            titleTextView.setText(convertTimestampToDate(timestamp));
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

            // Create TextViews for messages
            for (int i = 0; i < messages.length(); i++) {
                try {
                    JSONObject message = (JSONObject) messages.get(i);
                    // Inflate the custom message layout
                    View messageLayout = inflater.inflate(R.layout.message_layout, null, false);
                    TextView messageTextView = messageLayout.findViewById(R.id.messageTextView);

                    if (message.has("Portrait")) {
                        messageTextView.setText(message.getString("Portrait"));
                        messageTextView.setBackgroundColor(Color.GRAY);
                    } else if (message.has("Reversed")) {
                        messageTextView.setText(message.getString("Reversed"));
                        messageTextView.setBackgroundColor(Color.MAGENTA);
                    }
                    // Add the custom message layout to the messagesLayout
                    messagesLayout.addView(messageLayout);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // Inflate the delete button from XML
            Button deleteButton = (Button) inflater.inflate(R.layout.delete_button, null);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Create an AlertDialog
                    new AlertDialog.Builder(ConvoHistoryActivity.this)
                            .setTitle("Delete Entry")
                            .setMessage("Are you sure you want to delete this entry?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FileHelper.deleteSpecificData(ConvoHistoryActivity.this, timestamp);
                                    recreate();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });


            // Add the delete button to the messagesLayout
            messagesLayout.addView(deleteButton);

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