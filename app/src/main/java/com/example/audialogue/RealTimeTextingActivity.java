package com.example.audialogue;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RealTimeTextingActivity extends AppCompatActivity {

    private TextView centerText;
    private long TIMESTAMP = System.currentTimeMillis() / 1000;

    private boolean isFlipped = false;
    private EditText editText;
    private ImageButton backButton;
    private ImageButton historyButton;

    /*
    The climax of this course is its final project. The final project is your opportunity to take your newfound savvy with programming out for a spin and develop your very own piece of software. So long as your project draws upon this course’s lessons, the nature of your project is entirely up to you.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_texting);

        // Enable full-screen mode
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = new Intent(RealTimeTextingActivity.this, MainActivity.class);
                if (launchIntent != null) {
                    // null pointer check in case package name was not found
                    startActivity(launchIntent);
                    finish();
                }
            }
        });

        historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = new Intent(RealTimeTextingActivity.this, ConvoHistoryActivity.class);
                if (launchIntent != null)
                {
                    // null pointer check in case package name was not found
                    startActivity(launchIntent);
                    finish();
                }
            }
        });


        // Toggle Center Text to flip vertically
        centerText = findViewById(R.id.centerText);
        centerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFlip();
            }
        });

        // editText at the bottom
        editText = findViewById(R.id.editText);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Update centerText as the text in editText changes
                centerText.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This space intentionally left blank
            }
        });

        // Handle the "Send" button on the keyboard
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    // Save the text in history
                    String text = String.valueOf(centerText.getText());

                    editText.setText("");
                    centerText.setText(text);

                    // Hide keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    }
                    toggleFlip();

                    // Add new data to JSON
                    JSONObject newData = new JSONObject();
                    try {
                        if (isFlipped) {
                            newData.put("Reversed", text);
                        } else {
                            newData.put("Portrait", text);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    FileHelper.addMessage(RealTimeTextingActivity.this, String.valueOf(TIMESTAMP), newData);

                    handled = true;
                }
                return handled;
            }
        });
    }

    private void toggleFlip() {
        if (isFlipped) {
            // Restore to normal
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            // Rotate 180 degrees
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        }
        isFlipped = !isFlipped;
    }
}

