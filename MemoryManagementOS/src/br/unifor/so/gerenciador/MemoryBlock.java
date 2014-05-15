package br.unifor.so.gerenciador;

public class MemoryBlock {
	private static int BLOCK_ID = 0;
	
	private int id;
	private int list;
	private int totalSize;
	private int usedSpace;
	private Process process;
	private Status status;
	private MemoryBlock nextBlock;
	private MemoryBlock previousBlock;
	
	// CONSTRUCTOR
	
	public MemoryBlock() {
		this.id = -1;
		this.totalSize = 0;
		this.usedSpace = 0;
		this.nextBlock = null;
		this.previousBlock = null;
	}
	
	public MemoryBlock(int totalSize) {
		this.id = BLOCK_ID++;
		this.totalSize = totalSize;
		this.usedSpace = 0;
		this.nextBlock = null;
		this.previousBlock = null;
	}

	// GETTERS & SETTERS
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getList() {
		return list;
	}
	
	public void setList(int list) {
		this.list = list;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public int getUsedSpace() {
		return usedSpace;
	}

	public void setUsedSpace(int usedSpace) {
		this.usedSpace = usedSpace;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public MemoryBlock getNextBlock() {
		return nextBlock;
	}

	public void setNextBlock(MemoryBlock nextBlock) {
		this.nextBlock = nextBlock;
	}

	public MemoryBlock getPreviousBlock() {
		return previousBlock;
	}

	public void setPreviousBlock(MemoryBlock previousBlock) {
		this.previousBlock = previousBlock;
	}
	
	// METHODS

	
}
