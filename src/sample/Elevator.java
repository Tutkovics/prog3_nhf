package sample;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.sqrt;

public class Elevator {
    public String name;
    public int maxSeats, maxFloor, minFloor;
    public int actualFloor;
    public double acceleration; // m/s^2
    private static double maxSpeed = 3; // m/s
    private static double floorToMeter = 2.5;
    public static double openDoor = 4.0; // sec
    private static double waitTime = 8.0; // sec
    public TreeSet<Integer> calls = new TreeSet<Integer>();

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

    public double calculateArrivalTimeNew(int callersFloor){
        // sum the time beetween floor
        boolean shouldGoUpwards =  (callersFloor - actualFloor) >= 0;
        boolean empty = calls.isEmpty();
        if( empty){
            //empty elevator


        } else {
            // need to go some floor
        }

        return 0.0;
    }

    public double timeBeetweenTwoFloor(int startFloor, int endFloor){
        double time = 0;
        double distance = abs( startFloor - endFloor) * floorToMeter;
        double halfDistance = distance/2; // need same time to accelation and slow down

        double timeToMaxSpeed = acceleration/maxSpeed;
        double couldAccelerate = sqrt((halfDistance/acceleration)*2);

        if( couldAccelerate > timeToMaxSpeed){
            // le kell korlátozni
            time += timeToMaxSpeed;
            double distanceLeft = halfDistance - ((acceleration/2)*timeToMaxSpeed*timeToMaxSpeed);
            time += distanceLeft/maxSpeed;
        } else {
            //nem kell korlátozni
            time += couldAccelerate;
        }

        return time*2;
    }

    public void addCall(int i){
        this.calls.add(i);
    }


}

    /*public double calculateArrivalTime(int startFloor){
        double timeAcceleration = 0;
        double diff = abs(startFloor - actualFloor) * floorToMeter;

        double timeToMaxSpeed = maxSpeed/acceleration;

        if( (diff / 2) >  acceleration/2*timeToMaxSpeed*timeToMaxSpeed ){
            // if can reach the max speed
        } else {
            // s = a/2  * t^2 --> t(gyorsulás) = a/2 * s
            // diff/2 mert ugyan ennyi idő kell neki lelassulni is
            timeAcceleration = Math.sqrt(acceleration/2*(diff/2));
        }

        double constantSpeedTime = 0.0;

        //double timeDeceleration = Math.sqrt(acceleration/2*(diff/2));
        //double timeDeceleration = timeAcceleration

        double openDoorTime = calls.size() * ( openDoor*2 + waitTime);

        return 2*timeAcceleration + constantSpeedTime + openDoorTime;
    }*/