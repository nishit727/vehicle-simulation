package com.nishit.simulation;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Vehicle class represents a vehicle. It contains the vehicle ID, position and color.
 * It runs in a separate thread and updates the current vehicle position based on the received destination coordinates.
 */
public class Vehicle implements Runnable {

    private int vehicleID, x, y, r, g, b;
    private float stepSize;
    private ConcurrentLinkedQueue<int[]> destinationQueue;
    private volatile boolean running = true;

    /**
     * Class constructor
     * @param vehicleID Unique vehicle ID
     * @param x Current X coordinate of the vehicle
     * @param y Current Y coordinate of the vehicle
     * @param r Red color value
     * @param g Green color value
     * @param b Blue color value
     * @param stepSize Number of pixels the vehicle can move in each step
     * @param destinationQueue Queue for the destination coordinates
     */
    public Vehicle(int vehicleID, int x, int y, int r, int g, int b, float stepSize,
                   ConcurrentLinkedQueue<int[]> destinationQueue) {
        System.out.println("Vehicle class "+vehicleID);
        this.vehicleID = vehicleID;
        this.x = x;
        this.y = y;
        this.r = r;
        this.g = g;
        this.b = b;
        this.stepSize = stepSize;
        this.destinationQueue = destinationQueue;
    }

    /**
     * Returns the vehicle properties
     * @return Vehicle ID, position values and RGB color values
     */
    public int[] getPosition() {
        return new int[]{vehicleID, x, y, r, g, b};
    }

    /**
     * Checks for a new destination and updates the current vehicle position to move towards the new destination.
     */
    @Override
    public void run() {
        int[][] points = null;
        int i = 0;
        while (running) {
            if (!destinationQueue.isEmpty()) {
                int[] pos = destinationQueue.poll();
                System.out.println("New destination for vehicle ID: " + vehicleID + " is X: " + pos[0] + ", Y: " + pos[1]);
                points = calculatePath(pos[0], pos[1]);
                i = 0;
            }
            if (points != null && i < points.length) {
                this.x = points[i][0];
                this.y = points[i][1];
                i++;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Calculates the intermediate coordinates from vehicle's current position to destination position.
     * @param x Destination X coordinate
     * @param y Destination Y coordinate
     * @return Intermediate X-Y coordinates from the current position to the destination position
     */
    private int[][] calculatePath(int x, int y) {
        double distance = Math.sqrt(Math.pow(x - this.x, 2)+Math.pow(y - this.y, 2));
        double ratio = stepSize/distance;
        int numOfPoints = (int) Math.floor(distance/stepSize) + 1;
        int[][] points = new int[numOfPoints][2];
        for (int i = 0; i < numOfPoints - 1; i++) {
            points[i][0] = (int)((1 - (ratio * (i + 1))) * this.x + (ratio * (i + 1)) * x);
            points[i][1] = (int)((1 - (ratio * (i + 1))) * this.y + (ratio * (i + 1)) * y);
        }
        points[numOfPoints - 1][0] = x;
        points[numOfPoints - 1][1] = y;
        return points;
    }

    /**
     * Terminates the destination updation
     */
    public void terminate() {
        System.out.println("Terminate vehicle "+vehicleID);
        running = false;
    }
}
