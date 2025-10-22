package com.example.m03_bounce;

public class DataModel {
    private final float modelX;
    private final float modelY;
    private final float modelDX;
    private final float modelDY;
    private final int color;
    private final String name;

    public DataModel(float modelX, float modelY, float modelDX, float modelDY, int color, String name) {
        this.modelX = modelX;
        this.modelY = modelY;
        this.modelDX = modelDX;
        this.modelDY = modelDY;
        this.color = color;
        this.name = name;
    }

    public float getModelX() { return modelX; }
    public float getModelY() { return modelY; }
    public float getModelDX() { return modelDX; }
    public float getModelDY() { return modelDY; }
    public int getColor() { return color; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "DataModel{" +
                "x=" + modelX +
                ", y=" + modelY +
                ", dx=" + modelDX +
                ", dy=" + modelDY +
                ", color=" + color +
                ", name='" + name + '\'' +
                '}';
    }
}
