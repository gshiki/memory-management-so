package br.unifor.so.gerenciador.algorithms;

import java.util.List;

import br.unifor.so.gerenciador.Memory;
import br.unifor.so.gerenciador.MemoryBlock;
import br.unifor.so.gerenciador.Process;
import br.unifor.so.gerenciador.Status;

public class QuickFit {
	private List<Process> processList;
	private List<Process> abortedList;
	private Memory memory;
	
	/** CONSTRUCTOR **/
	ERRO
	public QuickFit(List<Process> processList, List<Process> abortedList, Memory memory) {
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
			isAllocated = verifyAllocation(pointer, process);
			// Se o processo não for alocado...
			if (!isAllocated) {
				// ... verifica se há espaço na memória para a criação de um bloco com o tamanho correspondente ao processo.
				if (memory.canCreateBlock(process.getBytes())) {
					memory.insertFreeBlock(process.getBytes());
					isAllocated = verifyAllocation(memory.getHeaderFree(), process);
				// Senão, o processo é abortado.
				}else{
					abort(process);
				}
			}
		}
	}
	
	public boolean verifyAllocation(MemoryBlock pointer, Process process){
		while(pointer.getNextBlock() != null){
			// ...verifica se o processo encaixa no bloco de memória.
			if (doesProcessFitMemoryBlock(process, pointer.getNextBlock())) {
				// Se encaixar, aloca o processo no bloco de memória.
				process.setStatus(Status.IN_USE);
				pointer.getNextBlock().setProcess(process);
				pointer.getNextBlock().setUsedSpace(process.getBytes());
				memory.transferFreeToBusy(pointer.getNextBlock().getId());
				System.out.println("ALOCOU PROCESSO " + process.getId());
				return true;
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
	
	public void createQuickLists(){
	}
	
}
