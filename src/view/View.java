package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import model.Triangle;
import controller.Controller;

public class View implements Observer {

	private final static int WIDTH = 800;
	private final static int HEIGHT = 800;
	
	private final static int EXTRA_WIDTH = 50;
	private final static int EXTRA_HEIGHT = 150;

	private static final int square_size = 50;



	private Controller contr;
	private JFrame f;
	private JPanel playPanel, beatPanel, t;

	public View(final Controller contr) {
		this.contr = contr;
		contr.addObserver(this);

		// Bestämmer avståndet mellan boxarna

		f = new JFrame();
		
		
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//		f.setLocationRelativeTo(null);
		
		playPanel = new PlayPanel(contr, square_size);
		f.add(playPanel);
		
		beatPanel = new BeatPanel(contr, square_size);
		f.add(beatPanel, "South");
		playPanel.setPreferredSize(new Dimension(500, 500));
		System.out.println(beatPanel.getPreferredSize());

		// Toolbar
		t = new Toolbar(contr);
		f.add(t, "North");
		f.setResizable(false);
		f.pack();
		f.setVisible(true);
	}

	

	@Override
	public void update(Observable arg0, Object arg1) {
		f.repaint();
//		beatPanel.repaint();
		f.pack();
//		f.validate();
//		f.revalidate();
		
		//Om storleken har ändrats
		if (arg1 != null && (boolean)arg1) {
			//Borders = 6 pixlar
			int x = playPanel.getPreferredSize().width + 6;
			//Borders =  28 pixlar
			int y = beatPanel.getPreferredSize().height + playPanel.getPreferredSize().height + t.getPreferredSize().height + 28;
			
			f.setPreferredSize(new Dimension(x,y));
		}
			
	}

}
