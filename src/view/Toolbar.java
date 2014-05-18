package view;

import java.awt.Color;
import java.awt.Dimension;
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
	private Controller c;
	private JButton play, stop, save, midisave, clear, load, randomize, swagmode;
	private ArrayList<JButton> pageButton = new ArrayList<JButton>();
	private JSlider tempoSlider, pageSlider;
	private JComboBox instrument, scale;
	private JPanel pagePanel = new JPanel();
	private int prevIndex;
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(0, 130);	
	}

	public Toolbar(final Controller c) {
		this.c = c;
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
		
		
		GridLayout layout = new GridLayout(0, 4);
		setLayout(layout);

		this.add(play);
		this.add(stop);
		this.add(save);
		this.add(load);
		this.add(clear);
		this.add(midisave);
		this.add(randomize);
		this.add(swagmode);
		this.add(instrument);
		this.add(scale);
		this.add(tempoSlider);
		this.add(new JPanel());
		this.add(pagePanel);
		

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
				c.currentPage = pageButton.size()-2;
				pageButton.get(c.currentPage).setBackground(Color.gray);
			}
			else {
				pageButton.get(c.currentPage).setBackground(null);;
				int page = Integer.parseInt(e.getActionCommand());
				c.currentPage = page-1;
				
				pageButton.get(page-1).setBackground(Color.gray);
				
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
//				load();
			} else if (e.getActionCommand().equals("RANDOMIZE")) {
				c.randomize();
			} else if (e.getActionCommand().equals("SWAGMODE")) {
//				clear();
//				c.swagMode();
			} else if (e.getActionCommand().equals("SAVE MIDI")) {
				MidiFile mf = new MidiFile();
//				mf.playSequence(c.getBoxes(), c.getSound(), tempoSlider.getValue());

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
			
			//Sparar metadata, 5 rader
			writer.println(c.getBoxes().length);
			writer.println(c.getBoxes()[0].length);
			writer.println(c.getSound().getPack());
			writer.println(c.getSound().getScale());
			writer.println(c.BPM);
			
			//Sparar m�nstret
			for (int i = 0; i < c.getBoxes().length; i++) {
				for (int j = 0; j < c.getBoxes()[i].length; j++) {
					writer.print(c.getBoxes()[i][j] + " ");
				}
				writer.println();
			}
			writer.close();
		}

//		public void load() {
//			JFileChooser fc = new JFileChooser();
//			fc.setCurrentDirectory(new File("."));
//			fc.showOpenDialog(null);
//			File file = fc.getSelectedFile();
//
//			BufferedReader br = null;
//			String line = "";
//
//			try {
//				br = new BufferedReader(new FileReader(file));
//				
//				//L�ser in 5 rader metadata
////				c.setSize(Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine()));
//				c.getSound().changepack(br.readLine());
//				c.getSound().changeScale(br.readLine());
//				c.BPM = Integer.parseInt(br.readLine());				
//				line = br.readLine();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			
//			
//			
//			int row = 0;
//			while (line != null) {
//				String[] tokens = line.split(" ");
//				for (int i = 0; i < tokens.length; i++) {
//					if (tokens[i].equals("true"))
//						c.getBoxes()[row][i] = true;
//					else
//						c.getBoxes()[row][i] = false;
//				}
//
//				try {
//					line = br.readLine();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				row++;
//
//			}
//			c.beat = 0;
//			
//			//TODO
//			//�ndra valda dropdown-alternativ
//		}

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		this.repaint();
		if (arg1 != null && (boolean)arg1) {
			if(prevIndex != c.currentPage)
				pageButton.get(prevIndex).setBackground(null);
			else
				pageButton.get(prevIndex).setBackground(Color.gray);
			
			prevIndex = c.bar;
			pageButton.get(c.bar).setBackground(Color.red);
		}
		
	}

}
