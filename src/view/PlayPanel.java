package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

	// 0 = note, 1 = selection
	public int mode = 0;

	private Polygon selection;
	private Polygon selectedBoxes;
	private Dimension selectionStart = new Dimension();
	private Dimension selectionEnd = new Dimension();

	public boolean resizerSelected;
	private float[] dashSpace = { 0 };

	public PlayPanel(Controller contr, int square_size) {
		this.square_size = square_size;
		this.contr = contr;
		Listener l = new Listener();
		this.addMouseListener(l);
		this.addMouseMotionListener(l);
		this.addKeyListener(l);

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(square_size * contr.getBoxes()[0][0].length, square_size * contr.getBoxes()[0].length);
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;

		this.requestFocusInWindow();

		for (int i = 0; i < contr.getBoxes()[contr.currentPage].length; i++) {

			for (int j = 0; j < contr.getBoxes()[contr.currentPage][i].length; j++) {
				g.setColor(Color.LIGHT_GRAY);
				g.drawRect(j * square_size, i * square_size, square_size, square_size);

				// Om boxen är aktiv
				if (contr.getBoxes()[contr.currentPage][i][j] == true) {
					g.setColor(Color.gray);
					// Om rutan är markerad
					if (selectedBoxes != null && j >= selectedBoxes.xpoints[0] && j < selectedBoxes.xpoints[1] && i >= selectedBoxes.ypoints[0]
							&& i < selectedBoxes.ypoints[1]) {
						g.setColor(Color.black);
						g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, dashSpace, 0));
						g.drawRect(j * square_size + 3, i * square_size + 3, square_size - 5, square_size - 5);
						g.setStroke(new BasicStroke());
						g.setColor(Color.pink);
					}
					// Om rutan spelas
					if (contr.beat == j && contr.bar == contr.currentPage)
						g.setColor(Color.red);
					g.fillRect(j * square_size + 3, i * square_size + 3, square_size - 5, square_size - 5);

				}
			}

		}

		// Selection-fyrkant
		if (selection != null) {
			float[] dashSpacing = { 10.0f };
			g.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, dashSpacing, 0));
			g.setColor(Color.red);
			g.draw(selection);
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
		// För animation på markerade boxar
		dashSpace[0] = ((dashSpace[0] + 0.01f) % 3) + 10;

	}

	private class Listener extends MouseAdapter implements MouseMotionListener, KeyListener {

		private boolean resizing, selecting;
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
					resizing = true;
				} else {
					// Om edit-mode är aktiverat
					if (mode == 0)
						contr.getBoxes()[contr.currentPage][e.getY() / square_size][e.getX() / square_size] = true;
					else if (mode == 1) {
						startX = e.getX();
						startY = e.getY();
						selecting = true;
					}
				}
			}
			// Andra musknappar
			else
				contr.getBoxes()[contr.currentPage][e.getY() / square_size][e.getX() / square_size] = false;
			repaint();
		}

		public void mouseReleased(MouseEvent e) {
			resizing = false;
			selecting = false;
			selection = null;

		}

		public void mouseMoved(MouseEvent e) {
			if (t.contains(new Point(e.getX(), e.getY())))
				resizerSelected = true;
			else
				resizerSelected = false;
		}

		public void mouseDragged(MouseEvent e) {
			if (resizing) {
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
			} else if (selecting) {
				int deltaX = e.getX() - startX;
				int deltaY = e.getY() - startY;

				// Räknar ut vilken ruta som selectionen började på
				int startSquareX = startX / square_size;
				int startSquareY = startY / square_size;
				selectionStart = new Dimension(startSquareX, startSquareY);

				// Räknar ut hur många rutor som är markerade
				int selectedX = deltaX / square_size + 1;
				int selectedY = deltaY / square_size + 1;
				selectionEnd = new Dimension(startSquareX + selectedX, startSquareY + selectedY);
				System.out.println("asd");
				
				// Sätter övre vänstra hörnet som första positionen i rektangeln
				int[] x = new int[2];
				int[] y = new int[2];
				if (selectedX > 0) {
					x[0] = startSquareX;
					x[1] = startSquareX + selectedX;
				} else {
					x[1] = startSquareX;
					x[0] = startSquareX + selectedX;
				}
				if (selectedY > 0) {
					y[0] = startSquareY;
					y[1] = startSquareY + selectedY;
				} else {
					y[1] = startSquareY;
					y[0] = startSquareY + selectedY;
				}
				selectedBoxes = new Polygon(x, y, 2);

				//Bestämmer markeringsfyrkanten
				int[] x1 = { startSquareX * square_size, startSquareX * square_size + selectedX * square_size,
						startSquareX * square_size + selectedX * square_size, startSquareX * square_size };
				int[] y1 = { startSquareY * square_size, startSquareY * square_size, startSquareY * square_size + selectedY * square_size,
						startSquareY * square_size + selectedY * square_size };
				selection = new Polygon(x1, y1, 4);

				repaint();
			}
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_DELETE && selectedBoxes != null) {
				for (int i = selectedBoxes.ypoints[0]; i < selectedBoxes.ypoints[1]; i++) {
					for (int j = selectedBoxes.xpoints[0]; j < selectedBoxes.xpoints[1]; j++) {
						contr.getBoxes()[contr.currentPage][i][j] = false;
					}
					
				}
			}
				
		}

		public void keyReleased(KeyEvent arg0) {
		}

		public void keyTyped(KeyEvent arg0) {
		}

	}

}
