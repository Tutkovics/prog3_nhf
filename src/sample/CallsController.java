package sample;

import java.awt.*;
import java.io.*;
import java.util.TreeSet;

public class CallsController{
    public static TreeSet<Call> proba = new TreeSet<>();

    public CallsController() throws IOException, ClassNotFoundException {
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
        save("kuki");

        list();
    }

    public static void save(String fileName) throws IOException {
        File outputFile = new File(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile));
        oos.writeObject(proba);
        oos.close();
    }

    public static void backUp(String fileName) throws IOException, ClassNotFoundException {
        File inputFile = new File(fileName);
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFile));
        proba = (TreeSet<Call>) ois.readObject();
        ois.close();
    }

    public static void list(){
        for(Call c:proba){
            System.out.println("ID: "+c.id+" From: "+c.from+" To: "+c.to+" Status: "+c.s+
                    " Timer: " + c.timer.hh+":"+c.timer.mm+":"+c.timer.ss);
        }
    }


}
