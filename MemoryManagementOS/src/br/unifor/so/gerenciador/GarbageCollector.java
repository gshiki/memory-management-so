package br.unifor.so.gerenciador;

import java.util.ArrayList;
import java.util.List;

public class GarbageCollector extends Thread {
	private Memory memory;
	private int algorithm;
	private List<Process> completedList;
	
	// CONSTRUCTOR

	public GarbageCollector(Memory memory, int algorithm) {
		this.memory = memory;
		this.algorithm = algorithm;
		this.completedList = new ArrayList<Process>();
	}
	
	public List<Process> getCompletedList() {
		return completedList;
	}
	
	// METHODS
	
	// Procura por blocos de memória com processos finalizados e os retorna para a lista de livres
	public void free(){
		MemoryBlock pointer = memory.getHeaderBusy();
		
		while(pointer != null && pointer.getNextBlock() != null){
			if (pointer.getNextBlock().getProcess().getTime() == 0) {
				System.out.println(" ------------------- DESALOCOU " + pointer.getNextBlock().getId());
				pointer.getNextBlock().getProcess().setStatus(Status.COMPLETE);
				completedList.add(pointer.getNextBlock().getProcess());
				pointer.getNextBlock().setProcess(null);
				memory.transferBusyToFree(pointer.getNextBlock().getId());
			}
			pointer = pointer.getNextBlock();
		}
	}
	
	public void merge(){
//		MemoryBlock pointer = memory.getHeaderFree();
//		MemoryBlock aux;
//		int freeSpace = memory.getUsedFreeSpace();
//		
//		while(pointer.getNextBlock() != null){
//			aux = pointer = pointer.getNextBlock();
//			
//			int size = pointer.getTotalSize();
//			
//			while(pointer.getNextBlock() != null && pointer.getNextBlock().getId() + 1 == pointer.getId()){
//				size += pointer.getNextBlock().getTotalSize();
//				
//				memory.deleteFreeBlock(pointer.getNextBlock().getId());
//				
//				pointer = poin
//			}
//		}
//		
//		
	}
	
	@Override
	public void run() {
		while (true) {
			free();
			switch (algorithm) {
			case 6:
				merge();
				break;
			default:
				break;
			}
			try {
				sleep(5 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
