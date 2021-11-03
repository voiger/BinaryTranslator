package com.uialert.binarytranslator.server;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;

import com.uialert.binarytranslator.R;

public class ActivityServer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        EngineSurfaceViewServer engineSurfaceViewServer = new EngineSurfaceViewServer();
        EngineSurfaceViewServer.Engine engine = new EngineSurfaceViewServer.Engine(surfaceView.getHolder(),this);
    }
}