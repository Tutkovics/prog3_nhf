package sample;

import java.util.HashSet;
import java.util.Set;

public class ElevatorController {
    public Set<Elevator> elevatorSet = new HashSet<Elevator>();

    public void addNewElevator(Elevator ele){
        elevatorSet.add(ele);
    }

    public void addNewCall(int startFloor, int endFloor){
        Elevator fastest = elevatorSet.iterator().next();
        double calculatedTime, time = 100;

        for( Elevator e: elevatorSet){
            calculatedTime =  e.calculateArrivalTime(startFloor);
            if( time > calculatedTime){
                time = calculatedTime;
                fastest = e;
            }
        }

        fastest.calls.add(endFloor);
        System.out.println("Fastest: " + fastest.name + " time: " + time);
    }
}
