package com.example.audialogue;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RealTimeTextingActivity extends AppCompatActivity {

    private TextView centerText;
    private long TIMESTAMP = System.currentTimeMillis() / 1000;

    private boolean isFlipped = false;
    private boolean hasMessage = false;
    private boolean isInputted = false;
    private String previousMessage = "";
    private ArrayList<JSONObject> messages = new ArrayList<>();
    private EditText editText;
    private ImageButton backButton;
    private Button currentChatButton;


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

                // Create an AlertDialog
                if (hasMessage)
                {
                    new AlertDialog.Builder(RealTimeTextingActivity.this)
                            .setTitle("Navigating to Chat History")
                            .setMessage("Are you sure to exit the current conversation? \nYour chat history will be saved automatically.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent launchIntent = new Intent(RealTimeTextingActivity.this, MainActivity.class);
                                    if (launchIntent != null) {
                                        // null pointer check in case package name was not found
                                        startActivity(launchIntent);
                                        finish();
                                    }

                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {

                    Intent launchIntent = new Intent(RealTimeTextingActivity.this, MainActivity.class);
                    if (launchIntent != null) {
                        // null pointer check in case package name was not found
                        startActivity(launchIntent);
                        finish();
                    }
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
                hasMessage = true;
                isInputted = true;
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
                    toggleFlip();
                    handled = true;
                }
                return handled;
            }
        });


        // Current chat history button
        currentChatButton = findViewById(R.id.currentChatHistory);
        currentChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageListDialogFragment.newInstance(messages).show(getSupportFragmentManager(), "messageList");
            }
        });



    }

    private void toggleFlip() {


        String text = String.valueOf(centerText.getText());
        // Made sure it keep track of when the user typed something
        if (hasMessage && isInputted)
        {
            editText.setText("");
            centerText.setText(text);

            // Save the text in history and add new data to JSON
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
            messages.add(newData);
            FileHelper.addMessage(RealTimeTextingActivity.this, String.valueOf(TIMESTAMP), newData);

            // Hide keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        }

        if (isFlipped) {
            // Restore to normal
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            // Rotate 180 degrees
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        }
        isFlipped = !isFlipped;
        isInputted = false;
    }
}

