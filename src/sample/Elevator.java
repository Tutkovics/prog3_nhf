package sample;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static java.lang.StrictMath.abs;

public class Elevator {
    public String name;
    public int maxSeats, maxFloor, minFloor;
    public int actualFloor;
    public double acceleration; // m/s^2
    private static double maxSpeed = 10.0; // m/s
    private static double floorToMeter = 2.5;
    private static double openDoor = 4.0; // sec
    private static double waitTime = 8.0; // sec
    public Set<Integer> calls = new HashSet<Integer>();

    Random random = new Random();

    public Elevator( String nameOfElv, int seats, int floorMin, int floorMax,  double accel){
        maxSeats = seats;
        maxFloor = floorMax;
        minFloor = floorMin;
        acceleration = accel;
        name = nameOfElv;

        // set to a random floor
        actualFloor = random.nextInt(maxFloor + 1 - minFloor) + minFloor;
    }


    public double calculateArrivalTime(int startFloor){
        double diff = abs(startFloor - actualFloor) * floorToMeter;

        // s = a/2  * t^2 --> t(gyorsulás) = a/2 * s
        // diff/2 mert ugyan ennyi idő kell neki lelassulni is
        double timeAcceleration = Math.sqrt(acceleration/2*(diff/2));
        //nem vizsgálja, hogy max sebességnél többel megy-e

        double constantSpeedTime = 0.0;

        //double timeDeceleration = Math.sqrt(acceleration/2*(diff/2));
        //double timeDeceleration = timeAcceleration

        double openDoorTime = calls.size() * ( openDoor*2 + waitTime);

        return 2*timeAcceleration + constantSpeedTime + openDoorTime;
    }


}
