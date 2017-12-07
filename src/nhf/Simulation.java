package nhf;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;

import static java.lang.Thread.sleep;

/**
 * A szimuláció megjelenéséért, indításáért felelős osztály.
 * Az egyik leggányabb, amit valaha láttam, azonban ezt a layoutmanager okozza (nagyrészt) :)
 */
public class Simulation extends JFrame implements Runnable, ActionListener {
    private boolean simulating;
    private Container cont;
    private Timer timer;
    GridBagConstraints c;
    public int playSpeed;
    private Thread thread, aThread, bThread, cThread, dThread;
    private Color free, busy;

    private JLabel timeLabel, aLabel, bLabel, cLabel, dLabel; //elevators title
    private JLabel currentLevelLabel, currentSpeedLabel, NOPLabel, DPLabel;//Number Of Passengers; Done Passengers
    private JLabel ACLLabel, BCLLabel, CCLLabel, DCLLabel;//Current Level Labels
    private JLabel ACSLabel, BCSLabel, CCSLabel, DCSLabel;//Current Speed Labels
    private JLabel ANOPLabel, BNOPLabel, CNOPLabel, DNOPLabel; //NOP Labels
    private JLabel ADPLabel, BDPLabel, CDPLabel, DDPLabel; //DP labels
    private JTextArea logPane;
    private JButton startBtn, stopBtn, editBtn;

    private Elevator aElevator, bElevator, cElevator, dElevator;
    private ElevatorController controller;

    private ArrayList<Call> calls;


    public Simulation(String title) {
        super(title);

        //set layout manager
        setLayout(new GridBagLayout());//--> wrong choice
        c = new GridBagConstraints();

        //set the variables
        cont = getContentPane();
        timer = new Timer(0, 0, 0);
        playSpeed = 1;//default speed

        free = new Color(140, 217, 140);
        busy = new Color(255, 128, 128);

        thread = new Thread(this);

        addComponents();

        //construct the elevators
        aElevator = new Elevator("A", 15, 0, 18, 0.6, logPane);
        bElevator = new Elevator("B", 15, -1, 18, 0.6, logPane);
        cElevator = new Elevator("C", 20, -1, 18, 0.6, logPane);
        dElevator = new Elevator("D", 20, -1, 18, 0.5, logPane);

        //add them to ElevatorController
        controller = new ElevatorController(logPane);
        controller.addNewElevator(aElevator);
        controller.addNewElevator(bElevator);
        controller.addNewElevator(cElevator);
        controller.addNewElevator(dElevator);

        refresh();
    }

    private void initCalls() throws IOException, ClassNotFoundException {
        File inputFile = new File("calls.dat");
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFile));
        calls = (ArrayList<Call>) ois.readObject();
        ois.close();
    }

    private void addComponents() {

        createMenu();

        //// Start and Stop button ////
        startBtn = new JButton("Start");
        startBtn.setToolTipText("Szimuláció elindítása");
        startBtn.setActionCommand("start_simulating");
        startBtn.addActionListener(this);
        startBtn.setEnabled(true);
        c.fill = GridBagConstraints.HORIZONTAL;
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
        c.insets = new Insets(0, 100, 0, 0);  //left padding
        c.gridy = 0;
        c.gridx = 2;
        c.gridwidth = 5;
        cont.add(timeLabel, c);

        //// Titles ////
        aLabel = new JLabel("A");
        aLabel.setFont(title);
        aLabel.setOpaque(true);
        c.insets = new Insets(40, 0, 0, 20);
        c.gridwidth = 1;
        c.gridy = 1;
        c.gridx = 1;
        cont.add(aLabel, c);

        bLabel = new JLabel("B");
        bLabel.setFont(title);
        bLabel.setOpaque(true);
        c.gridy = 1;
        c.gridx = 2;
        cont.add(bLabel, c);

        cLabel = new JLabel("C");
        cLabel.setFont(title);
        cLabel.setOpaque(true);
        c.gridy = 1;
        c.gridx = 3;
        cont.add(cLabel, c);

        dLabel = new JLabel("D");
        dLabel.setFont(title);
        dLabel.setOpaque(true);
        c.gridy = 1;
        c.gridx = 4;
        cont.add(dLabel, c);

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
        ACLLabel = new JLabel();
        c.gridx = 1;
        cont.add(ACLLabel, c);

        BCLLabel = new JLabel();
        c.gridx = 2;
        cont.add(BCLLabel, c);

        CCLLabel = new JLabel();
        c.gridx = 3;
        cont.add(CCLLabel, c);

        DCLLabel = new JLabel();
        c.gridx = 4;
        cont.add(DCLLabel, c);

        //number of passengers
        c.gridy = 4;
        ANOPLabel = new JLabel();
        c.gridx = 1;
        cont.add(ANOPLabel, c);

        BNOPLabel = new JLabel();
        c.gridx = 2;
        cont.add(BNOPLabel, c);

        CNOPLabel = new JLabel();
        c.gridx = 3;
        cont.add(CNOPLabel, c);

        DNOPLabel = new JLabel();
        c.gridx = 4;
        cont.add(DNOPLabel, c);

        //done passengers
        c.gridy = 5;
        ADPLabel = new JLabel();
        c.gridx = 1;
        cont.add(ADPLabel, c);

        BDPLabel = new JLabel();
        c.gridx = 2;
        cont.add(BDPLabel, c);

        CDPLabel = new JLabel();
        c.gridx = 3;
        cont.add(CDPLabel, c);

        DDPLabel = new JLabel();
        c.gridx = 4;
        cont.add(DDPLabel, c);

        //speed
        c.gridy = 6;
        ACSLabel = new JLabel();
        c.gridx = 1;
        cont.add(ACSLabel, c);

        BCSLabel = new JLabel();
        c.gridx = 2;
        cont.add(BCSLabel, c);

        CCSLabel = new JLabel();
        c.gridx = 3;
        cont.add(CCSLabel, c);

        DCSLabel = new JLabel();
        c.gridx = 4;
        cont.add(DCSLabel, c);


        //// Log text panel ////
        logPane = new JTextArea();
        logPane.setEditable(false);

        JScrollPane logScrollPane = new JScrollPane(logPane);
        logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        logScrollPane.setPreferredSize(new Dimension(250, 180));
        logScrollPane.setMinimumSize(new Dimension(10, 10));
        c.gridy = 7;
        c.gridx = 0;
        c.gridwidth = 7;
        c.insets = new Insets(40, 0, 0, 0);
        cont.add(logScrollPane, c);

        //// Speed slider ////
        JSlider speedSlider = new JSlider(JSlider.VERTICAL, 1, 30, 1);
        //Turn on labels at major tick marks.
        speedSlider.setMajorTickSpacing(3);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setFont(new Font("Arial", Font.PLAIN, 15));
        speedSlider.setMinimumSize(new Dimension(50, 300));

        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                playSpeed = source.getValue();
                controller.setSimulateSpeed(playSpeed);
                System.out.println("New play speed --> " + playSpeed);
            }
        });

        c.gridx = 5;
        c.gridy = 1;
        c.gridheight = 6;
        cont.add(speedSlider, c);

    }

    private void createMenu() {
        JMenuBar menubar = new JMenuBar();

        JMenu menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_M);

        JMenuItem editMenuItem = new JMenuItem("Hívások");
        editMenuItem.setMnemonic(KeyEvent.VK_H);
        editMenuItem.setToolTipText("Hívások szerkesztése");
        editMenuItem.setActionCommand("edit_calls");
        editMenuItem.addActionListener(this);

        JMenuItem eMenuItem = new JMenuItem("Bezár");
        eMenuItem.setMnemonic(KeyEvent.VK_B);
        eMenuItem.setToolTipText("Alkalmazás bezárása");
        eMenuItem.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });

        menu.add(editMenuItem);
        menu.add(eMenuItem);

        menubar.add(menu);

        setJMenuBar(menubar);
    }

    /**
     * Újra kiírja a szimulációban részt vevő lifteknek az aktuális állapotát.
     * Nem vár paramétert, és nem is ad vissza semmit.
     */
    private void refresh() {

        timeLabel.setText(timer.getNiceFormat());
        if (aElevator.status == Elevator.Status.WAIT_FOR_CALL) {
            aLabel.setBackground(free);
        } else {
            aLabel.setBackground(busy);
        }
        if (bElevator.status == Elevator.Status.WAIT_FOR_CALL) {
            bLabel.setBackground(free);
        } else {
            bLabel.setBackground(busy);
        }
        if (cElevator.status == Elevator.Status.WAIT_FOR_CALL) {
            cLabel.setBackground(free);
        } else {
            cLabel.setBackground(busy);
        }
        if (dElevator.status == Elevator.Status.WAIT_FOR_CALL) {
            dLabel.setBackground(free);
        } else {
            dLabel.setBackground(busy);
        }

        ACLLabel.setText(String.valueOf(aElevator.actualFloor));
        BCLLabel.setText(String.valueOf(bElevator.actualFloor));
        CCLLabel.setText(String.valueOf(cElevator.actualFloor));
        DCLLabel.setText(String.valueOf(dElevator.actualFloor));

        DecimalFormat format = new DecimalFormat("##0.00");
        ACSLabel.setText(String.valueOf(format.format(aElevator.actualSpeed)));
        BCSLabel.setText(String.valueOf(format.format(bElevator.actualSpeed)));
        CCSLabel.setText(String.valueOf(format.format(cElevator.actualSpeed)));
        DCSLabel.setText(String.valueOf(format.format(dElevator.actualSpeed)));

        ANOPLabel.setText(String.valueOf(aElevator.actualPassengers));
        BNOPLabel.setText(String.valueOf(bElevator.actualPassengers));
        CNOPLabel.setText(String.valueOf(cElevator.actualPassengers));
        DNOPLabel.setText(String.valueOf(dElevator.actualPassengers));

        ADPLabel.setText(String.valueOf(aElevator.donePassengers));
        BDPLabel.setText(String.valueOf(bElevator.donePassengers));
        CDPLabel.setText(String.valueOf(cElevator.donePassengers));
        DDPLabel.setText(String.valueOf(dElevator.donePassengers));
    }


    @Override
    public void run() {
        cleanElevatorsAttribute();

        //start elevators' threads
        aThread.start();
        bThread.start();
        cThread.start();
        dThread.start();

        try {
            initCalls(); //read the calls from file
            logPane.append("Hívások beolvasva (" + calls.size() + "db)\n");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        calls.sort(new Comparator<Call>() {
            @Override
            public int compare(Call o1, Call o2) {
                if (o1.timer.hh != o2.timer.hh) {
                    return o1.timer.hh - o2.timer.hh;
                } else {
                    if (o1.timer.mm != o2.timer.mm) {
                        return o1.timer.mm - o2.timer.mm;
                    } else {
                        if (o1.timer.ss != o2.timer.ss) {
                            return o1.timer.ss - o2.timer.ss;
                        } else {
                            return 0;
                        }
                    }
                }
            }
        });


        int sleepTime;
        int doneCalls = 0;
        Call call = null;

        while (simulating) {
            if (doneCalls == calls.size()) {
//                TODO stop the simulation
            } else {
                do {
                    call = calls.get(doneCalls);
                    if (call.timer.equals(timer)) {
                        System.out.println(timer.getNiceFormat() + " - call:" + call.from + " - " + call.to + " - " + call.timer.getNiceFormat());
                        controller.addNewCall(call);
                        doneCalls++;
                    }
                    if (doneCalls == calls.size()) {
                        break;
                    }
                } while (call.timer.equals(calls.get(doneCalls)));
            }

            sleepTime = Math.round(1000 / playSpeed);
            refresh();
            timer.addSecond();
            try {
                sleep(sleepTime);
            } catch (InterruptedException ex) {

            }
        }
    }

    private void cleanElevatorsAttribute() {
        aElevator.cleanAttributes();
        bElevator.cleanAttributes();
        cElevator.cleanAttributes();
        dElevator.cleanAttributes();
    }


    public void actionPerformed(ActionEvent e) {
        if ("start_simulating".equals(e.getActionCommand())) {
            simulating = true;
            startBtn.setEnabled(false);
            stopBtn.setEnabled(true);
            editBtn.setEnabled(false);

            timer.setTime(0, 0, 0);
            refresh();

            thread = new Thread(this);
            aThread = new Thread(aElevator);
            bThread = new Thread(bElevator);
            cThread = new Thread(cElevator);
            dThread = new Thread(dElevator);
            thread.start();

            logPane.append("Szimuláció elindítva (" + playSpeed + "x)\n");
        } else if ("stop_simulating".equals(e.getActionCommand())) {
            simulating = false;
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            editBtn.setEnabled(true);

            thread.stop();
            aThread.stop();
            bThread.stop();
            cThread.stop();
            dThread.stop();
            logPane.append("Szimuláció leállítva (" + timer.getNiceFormat() + ")\n");
        } else if ("edit_calls".equals(e.getActionCommand())) {
            try {
                CallsController call = new CallsController();
            } catch (IOException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }

}
