package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.css.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.*;


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

        //play.getStylesheets().add("test.css");

       stage.setTitle("Liftszimulátor");
       stage.setScene(menu);
       stage.show();

    }


    public static void main(String[] args) {
        //launch(args);

        Elevator a = new Elevator("A",15,0,18,1.0);
        Elevator b = new Elevator("B",15,-1,18,1.0);
        Elevator c = new Elevator("C",20,-1,18,1.0);
        Elevator d = new Elevator("D",20,-1,18,0.2);

        ElevatorController controller = new ElevatorController();
        controller.addNewElevator(a);
        controller.addNewElevator(b);
        controller.addNewElevator(c);
        controller.addNewElevator(d);

        controller.addNewCall(2, 5);
    }


}