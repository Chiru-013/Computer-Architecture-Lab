






package generic;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


import processor.Clock;
import processor.Processor;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	static EventQueue eventQueue;
	public static long storeresp;
	public static int ninsts;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p) throws FileNotFoundException
	{
		eventQueue = new EventQueue();
		storeresp = 0;//
		ninsts = 0;//
		Simulator.processor = p;//
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile) throws FileNotFoundException {
		/*
		 * TODO
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */
		 
		DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(assemblyProgramFile)));
		
		try{
			int n = dis.readInt();
			int i;
			for(i=0;i<n;i++){
				int temp = dis.readInt();
				processor.getMainMemory().setWord(i,temp);
			}
			
			int pc = i;
			int offset = 1;
			processor.getRegisterFile().setProgramCounter(pc);

			while(dis.available()>0){
				int temp = dis.readInt();
				processor.getMainMemory().setWord(i,temp);
				i += offset;
			}
			
			processor.getRegisterFile().setValue(0,0);
			processor.getRegisterFile().setValue(1,65535);
			processor.getRegisterFile().setValue(2,65535);

			dis.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static EventQueue getEventQueue() { 
		return eventQueue ; 
	}//
	
	public static void simulate() {
		int ncycles = 0;
		
		while(Simulator.simulationComplete == false) {
			processor.getRWUnit().performRW();
			processor.getMAUnit().performMA();
			processor.getEXUnit().performEX();
			eventQueue.processEvents();
			processor.getOFUnit().performOF();
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			ncycles += 1;
		}
		
		// TODO
		// set statistics
		Statistics.setNumberOfInstructions(ninsts);
		Statistics.setNumberOfCycles(ncycles);
		Statistics.setIPC();//
		
	}
	
	public static void setSimulationComplete(boolean value)	{
		simulationComplete = value;
	}

}










// package generic;

// import java.io.DataInputStream;
// import java.io.FileInputStream;
// import java.io.IOException;
// import java.io.InputStream;

// import processor.Clock;
// import processor.Processor;


// public class Simulator {
		
// 	static Processor processor;
// 	static boolean simulationComplete;
// 	/**/static EventQueue eventQueue;
// 	public static long storeresp;
// 	public static int ninsts;/* */
	
// 	public static void setupSimulation(String assemblyProgramFile, Processor p)
// 	{
// 		Simulator.processor = p;
// 		loadProgram(assemblyProgramFile);
// 		storeresp = 0;//
// 		ninsts = 0;//
// 		Simulator.processor = p;//
// 		simulationComplete = false;
// 	}
	
// 	static void loadProgram(String assemblyProgramFile)
// 	{
// 		/*
// 		 * TOD0
// 		 * 1. load the program into memory according to the program layout described
// 		 *    in the ISA specification
// 		 * 2. set PC to the address of the first instruction in the main
// 		 * 3. set the following registers:
// 		 *     x0 = 0
// 		 *     x1 = 65535
// 		 *     x2 = 65535
// 		 */
// 		InputStream temp1=null;
// 		try{
// 			temp1= new FileInputStream(assemblyProgramFile);
// 			DataInputStream temp2 = new DataInputStream(temp1);
// 			int next=0;
// 			if(temp2.available()>0){
// 				next = temp2.readInt();
// 				processor.getRegisterFile().setProgramCounter(next);
// 			} 


// 			for(int address=0;temp2.available()>0;address++){
// 				next=temp2.readInt();
// 				processor.getMainMemory().setWord(address, next);
// 				address++;
// 			}
// 			processor.getRegisterFile().setValue(0, 0);
// 			processor.getRegisterFile().setValue(1,65535);
// 			processor.getRegisterFile().setValue(2, 65535);
// 			temp2.close();
// 		}
// 		catch(IOException e){
// 			e.printStackTrace();
// 		}

// 	}

// 	public static EventQueue getEventQueue() { 
// 		return eventQueue ; 
// 	}//
	
// 	public static void simulate()
// 	{
// 		int ncycles = 0;
// 		while(simulationComplete == false)
// 		{
// 			processor.getRWUnit().performRW();
// 			processor.getMAUnit().performMA();
// 			processor.getEXUnit().performEX();
// 			eventQueue.processEvents();
// 			processor.getOFUnit().performOF();
// 			processor.getIFUnit().performIF();
// 			Clock.incrementClock();
// 			// System.out.println(processor.getRegisterFile().getContentsAsString()); 
// 			++ncycles;
// 		}
		  
// 		// TOD0
// 		// set statistics
// 		Statistics.setNumberOfInstructions(ninsts);
// 		Statistics.setNumberOfCycles(ncycles);
// 		Statistics.setIPC();//
// 		// Statistics.setCPI(numinst, ncycles);
// 		// Statistics.setIPC(numinst, ncycles);
// 	}
	
// 	public static void setSimulationComplete(boolean value)
// 	{
// 		simulationComplete = value;
// 	}

	

