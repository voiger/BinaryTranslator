package com.uialert.binarytranslator.server;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;

public class EngineSurfaceViewServer {


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

        ModelServer modelServer;
        RenderServer renderServer;


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

                        modelServer.update(context);
                        renderServer.draw(canvas, modelServer);
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

        public Engine(SurfaceHolder surfaceHolder, Context context) {
            this.context = context;
             modelServer = new ModelServer(context);
            renderServer = new RenderServer();

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
