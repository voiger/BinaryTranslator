package com.uialert.binarytranslator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowMetrics;

import com.uialert.binarytranslator.client.ActivityClient;
import com.uialert.binarytranslator.server.ActivityServer;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CAMERA = 22222;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        SizeDisplay.x = display.getWidth();
        SizeDisplay.y = display.getHeight();

    }

    public void startServer(View view) {
        startActivity(new Intent(this, ActivityServer.class));
    }

    public void startClient(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        } else {
            startActivity(new Intent(this, ActivityClient.class));
        }

    }
}