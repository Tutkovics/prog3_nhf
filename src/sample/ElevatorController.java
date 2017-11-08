package sample;

import java.util.HashSet;
import java.util.Set;

public class ElevatorController {
    public Set<Elevator> elevatorSet = new HashSet<Elevator>();

    public void addNewElevator(Elevator ele){
        elevatorSet.add(ele);
    }

    public void addNewCall(int startFloor, int endFloor){
        // get the fastest arrival elevator
        Elevator fastest = elevatorSet.iterator().next();
        double calculatedTime, time = 1000;


        for( Elevator e: elevatorSet){
            if(e.calls.isEmpty()){
                // rögtön arra a szintre megy ahonnan hívták
                time = e.timeBeetweenTwoFloor(e.actualFloor,startFloor);
                time += e.openDoor;
            } else {
                boolean needToGoUp = e.actualFloor < e.calls.first();
                int stops = 0;
                if( (needToGoUp && startFloor > e.actualFloor) || (!needToGoUp && startFloor < e.actualFloor)){
                    // útba esik
                } else {
                    // előtte ki kell üríteni magát
                }
                // 2 eset van:
                // 1) abba az irányba megy ahova hívták
                // 2) előtte ki kell ürítenie magát
                for(Integer i: e.calls){
                    System.out.println("Ide kell menniiii ("+e.name+"): " + i);
                }
            }
        }

        fastest.calls.add(endFloor);
        System.out.println("Fastest: " + fastest.name + " time: " + time + " Current floor: " + fastest.actualFloor);
    }
}
