package sample;

import java.io.Serializable;
import java.security.InvalidParameterException;
import static java.lang.Thread.sleep;

public class Timer implements Runnable, Serializable {
    public int hh;
    public int mm;
    public int ss;

    /**
     * A <code>Timer</code> osztály konstruktora, beállítja a megadott értékeket,
     * valamint ellenőrzi, hogy megfelelő formátumúak-e azok.
     *
     * @param hour óra
     * @param minute perc
     * @param second másodperc
     * @throws InvalidParameterException Rossz paraméterekkel lett meghívva a konstruktor
     */
    public Timer(int hour, int minute, int second) throws InvalidParameterException{
        if( hour < 0 || minute < 0 || minute > 59 || second < 0 || second > 59){
            throw new InvalidParameterException("Rossz formátum a Timer létrehozásakor");
        } else {
            hh = hour;
            mm = minute;
            ss = second;
        }
    }

    /**
     * 1 másodperccel növeli az értékét
     */
    public void addSecond(){
        if(ss < 59){
            ss++;
        } else if( ss == 59){
            ss = 0;
            if( mm < 59 ){
                mm++;
            } else if (mm == 59){
                mm = 0;
                hh++;
            }
        }
        System.out.println(hh + ":" +mm +":" + ss );
    }

    /**
     * Az <code>equals</code> függvény megírása, hogy 2 db <code>Timer</code>
     * összehasonlítható legyen értékük szerint.
     *
     * @param t az összehasonlítandó <code>Timer</code> objektum
     * @return igaz, ha minden értéke azonos
     */
    public boolean equals(Timer t) {
        if(this.hh == t.hh && this.mm == t.mm && this.ss == t.ss){
            return true;
        } else {
            return false;
        }
    }


    /**
     * Alkalmassá teszi az osztály példányait arra, hogy autómatikusan
     * növeljék magukat, ezzel létrehozva egy egységes számlálót, amihez
     * későbbiekben a szimuláziót igazítani lehet majd.
     */
    @Override
    public void run() {
        while(true)
        {
            addSecond();
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
