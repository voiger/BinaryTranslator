package com.uialert.binarytranslator.server;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.uialert.binarytranslator.CommunizationColours;

public class RenderServer {
    void draw(Canvas canvas, ModelServer modelServer){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(CommunizationColours.broadcastNull);
        canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),paint);

        paint.setColor(modelServer.getColor());
        //paint.setColor(CommunizationColours.broadcast1);
        canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),paint);

    }
}
