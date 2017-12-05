package nhf;


import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import static java.lang.Thread.sleep;

public class Simulation extends JFrame implements Runnable, ActionListener{
    boolean simulating;
    private Container cont;
    private Timer timer;
    GridBagConstraints c;
    private double playSpeed;
    private Thread thread;

    private JLabel timeLabel, aLabel, bLabel, cLabel, dLabel;
    private JLabel currentLevelLabel, currentSpeedLabel, NOPLabel, DPLabel;
    private JTextArea logPane;
    //Number Of Passengers; Done Passengers
    private JButton startBtn, stopBtn;


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


        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 72));
        timeLabel.setText(timer.getNiceFormat());
        c.insets = new Insets(0,100,0,0);  //left padding
        c.gridy = 0;
        c.gridx = 2;
        c.gridwidth = 5;
        cont.add(timeLabel, c);

        aLabel = new JLabel("A");
        aLabel.setFont(new Font("Arial", Font.BOLD, 72));
        aLabel.setOpaque(true);
        aLabel.setBackground(Color.lightGray);
        c.insets = new Insets(0,0,0,20);
        c.gridwidth = 1;
        c.gridy = 1;
        c.gridx = 1;
        cont.add(aLabel,c);

        bLabel = new JLabel("B");
        bLabel.setFont(new Font("Arial", Font.BOLD, 72));
        bLabel.setOpaque(true);
        bLabel.setBackground(Color.lightGray);
        c.gridy = 1;
        c.gridx = 2;
        cont.add(bLabel,c);

        cLabel = new JLabel("C");
        cLabel.setFont(new Font("Arial", Font.BOLD, 72));
        cLabel.setOpaque(true);
        cLabel.setBackground(Color.lightGray);
        c.gridy = 1;
        c.gridx = 3;
        cont.add(cLabel,c);

        dLabel = new JLabel("D");
        dLabel.setFont(new Font("Arial", Font.BOLD, 72));
        dLabel.setOpaque(true);
        dLabel.setBackground(Color.lightGray);
        c.gridy = 1;
        c.gridx = 4;
        cont.add(dLabel,c);

        currentLevelLabel = new JLabel("Aktuális emelet");
        c.insets = new Insets(10, 0, 0, 30);
        c.gridx = 0;
        c.gridy = 3;
        cont.add(currentLevelLabel, c);

        NOPLabel = new JLabel("Utasok száma");
        c.gridy = 4;
        cont.add(NOPLabel, c);

        DPLabel = new JLabel("Kiszolgált utasok");
        c.gridy = 5;
        cont.add(DPLabel, c);

        currentSpeedLabel = new JLabel("Sebesség");
        c.gridy = 6;
        cont.add(currentSpeedLabel, c);



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
            timer.setTime(0,0,0);
            refresh();
            thread.start();
        } else if("stop_simulating".equals(e.getActionCommand())){
            simulating = false;
            //System.out.println("szimuláció leállítva");
            logPane.append("Szimuláció leállítva " + timer.getNiceFormat() + "\n");
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            thread.stop();
            thread = new Thread(this);
        }
    }
}
