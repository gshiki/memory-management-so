package br.unifor.so.gerenciador;

public class Main {
	static int MEMORY_SIZE = 512;
	static int ALGORITHM = 6;
	static int PROCESS = 10;
	
	
	public static void main(String[] args) {
		Memory memory = new Memory(MEMORY_SIZE);
		GarbageCollector garbage = new GarbageCollector(memory, ALGORITHM);
		Manager manager = new Manager(ALGORITHM, MEMORY_SIZE, PROCESS, memory);
		
		garbage.start();
		
		System.out.println("----------PROCESSOS");
		if (manager.getProcessList().isEmpty() == false) {
			for (int i = 0; i < manager.getProcessList().size(); i++) {
				System.out.println(manager.getProcessList().get(i).getBytes());
			}
		}
		
		manager.initializeAlgorithm();
		manager.execute();
		
	}
}
