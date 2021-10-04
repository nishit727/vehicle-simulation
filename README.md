# Vehicle Simulation

This project is a simulation for vehicle navigation. It provides a GUI to add vehicles
and move vehicles to desired destinations.

## Running the application

The project is a standard Maven project. To run it from the command line,
type `mvnw` (Windows), or `./mvnw` (Mac & Linux), then open
http://localhost:8080 in your browser.

## Deploying to Production

To create a production build, call `mvnw clean package -Pproduction` (Windows),
or `./mvnw clean package -Pproduction` (Mac & Linux).
This will build a JAR file with all the dependencies and front-end resources,
ready to be deployed. The file can be found in the `target` folder after the build completes.

## Running the application from JAR file

Once the JAR file is built, you can run it using
`java -jar target/vehiclesimulation-1.0-SNAPSHOT.jar`

## Project structure

- `views` package in `src/main/java` contains the server-side Java views of your application.
- `views` folder in `frontend/` contains the client-side JavaScript views of your application.
- `themes` folder in `frontend/` contains the custom CSS styles.

## Usage

- GUI provides an empty canvas and two buttons.
- Add vehicle button adds a vehicle to random location on the canvas with a random color.
- Reset button resets the simulation.
- To set destination for a vehicle, first select the vehicle by left-clicking on the
  vehicle and then select the destination by left-clicking anywhere on the canvas.


