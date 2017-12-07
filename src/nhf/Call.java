package nhf;

import java.io.Serializable;

/**
 * A nagyházi<code>Call</code> osztálya.
 *
 * <p>Az elemi szintű lifthívások osztálya. Tárolva van benne, hogy honnan hova
 * utazik az illető, illetve, hogy a maga a hívás melyik fázisban van. Pl: felvette-e már
 * az utast a lift, vagy még meg kell állnia a szinten.
 *
 * @author  Tutkovics András
 */
public class Call implements Comparable<Call>, Serializable{
    enum Status{ CALLED, GET_IN, DONE }
    private static int counter = 0;
    int from;
    int to;
    Status s;
    Timer timer;
    int id;

    /**
     * A <code>Call</code> osztály konstruktora.
     * <p>Alapértelmezettként a <code>CALLED</code> státusz lesz beállítva.</p>
     * @param f melyik szintről hívták a liftet
     * @param t melyik szintre akarnak utazni a lifttel
     */
    public Call(int f, int t, Timer time){
        counter++;
        from = f;
        to = t;
        s = Status.CALLED;
        timer = time;
        id = counter;
    }

    /**
     * Muszáj volt felülírni, különben nem tudna a <code>TreeSet</code>
     * valami sorrendet felállítani köztük.
     *
     * @param o a másik <code>Call</code> objektum
     * @return a két objektum célállomásának különbsége
     */
    @Override
    public int compareTo(Call o) {
        return this.to - o.to;
    }
}
