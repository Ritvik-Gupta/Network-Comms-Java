package src;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/* Class extending Jframe */
public class Train extends JFrame {
   String onOffValue = "OFF";
   String dOpenValue = "CLOSED";
   String tMovingValue = "NO";
   String tReadyValue = "NO";
   String trainInfoContent = "AH HERROOOO!!";
   int stationNo = 0;

   /* Constuctor method for the object */
   Train() {
      /* setting the frame title */
      super("Train Control System");
      /* calling buildGUI to set up the interface */
      buildGUI();

      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      pack();
      setVisible(true);
   }

   private void buildGUI() {
      JPanel station = new JPanel();
      station.setLayout(new BorderLayout());
      JPanel graphic = new JPanel();
      Graphic g = new Graphic(stationNo);
      graphic.add(g);
      station.add(graphic, BorderLayout.NORTH);
      station.setBorder(new TitledBorder(new EtchedBorder(), " Station     Information "));
      add(station, BorderLayout.NORTH);

      JPanel trains = new JPanel();
      trains.setLayout(new BorderLayout());
      JTextArea trainInfo = new JTextArea(trainInfoContent, 3, 70);
      trains.add(trainInfo);
      trains.setBorder(new TitledBorder(new EtchedBorder(), " Train Information "));
      add(trains, BorderLayout.CENTER);

      JPanel controls = new JPanel();

      controls.setLayout(new BorderLayout());
      JPanel buttons = new JPanel();
      buttons.setLayout(new GridLayout(3, 2));
      buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
      Button start = new Button("Start Train");
      buttons.add(start);
      update test = new update(trainInfo, stationNo, g);
      start.addActionListener(test);
      Button open = new Button("Open Doors");
      buttons.add(open);
      Button close = new Button("Close Doors");
      buttons.add(close);
      Button go = new Button("Go");
      buttons.add(go);
      Button stop = new Button("Stop");
      buttons.add(stop);
      Button shutdown = new Button("Shutdown Train");
      buttons.add(shutdown);
      controls.add(buttons, BorderLayout.WEST);

      JPanel display = new JPanel();
      display.setLayout(new GridLayout(2, 4));

      JTextField onOffTitle = new JTextField("Train on: ");
      onOffTitle.setEditable(false);
      display.add(onOffTitle);
      JTextField onOff = new JTextField(onOffValue);
      onOff.setEditable(false);
      display.add(onOff);

      JTextField dOpenTitle = new JTextField("Doors Open: ");
      dOpenTitle.setEditable(false);
      display.add(dOpenTitle);
      JTextField dOpen = new JTextField(dOpenValue);
      dOpen.setEditable(false);
      display.add(dOpen);

      JTextField tMovingTitle = new JTextField("Train Moving: ");
      tMovingTitle.setEditable(false);
      display.add(tMovingTitle);
      JTextField tMoving = new JTextField(tMovingValue);
      tMoving.setEditable(false);
      display.add(tMoving);

      JTextField tReadyTitle = new JTextField("Train Ready: ");
      tReadyTitle.setEditable(false);
      display.add(tReadyTitle);
      JTextField tReady = new JTextField(tReadyValue);
      tReady.setEditable(false);
      display.add(tReady);

      controls.add(display, BorderLayout.CENTER);
      add(controls, BorderLayout.SOUTH);
   }

   /* Main method declaring a new train control system object */
   public static void main(String[] args) {
      Train TCS = new Train();
   }
}

class Graphic extends JPanel {

   int station;
   boolean isFirstCall;

   Graphic(int s) {
      this.station = s;
      this.isFirstCall = true;
   }

   public void newStation(int s) {
      station = s;
      isFirstCall = false;
   }

   @Override
   public void paint(Graphics g) {
      super.paint(g);
      Graphics2D g2 = (Graphics2D) g;

      g2.drawString("Darlington", 1, 36);
      g2.drawString("North Road", 140, 36);
      g2.drawString("Heighington", 280, 36);
      g2.drawString("Shildon", 436, 36);
      g2.drawString("Newton Aycliffe", 560, 36);
      g2.drawString("Bishop Auckland", 698, 36);

      g2.setStroke(new BasicStroke(5));
      g2.drawLine(25, 11, 742, 11);
      g2.setColor(Color.gray);
      g2.drawLine(25, 11, 25 + (station * 143), 11);

      g2.setStroke(new BasicStroke(9));
      int x = 23;
      for (int i = 0; i < 6; i++) {
         g2.drawOval(x, 7, 10, 10);
         x = x + 143;
      }
      g2.setColor(Color.black);
      x = 23 + (station * 143);
      for (int i = 0; i < 6; i++) {
         g2.drawOval(x, 7, 10, 10);
         x = x + 143;
      }

      g2.setColor(Color.white);
      x = 23;
      for (int i = 0; i < 6; i++) {
         g2.fillOval(x, 7, 10, 10);
         x = x + 143;
      }
      g2.setColor(Color.red);
      g2.fillOval(23 + (143 * station), 7, 10, 10);
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(800, 40);
   }
}

class update implements ActionListener {
   JTextArea output;
   int stationNo;
   Graphic graphic;

   update(JTextArea out, int station, Graphic p) {
      output = out;
      stationNo = station;
      graphic = p;
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      output.append("Start Train");
      stationNo = stationNo + 1;
      graphic.newStation(stationNo);
      graphic.repaint();
      output.append(" " + stationNo);
   }
}
