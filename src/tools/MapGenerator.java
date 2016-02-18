package tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MapGenerator extends JFrame{
	JTextArea pathTextBox = new JTextArea("content/level.png");
	JButton generateButton = new JButton("Generate");
	JLabel imageLabel = new JLabel();
	BufferedImage img;
	JTextField jsonTextField = new JTextField();
	
	public MapGenerator(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 640);
		setLocation(640, 300);
		init();
	}
	
	private void init(){
		add(new JLabel("Path to level image"));
		add(pathTextBox);
		add(generateButton);
		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// do stuff
			}
		});
		add(imageLabel);
		add(jsonTextField);
	}
}
