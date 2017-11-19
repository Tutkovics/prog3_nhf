package sample;

import javafx.application.Application;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.fxml.FXMLLoader;
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

import java.util.TreeSet;

/**
 * A nagyházi<code>Main</code> osztálya.
 *
 * <p>Itt kerülnek létrehozásra a liftek, illetve itt adom hozzá
 * a liftkontrollerhez. Valamint a megjelenítésért, és az osztályok
 * együttműködéséért felelős.
 *
 * @author  Tutkovics András
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception{
       // Build the menu
       Text menuText = new Text("Menü");
       menuText.setX(50);
        menuText.setY(50);
        menuText.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.REGULAR, 20));

        //Set Buttons
        Button play = new Button("Indít");
        Button settings = new Button("Beállítások");
        Button newFile = new Button("Új szimuláció");
        //css
        play.setStyle("-fx-min-width: 120px;");
        settings.setStyle("-fx-min-width: 120px;");
        newFile.setStyle("-fx-min-width: 120px;");

        //Set Grid Pane
        GridPane menuGrid = new GridPane();
        menuGrid.setPadding( new Insets(10,10,10,10));
        menuGrid.setVgap(5);
        menuGrid.setHgap(5);
        menuGrid.setAlignment(Pos.CENTER);

        menuGrid.add(menuText,0,0);
        menuGrid.add(play,0,2);
        menuGrid.add(newFile,0,3);
        menuGrid.add(settings,0,4);
        Scene menu = new Scene(menuGrid, 600, 300);

       stage.setTitle("Liftszimulátor");
       stage.setScene(menu);
       stage.show();

    }


    public static void main(String[] args) {
        //launch(args);

        //Elevator a = new Elevator("A",15,0,18,0.6);
        Elevator a = new Elevator("A",15,0,18,0.6);
        Elevator b = new Elevator("B",15,-1,18,0.6);
        Elevator c = new Elevator("C",20,-1,18,0.6);
        Elevator d = new Elevator("D",20,-1,18,0.6);

        ElevatorController controller = new ElevatorController();
        controller.addNewElevator(a);
        controller.addNewElevator(b);
        controller.addNewElevator(c);
        controller.addNewElevator(d);

       a.addCall(2);
       a.addCall(10);
       b.addCall( 14);
       d.addCall(-1);

       try {
           controller.addNewCall(-1, 10);
       }catch (Exception e){
           System.out.println("Balfasz");
       }

       TreeSet<Call> proba = new TreeSet<>();
       Timer ti = new Timer(0,12,35);
       Call c1 = new Call(1,2, ti);
       Call c2 = new Call(1,9, ti);
       Call c3 = new Call(1,-1, ti);
       Call c4 = new Call(1,-100, ti);
       proba.add(c1);
       proba.add(c2);
       proba.add(c3);
       proba.add(c4);
       c2.s = Call.Status.GET_IN;
       c1.s = Call.Status.DONE;
       for(Call e: proba){
           System.out.println("From:\t" + e.from + "\tTo:\t" + e.to + "\tS: " + e.s);
       }
        System.out.println("Counter: "+Call.counter);

        Timer t1 = new Timer(00,59,55);
        Timer t2 = new Timer(00,15,50);
        if(t1.equals(t2)) System.out.println("Azonosak");
        t1.addSecond();
        if(t1.equals(t2)) System.out.println("Azonosak");
        t2.addSecond();

        if(t1.equals(t2)) System.out.println("Azonosak");
        Thread th = new Thread(t1);
        th.start();

    }

}
