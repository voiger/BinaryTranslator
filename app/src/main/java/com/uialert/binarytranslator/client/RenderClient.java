package com.uialert.binarytranslator.client;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.uialert.binarytranslator.Pixel;

public class RenderClient {
    Paint paint;
    public RenderClient() {

        paint = new Paint();
        paint.setColor(Color.WHITE);
    }


    public void draw(Canvas canvas, ModelClient modelClient) {
        try {
            for (Pixel pixel: modelClient.getPixelArrayList()){
                canvas.drawPoint(pixel.getX(),pixel.getY(),paint);
            }
        } catch (Exception e) {

        }
    }
}
