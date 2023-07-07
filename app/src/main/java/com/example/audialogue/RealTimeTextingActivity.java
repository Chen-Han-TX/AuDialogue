package com.example.audialogue;
import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RealTimeTextingActivity extends AppCompatActivity {

    private TextView centerText;
    private boolean isFlipped = false;
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
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Toggle Center Text to flip vertically
        centerText = findViewById(R.id.center_text);
        centerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFlip();
            }
        });
    }

    private void toggleFlip() {
        if (isFlipped) {
            // Restore to normal
            centerText.setScaleY(1);
        } else {
            // Flip vertically
            centerText.setScaleY(-1);
        }
        isFlipped = !isFlipped;
    }
}