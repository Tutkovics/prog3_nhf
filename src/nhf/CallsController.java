package nhf;

import java.io.*;

import java.util.TreeSet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public class CallsController extends JFrame {
    public static TreeSet<Call> proba = new TreeSet<>();

    public CallsController() throws IOException, ClassNotFoundException {
        /*Timer ti = new Timer(0,12,35);
        Timer ti2 = new Timer(1,12,35);
        Timer ti3 = new Timer(2,12,35);
        Timer ti4 = new Timer(3,12,35);
        Call c1 = new Call(1,2, ti);
        Call c2 = new Call(10,9, ti2);
        Call c3 = new Call(3,-1, ti3);
        Call c4 = new Call(1,-100, ti4);
        proba.add(c1);
        proba.add(c2);
        proba.add(c3);
        proba.add(c4);
        c2.s = Call.Status.GET_IN;
        c1.s = Call.Status.DONE;*/
        //save("kuki");
        backUp("kuki");

        String[] columns = new String[]{
                /*"Id", */"From", "To", "Hour", "Minute", "Second", "Delete"
        };

        //JTable table = new JTable(data, columns);
        JTable table = new JTable(new AbstractTableModel() {
            public String getColumnName(int col) {
                return columns[col].toString();
            }

            public int getRowCount() {
                fireTableDataChanged();//refresh the table, redraw after changeing
                return proba.size();
            }

            public int getColumnCount() {
                return columns.length;
            }

            public Object getValueAt(int row, int col) {
                //Return the value of Treeset[row][col]
                Object o = null;
                col++;//hide id
                int i = 0;
                for (Call c : proba) {
                    switch (col) {
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
                        case 6:
                            //final JButton btn = new JButton("Del");
                            //return btn;
                            o = "X";
                            break;
                    }
                    if (i == row) break;
                    i++;
                }
                return o;
            }

            public boolean isCellEditable(int row, int col) {
                /* col = from argument --> editable
                if (col == 0) {
                    return false;
                } else {
                    return true;
                }*/
                return true;
            }

            public void setValueAt(Object value, int row, int col) {
                //Set the value: TreeSet[row][int] = value;
                col++;//hide id
                int i = 0;
                for (Call c : proba) {
                    if (i == row) {
                        switch (col) {
                            case 0:
                                c.id = (int) value;
                                break;
                            case 1:
                                c.from = Integer.parseInt((String) value);
                                break;
                            case 2:
                                c.to = Integer.parseInt((String) value);
                                break;
                            case 3:
                                c.timer.hh = Integer.parseInt((String) value);
                                break;
                            case 4:
                                c.timer.mm = Integer.parseInt((String) value);
                                break;
                            case 5:
                                c.timer.ss = Integer.parseInt((String) value);
                                break;
                        }
                        break;
                    }
                    i++;
                }

                try {
                    listCalls();
                    save("kuki");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        table.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        table.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox()));

        this.setLayout(new BorderLayout());
        //add the table to the frame
        this.add(new JScrollPane(table), BorderLayout.EAST);

        JButton btn = new JButton("Új hívás");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Új hozzáadása");
                Timer ti = new Timer(
                        Integer.parseInt(JOptionPane.showInputDialog("Hour: ")),
                        Integer.parseInt(JOptionPane.showInputDialog("Minute: ")),
                        Integer.parseInt(JOptionPane.showInputDialog("Second: "))
                );
                Call call = new Call(
                        Integer.parseInt(JOptionPane.showInputDialog("From: ")),
                        Integer.parseInt(JOptionPane.showInputDialog("To: ")),
                        ti
                );
                //proba.add(new Call(0, 0, new Timer(0, 0, 0)));
                proba.add(call);
                table.getRowCount();
                //fireTableChanged(null);
                try {
                    save("kuki");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });


        this.add(btn, BorderLayout.NORTH);

        this.setTitle("Hívások szerkesztése");
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
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

    public static void listCalls() {
        for (Call c : proba) {
            System.out.println("ID: " + c.id + " From: " + c.from + " To: " + c.to + " Status: " + c.s +
                    " Timer: " + c.timer.hh + ":" + c.timer.mm + ":" + c.timer.ss);
        }
    }

    public static void main(String[] args) {
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

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;

        private String label;
        private int row;

        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            this.row = row;
            button.setText(label);
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                Call call = null;
                int i = 0;
                for(Call c: proba){
                    if(i == row){
                        call = c;
                        break;
                    }
                    i++;
                }
                proba.remove(call);
                JOptionPane.showMessageDialog(button, "Successfully deleted!");

                // System.out.println(label + ": Ouch!");
            }
            isPushed = false;
            return new String(label);
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    /**
     * Copy from: http://www.java2s.com/Code/Java/Swing-Components/ButtonTableExample.htm
     */
}