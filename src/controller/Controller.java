package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import model.MidiFile;

public class Controller extends Observable implements Runnable {
	private final static double WIDTH = 800;
	private final static double HEIGHT = 800;

	private final static int MAX_BEATS = 16;
	private final static int MAX_NOTES = 16;

	// boxes[kolumner][rader]
	private boolean[][][] boxes = new boolean[1][16][16];
	public int beat = 0;
	public int bar = 0;
	public int currentPage;

	private boolean play = true;
	private boolean swagify;

	public int BPM = 120;
	private Sound sound;

	public Controller() {
		sound = new Sound();
	}

	public void run() {

		while (true) {

			long time = System.currentTimeMillis();
			if (play) {
				beat = (beat + 1) % boxes[0][0].length;
				if (beat == 0) {
					bar++;
					if (bar == boxes.length)
						bar = 0;
				}

				for (int i = 0; i < boxes[0].length; i++) {
					if (boxes[bar][i][beat])
						sound.startSound(boxes[0].length - 1 - i);
				}
			}

			super.setChanged();
			super.notifyObservers();

			try {
				int wait = ((1000 * BPM / 60) / 16);
				Thread.sleep(wait - (System.currentTimeMillis() - time));
			} catch (Exception e) {
			}

		}
	}

	// public void randomize() {
	// for (int i = 0; i < boxes.length; i++) {
	// for (int j = 0; j < boxes[i].length; j++) {
	// if (Math.random() < 0.15) {
	// boxes[i][j] = true;
	// // //Om det är samma fast annan oktav
	// // if (boxes[i][j%5] == false)
	//
	// // Om det finns en ruta precis under ller över rutan
	// if (i < boxes.length - 1 && i > 1)
	// if (boxes[i + 1][j] == true || boxes[i - 1][j])
	// boxes[i][j] = false;
	//
	// // Om det finns mer en fyra rutor på samma beat
	// int counter = 0;
	// for (int j2 = 0; j2 < boxes[i].length; j2++) {
	// if (boxes[i][j2])
	// counter++;
	// }
	// if (counter > 4)
	// boxes[i][j] = false;
	//
	// } else
	// boxes[i][j] = false;
	// }
	// }
	// }

	public void play() {
		play = true;
	}

	public void stop() {
		play = false;
		beat = 0;
	}

	public void addPage() {
		boolean[][][] temp = new boolean[boxes.length + 1][boxes[0].length][boxes[0][0].length];
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[0].length; j++) {
				for (int k = 0; k < boxes[0][0].length; k++) {
					temp[i][j][k] = boxes[i][j][k];
				}
			}

		}

		boxes = temp;
	}

	public void setSize(int x, int y) {
		if (x > MAX_BEATS || y > MAX_NOTES || x == 0 || y == 0)
			return;

		// Kopiera till den nya arrayen
		boolean[][][] b = new boolean[boxes.length][x][y];

		for (int k = 0; k < b.length; k++) {
			for (int i = 0; i < b[k].length; i++) {
				for (int j = 0; j < b[k][i].length; j++) {

					if (i < boxes[k].length && j < boxes[k][i].length)

						// Om antalet rader ökade, förlfytta alla element ett
						// steg
						// neråt
						if (x > boxes[k].length) {
							b[k][i + 1][j] = boxes[k][i][j];
							// Om antalat rader minskade, flytta alla elment ett
							// steg uppåt
						} else if (x < boxes[k].length) {
							if (i == 0)
								b[k][i][j] = false;
							else
								b[k][i - 1][j] = boxes[k][i][j];
						} else
							b[k][i][j] = boxes[k][i][j];
				}
			}
		}
		boxes = b;

		super.setChanged();
		super.notifyObservers(true);

	}

	public void swagMode() {
		if (swagify)
			swagify = false;
		else {
			bar = 1;
			swagify = true;
		}
	}

	// public void swagify(int bar) {
	//
	// // Börja med att lägga ut fyra block i lägra delen
	// if (bar == 1) {
	// for (int i = 0; i < 4; i++) {
	// boxes[(int) (Math.random() * boxes.length)][(int) (boxes[0].length / 2 +
	// Math.random() * boxes[0].length / 2)] = true;
	// }
	// }
	//
	// // varannan bar ska några noter läggas till
	// else if (bar % 2 == 0 && bar > 2 && bar < 20) {
	// for (int i = 0; i < 4; i++) {
	// boxes[(int) (Math.random() * boxes.length)][(int) (Math.random() *
	// boxes[0].length)] = true;
	// }
	// }
	//
	// if (bar == 20)
	// randomize();
	//
	// if (bar > 20 && (bar - 20) % 7 == 0)
	// randomize();
	//
	// }

	public int getBeat() {
		return beat;
	}

	public boolean[][][] getBoxes() {
		return boxes;
	}

	public Sound getSound() {
		return sound;
	}

}
