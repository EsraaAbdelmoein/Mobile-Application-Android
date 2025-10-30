package com.example.assg8intents;

import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements ActivityResultCaller {

    private static final String TAG = "Assg8";

    private ImageView imgBackground;
    private DrawingView drawingView;

    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia; // Photo Picker (API 33+)
    private ActivityResultLauncher<String[]> openDocument;            // Fallback (API <= 32)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate started");
        setContentView(R.layout.activity_main);

        imgBackground = findViewById(R.id.imgBackground);
        drawingView = findViewById(R.id.drawingView);

        Button btnImport = findViewById(R.id.btnImport);
        Button btnClear  = findViewById(R.id.btnClear);

        pickMedia = registerForActivityResult(
                new ActivityResultContracts.PickVisualMedia(),
                uri -> {
                    if (uri != null) {
                        Log.d(TAG, "PhotoPicker URI: " + uri);
                        showImage(uri);
                    } else {
                        Log.d(TAG, "No media selected");
                    }
                }
        );

        openDocument = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        Log.d(TAG, "OpenDocument URI: " + uri);
                        getContentResolver().takePersistableUriPermission(
                                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );
                        showImage(uri);
                    } else {
                        Log.d(TAG, "No document selected");
                    }
                }
        );

        btnImport.setOnClickListener(v -> launchPicker());
        btnClear.setOnClickListener(v -> drawingView.clearAll());

        Log.i(TAG, "UI wired. Ready.");
    }

    private void launchPicker() {
        Log.i(TAG, "Launching image picker");
        if (Build.VERSION.SDK_INT >= 33) {
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        } else {
            openDocument.launch(new String[]{"image/*"});
        }
    }

    private void showImage(Uri uri) {
        try {
            imgBackground.setImageURI(uri);
            Log.i(TAG, "Image set as background. You can draw on top now.");
        } catch (Exception e) {
            Log.e(TAG, "Error showing image", e);
        }
    }
}
