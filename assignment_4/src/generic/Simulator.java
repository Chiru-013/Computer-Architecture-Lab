// package generic;

// import java.io.DataInputStream;
// import java.io.FileInputStream;
// import java.io.FileNotFoundException;
// import java.io.IOException;
// import java.io.InputStream;

// import processor.Clock;
// import processor.Processor;
// import generic.Statistics;

// public class Simulator {
		
// 	static Processor processor;
// 	static boolean simulationComplete;
	
// 	public static void setupSimulation(String assemblyProgramFile, Processor p) {
// 		Simulator.processor = p;
// 		try {
// 			loadProgram(assemblyProgramFile);
// 		} catch (IOException e) {
// 			e.printStackTrace();
// 		}
		
// 		simulationComplete = false;
// 	}
	
// 	static void loadProgram(String assemblyProgramFile) throws IOException {
// 		/*
// 		 * TODO
// 		 * 1. load the program into memory according to the program layout described
// 		 *    in the ISA specification
// 		 * 2. set PC to the address of the first instruction in the main
// 		 * 3. set the following registers:
// 		 *     x0 = 0
// 		 *     x1 = 65535
// 		 *     x2 = 65535
// 		 */
// 		InputStream is = null;
// 		try {
// 			is = new FileInputStream(assemblyProgramFile);
// 		}
// 		catch (FileNotFoundException e) {
// 			e.printStackTrace();
// 		}
// 		DataInputStream dis = new DataInputStream(is);

// 		int address = -1;
// 		while(dis.available() > 0) {
// 			int next = dis.readInt();
// 			if(address == -1)
// 				processor.getRegisterFile().setProgramCounter(next);
// 			else
// 				processor.getMainMemory().setWord(address, next);
// 			address += 1;
// 		}
        
//         processor.getRegisterFile().setValue(0, 0);
//         processor.getRegisterFile().setValue(1, 65535);
//         processor.getRegisterFile().setValue(2, 65535);
        
// 	}
			
// 	public static void simulate() {
// 		while(simulationComplete == false) {
// 			processor.getRWUnit().performRW();
// 			Clock.incrementClock();
// 			processor.getMAUnit().performMA();
// 			Clock.incrementClock();	
// 			processor.getEXUnit().performEX();
// 			Clock.incrementClock();	
// 			processor.getOFUnit().performOF();
// 			Clock.incrementClock();
// 			processor.getIFUnit().performIF();
// 			Clock.incrementClock();

// 			Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
// 			Statistics.setNumberOfCycles(Statistics.getNumberOfCycles() + 1);
// 		}
		
// 		// System.out.println("Number of Cycles: " + Statistics.getNumberOfCycles());
// 		// System.out.println("Number of OF Stalls: " + (Statistics.getNumberOfInstructions() - Statistics.getNumberOfRegisterWriteInstructions()));
// 		// System.out.println("Number of Wrong Branch Instructions: " + Statistics.getNumberOfBranchTaken());
// 	}
	
// 	public static void setSimulationComplete(boolean value) {
// 		simulationComplete = value;
// 	}
// }

package generic;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import processor.Clock;
import processor.Processor;

public class Simulator {
		
	static Processor processor;
	static boolean simulationComplete;
	
	public static void setupSimulation(String assemblyProgramFile, Processor p)
	{
		Simulator.processor = p;
		loadProgram(assemblyProgramFile);
		
		simulationComplete = false;
	}
	
	static void loadProgram(String assemblyProgramFile)
	{
		/*
		 * TOD0
		 * 1. load the program into memory according to the program layout described
		 *    in the ISA specification
		 * 2. set PC to the address of the first instruction in the main
		 * 3. set the following registers:
		 *     x0 = 0
		 *     x1 = 65535
		 *     x2 = 65535
		 */
		InputStream tumajar=null;
		try{
			tumajar= new FileInputStream(assemblyProgramFile);
			DataInputStream bisaun = new DataInputStream(tumajar);
			int next=0;
			if(bisaun.available()>0){
				next = bisaun.readInt();
				processor.getRegisterFile().setProgramCounter(next);
			} 
			for(int address=0;bisaun.available()>0;address++){
				next=bisaun.readInt();
				processor.getMainMemory().setWord(address, next);
			}
			processor.getRegisterFile().setValue(0, 0);
			processor.getRegisterFile().setValue(1,65535);
			processor.getRegisterFile().setValue(2, 65535);
			bisaun.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}

	}
	
	public static void simulate()
	{
		int numinst = 0;
		int numcycles = 0;
		while(simulationComplete == false)
		{
			processor.getRWUnit().performRW();
			Clock.incrementClock();
			processor.getMAUnit().performMA();
			Clock.incrementClock();	
			processor.getEXUnit().performEX();
			Clock.incrementClock();	
			processor.getOFUnit().performOF();
			Clock.incrementClock();
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			// System.out.println(processor.getRegisterFile().getContentsAsString()); 
			++numinst;
			++numcycles;
		}
		  
		// TOD0
		// set statistics
		Statistics.setNumberOfInstructions(numinst);
		Statistics.setNumberOfCycles(numcycles);
		
		// Statistics.setCPI(numinst, numcycles);
		// Statistics.setIPC(numinst, numcycles);
	}
	
	public static void setSimulationComplete(boolean value)
	{
		simulationComplete = value;
	}
}
