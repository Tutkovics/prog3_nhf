package nhf;

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
     * @param call maga a hívás. Minden szükséges adatot tartalmaz
     */
    public void addNewCall(Call call){
        // get the fastest arrival elevator
        Elevator fastest = elevatorSet.iterator().next();
        double time, fastestTime = 1000.0;


        for( Elevator e: elevatorSet){
            if( (call.from <= e.maxFloor && call.from >= e.minFloor) && (call.to <= e.maxFloor && call.to >= e.minFloor)){
                time = e.calculateArrivalTime(call);
                System.out.println(e.name + " szükséges ideje: " + time);
                System.out.println("Eddigi leggyorsabb: "+ fastestTime);

                if(fastestTime > time){
                    fastest = e;
                    fastestTime = time;
                }
                //esetleg figyelhetné azt is, hogy melyik liftben hágy hívás van eddig
            } else {
                System.out.println("Rossz szintet adtál meg");
            }
        }

        System.out.println("Leggyorsabb lift: " + fastest.name);
        fastest.addCall(call);
    }
}
