//DESCRIPTION: Start panel. Can show instructions + about + create main game. 
//CODE COMMENTED OUT: NOT FUNCTIONING / NOT IMPLEMENTED

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class Start extends JPanel implements ActionListener {
	static JFrame frame;
	static JPanel myPanel;

	static JPanel display;
	JButton instructions;
	JButton about;
	JButton play;
	JButton score;

	GamePanel game;
	static Start begin;

	// NOT IMPLEMENTED:
	// HashMap <String, Score> scores = new HashMap <String, Score> ();
	// Score class would have had String = name, Integer = score;

	public Start() {
		reinitialize();
	}

	// constructs jpanel with jbuttons + field
	// if game was fully functional, would be called repeatedly
	public void reinitialize() {
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(400, 400));
		frame.setLocation(200, 200);

		myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(4, 4));

		instructions = new JButton("Instructions");
		this.add(instructions);
		instructions.addActionListener(this);
		instructions.setActionCommand("instructions");

		about = new JButton("About");
		this.add(about);
		about.addActionListener(this);
		about.setActionCommand("about");

		play = new JButton("Play");
		this.add(play);
		play.addActionListener(this);
		play.setActionCommand("play");

//				score = new JButton("High Scores");
//				this.add(score);
//				score.addActionListener(this);
//				score.setActionCommand("score");

		frame.add(this);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) throws IOException {
		begin = new Start();
		frame.pack();
		frame.setVisible(true);

	}

	// displays information/buttons + starts game.
	@Override
	public void actionPerformed(ActionEvent e) {
		String eventName = e.getActionCommand();

		if (eventName.equals("play")) {
			frame.removeAll();
			frame.setVisible(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			game = new GamePanel();

		} else if (eventName.equals("instructions")) {
			JOptionPane.showMessageDialog(frame,
					"Use keyboard arrows to move. Jump up to hit bricks and raise your score. Watch out for enemies...");

		} else if (eventName.equals("about")) {
			JOptionPane.showMessageDialog(frame,
					"Name: Catherine, \nDate: 1/27/2021 \nGAME IS NONFUNTIONAL: cannot die, cannot restart, no scores shown ");

//		} else if (eventName.equals("score")) {
//			if (scores.isEmpty())
//				JOptionPane.showMessageDialog(frame, "No score avalible.");
//			else {
//				sortScore();
//				//PSEUDOCODE: use a string to order the scores neatly, joptionpane to show the string
//			}

		}
	}
	
	// NOT IMPLEMENTED
//	public void addScore (String name, int score) {
//		scores.add(name, score);
//		reinitialize();
//	}

//public void sortScore (){
//	Collection<Score>list = score.values();
//	TreeSet <Score> tree = new TreeSet <Score> (list);
	// create a comparator that treeSet default uses to sort by values
//}

}
