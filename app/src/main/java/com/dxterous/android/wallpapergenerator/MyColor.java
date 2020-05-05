package com.dxterous.android.wallpapergenerator;

import android.graphics.Color;

import java.util.Random;

public class MyColor {
    public static float[] getRandomColor() {
        Random random = new Random();
        float red = random.nextFloat();
        float blue = random.nextFloat();
        float green = random.nextFloat();
        float alpha = 1.0f;
        return new float[]{red, green, blue, alpha};
    }

    public static float[] red() {
        return new float[]{
                Color.red(Color.RED) / 255f,
                Color.green(Color.RED) / 255f,
                Color.blue(Color.RED) / 255f,
                1.0f
        };
    }

    public static float[] green() {
        return new float[]{
                Color.red(Color.GREEN) / 255f,
                Color.green(Color.GREEN) / 255f,
                Color.blue(Color.GREEN) / 255f,
                1.0f
        };
    }

    public static float[] blue() {
        return new float[]{
                Color.red(Color.BLUE) / 255f,
                Color.green(Color.BLUE) / 255f,
                Color.blue(Color.BLUE) / 255f,
                1.0f
        };
    }

    public static float[] yellow() {
        return new float[]{
                Color.red(Color.YELLOW) / 255f,
                Color.green(Color.YELLOW) / 255f,
                Color.blue(Color.YELLOW) / 255f,
                1.0f
        };
    }

    public static float[] cyan() {
        return new float[]{
                Color.red(Color.CYAN) / 255f,
                Color.green(Color.CYAN) / 255f,
                Color.blue(Color.CYAN) / 255f,
                1.0f
        };
    }

    public static float[] gray() {
        return new float[]{
                Color.red(Color.GRAY) / 255f,
                Color.green(Color.GRAY) / 255f,
                Color.blue(Color.GRAY) / 255f,
                1.0f
        };
    }

}
