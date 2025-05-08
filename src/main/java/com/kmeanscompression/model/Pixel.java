package com.kmeanscompression.model;

public class Pixel {
    private int r;
    private int g;
    private int b;
    private int cluster;
    private int x;
    private int y;

    public Pixel(int r, int g, int b, int x, int y) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.x = x;
        this.y = y;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getCluster() {
        return cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Calculate Euclidean distance between this pixel and another
    public double distance(Pixel p) {
        return Math.sqrt(
            Math.pow(this.r - p.r, 2) +
            Math.pow(this.g - p.g, 2) +
            Math.pow(this.b - p.b, 2)
        );
    }
} 