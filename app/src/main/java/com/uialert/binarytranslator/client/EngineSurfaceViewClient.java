package com.uialert.binarytranslator.client;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.uialert.binarytranslator.server.ModelServer;
import com.uialert.binarytranslator.server.RenderServer;

public class EngineSurfaceViewClient {


    public static class Engine {

//    private float fps = 120;
//    private float second = 1000000000;
//    private float updateTime = second / fps;
//    private float delta = 0;

        private long timer = System.currentTimeMillis();
        private int fpsMath = 0;

        boolean lockCanvas = false;

        private SurfaceHolder.Callback callback;
        private SurfaceHolder surfaceHolder;

        private volatile boolean stopped;

        long time = System.nanoTime();

        Context context;

        ModelClient modelClient;
        RenderClient renderClient;


        Runnable threadRunnable = new Runnable() {
            @Override
            public void run() {
                while (!stopped) {
                    Canvas canvas;
                    if (surfaceHolder == null || lockCanvas) {
                        synchronized (Engine.this) {
                            try {
                                Engine.this.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        time = System.nanoTime();
                        canvas = surfaceHolder.lockCanvas();
                        lockCanvas = true;

                        modelClient.update(context);
                        renderClient.draw(canvas, modelClient);
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        lockCanvas = false;

                        fpsMath++;

                        if (System.currentTimeMillis() - timer > 1000) {
                            timer += 1000;
                            Log.e("Fps", fpsMath + "");
                            fpsMath = 0;
                        }
                    }
                }
            }
        };

        public void stop() {
            this.stopped = true;
        }

        public Engine(SurfaceHolder surfaceHolder, Context context, LifecycleOwner lifecycle) {
            this.context = context;
             modelClient = new ModelClient(context,lifecycle);
            renderClient = new RenderClient();

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        modelClient.update(context);
                    }
                }
            });

            Thread engineThread = new Thread(threadRunnable, "EngineThread");
            engineThread.start();

            callback = new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                    Engine.this.surfaceHolder = surfaceHolder;
                    synchronized (Engine.this) {
                        lockCanvas = false;
                        Engine.this.notifyAll();
                    }
                }

                @Override
                public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                    Engine.this.surfaceHolder = surfaceHolder;
                }

                @Override
                public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
                    Engine.this.surfaceHolder = null;
                }
            };

            surfaceHolder.addCallback(callback);
        }


    }
}
