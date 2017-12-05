package nhf;


import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import static java.lang.Thread.sleep;

/**
 * A szimuláció megjelenéséért, indításáért felelős osztály.
 * Az egyik leggányabb, amit valaha láttam, azonban ezt a layoutmanager okozza (nagyrészt) :)
 */
public class Simulation extends JFrame implements Runnable, ActionListener{
    boolean simulating;
    private Container cont;
    private Timer timer;
    GridBagConstraints c;
    private double playSpeed;
    private Thread thread;

    private JLabel timeLabel, aLabel, bLabel, cLabel, dLabel; //elevators title
    private JLabel currentLevelLabel, currentSpeedLabel, NOPLabel, DPLabel;//Number Of Passengers; Done Passengers
    private JLabel ACLLabel, BCLLabel, CCLLabel, DCLLabel;//Current Level Labels
    private JLabel ACSLabel, BCSLabel, CCSLabel, DCSLabel;//Current Speed Labels
    private JLabel ANOPLabel, BNOPLabel, CNOPLabel, DNOPLabel; //NOP Labels
    private JLabel ADPLabel, BDPLabel, CDPLabel, DDPLabel; //DP labels
    private JTextArea logPane;
    private JButton startBtn, stopBtn, editBtn;


    public Simulation(String title){
        super(title);

        //set layout manager
        setLayout(new GridBagLayout());
        c = new GridBagConstraints();

        //set the variables
        cont = getContentPane();
        timer = new Timer(0,0,0);
        playSpeed = 1.0;

        thread = new Thread( this);

        addComponents();
    }

    private void addComponents() {
        //start and stop button
        /*ImageIcon StartButtonIcon = createImageIcon("../117999.png");
        ImageIcon StopButtonIcon = createImageIcon("../117999.png");*/

        //// Start and Stop button ////
        startBtn = new JButton("Start");
        startBtn.setToolTipText("Szimuláció elindítása");
        startBtn.setActionCommand("start_simulating");
        startBtn.addActionListener(this);
        startBtn.setEnabled(true);
        c.fill = GridBagConstraints.HORIZONTAL;
        //c.weightx = 0.5;
        c.gridy = 0;
        c.gridx = 0;
        cont.add(startBtn, c);


        stopBtn = new JButton("Stop");
        stopBtn.setToolTipText("Szimuláció befejezése");
        stopBtn.setActionCommand("stop_simulating");
        stopBtn.addActionListener(this);
        stopBtn.setEnabled(false);
        c.gridx = 1;
        c.gridy = 0;
        cont.add(stopBtn, c);

        editBtn = new JButton("Edit");
        editBtn.setToolTipText("Hívások szerkesztése");
        editBtn.setActionCommand("edit_calls");
        editBtn.addActionListener(this);
        editBtn.setEnabled(true);
        c.gridx = 2;
        c.gridy = 0;
        cont.add(editBtn, c);

        //// Timer Label ////
        Font title = new Font("Arial", Font.PLAIN, 72);
        timeLabel = new JLabel();
        timeLabel.setFont(title);
        timeLabel.setText(timer.getNiceFormat());
        c.insets = new Insets(0,100,0,0);  //left padding
        c.gridy = 0;
        c.gridx = 2;
        c.gridwidth = 5;
        cont.add(timeLabel, c);

        //// Titles ////
        aLabel = new JLabel("A");
        aLabel.setFont(title);
        aLabel.setOpaque(true);
        aLabel.setBackground(Color.lightGray);
        c.insets = new Insets(40,0,0,20);
        c.gridwidth = 1;
        c.gridy = 1;
        c.gridx = 1;
        cont.add(aLabel,c);

        bLabel = new JLabel("B");
        bLabel.setFont(title);
        bLabel.setOpaque(true);
        bLabel.setBackground(Color.lightGray);
        c.gridy = 1;
        c.gridx = 2;
        cont.add(bLabel,c);

        cLabel = new JLabel("C");
        cLabel.setFont(title);
        cLabel.setOpaque(true);
        cLabel.setBackground(Color.lightGray);
        c.gridy = 1;
        c.gridx = 3;
        cont.add(cLabel,c);

        dLabel = new JLabel("D");
        dLabel.setFont(title);
        dLabel.setOpaque(true);
        dLabel.setBackground(Color.lightGray);
        c.gridy = 1;
        c.gridx = 4;
        cont.add(dLabel,c);

        //// Attributes ////
        Font attribute = new Font("Arial", Font.BOLD, 18);
        currentLevelLabel = new JLabel("Aktuális emelet");
        currentLevelLabel.setFont(attribute);
        c.insets = new Insets(10, 0, 0, 30);
        c.gridx = 0;
        c.gridy = 3;
        cont.add(currentLevelLabel, c);

        NOPLabel = new JLabel("Utasok száma");
        NOPLabel.setFont(attribute);
        c.gridy = 4;
        cont.add(NOPLabel, c);

        DPLabel = new JLabel("Kiszolgált utasok");
        DPLabel.setFont(attribute);
        c.gridy = 5;
        cont.add(DPLabel, c);

        currentSpeedLabel = new JLabel("Sebesség");
        currentSpeedLabel.setFont(attribute);
        c.gridy = 6;
        cont.add(currentSpeedLabel, c);

        //// Values of the attrubutes ////
        // Current level
        c.gridy = 3;
        ACLLabel = new JLabel("15");
        c.gridx = 1;
        cont.add(ACLLabel, c);

        BCLLabel = new JLabel("1");
        c.gridx = 2;
        cont.add(BCLLabel, c);

        CCLLabel = new JLabel("19");
        c.gridx = 3;
        cont.add(CCLLabel, c);

        DCLLabel = new JLabel("-1");
        c.gridx = 4;
        cont.add(DCLLabel, c);

        //number of passengers
        c.gridy = 4;
        ANOPLabel = new JLabel("0");
        c.gridx = 1;
        cont.add(ANOPLabel,c);

        BNOPLabel = new JLabel("24");
        c.gridx = 2;
        cont.add(BNOPLabel,c);

        CNOPLabel = new JLabel("2");
        c.gridx = 3;
        cont.add(CNOPLabel,c);

        DNOPLabel = new JLabel("0");
        c.gridx = 4;
        cont.add(DNOPLabel,c);
        
        //done passangers
        c.gridy = 5;
        ADPLabel = new JLabel("5");
        c.gridx = 1;
        cont.add(ADPLabel,c);

        BDPLabel = new JLabel("32");
        c.gridx = 2;
        cont.add(BDPLabel,c);

        CDPLabel = new JLabel("12");
        c.gridx = 3;
        cont.add(CDPLabel,c);

        DDPLabel = new JLabel("10");
        c.gridx = 4;
        cont.add(DDPLabel,c);
        
        //speed
        c.gridy = 6;
        ACSLabel = new JLabel("0");
        c.gridx = 1;
        cont.add(ACSLabel,c);

        BCSLabel = new JLabel("2.001");
        c.gridx = 2;
        cont.add(BCSLabel,c);

        CCSLabel = new JLabel("1.23");
        c.gridx = 3;
        cont.add(CCSLabel,c);

        DCSLabel = new JLabel("0");
        c.gridx = 4;
        cont.add(DCSLabel,c);


        //// Log text panel ////
        logPane = new JTextArea();
        logPane.setEditable(false);
        logPane.setText("Helló! Itt fognak megjelenni a részletes információk az egyes hívásokról\n");

        JScrollPane logScrollPane = new JScrollPane(logPane);
        logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        logScrollPane.setPreferredSize(new Dimension(250, 180));
        logScrollPane.setMinimumSize(new Dimension(10, 10));
        c.gridy = 7;
        c.gridx = 0;
        c.gridwidth = 7;
        c.insets = new Insets(40,0,0,0);
        cont.add(logScrollPane, c);

    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Simulation.class.getResource(path);
        //error handling omitted for clarity...
        return new ImageIcon(imgURL);
    }

    private void refresh(){
        timeLabel.setText(timer.getNiceFormat());
    }


    @Override
    public void run() {
        int sleepTime;
        while(true){
            timer.addSecond();
            sleepTime = (int) (playSpeed*1000);
            try{
                sleep(sleepTime);
                refresh();
            } catch (InterruptedException ex){

            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if("start_simulating".equals(e.getActionCommand())){
            //System.out.println("szimuláció elindítva");
            logPane.append("Szimuláció elindítva\n");
            simulating = true;
            startBtn.setEnabled(false);
            stopBtn.setEnabled(true);
            editBtn.setEnabled(false);
            timer.setTime(0,0,0);
            refresh();
            thread.start();
        } else if("stop_simulating".equals(e.getActionCommand())){
            simulating = false;
            //System.out.println("szimuláció leállítva");
            logPane.append("Szimuláció leállítva " + timer.getNiceFormat() + "\n");
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            editBtn.setEnabled(true);
            thread.stop();
            thread = new Thread(this);
        } else if( "edit_calls".equals(e.getActionCommand())){
            try {
                CallsController call = new CallsController();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }
}