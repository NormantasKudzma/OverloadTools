package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import tools.MapGenerator;

public class ToolsGUI extends JFrame{
	public ToolsGUI(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 640);
		setLocation(640, 300);
		init();
	}
	
	private void init(){
		JButton mapGeneratorButton = new JButton("Map generator");
		mapGeneratorButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				(new MapGenerator()).setVisible(true);
			}
		});
		add(mapGeneratorButton);
	}
}
