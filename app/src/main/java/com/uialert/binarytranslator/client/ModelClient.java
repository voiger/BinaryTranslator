package com.uialert.binarytranslator.client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;
import com.uialert.binarytranslator.CommunizationColours;
import com.uialert.binarytranslator.MainActivity;
import com.uialert.binarytranslator.Pixel;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutionException;

public class ModelClient {

    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    YUVtoRGB translator = new YUVtoRGB();
    Bitmap bitmapR;

    Queue<Pixel> pixelsAdd = new PriorityQueue<>();
    Queue<Integer> decodeColor = new PriorityQueue<>();

    ArrayList<Pixel> pixelArrayList = new ArrayList<>();

    public ModelClient(Context context, LifecycleOwner lifecycle) {

        cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                    //Preview preview = new Preview.Builder().build();

                    //ImageCapture imageCapture = new ImageCapture.Builder().build();

                    ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                            .setTargetResolution(new Size(10, 10))
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build();

                    CameraSelector cameraSelector = new CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build();

                    imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context),
                            new ImageAnalysis.Analyzer() {
                                @Override
                                public void analyze(@NonNull ImageProxy image) {
                                    @SuppressLint("UnsafeOptInUsageError") Image img = image.getImage();
                                    Bitmap bitmap = translator.translateYUV(img, context);

                                    int size = bitmap.getWidth() * bitmap.getHeight();
                                    int[] pixels = new int[size];
                                    bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0,
                                            bitmap.getWidth(), bitmap.getHeight());
                                    decodeColor.add(bitmap.getPixel(bitmap.getWidth() / 2, bitmap.getHeight() / 2));

//                                    for (int i = 0; i < size; i++) {
//                                        int color = pixels[i];
//                                        int r = color >> 16 & 0xff;
//                                        int g = color >> 8 & 0xff;
//                                        int b = color & 0xff;
//                                        int gray = (r + g + b) / 3;
//                                        pixels[i] = 0xff000000 | gray << 16 | gray << 8 | gray;
//                                    }
                                    bitmap.setPixels(pixels, 0, bitmap.getWidth(), 0, 0,
                                            bitmap.getWidth(), bitmap.getHeight());

                                    image.close();
                                }
                            });

                    cameraProvider.bindToLifecycle(lifecycle, cameraSelector, imageAnalysis);

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(context));

    }

    int flagBroadCast = 0;
    int x = 0;
    int y = 0;
    String binary = "";
    int flag = 0;

    int errorSize = 500000;

    public void update(Context context) {
        if (decodeColor.size() < 1) {
            return;
        }
        int color = decodeColor.remove();

//        Log.e("waeaweaw", "update: " + color+":"+ CommunizationColours.broadcast1);
//        if (color - errorSize < CommunizationColours.broadcast1 &&
//                color + errorSize > CommunizationColours.broadcast1) {
//            if (flagBroadCast == CommunizationColours.broadcastY) {
//                flagBroadCast = CommunizationColours.broadcastX;
//                x = binaryStringTOInt(binary);
//                //pixelsAdd.add(new Pixel(x,y, Color.WHITE));
//                pixelArrayList.add(new Pixel(x, y, Color.WHITE));
//                binary = "";
//            }
//            if (flagBroadCast == 0) {
//                flagBroadCast = CommunizationColours.broadcastX;
//            }
//            Log.e("waeaweaw", "update: " + "xdgjklsdfjklghsdfgsdfhghhgaghs6gr67uqu67w23tgr7");
//        }

        if (color - errorSize < CommunizationColours.broadcastNull &&
                color + errorSize > CommunizationColours.broadcastNull) {
            flag = 0;
        }

        if (color - errorSize < CommunizationColours.broadcastX &&
                color + errorSize > CommunizationColours.broadcastX) {
            Log.e("updateX", "updateX  " + "x");
            if (CommunizationColours.broadcastX == flag) {
                return;
            }

            flag = CommunizationColours.broadcastX;
            if (flagBroadCast == CommunizationColours.broadcastY) {
                flagBroadCast = CommunizationColours.broadcastX;
                x = binaryStringTOInt(binary);
                //pixelsAdd.add(new Pixel(x,y, Color.WHITE));
                pixelArrayList.add(new Pixel(x, y, Color.WHITE));
                binary = "";
            }
            if (flagBroadCast == 0) {
                flagBroadCast = CommunizationColours.broadcastX;
            }

        }
        if (color - errorSize < CommunizationColours.broadcastY &&
                color + errorSize > CommunizationColours.broadcastY) {
            if (CommunizationColours.broadcastY == flag) {
                return;
            }
            flag = CommunizationColours.broadcastY;
            if (flagBroadCast == CommunizationColours.broadcastX) {
                flagBroadCast = CommunizationColours.broadcastY;
                y = binaryStringTOInt(binary);
                binary = "";
            }
            Log.e("waeaweaw", "update: " + "y");
        }
        if (color - errorSize < CommunizationColours.broadcast0 &&
                color + errorSize > CommunizationColours.broadcast0) {
            if (CommunizationColours.broadcast0 == flag) {
                return;
            }
            flag = CommunizationColours.broadcast0;
            binary = binary + 0;
            Log.e("waeaweaw", "update: " + "0");
        }
        if (color - errorSize < CommunizationColours.broadcast1 &&
                color + errorSize > CommunizationColours.broadcast1) {
            if (CommunizationColours.broadcast1 == flag) {
                return;
            }
            binary = binary + 1;
            flag = CommunizationColours.broadcast1;
        }
        Log.e("waeaweaw", "up " + " x == " + x + " y == " + y);
    }

    int binaryStringTOInt(String binaryString) {
        double convertedDouble = 0;

        for (int i = 0; i < binaryString.length(); i++) {

            if (binaryString.charAt(i) == '1') {
                int len = binaryString.length() - 1 - i;
                convertedDouble += Math.pow(2, len);
            }
        }
        Log.e("waeaweaw", "update:fdsfsd " + convertedDouble);
        return (int) convertedDouble;
    }

    public ArrayList<Pixel> getPixelArrayList() {
        pixelArrayList.add(new Pixel(100, 100, 121));
        return pixelArrayList;
    }
}
