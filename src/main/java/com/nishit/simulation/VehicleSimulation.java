package com.nishit.simulation;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Simulates the addition and movement of vehicles.
 */
public class VehicleSimulation {

    public int vehicleID;
    private ArrayList<ConcurrentLinkedQueue<int[]>> destinationQueues;
    public ArrayList<Vehicle> vehicles;
    private ArrayList<Thread> vehicleThreads;

    /**
     * Class constructor. Initializes all the ArrayLists.
     */
    public VehicleSimulation() {
        vehicleID = 0;
        destinationQueues = new ArrayList<>();
        vehicles = new ArrayList<>();
        vehicleThreads = new ArrayList<>();
    }

    /**
     * Create and add a new vehicle to the simulation and start the vehicle thread.
     * @param x X coordinate of the vehicle
     * @param y Y coordinate of the vehicle
     * @param r Red color value
     * @param g Green color value
     * @param b Blue color value
     * @param stepSize Number of pixels the vehicle can move in each step
     */
    public void addNewVehicle(int x, int y, int r, int g, int b, int stepSize) {
        ConcurrentLinkedQueue<int[]> destinationQueue = new ConcurrentLinkedQueue<>();
        destinationQueues.add(destinationQueue);
        vehicleID ++;
        Vehicle vehicle = new Vehicle(vehicleID, x, y, r, g, b, stepSize, destinationQueue);
        Thread vehicleThread = new Thread(vehicle);
        vehicleThread.start();
        vehicles.add(vehicle);
        vehicleThreads.add(vehicleThread);
    }

    /**
     * Set a new destination for a vehicle.
     * @param vehicleID Unique vehicle ID
     * @param x X coordinate of destination
     * @param y Y coordinate of destination
     */
    public void setDestination(int vehicleID, int x, int y) {
        if (destinationQueues.size() >= vehicleID) {
            destinationQueues.get(vehicleID - 1).add(new int[]{x, y});
        }
    }

    /**
     * Reset the simulation and remove all the previously added vehicles.
     */
    public void reset() {
        System.out.println("Reset simulation");
        for (Vehicle vehicle: vehicles) {
            vehicle.terminate();
        }
        for (Thread thread: vehicleThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        vehicles.clear();
        vehicleThreads.clear();
        destinationQueues.clear();
        vehicleID = 0;
    }
}
