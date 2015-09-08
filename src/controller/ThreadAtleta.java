package controller;

import java.util.concurrent.Semaphore;

public class ThreadAtleta extends Thread {

	private int idAtleta;
	private Semaphore semaforoTiro;
	private static final int DISTANCIA_FINAL_CORRIDA = 5000;
	private static final int DISTANCIA_FINAL_NADO = 1000;
	private static long[] rankingTempo;
	private static long[] rankingTiro;
	private static long[][] rankingFinal;
	private long tempoInicio;
	private long tempoFim;
	private static int posicao;
	private int totalAtletas;
	private int tiro;
	
	public ThreadAtleta(int idAtleta, 
			Semaphore semaforoTiro,
			int totalAtletas){
		this.idAtleta = idAtleta;
		this.semaforoTiro = semaforoTiro;
		this.totalAtletas = totalAtletas;
		rankingFinal = new long[totalAtletas][2];
		rankingTempo = new long[totalAtletas];
		rankingTiro = new long[totalAtletas];
		
	}

	private void largada(){
		tempoInicio = System.currentTimeMillis();
		System.out.println("Atleta #"+idAtleta+
				" Iniciou o Triatlo");
	}
	
	private void corrida(){
		int distanciaPercorrida = 0;
		while (distanciaPercorrida < DISTANCIA_FINAL_CORRIDA){
			int rodada = (int)((Math.random() * 5) + 20);
			distanciaPercorrida += rodada;
			System.out.println("Atleta #"+idAtleta+
					" correu "+distanciaPercorrida+" metros");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void chegadaTiro(){
		System.out.println("Atleta #"+idAtleta+
				" Chegou no tiro");
	}
	
	private void tiro(){
		System.out.println("Atleta #"+idAtleta+
				" pegou a arma");
		for(int i = 0 ; i < 3 ; i++){
			int alvo = (int)((Math.random() * 5));
			tiro = tiro + alvo;
			System.out.println("Atleta #"+idAtleta+
					" acertou a pontuação "+alvo);
			int tempo = (int)((Math.random() * 100) + 200);
			try {
				Thread.sleep(tempo);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void chegadaNado(){
		System.out.println("Atleta #"+idAtleta+
				" Chegou no mar");
	}
	
	private void nado(){
		int distanciaPercorrida = 0;
		while (distanciaPercorrida < DISTANCIA_FINAL_NADO){
			int rodada = (int)((Math.random() * 4) + 10);
			distanciaPercorrida += rodada;
			System.out.println("Atleta #"+idAtleta+
					" nadou "+distanciaPercorrida+" metros");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void termino(){
		tempoFim = System.currentTimeMillis();
		long tempo = (int)(((tempoFim - tempoInicio) / 1000) - tiro);
		rankingTempo[idAtleta] = tempo;
		rankingTiro[idAtleta] = tiro;
		System.out.println("Atleta #"+idAtleta+
				" Terminou o Triatlo com "+tiro+" pontos e "+tempo+" segundos");
		posicao++;
	}
	
	private void saidaRanking(){
		if (posicao == totalAtletas - 1){
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (int i = 0 ; i < totalAtletas ; i++){
				long menor = rankingTempo[0];
				int id = 0;
				for (int j = 0 ; j < totalAtletas ; j++){
					if (rankingTempo[j] < menor){
						menor = rankingTempo[j];
						id = j;
					}
				}
				rankingFinal[i][0] = id;
				rankingFinal[i][1] = menor;
				rankingTempo[id] = 999999999;
			}
			for (int i = 1 ; i <= totalAtletas; i++){
				System.out.println(i+"º - Atleta #"+
						rankingFinal[i-1][0]+
						" com "+rankingFinal[i-1][1]+
						" segundos");
			}
		}
	}
	
	@Override
	public void run() {
		largada();
		corrida();
		chegadaTiro();
		try {
			semaforoTiro.acquire();
			tiro();
			chegadaNado();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaforoTiro.release();
			nado();
			termino();
			saidaRanking();
		}
	}

}
