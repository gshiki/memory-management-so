package br.unifor.so.gerenciador.algorithms;

import java.util.List;

import br.unifor.so.gerenciador.Memory;
import br.unifor.so.gerenciador.MemoryBlock;
import br.unifor.so.gerenciador.Process;
import br.unifor.so.gerenciador.Status;


public class NextFit {
	private List<Process> processList;
	private List<Process> abortedList;
	private Memory memory;
	private int pointer;
	private MemoryBlock point;
	
	/** CONSTRUCTOR **/
	
	public NextFit(List<Process> processList, List<Process> abortedList, Memory memory) {
		this.processList = processList;
		this.abortedList = abortedList;
		this.memory = memory;
		this.point = memory.getHeaderFree();
		this.pointer = memory.getHeaderFree().getId();	
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
		boolean isAllocated = false;
		// Se a lista de processos estiver com conteúdo...
		if (!processList.isEmpty()) {
			// ...pego o primeiro processo da lista.
			Process process = processList.remove(0);
			// Enquanto o próximo bloco não for nulo...
			isAllocated = insertProcess(process);
			// Se o processo não for alocado...
			if (!isAllocated) {
				// ... verifica se há espaço na memória para a criação de um bloco com o tamanho correspondente ao processo.
				if (memory.canCreateBlock(process.getBytes())) {
					memory.insertFreeBlock(process.getBytes());
					isAllocated = insertProcess(process);
				// Senão, o processo é abortado.
				}else{
					abort(process);
				}
			}
		}
	}
	
	public boolean insertProcess(Process process){
		MemoryBlock aux = point;
		
		if (pointer == -1 && aux.getNextBlock() == null) {
			return false;
		}
		
		if (aux.getNextBlock() != null) {
			while(aux.getNextBlock() != null){
				// ...verifica se o processo encaixa no bloco de memória.
				if (doesProcessFitMemoryBlock(process, aux.getNextBlock())) {
					// Se encaixar, aloca o processo no bloco de memória.
					pointer = aux.getNextBlock().getId();
					point = aux.getNextBlock();
					process.setStatus(Status.IN_USE);
					aux.getNextBlock().setProcess(process);
					aux.getNextBlock().setUsedSpace(process.getBytes());
					memory.transferFreeToBusy(aux.getNextBlock().getId());
					System.out.println("ALOCOU PROCESSO " + process.getId());
					return true;
					// Senão, verifica no próximo bloco de memória.
				}else{
					aux = aux.getNextBlock();
				}
			}
		}
		if (aux.getStatus() == Status.BUSY) {
			while (aux.getStatus() == Status.BUSY) {
				if (memory.searchFreeMemoryBlock(pointer + 1) != null) {
					if (doesProcessFitMemoryBlock(process, aux)) {
						aux = memory.searchFreeMemoryBlock(pointer + 1);
						break;
					}
				} else if (memory.searchBusyMemoryBlock(pointer + 1) != null){
					if (doesProcessFitMemoryBlock(process, aux)) {
						aux = memory.searchBusyMemoryBlock(pointer + 1);
						break;
					}
				}
				pointer++;
			}
			
			pointer = aux.getId();
			point = aux;
			process.setStatus(Status.IN_USE);
			aux.setProcess(process);
			aux.setUsedSpace(process.getBytes());
			memory.transferFreeToBusy(aux.getId());
			System.out.println("ALOCOU PROCESSO " + process.getId());
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
