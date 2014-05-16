package br.unifor.so.gerenciador.algorithms;

import java.util.ArrayList;
import java.util.List;

import br.unifor.so.gerenciador.Memory;
import br.unifor.so.gerenciador.MemoryBlock;
import br.unifor.so.gerenciador.Process;
import br.unifor.so.gerenciador.Status;

public class QuickFit {
	private List<Process> processList;
	private List<Process> abortedList;
	private List<List<MemoryBlock>> mainList;
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
		insertMemoryBlockInList();
		switch (process.getBytes()) {
			case 32:
				if (!mainList.get(0).isEmpty()) {
					MemoryBlock chosen = mainList.get(0).remove(0);
					
					process.setStatus(Status.IN_USE);
					chosen.setProcess(process);
					chosen.setUsedSpace(process.getBytes());
					memory.transferFreeToBusy(chosen.getId());
					System.out.println("ALOCOU PROCESSO " + process.getId());
					return true;
				}
			case 64:
				if (!mainList.get(1).isEmpty()) {
					MemoryBlock chosen = mainList.get(1).remove(0);
					
					process.setStatus(Status.IN_USE);
					chosen.setProcess(process);
					chosen.setUsedSpace(process.getBytes());
					memory.transferFreeToBusy(chosen.getId());
					System.out.println("ALOCOU PROCESSO " + process.getId());
					return true;
				}
			case 128:
				if (!mainList.get(2).isEmpty()) {
					MemoryBlock chosen = mainList.get(2).remove(0);
					
					process.setStatus(Status.IN_USE);
					chosen.setProcess(process);
					chosen.setUsedSpace(process.getBytes());
					memory.transferFreeToBusy(chosen.getId());
					System.out.println("ALOCOU PROCESSO " + process.getId());	
					return true;
				}
			case 256:
				if (!mainList.get(3).isEmpty()) {
					MemoryBlock chosen = mainList.get(3).remove(0);
					
					process.setStatus(Status.IN_USE);
					chosen.setProcess(process);
					chosen.setUsedSpace(process.getBytes());
					memory.transferFreeToBusy(chosen.getId());
					System.out.println("ALOCOU PROCESSO" + process.getId());
					return true;
				}
			case 512:
				if (!mainList.get(4).isEmpty()) {
					MemoryBlock chosen = mainList.get(4).remove(0);
					
					process.setStatus(Status.IN_USE);
					chosen.setProcess(process);
					chosen.setUsedSpace(process.getBytes());
					memory.transferFreeToBusy(chosen.getId());
					System.out.println("ALOCOU PROCESSO " + process.getId());
					return true;
				}
			case 1024:
				if (!mainList.get(5).isEmpty()) {
					MemoryBlock chosen = mainList.get(5).remove(0);
					
					process.setStatus(Status.IN_USE);
					chosen.setProcess(process);
					chosen.setUsedSpace(process.getBytes());
					memory.transferFreeToBusy(chosen.getId());
					System.out.println("ALOCOU PROCESSO " + process.getId());
					return true;
				}
			default:
				break;
		}
		
		return false;
	
	}
	
	public void abort(Process process){
		process.setStatus(Status.ABORTED);
		abortedList.add(process);
		System.out.println("ABORTOU O " + process.getId());
	}
	
	public void createQuickLists(){
		mainList = new ArrayList<List<MemoryBlock>>();
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
	
	public void insertMemoryBlockInList(){
		MemoryBlock header = memory.getHeaderFree();
		
		while (header.getNextBlock() != null) {

			switch (header.getNextBlock().getTotalSize()) {
				case 32:
					header.getNextBlock().setList(32);
					mainList.get(0).add(header.getNextBlock());
					organizeBlocksById(mainList.get(0));
					break;
				case 64:
					header.getNextBlock().setList(64);
					mainList.get(1).add(header.getNextBlock());
					organizeBlocksById(mainList.get(1));
					break;
				case 128:
					header.getNextBlock().setList(128);
					mainList.get(2).add(header.getNextBlock());
					organizeBlocksById(mainList.get(2));
					break;
				case 256:
					header.getNextBlock().setList(256);
					mainList.get(3).add(header.getNextBlock());
					organizeBlocksById(mainList.get(3));
					break;
				case 512:
					header.getNextBlock().setList(512);
					mainList.get(4).add(header.getNextBlock());
					organizeBlocksById(mainList.get(4));
					break;
				case 1024:
					header.getNextBlock().setList(1024);
					mainList.get(5).add(header.getNextBlock());
					organizeBlocksById(mainList.get(5));
					break;
				default:
					break;
			}
			break;
				
		}
	}
	
	public List<MemoryBlock> organizeBlocksById(List<MemoryBlock> blocks){
		if (!blocks.isEmpty()) {
			for (int i = 0; i < blocks.size(); i++) {
				for (int j = i; j < blocks.size(); j++) {
					if (i == blocks.get(j).getId()) {
						MemoryBlock removedBlock = blocks.remove(j);
						blocks.add(i, removedBlock);
					}
				}
			}
		}
		
		return blocks;
	}
	
}
