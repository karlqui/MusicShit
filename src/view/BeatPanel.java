package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import controller.Controller;

public class BeatPanel extends JPanel{
	private Controller contr;
	private int square_size;

	
	public BeatPanel(Controller contr, int square_size) {
		this.contr = contr;
		this.square_size = square_size;

	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(square_size*contr.getBoxes()[0].length, 30);				
	}
	

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		// Beat-markör
		for (int i = 0; i < contr.getBoxes()[0][0].length; i++) {
			g.setColor(Color.DARK_GRAY);
			if (contr.getBeat() == i)
				g.setColor(Color.RED);
			g.fillRect(i * square_size + square_size / 2 - 5, 11, 10, 10);
			if (i % 4 == 0)
				g.fillRect(i * square_size + square_size / 2 - 7, 9, 14, 14);
		}
	}
}
