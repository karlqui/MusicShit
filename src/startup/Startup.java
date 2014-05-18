package startup;

import model.MidiFile;
import view.View;
import controller.Controller;

public class Startup {

	public static void main(String[] args) {
		Controller c = new Controller();
		new View(c);
		new Thread(c).start();

	}

}
