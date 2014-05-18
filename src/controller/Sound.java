package controller;

import java.io.FileInputStream;
import java.io.InputStream;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Sound {
	

	private String pack = "guitar";
	private String scale = "pentatonic";

	String url[] = {
			"audio/" + pack + "/C3.wav",
			"audio/" + pack + "/D3.wav",
			"audio/" + pack + "/E3.wav",
			"audio/" + pack + "/G3.wav",
			"audio/" + pack + "/A3.wav",
			"audio/" + pack + "/C4.wav",
			"audio/" + pack + "/D4.wav",
			"audio/" + pack + "/E4.wav",
			"audio/" + pack + "/G4.wav",
			"audio/" + pack + "/A4.wav",
			"audio/" + pack + "/C5.wav",
			"audio/" + pack + "/D5.wav",
			"audio/" + pack + "/E5.wav",
			"audio/" + pack + "/G5.wav",
			"audio/" + pack + "/A5.wav",
			"audio/" + pack + "/C6.wav",
	};
	
	public Sound() {
		//Preload
	}
	
	public void startSound(final int i) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                AePlayWave apw = new AePlayWave(url[i]);
                apw.start();

                try{
                    boolean alive = apw.isAlive();
                    while(alive){
                        //check periodically if the thread is alive
                        alive = apw.isAlive();
                        Thread.currentThread().sleep(50);
                    }
                }catch(InterruptedException e){
                    System.out.println("Interrupted");
                    e.printStackTrace();
                }
            
        }}).start();    
	}	
	
	public void changepack(String pack) {
		this.pack = pack;
		String url[] = {
				"audio/" + pack + "/C3.wav",
				"audio/" + pack + "/D3.wav",
				"audio/" + pack + "/E3.wav",
				"audio/" + pack + "/G3.wav",
				"audio/" + pack + "/A3.wav",
				"audio/" + pack + "/C4.wav",
				"audio/" + pack + "/D4.wav",
				"audio/" + pack + "/E4.wav",
				"audio/" + pack + "/G4.wav",
				"audio/" + pack + "/A4.wav",
				"audio/" + pack + "/C5.wav",
				"audio/" + pack + "/D5.wav",
				"audio/" + pack + "/E5.wav",
				"audio/" + pack + "/G5.wav",
				"audio/" + pack + "/A5.wav",
				"audio/" + pack + "/C6.wav",
		};
		this.url = url;		
	}
	
	public void changeScale(String scale) {
		this.scale = scale;
		if (scale.equals("egyptian")) {
		String url[] = {
				"audio/" + pack + "/C3.wav",
				"audio/" + pack + "/C#3.wav",
				"audio/" + pack + "/E3.wav",
				"audio/" + pack + "/F3.wav",
				"audio/" + pack + "/G3.wav",
				"audio/" + pack + "/G#3.wav",
				"audio/" + pack + "/B3.wav",
				"audio/" + pack + "/C4.wav",
				"audio/" + pack + "/C#4.wav",
				"audio/" + pack + "/E4.wav",
				"audio/" + pack + "/F4.wav",
				"audio/" + pack + "/G4.wav",
				"audio/" + pack + "/G#4.wav",
				"audio/" + pack + "/B4.wav",
				"audio/" + pack + "/C4.wav",
				"audio/" + pack + "/E4.wav",
		};
		this.url = url;
		}
		else if (scale.equals("pentatonic")) {
			String url[] = {
					"audio/" + pack + "/C3.wav",
					"audio/" + pack + "/D3.wav",
					"audio/" + pack + "/E3.wav",
					"audio/" + pack + "/G3.wav",
					"audio/" + pack + "/A3.wav",
					"audio/" + pack + "/C4.wav",
					"audio/" + pack + "/D4.wav",
					"audio/" + pack + "/E4.wav",
					"audio/" + pack + "/G4.wav",
					"audio/" + pack + "/A4.wav",
					"audio/" + pack + "/C5.wav",
					"audio/" + pack + "/D5.wav",
					"audio/" + pack + "/E5.wav",
					"audio/" + pack + "/G5.wav",
					"audio/" + pack + "/A5.wav",
					"audio/" + pack + "/C6.wav",
			};
			this.url = url;
		}	
		else if (scale.equals("blues")) {
			String url[] = {
					"audio/" + pack + "/C3.wav",
					"audio/" + pack + "/D#3.wav",
					"audio/" + pack + "/F3.wav",
					"audio/" + pack + "/F#3.wav",
					"audio/" + pack + "/G3.wav",
					"audio/" + pack + "/A#3.wav",
					"audio/" + pack + "/C4.wav",
					"audio/" + pack + "/D#4.wav",
					"audio/" + pack + "/F4.wav",
					"audio/" + pack + "/F#4.wav",
					"audio/" + pack + "/G4.wav",
					"audio/" + pack + "/A#4.wav",
					"audio/" + pack + "/C5.wav",
					"audio/" + pack + "/D#5.wav",
					"audio/" + pack + "/F5.wav",
					"audio/" + pack + "/F#5.wav",
			};
			this.url = url;
		}	
	}
	
	public int getMidiNote(int i) {
		int note = 60;
		if (scale.equals("pentatonic")) {
			note = note + 2*i;
			if (i >= 3)
				note++;
			if (i >= 5)
				note++;
			if (i >= 8)
				note++;
			if (i >= 10)
				note++;
			if (i >= 13)
				note++;
			if (i >= 15)
				note++;
		}
		
		return note;		
	}
	
	public String getPack() {
		return pack;
	}


	public String getScale() {
		return scale;
	}

}
