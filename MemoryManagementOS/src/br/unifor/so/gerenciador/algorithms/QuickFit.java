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
	public QuickFit(List<Process> processList, List<Process> abortedList, Memory memory) {
		this.processList = processList;
		this.abortedList = abortedList;
		this.memory = memory;
		createQuickLists();
	}
	
	/** GETTERS & SETTERS **/
	
	public List<Process> getProcessList() {
		return processList;
	}

	public List<Process> getAbortedList() {
		return abortedList;
	}

	/** METHODS **/
	
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
				// ... verifica se h� espa�o na mem�ria para a cria��o de um bloco com o tamanho correspondente ao processo.
				if (memory.canCreateBlock(process.getBytes())) {
					memory.insertFreeBlock(process.getBytes());
					isAllocated = insertProcess(memory.getHeaderFree(), process);
				// Sen�o, o processo � abortado.
				}else{
					abort(process);
				}
			}
		}
	}
	
	public boolean insertProcess(MemoryBlock pointer, Process process){
		while(pointer.getNextBlock() != null){
			// ...verifica se o processo encaixa no bloco de mem�ria.
			if (doesProcessFitMemoryBlock(process, pointer.getNextBlock())) {
				// Se encaixar, aloca o processo no bloco de mem�ria.
				process.setStatus(Status.IN_USE);
				pointer.getNextBlock().setProcess(process);
				pointer.getNextBlock().setUsedSpace(process.getBytes());
				memory.transferFreeToBusy(pointer.getNextBlock().getId());
				System.out.println("ALOCOU PROCESSO " + process.getId());
				return true;
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
	
	public void createQuickLists(){
		List<List<MemoryBlock>> mainList = new ArrayList<List<MemoryBlock>>();
		List<MemoryBlock> thirtyTwo = new ArrayList<MemoryBlock>();
		List<MemoryBlock> sixtyFour = new ArrayList<MemoryBlock>();
		List<MemoryBlock> oneHundredTwentyEight = new ArrayList<MemoryBlock>();
		List<MemoryBlock> twoHundredFiftySix = new ArrayList<MemoryBlock>();
		List<MemoryBlock> fiveHundrerTwelve = new ArrayList<MemoryBlock>();
		List<MemoryBlock> oneThousandTwentyFour = new ArrayList<MemoryBlock>();
		
		mainList.add(thirtyTwo);
		mainList.add(sixtyFour);
		mainList.add(oneHundredTwentyEight);
		mainList.add(twoHundredFiftySix);
		mainList.add(fiveHundrerTwelve);
		mainList.add(oneThousandTwentyFour);
	}
	
	public void insertMemoryBlockInList(MemoryBlock header){
		
	}
	
	public void removeMemoryBlockFromList(){
		
	}
	
	public void addProcessInMemoryBlock(){
		
	}
	
	
}
