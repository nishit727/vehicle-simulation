package com.nishit.simulation;

import org.junit.jupiter.api.*;

import java.util.Arrays;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VehicleSimulationTest {

    VehicleSimulation vehicleSimulation;

    @BeforeAll
    void setUp() {
        vehicleSimulation = new VehicleSimulation();
    }

    @Test
    @Order(1)
    void addNewVehicle() {
        vehicleSimulation.addNewVehicle(10,10,0,0,0,2);
        Assertions.assertEquals(1, vehicleSimulation.vehicleID);
        Assertions.assertEquals(1, vehicleSimulation.vehicles.size());
        Assertions.assertArrayEquals(new int[]{1,10,10,0,0,0}, vehicleSimulation.vehicles.get(0).getPosition());
        vehicleSimulation.addNewVehicle(20,10,0,0,0,2);
        Assertions.assertEquals(2, vehicleSimulation.vehicleID);
        Assertions.assertEquals(2, vehicleSimulation.vehicles.size());
        Assertions.assertArrayEquals(new int[]{2,20,10,0,0,0}, vehicleSimulation.vehicles.get(1).getPosition());
    }

    @Test
    @Order(2)
    void setDestination() throws InterruptedException {
        vehicleSimulation.setDestination(2,15,20);
        Thread.sleep(1000);
        System.out.println(Arrays.toString(vehicleSimulation.vehicles.get(1).getPosition()));
        Assertions.assertArrayEquals(new int[]{2,15,20,0,0,0}, vehicleSimulation.vehicles.get(1).getPosition());
    }

    @Test
    @Order(3)
    void reset() {
        vehicleSimulation.reset();
        Assertions.assertEquals(0, vehicleSimulation.vehicleID);
        Assertions.assertEquals(0, vehicleSimulation.vehicles.size());
    }

    @AfterAll
    void tearDown() {
        vehicleSimulation = null;
    }
}