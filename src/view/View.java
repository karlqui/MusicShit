package view;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import controller.Controller;

public class View extends JFrame implements Observer {

	private final static int WIDTH = 800;
	private final static int HEIGHT = 800;

	private final static int EXTRA_WIDTH = 50;
	private final static int EXTRA_HEIGHT = 150;

	private static final int square_size = 50;

	

	private Controller contr;
	private JPanel beatPanel;
	private PlayPanel playPanel;
	private Toolbar t;

	public View(final Controller contr) {
		this.contr = contr;
		contr.addObserver(this);

		// Bestämmer avståndet mellan boxarna
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setFocusable(true);
		// f.setLocationRelativeTo(null);

		playPanel = new PlayPanel(contr, square_size);
		this.add(playPanel);

		beatPanel = new BeatPanel(contr, square_size);
		this.add(beatPanel, "South");
		playPanel.setPreferredSize(new Dimension(500, 500));
		System.out.println(beatPanel.getPreferredSize());

		// Toolbar
		t = new Toolbar(contr);
		this.add(t, "North");
		

		
		
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		this.repaint();

		if (arg1 != null) {
			// Om edit-läget har aktiverats
			if ((int) arg1 == 0) {
				playPanel.mode = 0;
			}
			else if ((int) arg1 == 1) {
				playPanel.mode = 1;
			}
			
			// Om storleken har ändrats
			else if ((int) arg1 == 10) {
				// Borders = 6 pixlar
				int x = playPanel.getPreferredSize().width + 6;
				// Borders = 28 pixlar
				int y = beatPanel.getPreferredSize().height + playPanel.getPreferredSize().height + t.getPreferredSize().height + 28;

				this.setPreferredSize(new Dimension(x, y));
				this.pack();
			}
		}

	}

}
