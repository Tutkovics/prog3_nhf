package nhf;

import javax.swing.*;
import java.util.*;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.sqrt;
import static java.lang.Thread.sleep;

public class Elevator implements Runnable {
    public String name;
    public int maxSeats, maxFloor, minFloor;
    public int actualFloor;
    public double acceleration; // m/s^2
    private static double maxSpeed = 3; // m/s
    private static double floorToMeter = 2.5;
    public static double openDoor = 4.0; // sec
    public static double waitTime = 8.0; // sec
    public double actualSpeed = 0.0;
    public int actualPassengers = 0;
    public int donePassengers = 0;
    public int playSpeed = 1;
    private int sleepTime = 500;

    private JTextArea logArea;



    public enum Status{WAIT_FOR_CALL, WORKING;}
    public Status status;

    public ArrayList<Call> calls = new ArrayList<>(); // TreeSet, mert növekvő sorrendbe tárolja a szintek számát

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
    public Elevator( String nameOfElv, int seats, int floorMin, int floorMax,  double accel, JTextArea logArea){
        maxSeats = seats;
        maxFloor = floorMax;
        minFloor = floorMin;
        acceleration = accel;
        name = nameOfElv;
        status = Status.WAIT_FOR_CALL;
        this.logArea = logArea;


        // set to a random floor
        actualFloor = random.nextInt(maxFloor + 1 - minFloor) + minFloor;
    }

    @Override
    public void run() {
        System.out.println(name + " - aktuális szintje: " + actualFloor);

        while (true){
            try {
                sleep(sleepTime/playSpeed);
                runSimulation();
            } catch (InterruptedException e) {
                System.err.println("Error in Elevator.java");
                e.printStackTrace();
            }
        }
    }

    private void runSimulation() throws InterruptedException {
        boolean isValidCall = false;
        for(Call c: calls){
            if(c.s != Call.Status.DONE){
                isValidCall = true;
            }
        }
        if(!isValidCall){
            status = Status.WAIT_FOR_CALL;
        } else {
            status = Status.WORKING;
            //get the next stop
            int nextStop = 1000;
            for(Call c: calls){
                if( c.s != Call.Status.DONE ){
                    //if not done call
                    if( c.s == Call.Status.CALLED){
                        nextStop = nextStop > abs(actualFloor-c.from) ? c.from: nextStop;
                    } else {
                        nextStop = nextStop > abs(actualFloor - c.to) ? c.to : nextStop;
                    }
                }
            }
            System.out.println(name + " - köv. szint: "+nextStop);

            goFromTo(actualFloor, nextStop);

            //when arrived
            actualFloor = nextStop;
            for (Call c: calls){
                if(c.s == Call.Status.CALLED){
                    c.s = Call.Status.GET_IN;
                    actualPassengers++;
                } else if( c.s== Call.Status.GET_IN){
                    c.s = Call.Status.DONE;
                    donePassengers++;
                    actualPassengers--;
                }
                //open the door, wait and close
                logArea.append(name + " - Ajtó nyitás\n");
                actualSpeed = 0.0;
                sleep((long) openDoor*1000/playSpeed);
                logArea.append(name + " - Várakozás be- és kiszállásra\n");
                sleep((long) waitTime*1000/playSpeed);
                logArea.append(name + " - Ajtó csukás\n");
                sleep((long) openDoor*1000/playSpeed);
            }
        }
    }

    private void goFromTo(int actualFloor, int nextStop) throws InterruptedException {
        //set the attrubutes value while moving
        double travelTime = timeBeetweenTwoFloor(actualFloor, nextStop); //in seconds
        double spentTime = 0;
        for ( int i = 0; i<5; i++){
            //set params between two stop 5 times
            sleep((long) (travelTime*200/playSpeed));//convert to milisec
            spentTime+=travelTime/5;

            //get the actual speed
            actualSpeed = getSpeedAfterStart(spentTime);
//            TODO figyelni, hogy kezelje a lassulást is
            System.out.println(name + " - actualSpeed calculated: " + actualSpeed);

        }
    }

    private double getSpeedAfterStart(double spentTime) {
        double speed = acceleration * spentTime;
        return speed > maxSpeed ? maxSpeed: speed;
    }

    /**
     * Kiszámolja mennyi ideig tartana a liftnek megérkezni a hívó szintjér
     * Figyelembe veszi, hogy van-e híváslista éppen a liftben, vagy üres, mert akkor
     * rögtön az adott emeletre mehet. Ellenkező esetben ki kell még szolgálnia az útjába eső megállókat.
     *
     * @param call a hívás, ami tartalmazza, hogy melyik emeletről hívták a liftet
     * @return érkezési idő
     */
    public double calculateArrivalTime(Call call){

        double time = 0; //sum the times in second
        boolean empty = true;
        for(Call c: calls){
            if( c.s != Call.Status.DONE){
                //.isEmpty not good because we store every calls even that we processed the call
                empty = false;
                break;
            }
        }

        if(empty){
            //we could go caller's floor
            time = timeBeetweenTwoFloor(actualFloor, call.from);
            System.out.println(name + " - Rögtön tud menni a " + call.from + ". szintre.");
        } else {
            //if the elevator has passengers
            // 2 options: útba esik, ki kell üríteni magát

            boolean canGet = false;
            for (Call c: calls){
                if(c.s != Call.Status.DONE){
                    if( c.s == Call.Status.GET_IN){
                        if(actualFloor < c.to && actualFloor < call.from ||
                                actualFloor >c.to && actualFloor > call.from){
                            canGet = true;
                            System.out.println(name + " - canGet = " + canGet);
                        }
                    }
                }
            }

            if( canGet ){
                //we can stop in callers floor
                calls.sort(new Comparator<Call>() {
                    //we sort by destination
                    @Override
                    public int compare(Call o1, Call o2) {
                        return o1.to - o2.to;
                    }
                });

                TreeSet<Integer> needToStop = new TreeSet<>();

                for(Call c: calls){
                    //get the floors where we needd to stop
                    if(c.s != Call.Status.DONE){
                        if(c.s == Call.Status.GET_IN){
                            needToStop.add(c.to);
                        } else {
                            needToStop.add(c.from);
                        }
                    }
                }
                List<Integer> sortedStops = new ArrayList(needToStop);
                Collections.sort(sortedStops);

                int stops = 0;
                Integer previous = null;
                for(Integer i: sortedStops){
                    if( !isBetween(previous, i, call.from)){
                        //while there is more stops
                        time += timeBeetweenTwoFloor(previous,i);
                        stops++;
                    }
                    previous = i;
                }
                time += stops*(openDoor*2+waitTime);
            } else{
                //need to serve passengers
                Call previous = null;
                for(Call c: calls){
                    if(c.s != Call.Status.GET_IN && previous != null){
                        time += timeBeetweenTwoFloor(previous.to, c.to);
                        time += openDoor*2+waitTime;
                    }
                    previous = c;
                }
            }

        }
        return time;

        // sum the time beetween floors
        /*double time;

        if( calls.isEmpty()){
            // Ha üres a lift rögtön tud menni a hívó szintjére
            time = timeBeetweenTwoFloor(actualFloor, call.from);
            System.out.println(name + " üres --> rögtön tud menni a " + call.from + ". szintre");
        } else {
            // Abban az esetben, ha vannak utasok a liftben
            System.out.println(name + " vannak utasok; első emelete: " + calls.first().to);
            boolean needToGoUp = actualFloor <= calls.first().to;
            if( (needToGoUp && call.from >= actualFloor) || (!needToGoUp && call.from <= actualFloor)){
                // útba esik
                System.out.println(name + "útba esik");
                int tempFloor = actualFloor;
                int stops = 0;
                time = 0;
                for(Call nextCall: calls){
                    System.out.println("Következő megálló: "+ nextCall.to);
                    if((tempFloor <= call.from && nextCall.to >= call.from) ||
                            (tempFloor >= call.from && nextCall.to <= call.from)){
                        // Ha már nincs több útba eső megálló akkor nem kell tovább számolni
                        time += timeBeetweenTwoFloor(tempFloor, call.from);
                        System.out.println(name + " nincs több útba eső megálló");

                    } else {
                        time += timeBeetweenTwoFloor(tempFloor, nextCall.to);
                        System.out.println(name + " még meg kell álljon " + nextCall.to + ". emeleten");
                        tempFloor = nextCall.to;
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
                for(Call nextCall: calls){
                    time += timeBeetweenTwoFloor(tempFloor, nextCall.to);
                    tempFloor = nextCall.to;
                }
                //miután kiürült még oda kell érjen a hívó szintjére
                time += timeBeetweenTwoFloor(tempFloor, call.from);
                time += stops * (openDoor*2 + waitTime);
            }
        }

        return time;
        */
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
     * @param call a hozzáadni kívánt hívás
     */
    public void addCall(Call call){
        this.calls.add(call);
    }


    public boolean isBetween( Integer from, Integer to, Integer x){
        int bigger = Integer.max(from, to);
        int smaller = Integer.min(from, to);

        if(smaller < x && bigger > x){
            return true;
        }
        return false;
    }
}