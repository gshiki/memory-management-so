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
				int id = new Integer(pointer.getNextBlock().getId());
				
				System.out.println(" ------------------- DESALOCOU " + id);
				
				completedList.add(pointer.getNextBlock().getProcess());
				
				pointer.getNextBlock().getProcess().setStatus(Status.COMPLETE);
				pointer.getNextBlock().setProcess(null);
				
				memory.transferBusyToFree(id);
				
				if (algorithm == 6) {
					merge(id);
				}
			}
			pointer = pointer.getNextBlock();
		}
	}
	
	public void merge(int id){
		MemoryBlock block = memory.searchFreeMemoryBlock(id);
		
		mergeNext(block);
		mergePrevious(block);
	}
	
	public void mergePrevious(MemoryBlock block){
		if (block.getPreviousBlock() != null && block.getPreviousBlock().getId() > -1 && block.getId() == block.getPreviousBlock().getId() + 1) {
			mergePrevious(block.getPreviousBlock());
			
			MemoryBlock toRemove = block;
			
			block.getPreviousBlock().setTotalSize(block.getPreviousBlock().getTotalSize() + toRemove.getTotalSize());
			block.getPreviousBlock().setNextBlock(toRemove.getNextBlock());
			
			if (toRemove.getNextBlock() != null) {
				toRemove.getNextBlock().setPreviousBlock(toRemove.getPreviousBlock());
			}

			block = toRemove.getPreviousBlock();
			
			toRemove.setNextBlock(null);
			toRemove.setPreviousBlock(null);
			
			organizeBlocksFromId(block);
		}
	}
	
	public void mergeNext(MemoryBlock block){
		//VE OS PROXIMOS
		if (block.getNextBlock() != null && block.getId() == block.getNextBlock().getId() - 1) {
			mergeNext(block.getNextBlock());
			
			MemoryBlock toRemove = block.getNextBlock();
			
			block.setTotalSize(block.getTotalSize() + toRemove.getTotalSize());
			block.setNextBlock(toRemove.getNextBlock());
			//CASO SEJA O ULTIMO, NAO HA PROXIMO
			if (block.getNextBlock() != null) {
				block.getNextBlock().setPreviousBlock(block);
			}
			
			toRemove.setNextBlock(null);
			toRemove.setPreviousBlock(null);
			
			organizeBlocksFromId(block);
		}
	}
	
	public void organizeBlocksFromId(MemoryBlock blockAdded){
		List<MemoryBlock> blocks = memory.getBlocks();
		
		for (int i = 0; i < blocks.size(); i++) {
			MemoryBlock block = blocks.get(i);
			
			if (block.getId() > blockAdded.getId()) {
				block.setId(block.getId() - 1);
			}
		}
	}
	
	@Override
	public void run() {
		while (true) {
			free();
			
			try {
				sleep(5 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
