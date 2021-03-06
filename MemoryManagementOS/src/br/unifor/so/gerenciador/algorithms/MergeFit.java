package br.unifor.so.gerenciador.algorithms;

import java.util.List;

import br.unifor.so.gerenciador.Memory;
import br.unifor.so.gerenciador.MemoryBlock;
import br.unifor.so.gerenciador.Process;
import br.unifor.so.gerenciador.Status;

public class MergeFit {
	private List<Process> processList;
	private List<Process> abortedList;
	private Memory memory;
	
	/** CONSTRUCTOR **/
	
	public MergeFit(List<Process> processList, List<Process> abortedList, Memory memory) {
		this.processList = processList;
		this.abortedList = abortedList;
		this.memory = memory;
		init();
	}
	
	/** GETTERS & SETTERS **/
	
	public List<Process> getProcessList() {
		return processList;
	}

	public List<Process> getAbortedList() {
		return abortedList;
	}

	/** METHODS **/
	
	// Cria um bloco unico inicialmente
	public void init(){
		memory.insertFreeBlock(memory.getTotalSize());
	}
	
	// Insere um processo da lista de processos de acordo com o tamanho do bloco de mem�ria livre
	public void allocate(){
		MemoryBlock pointer = memory.getHeaderFree();
		boolean isAllocated = false;
		// Se a lista de processos estiver com conte�do...
		if (!processList.isEmpty()) {
			// ...pego o primeiro processo da lista.
			Process process = processList.remove(0);
			// Enquanto o pr�ximo bloco n�o for nulo...
			isAllocated = insertProcess(pointer, process);
			// Se o processo n�o for alocado...
			if (!isAllocated) {
				abort(process);
			}
		}
	}
	
	public boolean insertProcess(MemoryBlock pointer, Process process){
		while(pointer.getNextBlock() != null){
			// ...verifica se o processo encaixa no bloco de mem�ria.
			if (doesProcessFitMemoryBlock(process, pointer.getNextBlock())) {
				if (pointer.getNextBlock().getTotalSize() - process.getBytes() > 0) {
					if (pointer.getNextBlock().getNextBlock() != null) {
						memory.insertFreeBlockAfter(pointer.getNextBlock().getTotalSize() - process.getBytes(), pointer.getNextBlock());
						
						organizeBlocksFromId(pointer.getNextBlock());
					} else {
						memory.insertFreeBlock(pointer.getNextBlock().getTotalSize() - process.getBytes());
						
						organizeBlocksFromId(pointer.getNextBlock());
					}
					process.setStatus(Status.IN_USE);
					pointer.getNextBlock().setProcess(process);
					pointer.getNextBlock().setTotalSize(process.getBytes());
					pointer.getNextBlock().setUsedSpace(process.getBytes());
					
					memory.transferFreeToBusy(pointer.getNextBlock().getId());
					
					return true;
				} else {
					process.setStatus(Status.IN_USE);
					
					pointer.getNextBlock().setProcess(process);
					
					memory.transferFreeToBusy(pointer.getNextBlock().getId());
					
					return true;
				}
			// Sen�o, verifica no pr�ximo bloco de mem�ria.
			}else{
				pointer = pointer.getNextBlock();
			}
		}
		return false;
	}
	
	public boolean doesProcessFitMemoryBlock(Process process, MemoryBlock block){
		if (process.getBytes() <= block.getTotalSize()) {
			return true;
		}
		return false;
	}
	
	public void abort(Process process){
		process.setStatus(Status.ABORTED);
		abortedList.add(process);
		System.out.println("ABORTOU O " + process.getId());
	}
	
	public void organizeBlocksFromId(MemoryBlock blockAdded){
		List<MemoryBlock> blocks = memory.getBlocks();
		
		for (int i = 0; i < blocks.size() - 1; i++) {
			MemoryBlock block = blocks.get(i);
			
			if (block.getId() > blockAdded.getId()) {
				block.setId(block.getId() + 1);
			}
		}
		
		blockAdded.getNextBlock().setId(blockAdded.getId() + 1);
	}
	
}