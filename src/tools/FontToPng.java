package tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONObject;

public class FontToPng extends JFrame{
	JPanel panel = new JPanel();
	JTextField inputTextBox = new JTextField(System.getProperty("user.dir") + "\\content\\input");
	JTextField outputTextBox = new JTextField(System.getProperty("user.dir") + "\\content\\output");
	JTextArea symbolsTextBox = new JTextArea(" ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789().,;:-+=*?");
	JTextField sizeTextBox = new JTextField("14");
	
	public FontToPng(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 640);
		setLocation(200, 100);
		setTitle("MapGenerator");
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		init();
	}

	private void init() {
		add(panel);
		panel.add(new JLabel("Input font file"));
		panel.add(inputTextBox);
		panel.add(new JLabel("Output png/json filename"));
		panel.add(outputTextBox);
		panel.add(new JLabel("Symbols to add"));
		panel.add(symbolsTextBox);
		panel.add(new JLabel("Symbol size"));
		panel.add(sizeTextBox);
		JButton button = new JButton("Generate");
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				generatePng();
			}
		});
		panel.add(button);
	}
	
	private void generatePng(){
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, new File(inputTextBox.getText()));
			float fontSize = Float.parseFloat(sizeTextBox.getText());
			font = font.deriveFont(fontSize);
			
			BufferedImage oneSymbol = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			Graphics g1 = oneSymbol.getGraphics();
			g1.setColor(Color.WHITE);
			g1.setFont(font);

			FontMetrics metrics = g1.getFontMetrics(font);
	        FontRenderContext frc = metrics.getFontRenderContext();
			int symbolH = (int)(metrics.getHeight() * 1.1f);
			int area = symbolH * metrics.getMaxAdvance() * symbolsTextBox.getText().length();
			
			int minH = 1;
			
			while (minH * minH < area){
				minH *= 2;
			}
			
			BufferedImage png = new BufferedImage(minH, minH, BufferedImage.TYPE_INT_ARGB);
			Graphics g2 = png.getGraphics();
			g2.setColor(Color.WHITE);
			g2.setFont(font);
			
			int x = 0;
			int y = symbolH;
			int hStep = metrics.getMaxAdvance();
			
			System.out.println("Grid size [" + hStep + ";" + symbolH + "]");

			PrintWriter writer = new PrintWriter(new File(outputTextBox.getText() + ".json"));
			writer.println("{");
			writer.println("\t\"image\":\"" + outputTextBox.getText().substring(outputTextBox.getText().lastIndexOf("\\") + 1) + ".png\",");
			writer.println("\t\"symbols\":[");
			
			char[] symbols = symbolsTextBox.getText().toCharArray();
			for (char c : symbols){
				String str = "" + c;
				int w = metrics.charWidth(c);
				
				GlyphVector gv = font.createGlyphVector(frc, str);
				Rectangle bounds = gv.getVisualBounds().getBounds();
				int offset = -(bounds.height + bounds.y);
				
				System.out.println("Char " + c + " offset: " + offset + "bounds: " + bounds);				
				
				if (x + hStep > minH){
					x = 0;
					y += symbolH;
				}
				
				JSONObject symbolJson = new JSONObject();
				symbolJson.put("symbol", str)
					.put("x", x)
					.put("y", y - symbolH + offset + (int)(fontSize * 0.05f))
					.put("w", w + (int)(fontSize * 0.05f) + 1)
					.put("h", symbolH);
				if (offset != 0){
					JSONArray offsetJsonArray = new JSONArray();
					offsetJsonArray.put(0).put(offset);
					symbolJson.put("offset", offsetJsonArray);
				}
				
				writer.println("\t\t" + symbolJson.toString() + ",");
				
				g2.drawString(str, x, y + offset);
				x += hStep;
			}
			
			writer.println("\t]");
			writer.println("}");
			writer.flush();
			writer.close();

			ImageIO.write(png, "png", new File(outputTextBox.getText() + ".png"));
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}
