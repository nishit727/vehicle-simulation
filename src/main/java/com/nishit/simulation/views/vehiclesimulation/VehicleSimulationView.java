package com.nishit.simulation.views.vehiclesimulation;

import com.nishit.simulation.VehicleSimulation;
import com.nishit.simulation.Vehicle;
import com.nishit.simulation.views.MainLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import elemental.json.JsonObject;
import org.vaadin.pekkam.Canvas;
import org.vaadin.pekkam.CanvasRenderingContext2D;

/**
 * Main GUI view for the simulation.
 */
@PageTitle("Vehicle Simulation")
@Route(value = "simulation", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class VehicleSimulationView extends VerticalLayout {

    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 500;
    private static final int RADIUS = 30;
    private static final int STEP_SIZE = 2;

    private UpdateCanvas thread;
    private CanvasRenderingContext2D ctx;
    private VehicleSimulation vehicleSimulation;
    private int currentVehicle = -1;

    /**
     * Class constructor to initialize the UI and the simulation canvas.
     */
    public VehicleSimulationView() {

        vehicleSimulation = new VehicleSimulation();

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button addVehicle = new Button("Add Vehicle");
        addVehicle.addClickListener(this::addNewVehicle);

        Button clearCanvas = new Button("Reset");
        clearCanvas.addClickListener(this::resetSimulation);

        horizontalLayout.add(addVehicle);
        horizontalLayout.add(clearCanvas);
        add(horizontalLayout);

        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.getStyle().set("border", "1px solid");

        ctx = canvas.getContext();

        canvas.getElement().addEventListener("click", this::handleClick)
                .addEventData("event.offsetX")
                .addEventData("event.offsetY");

        add(canvas);
        add(new Paragraph("How to :"));
        add(new Text("Add Vehicle : add a new vehicle at a random location"));
        add( new HtmlComponent("br"));
        add(new Text("Select Vehicle : left-click the vehicle to select the vehicle"));
        add( new HtmlComponent("br"));
        add(new Text("Move Vehicle : left-click anywhere on the canvas to set the new destination for the " +
                "selected vehicle"));
        add( new HtmlComponent("br"));
        add(new Text("Reset : Reset the simulation"));
    }

    /**
     * Listens to clicks on the canvas and either selects a vehicle or adds new destination for the selected vehicle.
     * @param event DomEvent
     */
    private void handleClick(DomEvent event) {
        JsonObject eventData = event.getEventData();
        int x = (int) eventData.getNumber("event.offsetX");
        int y = (int) eventData.getNumber("event.offsetY");

        System.out.println("Clicked pos X: " + x + ", Y: " + y);

        if (!selectVehicle(x, y) && currentVehicle > -1) {
            vehicleSimulation.setDestination(currentVehicle, x, y);
        }
    }

    /**
     * Select a vehicle based on the click location.
     * @param x X coordinate of the click
     * @param y Y coordinate of the click
     * @return A boolean value indicating if a vehicle gets selected
     */
    private boolean selectVehicle(int x, int y) {
        int closestVehicleID = -1;
        double smallestDistance = Double.MAX_VALUE;
        for (Vehicle vehicle: vehicleSimulation.vehicles) {
            int[] value = vehicle.getPosition();
            double distance = Math.sqrt(Math.pow(x - value[1], 2)+Math.pow(y - value[2], 2));
            if (distance < smallestDistance && distance <= RADIUS) {
                smallestDistance = distance;
                closestVehicleID = value[0];
            }
        }
        if (closestVehicleID > -1) {
            currentVehicle = closestVehicleID;
            System.out.println("Selected vehicle with ID : "+closestVehicleID);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds a new vehicle at a random position and random color.
     * @param buttonClickEvent Click event for the add vehicle button
     */
    private void addNewVehicle(ClickEvent<Button> buttonClickEvent) {
        vehicleSimulation.addNewVehicle((int) (Math.random() * CANVAS_WIDTH), (int) (Math.random() * CANVAS_HEIGHT),
                (int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256), STEP_SIZE);
    }

    /**
     * Resets the simulation.
     * @param buttonClickEvent Click event for reset simulation button
     */
    private void resetSimulation(ClickEvent<Button> buttonClickEvent) {
        System.out.println("reset simulation");
        vehicleSimulation.reset();
        currentVehicle = -1;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        thread = new UpdateCanvas(attachEvent.getUI(), this);
        thread.start();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        thread.interrupt();
        thread = null;
    }

    /**
     * A thread to continuously update the canvas based on the vehicle positions.
     */
    private class UpdateCanvas extends Thread {

        private final UI ui;
        private final VehicleSimulationView view;

        /**
         * Class constructor
         * @param ui The UI object
         * @param view The VehicleNavigationView object
         */
        public UpdateCanvas(UI ui, VehicleSimulationView view) {
            this.ui = ui;
            this.view = view;
        }

        /**
         * Infinite loop to continuously update the canvas
         */
        @Override
        public void run() {
            while (true) {
                drawVehicles();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * Clear the canvas and re-draw all the vehicles at their current positions.
         */
        private void drawVehicles() {
            ui.access(() -> {
                view.ctx.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
                for (Vehicle vehicle: view.vehicleSimulation.vehicles) {
                    int[] value = vehicle.getPosition();
                    view.ctx.setLineWidth(2);
                    view.ctx.setFillStyle(String.format("rgb(%s, %s, %s)", value[3], value[4], value[5]));
                    view.ctx.beginPath();
                    view.ctx.arc(value[1], value[2], RADIUS, 0, 2 * Math.PI, false);
                    view.ctx.closePath();
                    view.ctx.stroke();
                    view.ctx.fill();
                }
            });
        }
    }
}

