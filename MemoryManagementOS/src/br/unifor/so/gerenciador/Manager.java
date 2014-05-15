package br.unifor.so.gerenciador;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.unifor.so.gerenciador.algorithms.BestFit;
import br.unifor.so.gerenciador.algorithms.FirstFit;
import br.unifor.so.gerenciador.algorithms.MergeFit;
import br.unifor.so.gerenciador.algorithms.NextFit;
import br.unifor.so.gerenciador.algorithms.QuickFit;
import br.unifor.so.gerenciador.algorithms.WorstFit;

public class Manager {
	private int algorithm;
	private int totalSize;
	private int processQtd;
	private Memory memory;
	private List<Process> processList;
	private List<Process> abortedList;

	private MergeFit mergeFit;
	private FirstFit firstFit;
	private QuickFit quickFit;
	private BestFit bestFit;
	private WorstFit worstFit;
	private NextFit nextFit;
	
	// CONSTRUCTOR
	
	public Manager(int algorithm, int totalSize, int processQtd, Memory memory){
		this.algorithm = algorithm;
		this.totalSize = totalSize;
		this.processQtd = processQtd;
		this.memory = memory;
		this.abortedList = new ArrayList<Process>();
		this.processList = createProcessList();
	}
	
	// GETTERS & SETTERS
	
	public int getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(int algorithm) {
		this.algorithm = algorithm;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public int getProcessQtd() {
		return processQtd;
	}

	public void setProcessQtd(int processQtd) {
		this.processQtd = processQtd;
	}

	public Memory getMemory() {
		return memory;
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	public List<Process> getProcessList() {
		return processList;
	}

	public void setProcessList(List<Process> processList) {
		this.processList = processList;
	}
	
	public List<Process> getAbortedList() {
		return abortedList;
	}

	public void setAbortedList(List<Process> abortedList) {
		this.abortedList = abortedList;
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
	
	// METHODS
	
	public List<Process> createProcessList(){
		List <Process> list = new ArrayList<Process>();
		
		for (int i = 0; i < processQtd; i++) {
			list.add(new Process());
		}
		
		return list;
	}
	
	public void lowerProcessesTime(){
		MemoryBlock pointer = memory.getHeaderBusy();
		
		while(pointer.getNextBlock() != null){
			if (pointer.getNextBlock().getProcess().getTime() > 0) {
				System.out.println("------------------MEMORY BLOCK ID " + pointer.getNextBlock().getId());
				pointer.getNextBlock().getProcess().lowerTime();
			}
			pointer = pointer.getNextBlock();
		}
	}
	
	public void addProcess(){
		Process process = new Process();
		processList.add(process);
	}
	
	public void initializeAlgorithm(){
		switch (algorithm) {
		case 1:
			firstFit = new FirstFit(processList, abortedList, memory);
			break;
		case 2:
			bestFit = new BestFit(processList, abortedList, memory);
			break;
		case 3:
			worstFit = new WorstFit(processList, abortedList, memory);
			break;
		case 4:
			nextFit = new NextFit(processList, abortedList, memory);
			break;
		case 5:
			quickFit = new QuickFit(processList, abortedList, memory);
			break;
		case 6:
			mergeFit = new MergeFit(processList, abortedList, memory);
			break;	
		default:
			break;
		}
	}
	
	public void execute(){
		switch (algorithm) {
		case 1:
//			do {
//				System.out.println("++++++++++ INICIO RODADA TEMPO = " + new Date() + " ++++++++++");
				firstFit.allocate();
				lowerProcessesTime();
//				System.out.println("++++++++++ FIM RODADA TEMPO = " + new Date() + " ++++++++++");
//				try {
//					Thread.sleep(1 * 1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			} while (isRunning());
			break;
		case 2:
//			do {
//				System.out.println("++++++++++ INICIO RODADA TEMPO = " + new Date() + " ++++++++++");
				bestFit.allocate();
				lowerProcessesTime();
//				System.out.println("++++++++++ FIM RODADA TEMPO = " + new Date() + " ++++++++++");
//				try {
//					Thread.sleep(1 * 1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			} while (isRunning());
			break;
		case 3:
//			do {
//				System.out.println("++++++++++ INICIO RODADA TEMPO = " + new Date() + " ++++++++++");
				worstFit.allocate();
				lowerProcessesTime();
//				System.out.println("++++++++++ FIM RODADA TEMPO = " + new Date() + " ++++++++++");
//				try {
//					Thread.sleep(1 * 1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			} while (isRunning());
			break;
		case 4:
//			do {
//				System.out.println("++++++++++ INICIO RODADA TEMPO = " + new Date() + " ++++++++++");
				nextFit.allocate();
				lowerProcessesTime();
//				System.out.println("++++++++++ FIM RODADA TEMPO = " + new Date() + " ++++++++++");
//				try {
//					Thread.sleep(1 * 1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			} while (isRunning());
			break;
		case 5:
//			do {
//				System.out.println("++++++++++ INICIO RODADA TEMPO = " + new Date() + " ++++++++++");
				quickFit.allocate();
				lowerProcessesTime();
				quickFit.insertMemoryBlockInList();
//				System.out.println("++++++++++ FIM RODADA TEMPO = " + new Date() + " ++++++++++");
//				try {
//					Thread.sleep(1 * 1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			} while (isRunning());
			break;
		case 6:
//			do {
				System.out.println("++++++++++ INICIO RODADA TEMPO = " + new Date() + " ++++++++++");
				mergeFit.allocate();
				lowerProcessesTime();
				System.out.println("++++++++++ FIM RODADA TEMPO = " + new Date() + " ++++++++++");
//				try {
//					Thread.sleep(1 * 1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			} while (isRunning());
			break;	

		default:
			break;
		}
	}
	
	public boolean isRunning(){
		return true;
	}
}
