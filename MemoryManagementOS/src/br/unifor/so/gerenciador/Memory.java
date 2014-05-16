package br.unifor.so.gerenciador;

import java.util.ArrayList;
import java.util.List;


public class Memory {
	private int totalSize;
	private MemoryBlock headerFree;
	private MemoryBlock headerBusy;
	
	// CONSTRUCTOR
	
	public Memory(int totalSize) {
		this.headerFree = new MemoryBlock();
		this.headerBusy = new MemoryBlock();
		
		this.totalSize = totalSize;
	}
	
	// GETTERS & SETTERS
	
	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public MemoryBlock getHeaderFree() {
		return headerFree;
	}

	public MemoryBlock getHeaderBusy() {
		return headerBusy;
	}

	// METHODS
	
	// Procura e retorna um bloco de memória livre dado seu ID
	public MemoryBlock searchFreeMemoryBlock(int id){
		MemoryBlock pointer = headerFree;
		
		while(pointer.getNextBlock() != null){
			if (pointer.getNextBlock().getId() == id) {
				return pointer.getNextBlock();
			}
			pointer = pointer.getNextBlock();
		}
		
		return null;
	}
	
	// Procura e retorna um bloco de memória ocupado dado seu ID
	public MemoryBlock searchBusyMemoryBlock(int id){
		MemoryBlock pointer = headerBusy;
		
		while(pointer.getNextBlock() != null){
			if (pointer.getNextBlock().getId() == id) {
				return pointer.getNextBlock();
			}
			pointer = pointer.getNextBlock();
		}
		
		return null;
	}
	
	// Retorna o espaço usado por blocos na lista de Free
	public int getUsedFreeSpace(){
		int usedSpace = 0;
		MemoryBlock pointer = headerFree;
		
		while (pointer.getNextBlock() != null) {
			usedSpace += pointer.getNextBlock().getTotalSize();
			pointer = pointer.getNextBlock();
		}
		
		return usedSpace;
	}
	
	// Retorna o espaço usado por blocos na lista de Busy
	public int getUsedBusySpace(){
		int usedSpace = 0;
		MemoryBlock pointer = headerBusy;
		
		while (pointer.getNextBlock() != null) {
			usedSpace += pointer.getNextBlock().getTotalSize();
			pointer = pointer.getNextBlock();
		}
		
		return usedSpace;
	}
	
	// Retorna o espaço usado por blocos em ambas as listas
	public int getTotalUsedSpace(){
		return getUsedBusySpace()+getUsedFreeSpace();
	}
	
	// Retorna o espaço que esta sobrando em toda a memória
	public int getTotalRemainingSpace(){
		return totalSize - (getUsedBusySpace()+getUsedFreeSpace());
	}
	
	public List<MemoryBlock> getBlocks() {
		MemoryBlock busy = getHeaderBusy();
		MemoryBlock free = getHeaderFree();
		
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
	
	// Verifica se um bloco pode ser criado na memória
	public boolean canCreateBlock(int size){
		int result = totalSize - getTotalUsedSpace();
		if (result >= size) {
			return true;
		}
		return false;
	}
	
	// Retorna o espaço restante na lista de Free
	public int getFreeSpace(){
		int usedSpace = 0;
		MemoryBlock pointer = headerFree;
		
		while (pointer.getNextBlock() != null) {
			usedSpace += pointer.getNextBlock().getTotalSize();
			pointer = pointer.getNextBlock();
		}
		
		return totalSize-usedSpace;
	}
	
	// Retorna o espaço restante na lista de ocupados
	public int getBusySpace(){
		int usedSpace = 0;
		MemoryBlock pointer = headerBusy;
		
		while (pointer.getNextBlock() != null) {
			usedSpace += pointer.getNextBlock().getTotalSize();
			pointer = pointer.getNextBlock();
		}
		
		return totalSize-usedSpace;
	}
	
	// Verifica se há espaço restante na lista de livres
	public boolean hasFreeSpace(){
		return getFreeSpace() > 0;
	}
	
	// Insere um bloco de memória no final da lista de livres
	public void insertFreeBlock(int totalSize){
		System.out.println(">>>>>>>>>>>>>> CRIOU UM BLOCO NOVO DE TAMANHO : " + totalSize);
		MemoryBlock newBlock = new MemoryBlock(totalSize);
		MemoryBlock pointer = headerFree;
		newBlock.setStatus(Status.FREE);
		
		while (pointer.getNextBlock() != null) {
			pointer = pointer.getNextBlock();
		}
		
		pointer.setNextBlock(newBlock);
		newBlock.setPreviousBlock(pointer);
		
	}
	
	// Insere um bloco de memória na lista de livres
	public void insertFreeBlockAfter(int totalSize, MemoryBlock block){
		System.out.println(">>>>>>>>>>>>>> CRIOU UM BLOCO NOVO DE TAMANHO : " + totalSize);
		MemoryBlock newBlock = new MemoryBlock(totalSize);
		newBlock.setStatus(Status.FREE);
		
		newBlock.setNextBlock(block.getNextBlock());
		block.getNextBlock().setPreviousBlock(newBlock);
		block.setNextBlock(newBlock);
		newBlock.setPreviousBlock(block);
	}
	 
	// Remove um bloco de memória da lista de Free
	public void deleteFreeBlock(int id){
		MemoryBlock pointer = headerFree;
		
		while(pointer.getNextBlock() != null){
			if (pointer.getNextBlock().getId() == id) {
				MemoryBlock aux = pointer.getNextBlock();
				if (aux.getNextBlock() == null) {
					pointer.setNextBlock(null);
				}else{
					pointer.setNextBlock(aux.getNextBlock());
					pointer.getNextBlock().setPreviousBlock(pointer);
					aux.setNextBlock(null);
				}
				aux.setPreviousBlock(null);
				
				break;
			}
			pointer = pointer.getNextBlock();
		}
	}
	
	// Imprime conteúdo da lista de blocos livres
	public void printFreeBlocks(){
		MemoryBlock pointer = headerFree.getNextBlock();
		while (pointer != null) {
			System.out.println("ID: " + pointer.getId());
			System.out.println("TAMANHO TOTAL: " + pointer.getTotalSize());
			System.out.println("ESPAÇO USADO: " + pointer.getUsedSpace());
			System.out.println("STATUS: " + pointer.getStatus());
			if (pointer.getPreviousBlock() != null) {
				System.out.println("ANTERIOR: " + pointer.getPreviousBlock().getId());
			}
			if (pointer.getNextBlock() != null) {
				System.out.println("PROXIMO: " + pointer.getNextBlock().getId());
			}
			System.out.println("============================");
			
			pointer = pointer.getNextBlock();
		}
	}
	
	// Imprime conteúdo da lista de blocos ocupados
	public void printBusyBlocks(){
		MemoryBlock pointer = headerBusy.getNextBlock();
		while (pointer != null) {
			System.out.println("ID: " + pointer.getId());
			System.out.println("TAMANHO TOTAL: " + pointer.getTotalSize());
			System.out.println("ESPAÇO USADO: " + pointer.getUsedSpace());
			System.out.println("STATUS: " + pointer.getStatus());
			if (pointer.getPreviousBlock() != null) {
				System.out.println("ANTERIOR: " + pointer.getPreviousBlock().getId());
			}
			if (pointer.getNextBlock() != null) {
				System.out.println("PROXIMO: " + pointer.getNextBlock().getId());
			}
			System.out.println("============================");
			
			pointer = pointer.getNextBlock();
		}
	}
	
	// Transfere um bloco da lista de livres para a lista de ocupados
	public void transferFreeToBusy(int id){
		MemoryBlock pointerFree = headerFree;
		MemoryBlock pointerBusy = headerBusy;
		
		while(pointerFree.getNextBlock() != null){
			if (pointerFree.getNextBlock().getId() == id) {
				MemoryBlock aux = pointerFree.getNextBlock();
				if (aux.getNextBlock() == null) {
					pointerFree.setNextBlock(null);
				}else{
					pointerFree.setNextBlock(aux.getNextBlock());
					pointerFree.getNextBlock().setPreviousBlock(pointerFree);
					aux.setNextBlock(null);
				}
				
				aux.setPreviousBlock(null);
				
				while (pointerBusy.getNextBlock() != null) {
					if (pointerBusy.getId() < aux.getId() && pointerBusy.getNextBlock().getId() > aux.getId()) {
						aux.setStatus(Status.BUSY);
						aux.setNextBlock(pointerBusy.getNextBlock());
						aux.setPreviousBlock(pointerBusy);
						aux.getNextBlock().setPreviousBlock(aux);
						pointerBusy.setNextBlock(aux);
						break;
					}
					pointerBusy = pointerBusy.getNextBlock();
				}
				
				if (pointerBusy.getNextBlock() == null) {
					aux.setStatus(Status.BUSY);
					aux.setPreviousBlock(pointerBusy);
					pointerBusy.setNextBlock(aux);
				}
				
				break;
			}
			pointerFree = pointerFree.getNextBlock();
		}
	}
	
	// Transfere um bloco da lista de ocupados para a lista de livres
	public void transferBusyToFree(int id){
		MemoryBlock pointerFree = headerFree;
		MemoryBlock auxBusy = headerBusy;
		
		while(auxBusy.getNextBlock() != null){
			if (auxBusy.getNextBlock().getId() == id) {
				MemoryBlock aux = auxBusy.getNextBlock();
				if (aux.getNextBlock() == null) {
					auxBusy.setNextBlock(null);
				}else{
					auxBusy.setNextBlock(aux.getNextBlock());
					auxBusy.getNextBlock().setPreviousBlock(auxBusy);
					aux.setNextBlock(null);
				}
				
				aux.setPreviousBlock(null);
				
				while (pointerFree.getNextBlock() != null) {
					if (pointerFree.getId() < aux.getId() && pointerFree.getNextBlock().getId() > aux.getId()) {
						aux.setStatus(Status.FREE);
						aux.setNextBlock(pointerFree.getNextBlock());
						aux.setPreviousBlock(pointerFree);
						aux.getNextBlock().setPreviousBlock(aux);
						pointerFree.setNextBlock(aux);
						break;
					}
					pointerFree = pointerFree.getNextBlock();
				}
				
				if (pointerFree.getNextBlock() == null) {
					aux.setStatus(Status.FREE);
					aux.setPreviousBlock(pointerFree);
					pointerFree.setNextBlock(aux);
				}
				
				break;
			}
			auxBusy = auxBusy.getNextBlock();
		}
	}
	
}
