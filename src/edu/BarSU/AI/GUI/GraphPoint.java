package edu.BarSU.AI.GUI;

/**
 * Created by Govor Alexander on 30.04.2017.
 */
public class GraphPoint {
    private double xPosition;
    private double yPosition;

    public GraphPoint(double xPosition, double yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public double getXPosition() {
        return xPosition;
    }

    public double getYPosition() {
        return yPosition;
    }

    @Override
    public String toString() {
        return "Point [OX: " + xPosition + ", OY:" + yPosition + "]";
    }
}
