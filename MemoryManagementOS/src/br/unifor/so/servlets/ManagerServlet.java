package br.unifor.so.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.unifor.so.gerenciador.GarbageCollector;
import br.unifor.so.gerenciador.Manager;
import br.unifor.so.gerenciador.Memory;
import br.unifor.so.gerenciador.MemoryBlock;
import br.unifor.so.gerenciador.Process;
import br.unifor.so.gerenciador.Status;

@WebServlet("/ManagerServlet")
public class ManagerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Date systemCycle;
	private Manager manager;
	private Memory memory;
	private GarbageCollector garbage;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String act = request.getParameter("action");
		
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		
		if (act != null && act.equals("start")) {
			int totalMemory = Integer.parseInt(request.getParameter("memorySize"));
			int processNo = Integer.parseInt(request.getParameter("processNo"));
			int algorithm = Integer.parseInt(request.getParameter("algorithm"));
			
			memory = new Memory(totalMemory);
			garbage = new GarbageCollector(memory, algorithm);
			manager = new Manager(algorithm, totalMemory, processNo, memory);
			
			garbage.start();
			
			manager.initializeAlgorithm();
			
			List<Process> processList = manager.getProcessList();
			
			systemCycle = new Date();
			
			request.setAttribute("processList", processList);
			request.setAttribute("ready", true);
			
			rd.forward(request , response);
		} else if (act != null && act.equals("add-process")) {
			if (manager != null) {
				manager.addProcess();
			}
		} else if (act != null && act.equals("execute")) {
			if (manager != null) {
				manager.execute();
				
				List<MemoryBlock> blocks = manager.organizeBlocksById(manager.getBlocks());
				List<Process> processList = manager.getProcessList();
				List<Process> completedList = garbage.getCompletedList();
				List<Process> abortedList = manager.getAbortedList();
				
				response.setContentType("text/plain");
				response.setCharacterEncoding("UTF-8");
				
				response.getWriter().append(rollCycle());
				response.getWriter().append(buildContainerMemoryBlocks(blocks, manager.getAlgorithm()));
				response.getWriter().append(buildContainerProcess(processList));
				response.getWriter().append(buildContainerCompletedProcesses(completedList));
				response.getWriter().append(buildContainerAbortedProcesses(abortedList));
				response.getWriter().flush();
			}
		}
	}
	
	public String rollCycle() {
		String html = "";
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.setTime(systemCycle);
		calendar.roll(Calendar.SECOND, 1);
		
		systemCycle = calendar.getTime();
		
		html += "tempoSistemaContainer=";
		html += "<div>" + systemCycle.getTime() + "</div>";
		
		return html;
	}
	
	public String buildContainerMemoryBlocks(List<MemoryBlock> blocks, int algorithm) {
		String html = "";
		
		html += "memoryBlockContainer=";
		
		for (int i = 0; i < blocks.size(); i++) {
			MemoryBlock block = blocks.get(i);
			
			if (block.getStatus() == Status.FREE) {
				html += "<div class='free'>";
				html += "#BLOCO " + block.getId() + "<br>";
				html += "Tamanho: 0/" + block.getTotalSize() + " bytes <br>";
				html += "</div>";
			} else {
				html += "<div class='busy'>";
				html += "#BLOCO " + block.getId() + "<br>";
				html += " > #P " + block.getProcess().getId() + "<br>";
				html += " > Tempo: " + block.getProcess().getTime() + " s <br>";
				html += " > Tamanho: " + block.getProcess().getBytes() +"/" + block.getTotalSize() + " bytes <br>";
				if (algorithm == 5) {
					html += " > Lista: " + block.getList() + " bytes <br>";
				}
				html += "</div>";
			}
		}
		
		html += "<separator>";
		
		return html;
	}
	
	public String buildContainerProcess(List<Process> processes) {
		String html = "";
		
		html += "processContainer=";
		
		for (int i = 0; i < processes.size(); i++) {
			Process process = processes.get(i);
		
			html += "<div class='process-info'>";
			html += "#P" + process.getId() + "<br>";
			html += "Tempo: " + process.getTime() + " s <br>";
			html += "Tamanho: " + process.getBytes() + " bytes <br>";
			html += "</div>";
		}
		
		html += "<separator>";
		
		return html;
	}
	
	public String buildContainerCompletedProcesses(List<Process> completed) {
		String html = "";
		
		html += "completedContainer=";
		
		for (int i = 0; i < completed.size(); i++) {
			Process completedProcess = completed.get(i);
		
			html += "<div class='completed'>";
			html += "#P" + completedProcess.getId() + "<br>";
			html += "Tempo: " + completedProcess.getTime() + " s <br>";
			html += "Tamanho: " + completedProcess.getBytes() + " bytes <br>";
			html += "</div>";
		}
		
		html += "<separator>";
		
		return html;
	}
	
	public String buildContainerAbortedProcesses(List<Process> aborted) {
		String html = "";
		
		html += "abortedContainer=";
		
		for (int i = 0; i < aborted.size(); i++) {
			Process abortedProcess = aborted.get(i);
		
			html += "<div class='aborted'>";
			html += "#P" + abortedProcess.getId() + "<br>";
			html += "Tempo: " + abortedProcess.getTime() + " s <br>";
			html += "Tamanho: " + abortedProcess.getBytes() + " bytes <br>";
			html += "</div>";
		}
		
		html += "<separator>";
		
		return html;
	}

}
