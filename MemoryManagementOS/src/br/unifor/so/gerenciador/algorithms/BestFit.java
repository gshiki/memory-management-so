package br.unifor.so.gerenciador.algorithms;

import java.util.List;

import br.unifor.so.gerenciador.Memory;
import br.unifor.so.gerenciador.MemoryBlock;
import br.unifor.so.gerenciador.Process;
import br.unifor.so.gerenciador.Status;

public class BestFit {
	private List<Process> processList;
	private List<Process> abortedList;
	private Memory memory;
	
	/** CONSTRUCTOR **/
	
	public BestFit(List<Process> processList, List<Process> abortedList, Memory memory) {
		this.processList = processList;
		this.abortedList = abortedList;
		this.memory = memory;
	}
	
	/** GETTERS & SETTERS **/
	
	public List<Process> getProcessList() {
		return processList;
	}

	public List<Process> getAbortedList() {
		return abortedList;
	}

	/** METHODS **/
	
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
				// ... verifica se há espaço na memória para a criação de um bloco com o tamanho correspondente ao processo.
				if (memory.canCreateBlock(process.getBytes())) {
					memory.insertFreeBlock(process.getBytes());
					isAllocated = insertProcess(pointer, process);
				// Senão, o processo é abortado.
				}else{
					abort(process);
				}
			}
		}
	}
	
	public boolean insertProcess(MemoryBlock pointer, Process process){
		int id = -1;
		int diff = memory.getTotalSize();
		while(pointer.getNextBlock() != null){
			// ...verifica se o processo encaixa no bloco de memória.
			if (doesProcessFitMemoryBlock(process, pointer.getNextBlock())) {
				if (pointer.getNextBlock().getTotalSize() - process.getBytes() < diff) {
					diff = pointer.getNextBlock().getTotalSize() - process.getBytes();
					id = pointer.getNextBlock().getId();
				}
			}
			pointer = pointer.getNextBlock();
		}
		
		if (id != -1) {
			process.setStatus(Status.IN_USE);
			
			memory.searchFreeMemoryBlock(id).setProcess(process);
			memory.searchFreeMemoryBlock(id).setUsedSpace(process.getBytes());
			
			memory.transferFreeToBusy(id);
			System.out.println(" <<<<<<<<<<<<<<<<<< ALOCOU PROCESSO " + process.getId());
			return true;
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
		System.out.println(" ################# ABORTOU O " + process.getId());
	}
}
