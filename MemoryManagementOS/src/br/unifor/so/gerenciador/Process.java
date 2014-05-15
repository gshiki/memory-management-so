package br.unifor.so.gerenciador;

import java.util.Random;

public class Process {
	
	public static int PROCESS_ID = 0;
	
	private int id;
	private int bytes;
	private int time;
	private Status status;
	private Random random = new Random();
	
	// CONSTRUCTOR
	
	public Process() {
		this.id = PROCESS_ID++;
		this.bytes = (int)(Math.pow(2, (random.nextInt(6) + 5)));
		this.time = random.nextInt(21) + 10;
		this.status = Status.NEW;
	}
	
	// GETTERS & SETTERS

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBytes() {
		return bytes;
	}

	public void setBytes(int bytes) {
		this.bytes = bytes;
	}

	public int getTime() {
		return time;
	}

	public void lowerTime() {
		System.out.println("======== ID " + getId() + " ========");
		System.out.println("TEMPO " + getTime());
		System.out.println("BYTES " + getBytes());
		System.out.println("STATUS " + getStatus());
		time--;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
