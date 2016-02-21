package tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONWriter;

public class MapGenerator extends JFrame{
	JPanel panel = new JPanel();
	JTextField pathTextBox = new JTextField(System.getProperty("user.dir") + "\\content\\level.png");
	JTextField offsetTextBox = new JTextField("0.5", 4);
	JTextField scaleTextBox = new JTextField("1", 4);
	JButton generateMapButton = new JButton("Generate map");
	JButton generateCollidersButton = new JButton("Generate colliders");
	JLabel imageLabel = new JLabel();
	BufferedImage img;
	JTextArea jsonTextBox = new JTextArea();
	HashMap<Integer, String> colorNames = new HashMap<Integer, String>();
	
	public MapGenerator(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 640);
		setLocation(200, 100);
		setTitle("MapGenerator");
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		init();
		fillColorNames();
	}
	
	private void fillColorNames() {
		colorNames.put(0xffff0000, "Red");
		colorNames.put(0xff00ff00, "Green");
		colorNames.put(0xff0000ff, "Blue");
		colorNames.put(0xffffffff, "White");
		colorNames.put(0xff000000, "Black");
		colorNames.put(0xffff00ff, "Purple");
	}

	private void init(){
		add(panel);
		JPanel top = new JPanel();
		top.add(new JLabel("Path to level image"));
		top.add(pathTextBox);
		top.add(generateMapButton);
		top.add(generateCollidersButton);
		top.add(new JLabel("Offset"));
		top.add(offsetTextBox);
		top.add(new JLabel("Scale"));
		top.add(scaleTextBox);
		panel.add(top);
		generateMapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generateMapJson();
			}
		});
		generateCollidersButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				generateCollidersJson();		
			}
			
		});
		panel.add(imageLabel);
		JScrollPane scroll = new JScrollPane(jsonTextBox);
		panel.add(scroll);
	}
	
	protected void generateCollidersJson() {
		try
		{
			img = ImageIO.read(new File(pathTextBox.getText()));
			imageLabel.setIcon(new ImageIcon(img));
			int w = img.getWidth();
			int h = img.getHeight();
			int[] pixels = new int[w * h];
			img.getRGB(0, 0, w, h, pixels, 0, w);

			int blockScale = Integer.parseInt(scaleTextBox.getText());
			float offset = Float.parseFloat(offsetTextBox.getText());

			StringBuilder string = new StringBuilder();
			int white = 0xffffffff;
			int color = 0;
			int start = -1;
			
			// A simple brute force algorithm for finding lines and collumns of length 1+
			// Get horizontal colliders
			for (int i = 0; i < h; ++i){
				start = -1;
				for (int j = 0; j < w; ++j){
					color = pixels[i * w + j];
					if (color != white){
						if (start == -1){
							start = j;
						}
					}
					else {
						if (start != -1 && j - start - 1 >= 1){
							int endX = j - 1;
							for (int k = start; k <= endX; ++k){
								pixels[i * w + k] = white;
							}
							string.append(putRectJson(start, endX, i, i, w, h, blockScale, offset) + ",\n");
							start = -1;
						}
						else
						{
							start = -1;
						}
					}
				}
			}
			
			// Get vertical colliders
			for (int j = 0; j < w; ++j){
				start = -1;
				for (int i = 0; i < h; ++i){
					color = pixels[i * w + j];
					if (color != white){
						if (start == -1){
							start = i;
						}
					}
					else {
						if (start != -1 && i - start - 1 >= 1){
							int endY = i - 1;
							for (int k = start; k <= endY; ++k){
								pixels[k * w + j] = white;
							}
							string.append(putRectJson(j, j, start, endY, w, h, blockScale, offset) + ",\n");
							start = -1;
						}
						else
						{
							start = -1;
						}
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
	
	private String putRectJson(int x1, int x2, int y1, int y2, int w, int h, int blockScale, float offset){
		float halfBlockScale = blockScale * 0.5f;
		float tx1 = x1 * blockScale + offset;
		float ty1 = h * blockScale - y1 * blockScale + offset;
		float tx2 = x2 * blockScale + offset;
		float ty2 = h * blockScale - y2 * blockScale + offset;
		return new JSONStringer().array()
			.object()
				.key("x").value(tx1 - halfBlockScale)
				.key("y").value(ty2 - halfBlockScale)
			.endObject()
			.object()
				.key("x").value(tx1 - halfBlockScale)
				.key("y").value(ty1 + halfBlockScale)
			.endObject()
			.object()
				.key("x").value(tx2 + halfBlockScale)
				.key("y").value(ty1 + halfBlockScale)
			.endObject()
			.object()
				.key("x").value(tx2 + halfBlockScale)
				.key("y").value(ty2 - halfBlockScale)
			.endObject()
		.endArray().toString();
	}

	private void generateMapJson(){
		try
		{
			img = ImageIO.read(new File(pathTextBox.getText()));
			imageLabel.setIcon(new ImageIcon(img));
			int w = img.getWidth();
			int h = img.getHeight();
			int[] pixels = new int[w * h];
			img.getRGB(0, 0, w, h, pixels, 0, w);

			int blockScale = Integer.parseInt(scaleTextBox.getText());
			float offset = Float.parseFloat(offsetTextBox.getText());
			
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
						String colorName = colorNames.get(color);
						if (colorName == null){
							colorName = "" + color;
						}
						entity.put("entity", colorName);
						entity.put("scale", scale);
						JSONArray position = new JSONArray();
						position.put(j * blockScale + offset);
						position.put(h * blockScale - i * blockScale + offset);
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
