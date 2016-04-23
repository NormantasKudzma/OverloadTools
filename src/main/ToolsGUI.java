package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import tools.FontToPng;
import tools.MapGenerator;

public class ToolsGUI extends JFrame{
	JPanel panel = new JPanel();
	
	public ToolsGUI(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 640);
		setLocation(200, 100);
		setTitle("OverloadTools main");
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		init();
	}
	
	private void init(){
		add(panel);
		
		JButton mapGeneratorButton = new JButton("Map generator");
		mapGeneratorButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				(new MapGenerator()).setVisible(true);
			}
		});
		panel.add(mapGeneratorButton);
	
		JButton fontToPngButton = new JButton("Font to png");
		fontToPngButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				(new FontToPng()).setVisible(true);
			}
		});
		panel.add(fontToPngButton);
	}
}
