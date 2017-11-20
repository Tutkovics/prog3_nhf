package sample;


import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.*;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

public class CallsController extends JFrame{
    public static TreeSet<Call> proba = new TreeSet<>();

    public CallsController() throws IOException, ClassNotFoundException {
        Timer ti = new Timer(0,12,35);
        Call c1 = new Call(1,2, ti);
        Call c2 = new Call(10,9, ti);
        Call c3 = new Call(3,-1, ti);
        Call c4 = new Call(1,-100, ti);
        proba.add(c1);
        proba.add(c2);
        proba.add(c3);
        proba.add(c4);
        c2.s = Call.Status.GET_IN;
        c1.s = Call.Status.DONE;
        save("kuki");

        String[] columns = new String[] {
                "Id", "From", "To", "Hour","Minute", "Second"
        };

        //actual data for the table in a 2d array
        Object[][] data = new Object[][] {
                {c1.id, c1.from, c1.to, c1.timer.hh, c1.timer.mm, c1.timer.ss},
                {c2.id, c2.from, c2.to, c2.timer.hh, c2.timer.mm, c2.timer.ss},
                {c3.id, c3.from, c3.to, c3.timer.hh, c3.timer.mm, c3.timer.ss},
        };


        //create table with data
        //Vector<Call> vec = new Vector<>(proba);

        //String[] result = proba.toArray(new String[proba.size()]);

        //JTable table = new JTable(data, columns);
        JTable table = new JTable(new AbstractTableModel() {
            public String getColumnName(int col) {
                return columns[col].toString();
            }
            public int getRowCount() { return proba.size(); }
            public int getColumnCount() { return columns.length; }
            public Object getValueAt(int row, int col) {
               Object o = null;
               int i = 0;
               for(Call c:proba){
                   switch (col){
                       case 0:
                           o = c.id;
                           break;
                       case 1:
                           o = c.from;
                           break;
                       case 2:
                           o = c.to;
                           break;
                       case 3:
                           o = c.timer.hh;
                           break;
                       case 4:
                           o = c.timer.mm;
                           break;
                       case 5:
                           o = c.timer.ss;
                           break;
                   }
                   if( i == row)break;
                   i++;
               }
                    return o;
            }
            public boolean isCellEditable(int row, int col)
            { return true; }
            public void setValueAt(Object value, int row, int col) {
                int i = 0;
                for(Call c:proba) {
                    switch (col) {
                        case 0:
                            c.id = (int) value;
                            break;
                        case 1:
                            c.from = (int) value;
                            break;
                        case 2:
                            c.to = (int) value;
                            break;
                        case 3:
                            c.timer.hh = (int) value;
                            break;
                        case 4:
                            c.timer.mm = (int) value;
                            break;
                        case 5:
                            c.timer.ss = (int) value;
                            break;
                    }
                    if (i == row) break;
                    i++;
                }
                //rowData[row][col] = value;
                //fireTableCellUpdated(row, col);
                try {
                    listCalls();
                    save("kuki");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        //add the table to the frame
        this.add(new JScrollPane(table));

        this.setTitle("Liftszimujlátor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

        listCalls();
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

    public static void listCalls(){
        for(Call c:proba){
            System.out.println("ID: "+c.id+" From: "+c.from+" To: "+c.to+" Status: "+c.s+
                    " Timer: " + c.timer.hh+":"+c.timer.mm+":"+c.timer.ss);
        }
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new CallsController();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
