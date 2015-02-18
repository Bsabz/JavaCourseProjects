package crapsgui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CrapsGUI extends JFrame {
int score = 50;
int rounds = 0;
boolean pass = true;
int bet = 0;
int dice1;
int dice2;
int point;
int phase=0; // 0-comeout; 1-point;
JPanel p0 = new JPanel();
JPanel p1 = new JPanel();
JPanel p2 = new JPanel();
JPanel p3 = new JPanel();
JLabel l11 = new JLabel("\"Pass\" line or the \"Don't Pass\" line: ");
JButton jbt01 = new JButton("\"Pass\" line");
JButton jbt02 = new JButton("\"Don't pass\" line");
JLabel l1 = new JLabel("Bet: ");
JButton jbt1 = new JButton("$1");
JButton jbt2 = new JButton("$2");
JButton jbt3 = new JButton("$3");
JButton jbt4 = new JButton("$4");
JButton jbt5 = new JButton("$5");
ImageIcon icon1 = new ImageIcon("images/1.jpg");
ImageIcon icon2 = new ImageIcon("images/2.jpg");
JLabel l2 = new JLabel("dice1");
JLabel l3 = new JLabel("dice2");
JLabel l4 = new JLabel("Come-out");
JLabel l5 = new JLabel("Score:50"); 
JButton jbtNext = new JButton("Next");
// Constructor
CrapsGUI(){
rounds++;
dice1 = (int)(Math.random()*6+1);
dice2 = (int)(Math.random()*6+1); 
//GUI
setSize(600, 500);
setLayout(new GridLayout(4,1));
// p0
p0.add(l11);
jbt01.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) { refreshDisplay("jbt01"); } } );
jbt02.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) { refreshDisplay("jbt02"); } } );
p0.add(jbt01); p0.add(jbt02);
add(p0);
// p1
l1.setText("Come-out phase. Bet: ");
p1.add(l1);
jbt1.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) { refreshDisplay("jbt1"); } } );
jbt2.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) { refreshDisplay("jbt2"); } } );
jbt3.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) { refreshDisplay("jbt3"); } } );
jbt4.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) { refreshDisplay("jbt4"); } } );
jbt5.addActionListener(new ActionListener() {public void actionPerformed(ActionEvent e) { refreshDisplay("jbt5"); } } );
p1.add(jbt1); p1.add(jbt2); p1.add(jbt3); p1.add(jbt4); p1.add(jbt5); 
add(p1);
// p2
p2.add(l4);
l2.setIcon(card_to_ImageIcon(dice1));
p2.add(l2);
l3.setIcon(card_to_ImageIcon(dice2));
p2.add(l3);
add(p2);
// p3
p3.add(l5);
p3.add(jbtNext);
add(p3);
setVisible(true);
repaint();
}

public static void main(String[] args) throws Exception {
CrapsGUI cg = new CrapsGUI();
cg.setVisible(true);
}
public void refreshDisplay(String source){
if(rounds>9) System.exit(0);
rounds++;
//GUI
if(source.equals("jbt01")){
pass = true;
l11.setText("\"Pass\" line!");
}else if(source.equals("jbt02")){
pass = false; 
l11.setText("\"Don't Pass\" line!");
} // implement all the other buttons
repaint();
}
public static ImageIcon card_to_ImageIcon(int c){
String fileString = "images/"+c+".jpg";
return new ImageIcon(fileString);
}
}