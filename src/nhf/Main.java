package nhf;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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

        try {
            //Elevator a = new Elevator("A",15,0,18,0.6);
            Elevator a = new Elevator("A",15,0,18,0.6);
            Elevator b = new Elevator("B",15,-1,18,0.6);
            //Elevator c = new Elevator("C",20,-1,18,0.6);
            //Elevator d = new Elevator("D",20,-1,18,0.6);

            ElevatorController controller = new ElevatorController();
            controller.addNewElevator(a);
            controller.addNewElevator(b);
            //controller.addNewElevator(c);
            //controller.addNewElevator(d);

            Timer ti = new Timer(0,12,35);
            Call c1 = new Call(2,3, ti);
            Call c2 = new Call(18,90, ti);
            Call c3 = new Call(1,-1, ti);

            Call c5 = new Call(10,5, ti);

            a.addCall(c1);
            //a.addCall(c2);
            //b.addCall( c3);


            /*Timer t1 = new Timer(00,59,55);
            Thread th = new Thread(t1);
            th.start();*/
            //CallsController call = new CallsController();
            controller.addNewCall(c5);

            //succesfully cloned to Windows


        }catch (Exception e){
            System.out.println("Balfasz");
            System.out.println(e.getStackTrace());
            e.printStackTrace();

        }
    }

}
