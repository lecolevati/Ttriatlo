package view;

import java.util.concurrent.Semaphore;

import controller.ThreadAtleta;

public class Triatlo {

	public static void main(String[] args) {
		Semaphore semaforoTiro = new Semaphore(3);
		int totalAtletas = 15;
		for (int i = 0 ; i < totalAtletas ; i++){
			Thread t = new ThreadAtleta(i, semaforoTiro, totalAtletas);
			t.start();
		}
	}

}
