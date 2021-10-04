package com.nishit.simulation;

import org.junit.jupiter.api.*;

import java.util.concurrent.ConcurrentLinkedQueue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VehicleTest {

    Vehicle v;
    ConcurrentLinkedQueue<int[]> destinationQueue;
    Thread t;

    @BeforeAll
    void setUp() {
        destinationQueue = new ConcurrentLinkedQueue<>();
        v = new Vehicle(0,0,0 ,0,0,0, 2, destinationQueue);
        t = new Thread(v);
        t.start();
    }

    @Test
    void testRun() throws InterruptedException {
        destinationQueue.add(new int[]{5,8});
        Thread.sleep(200);
        destinationQueue.add(new int[]{8,3});
        Thread.sleep(1000);
        Assertions.assertArrayEquals(new int[]{0,8,3,0,0,0}, v.getPosition());
    }

    @AfterAll
    void tearDown() throws InterruptedException {
        v.terminate();
        t.join();
        t = null;
        v = null;
        destinationQueue = null;
    }
}