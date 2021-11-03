package com.uialert.binarytranslator.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import android.os.Bundle;
import android.view.SurfaceView;

import com.uialert.binarytranslator.R;

public class ActivityClient extends AppCompatActivity {
    SurfaceView surfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        surfaceView = findViewById(R.id.surfaceViewClient);
        LifecycleOwner lifecycleOwner = this;
        EngineSurfaceViewClient.Engine client = new EngineSurfaceViewClient.Engine(surfaceView.getHolder(),this,lifecycleOwner);
    }
}