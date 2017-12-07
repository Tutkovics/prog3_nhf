package nhf;

import javax.swing.*;

/**
 * A nagyházi<code>Main</code> osztálya.
 *
 * <p>Itt kerülnek létrehozásra a liftek, illetve itt adom hozzá
 * a liftkontrollerhez. Valamint a megjelenítésért, és az osztályok
 * együttműködéséért felelős.
 *
 * @author  Tutkovics András
 */
public class Main{
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new Simulation("Szimulátor");
                frame.setSize(600,600);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}
