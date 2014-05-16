package br.unifor.so.gerenciador.algorithms;

import java.util.ArrayList;
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
	
	// Insere um processo da lista de processos de acordo com o tamanho do bloco de memória livre
	public void allocate(){
		MemoryBlock pointer = memory.getHeaderFree();
		boolean isAllocated = false;
		// Se a lista de processos estiver com conteúdo...
		if (!processList.isEmpty()) {
			// ...pego o primeiro processo da lista.
			Process process = processList.remove(0);
			// Enquanto o próximo bloco não for nulo...
			isAllocated = insertProcess(pointer, process);
			// Se o processo não for alocado...
			if (!isAllocated) {
				abort(process);
			}
		}
	}
	
	public boolean insertProcess(MemoryBlock pointer, Process process){
		while(pointer.getNextBlock() != null){
			// ...verifica se o processo encaixa no bloco de memória.
			if (doesProcessFitMemoryBlock(process, pointer.getNextBlock())) {
				if (pointer.getNextBlock().getTotalSize() - process.getBytes() > 0) {
					if (pointer.getNextBlock().getNextBlock() != null) {
						memory.insertFreeBlockAfter(pointer.getNextBlock().getTotalSize() - process.getBytes(), pointer.getNextBlock());
						organizeBlocksId(pointer.getNextBlock());
					} else {
						memory.insertFreeBlock(pointer.getNextBlock().getTotalSize() - process.getBytes());
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
			// Senão, verifica no próximo bloco de memória.
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
	
	public void organizeBlocksId(MemoryBlock block){
		int blockId = block.getId();
		List<MemoryBlock> list = getBlocks();
		MemoryBlock aux = null;
		MemoryBlock last = null;
		
		for (int i = blockId; i < list.size()-2; i++) {
			if (memory.searchFreeMemoryBlock(i + 1) != null) {
				aux = memory.searchFreeMemoryBlock(i + 1);
			} else if (memory.searchBusyMemoryBlock(i + 1) != null){
				aux = memory.searchBusyMemoryBlock(i + 1);
			}
			
			if (aux == last) {
				if (memory.searchFromFreeMemoryBlock(i + 1, aux) != null) {
					aux = memory.searchFromFreeMemoryBlock(i + 1, aux);
				} else if (memory.searchFromBusyMemoryBlock(i + 1, aux) != null){
					aux = memory.searchFromBusyMemoryBlock(i + 1, aux);
				}
			} 
			
			aux.setId(i + 2);
			last = aux;
		}
		
		block.getNextBlock().setId(blockId + 1);
	}
	
	public List<MemoryBlock> getBlocks() {
		MemoryBlock busy = memory.getHeaderBusy();
		MemoryBlock free = memory.getHeaderFree();
		
		List<MemoryBlock> blocks = new ArrayList<MemoryBlock>();
		
		while (busy.getNextBlock() != null) {
			blocks.add(busy.getNextBlock());
			
			busy = busy.getNextBlock();
		}
		while (free.getNextBlock() != null) {
			blocks.add(free.getNextBlock());
			
			free = free.getNextBlock();
		}
		return blocks;
	}
	
}