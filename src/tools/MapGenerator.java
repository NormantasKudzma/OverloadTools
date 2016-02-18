package tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONObject;

public class MapGenerator extends JFrame{
	JPanel panel = new JPanel();
	JTextField pathTextBox = new JTextField(System.getProperty("user.dir") + "\\content\\level.png");
	JButton generateButton = new JButton("Generate");
	JLabel imageLabel = new JLabel();
	BufferedImage img;
	JTextArea jsonTextBox = new JTextArea();
	
	public MapGenerator(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 640);
		setLocation(200, 100);
		setTitle("MapGenerator");
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		init();
	}
	
	private void init(){
		add(panel);
		JPanel top = new JPanel();
		top.add(new JLabel("Path to level image"));
		top.add(pathTextBox);
		top.add(generateButton);
		panel.add(top);
		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generateJson();
			}
		});
		panel.add(imageLabel);
		JScrollPane scroll = new JScrollPane(jsonTextBox);
		panel.add(scroll);
	}
	
	private void generateJson(){
		try
		{
			img = ImageIO.read(new File(pathTextBox.getText()));
			int w = img.getWidth();
			int h = img.getHeight();
			int[] pixels = new int[w * h];
			img.getRGB(0, 0, w, h, pixels, 0, w);

			JSONArray scale = new JSONArray();
			scale.put(1.0);
			scale.put(1.0);
			StringBuilder string = new StringBuilder();
			int white = 0xffffffff;
			int color = 0;
			
			for (int i = 0; i < h; i++){
				for (int j = 0; j < w; j++){
					color = pixels[i * w + j];
					if (color != white){
						JSONObject entity = new JSONObject();
						entity.put("entity", "" + color);
						entity.put("scale", scale);
						JSONArray position = new JSONArray();
						position.put(j + 0.5f);
						position.put(i + 0.5f);
						entity.put("position", position);
						string.append(entity.toString() + ",\n");
					}
				}
			}
			string.deleteCharAt(string.length() - 1);
			string.deleteCharAt(string.length() - 1);
			
			jsonTextBox.setText(string.toString());
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
