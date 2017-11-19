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
    public static double waitTime = 8.0; // sec
    public TreeSet<Integer> calls = new TreeSet<Integer>(); // TreeSet, mert növekvő sorrendbe tárolja a szintek számát

    Random random = new Random();


    /**
     * A Lift osztály konstruktora, beállítjuk a lift alapvető tulajdonságait,
     * amik minden liftnél különbözőek lehetnek. Azon attribútomok, amik állandóak
     * minden lift esetén azok static és privát elérhetőséggel rendelkeznek. Létrehozáskor
     * minden lift egy random emeletre kerül be, innen fog indulni a szimuláció.
     *
     * @param nameOfElv a lift neve, hogy könnyebb legyen dolgozni velük
     * @param seats a lift férőhelyeinek száma
     * @param floorMin a legkisebb emelet, ameddig a lift közlekedik
     * @param floorMax a legmagasabb emelet, ameddig a lift közlekedik
     * @param accel a lift gyorsulása
     */
    public Elevator( String nameOfElv, int seats, int floorMin, int floorMax,  double accel){
        maxSeats = seats;
        maxFloor = floorMax;
        minFloor = floorMin;
        acceleration = accel;
        name = nameOfElv;

        // set to a random floor
        //actualFloor = random.nextInt(maxFloor + 1 - minFloor) + minFloor;
        actualFloor = 0;
    }

    /**
     * Kiszámolja mennyi ideig tartana a liftnek megérkezni a hívó szintjér
     * Figyelembe veszi, hogy van-e híváslista éppen a liftben, vagy üres, mert akkor
     * rögtön az adott emeletre mehet. Ellenkező esetben ki kell még szolgálnia az útjába eső megállókat.
     *
     * @param callersFloor melyik emeletre kellene menni
     * @return érkezési idő
     */
    public double calculateArrivalTime(int callersFloor){
        // sum the time beetween floor
        double time;

        if( calls.isEmpty()){
            // Ha üres a lift rögtön tud menni a hívó szintjére
            time = timeBeetweenTwoFloor(actualFloor, callersFloor);
            System.out.println(name + " üres --> rögtön tud menni a " + callersFloor + ". szintre");
        } else {
            // Abban az esetben, ha vannak utasok a liftben
            System.out.println(name + " első emelete: " + calls.first());
            boolean needToGoUp = actualFloor <= calls.first();
            if( (needToGoUp && callersFloor >= actualFloor) || (!needToGoUp && callersFloor <= actualFloor)){
                // útba esik
                int tempFloor = actualFloor;
                int stops = 0;
                time = 0;
                for(Integer nextFloor: calls){
                    System.out.println("nextFloor: "+ nextFloor);
                    if((tempFloor <= callersFloor && nextFloor >= callersFloor) || (tempFloor >= callersFloor && nextFloor <= callersFloor)){
                        // Ha már nincs több útba eső megálló akkor nem kell tovább számolni
                        time += timeBeetweenTwoFloor(tempFloor, callersFloor);
                        System.out.println(name + " nincs több útba eső megálló");

                    } else {
                        time += timeBeetweenTwoFloor(tempFloor, nextFloor);
                        System.out.println(name + " még meg kell álljon " + nextFloor + ". emeleten");
                        tempFloor = nextFloor;
                        stops++;
                    }

                    time += stops * (openDoor*2 + waitTime);
                }
            } else {
                // előtte ki kell üríteni magát, mert a másik irányba megy
                System.out.println(name + " ki kell üríteni magát");
                int stops = calls.size();
                int tempFloor = actualFloor;
                time = 0;
                for(Integer nextFloor: calls){
                    time += timeBeetweenTwoFloor(tempFloor, nextFloor);
                    tempFloor = nextFloor;
                }
                time += stops * (openDoor*2 + waitTime);
            }
        }

        return time;
    }

    /**
     * A függvény kiszámolja, hogy mennyi ideig tart eljutnia egyik emeletről
     * a másikra. Figyelembe veszi a lift gyorsulását, és nem hagyja, hogy a
     * maximálisnak beállított sebességnél gyorsabban menjen.
     *
     * @param callersFloor indulási szint
     * @param endFloor érkezési szint
     * @return a megadott 2 emelett közti út megtételéhez szükséges idő másodpercben
     */
    public double timeBeetweenTwoFloor(int callersFloor, int endFloor){
        double time = 0;
        double distance = abs( callersFloor - endFloor) * floorToMeter;
        double halfDistance = distance/2; // need same time to accelation and slow down

        double timeToMaxSpeed = maxSpeed/acceleration;
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

    /**
     * Hozzáad egy szintet azon szintek listájához, ahhol meg kell állnia a liftnek,
     * mert van benne olyan "utas" aki az adott szintre hívta a liftet.
     *
     * @param i azon szint száma, ahol meg kell állni
     */
    public void addCall(int i){
        this.calls.add(i);
    }


}