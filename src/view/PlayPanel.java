package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import model.Triangle;
import controller.Controller;

public class PlayPanel extends JPanel {

	private int square_size;
	private Controller contr;
	private Triangle t;
	
	

	public boolean resizerSelected;

	public PlayPanel(Controller contr, int square_size) {
		this.square_size = square_size;
		this.contr = contr;
		Listener l = new Listener();
		this.addMouseListener(l);
		this.addMouseMotionListener(l);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(square_size * contr.getBoxes()[0][0].length, square_size * contr.getBoxes()[0].length);
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;		
		
		for (int i = 0; i < contr.getBoxes()[contr.currentPage].length; i++) {
			
			for (int j = 0; j < contr.getBoxes()[contr.currentPage][i].length; j++) {
				g.setColor(Color.LIGHT_GRAY);
				g.drawRect(j * square_size, i * square_size, square_size, square_size);

				// Om boxen är aktiv
				if (contr.getBoxes()[contr.currentPage][i][j] == true) {
					g.setColor(Color.gray);
					if (contr.beat == j && contr.bar == contr.currentPage)
						g.setColor(Color.red);
					g.fillRect(j * square_size + 3, i * square_size + 3, square_size - 5, square_size - 5);

				}
			}
		}

		// Dragpil
		if (!resizerSelected) {
			g.setColor(new Color(150, 150, 150));
			t = new Triangle(square_size * contr.getBoxes()[0][0].length, square_size * contr.getBoxes()[0].length, 15);
		} else {
			g.setColor(new Color(200, 200, 200));
			t = new Triangle(square_size * contr.getBoxes()[0][0].length, square_size * contr.getBoxes()[0].length, 25);
		}
		g.fillPolygon(t);
	}

	private class Listener extends MouseAdapter implements MouseMotionListener {

		private boolean dragging;
		private int startX = 0;
		private int startY = 0;

		@Override
		public void mousePressed(MouseEvent e) {
			// Vänster musknapp
			if (e.getButton() == 1) {
				// Om musen är på dragpilen
				if (t.contains(new Point(e.getX(), e.getY()))) {
					startX = square_size * contr.getBoxes()[0][0].length;
					startY = square_size * contr.getBoxes()[0].length;
					// startX = e.getX();
					// startY = e.getY();
					dragging = true;
				} else {
					contr.getBoxes()[contr.currentPage][e.getY() / square_size][e.getX() / square_size] = true;
				}
			}
			// Andra musknappar
			else
				contr.getBoxes()[contr.currentPage][e.getY() / square_size][e.getX() / square_size] = false;
			repaint();
		}

		public void mouseReleased(MouseEvent e) {
			dragging = false;
		}

		public void mouseMoved(MouseEvent e) {
			if (t.contains(new Point(e.getX(), e.getY())))
				resizerSelected = true;
			else
				resizerSelected = false;
		}

		public void mouseDragged(MouseEvent e) {
			if (dragging) {
				int deltaX = startX - e.getX();
				int deltaY = startY - e.getY();
				if (deltaX > square_size) {
					startX = e.getX();
					contr.setSize(contr.getBoxes()[0].length, contr.getBoxes()[0][0].length - 1);
				} else if (deltaX < -square_size) {
					startX = e.getX();
					contr.setSize(contr.getBoxes()[0].length, contr.getBoxes()[0][0].length + 1);
				} else if (deltaY < -square_size) {
					startY = e.getY();
					contr.setSize(contr.getBoxes()[0].length + 1, contr.getBoxes()[0][0].length);
				} else if (deltaY > square_size) {
					startY = e.getY();
					contr.setSize(contr.getBoxes()[0].length - 1, contr.getBoxes()[0][0].length);
				}

			}
		}

	}

}
