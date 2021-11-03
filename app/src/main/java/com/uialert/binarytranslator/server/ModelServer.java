package com.uialert.binarytranslator.server;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.PixelFormat;
import android.view.Display;

import com.uialert.binarytranslator.CommunizationColours;
import com.uialert.binarytranslator.Pixel;
import com.uialert.binarytranslator.R;
import com.uialert.binarytranslator.SizeDisplay;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ModelServer {
    Bitmap bitmap;
    int color;
    Queue<Integer> queueColor = new PriorityQueue<Integer>(120);
    ArrayList<Pixel> pixels = new ArrayList<>();

    public ModelServer(Context context) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.neeeeeoooon);
        bitmap = Bitmap.createScaledBitmap(bitmap, SizeDisplay.x, SizeDisplay.y, false);

        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                pixels.add(new Pixel(x, y, bitmap.getPixel(x, y)));
            }
        }
    }

    void update(Context context) {
        if (queueColor.size() > 100) {
            return;
        }
        Pixel pixel = pixels.get(0);
        pixels.remove(0);
        queueColor.add(CommunizationColours.broadcastX);
        convectorIntTO1010(Integer.toBinaryString(pixel.getX()));
        queueColor.add(CommunizationColours.broadcastY);
        convectorIntTO1010(Integer.toBinaryString(pixel.getY()));
//        queueColor.add(CommunizationColours.broadcastColor);
//        convectorIntTO1010(Integer.toBinaryString(pixel.getColor()));

    }

    void convectorIntTO1010(String hex) {
        for (int i = 0; i < hex.length(); i++) {
            if (hex.charAt(i) == '1') {
                queueColor.add((int) CommunizationColours.broadcast1);
            } else {
                queueColor.add((int) CommunizationColours.broadcast0);
            }
            queueColor.add(CommunizationColours.broadcastNull);

        }
    }


    public int getColor() {
        return queueColor.remove();
    }


}
