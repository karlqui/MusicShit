package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.MidiFile;
import controller.Controller;

public class Toolbar extends JPanel implements Observer {
	private static int COMPONENTS_PER_ROW = 4;
	private int HEIGHT = 150;
	
	private Controller c;
	private JButton play, stop, save, midisave, clear, load, randomize, swagmode;
	private ArrayList<JButton> pageButton = new ArrayList<JButton>();
	private JSlider tempoSlider, pageSlider;
	private JComboBox instrument, scale;
	private JPanel pagePanel = new JPanel();
	private JPanel toolPanel = new JPanel();
	private JPanel []rows = new JPanel[4];;
	private int prevIndex;

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(0, HEIGHT);
	}

	public Toolbar(final Controller c) {
		this.c = c;
		this.setFocusable(true);
		c.addObserver(this);
		ActionListener listener = new Listener();
		play = new JButton("PLAY");
		play.addActionListener(listener);
		stop = new JButton("STOP");
		stop.addActionListener(listener);
		save = new JButton("SAVE");
		save.addActionListener(listener);
		load = new JButton("LOAD");
		load.addActionListener(listener);
		clear = new JButton("CLEAR");
		clear.addActionListener(listener);
		tempoSlider = new JSlider(60, 200, 120);
		tempoSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				c.BPM = tempoSlider.getValue();
			}
		});

		instrument = new JComboBox();

		instrument.addItem("guitar");
		instrument.addItem("piano");
		instrument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.getSound().changepack((String) instrument.getSelectedObjects()[0]);
			}
		});

		scale = new JComboBox();

		scale.addItem("pentatonic");
		scale.addItem("egyptian");
		scale.addItem("blues");
		scale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.getSound().changeScale((String) scale.getSelectedObjects()[0]);
			}
		});

		midisave = new JButton("SAVE MIDI");
		midisave.addActionListener(listener);

		randomize = new JButton("RANDOMIZE");
		randomize.addActionListener(listener);

		swagmode = new JButton("SWAGMODE");
		swagmode.addActionListener(listener);

		ActionListener buttonListner = new ButtonListener();
		JButton temp = new JButton("1");
		temp.addActionListener(buttonListner);
		temp.setBackground(Color.gray);
		pageButton.add(temp);

		temp = new JButton("+");
		temp.addActionListener(buttonListner);
		pageButton.add(temp);

		pagePanel.add(pageButton.get(0));
		pagePanel.add(pageButton.get(1));
		temp = new JButton("EDIT");
		temp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c.setMode("EDIT");
			}
		});
		toolPanel.add(temp);
		temp = new JButton("SELECT");
		temp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c.setMode("SELECT");
			}
		});
		toolPanel.add(temp);
		

		GridLayout layout = new GridLayout(0, 1);
		setLayout(layout);

		for (int i = 0; i < rows.length; i++) {
			rows[i] = new JPanel();
			rows[i].setLayout(new GridLayout(0,COMPONENTS_PER_ROW));
			rows[i].setPreferredSize(new Dimension(0,HEIGHT/rows.length));
			this.add(rows[i]);
		}
		rows[rows.length-1].setLayout(new FlowLayout());
		
		rows[0].add(play);
		rows[0].add(stop);
		rows[0].add(save);
		rows[0].add(load);
		rows[1].add(clear);
		rows[1].add(midisave);
		rows[1].add(randomize);
		rows[1].add(swagmode);
		rows[2].add(instrument);
		rows[2].add(scale);
		rows[2].add(tempoSlider);
		rows[2].add(toolPanel);
		
		
		rows[rows.length-1].add(pagePanel);


	}

	public void paintComponent(Graphics g) {

	}

	private class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("+")) {
				JButton temp = new JButton(pageButton.size() + "");
				temp.addActionListener(this);
				pageButton.add(pageButton.size() - 1, temp);
				pageButton.get(c.currentPage).setBackground(null);
				c.addPage();
				c.currentPage = pageButton.size() - 2;
				pageButton.get(c.currentPage).setBackground(Color.gray);
			} else {
				pageButton.get(c.currentPage).setBackground(null);
				int page = Integer.parseInt(e.getActionCommand());
				c.currentPage = page - 1;

				pageButton.get(page - 1).setBackground(Color.gray);

			}

			pagePanel.removeAll();

			for (int i = 0; i < pageButton.size(); i++) {
				pagePanel.add(pageButton.get(i));
			}

			pagePanel.repaint();
			pagePanel.revalidate();

		}

	}

	private class Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("PLAY"))
				c.play();
			else if (e.getActionCommand().equals("STOP"))
				c.stop();
			else if (e.getActionCommand().equals("CLEAR")) {
				c.clear();
			} else if (e.getActionCommand().equals("SAVE"))
				save();
			else if (e.getActionCommand().equals("LOAD")) {
				 load();
			} else if (e.getActionCommand().equals("RANDOMIZE")) {
				c.randomize();
			} else if (e.getActionCommand().equals("SWAGMODE")) {
				// clear();
				// c.swagMode();
			} else if (e.getActionCommand().equals("SAVE MIDI")) {
				MidiFile mf = new MidiFile();
				// mf.playSequence(c.getBoxes(), c.getSound(),
				// tempoSlider.getValue());

				try {
					mf.writeToFile("test1.mid");
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

		}

		public void save() {
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("."));
			fc.showOpenDialog(null);
			File file = fc.getSelectedFile();
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(file);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			// Sparar metadata, 6 rader
			writer.println(c.getBoxes().length); // Antalet sidor
			writer.println(c.getBoxes()[0].length); // Antalet rader
			writer.println(c.getBoxes()[0][0].length); // Antalet kolumner
			writer.println(c.getSound().getPack()); // Ljudpaket
			writer.println(c.getSound().getScale()); // Skala
			writer.println(c.BPM); // BPM

			// Sparar mönstret
			for (int i = 0; i < c.getBoxes().length; i++) {
				for (int j = 0; j < c.getBoxes()[0].length; j++) {
					for (int k = 0; k < c.getBoxes()[0][0].length; k++) {
						writer.print(c.getBoxes()[i][j][k] + " ");
					}
					writer.println();
				}
			}
			writer.close();
		}

		public void load() {
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("."));
			fc.showOpenDialog(null);
			File file = fc.getSelectedFile();

			BufferedReader br = null;
			
			pageButton.clear();

			try {
				br = new BufferedReader(new FileReader(file));

				// Läser in 6 rader metadata
				boolean[][][] boxes = new boolean[Integer.parseInt(br.readLine())][Integer.parseInt(br.readLine())][Integer.parseInt(br.readLine())];
				c.getSound().changepack(br.readLine());
				c.getSound().changeScale(br.readLine());
				c.BPM = Integer.parseInt(br.readLine());
				
				
				// Återskapar boxarna
				for (int i = 0; i < boxes.length; i++) {
					
					JButton temp = new JButton(i+1 + "");
					temp.addActionListener(new ButtonListener());
					pageButton.add(temp);
					for (int j = 0; j < boxes[0].length; j++) {
						String [] line = br.readLine().split(" ");
						for (int k = 0; k < boxes[0][0].length; k++) {
							boxes[i][j][k] = line[k].equals("true");
						}
					}
				}				
				c.setBoxes(boxes);
				JButton temp = new JButton("+");
				temp.addActionListener(new ButtonListener());
				pageButton.add(temp);

				pagePanel.removeAll();

				for (int i = 0; i < pageButton.size(); i++) {
					pagePanel.add(pageButton.get(i));
				}
				pagePanel.repaint();
				pagePanel.revalidate();
				
			} catch (IOException e) {
				e.printStackTrace();
			}

			
			//Börjar från början
			c.beat = 0;
			c.bar = 0;
			

			// TODO
			// Ändra valda dropdown-alternativ
		}

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		this.repaint();
		if (arg1 != null && (int) arg1 == 11) {
			if (prevIndex != c.currentPage)
				pageButton.get(prevIndex).setBackground(null);
			else
				pageButton.get(prevIndex).setBackground(Color.gray);

			prevIndex = c.bar;
			pageButton.get(c.bar).setBackground(Color.red);
		}

	}

}
