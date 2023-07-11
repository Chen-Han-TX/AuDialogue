package com.chancechenhan.audialogue;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MessageListDialogFragment extends DialogFragment {
    ArrayList<JSONObject> messages;

    public MessageListDialogFragment() {
        // Required empty public constructor
    }

    public static MessageListDialogFragment newInstance(ArrayList<JSONObject> messages) {
        MessageListDialogFragment fragment = new MessageListDialogFragment();
        fragment.messages = messages;
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_list_dialog, container, false);

        LinearLayout messagesLayout = view.findViewById(R.id.messagesLayout);

        for (JSONObject message : messages) {
            try {
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

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = (int) (metrics.widthPixels * 0.85);
            int height = (int) (metrics.heightPixels * 0.5);  //50% of screen height
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setGravity(Gravity.CENTER);
        }
    }
}
