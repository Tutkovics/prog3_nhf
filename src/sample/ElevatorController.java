package sample;

import java.util.HashSet;
import java.util.Set;

public class ElevatorController {
    public Set<Elevator> elevatorSet = new HashSet<Elevator>();

    /**
     * Új liftet lehet hozzáadni a liftek listájához. Ezen liftek között fogja
     * szétosztani a program a beérkező lifthívásokat.
     *
     * @param ele a felvenni kívánt lift
     */
    public void addNewElevator(Elevator ele){
        elevatorSet.add(ele);
    }


    /**
     * Kiválasztja, hogy a elevatorSet listában megadott liftek közül melyik tudna
     * a leggyorsabban megérkezni a hívó szintjére, majd a nyertes liftnek hozzáadja
     * a hívások tömbjéhez.
     *
     * @param startFloor a hívónak a szintje, ahonnan utazni szeretne
     * @param endFloor a cél szint
     */
    public void addNewCall(int startFloor, int endFloor){
        // get the fastest arrival elevator
        Elevator fastest = elevatorSet.iterator().next();
        double time, fastestTime = 1000.0;


        for( Elevator e: elevatorSet){
            if( (startFloor <= e.maxFloor && startFloor >= e.minFloor) || (endFloor <= e.maxFloor && endFloor >= e.minFloor)){
                time = e.calculateArrivalTime(startFloor);
                System.out.println(e.name + " szükséges ideje: " + time);

                fastest = fastestTime > time ? e : fastest;
                //esetleg figyelhetné azt is, hogy melyik liftben hágy hívás van eddig
            }
        }

        System.out.println("Leggyorsabb lift: " + fastest.name);
        fastest.addCall(endFloor);
    }
}
