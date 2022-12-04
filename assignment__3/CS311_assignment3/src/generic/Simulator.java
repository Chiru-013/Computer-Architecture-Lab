
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
    public static int ninsts;
    public static long storeresp;
	
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
		InputStream temp=null;
		try{
			temp= new FileInputStream(assemblyProgramFile);
			DataInputStream temp2 = new DataInputStream(temp);
			int next=0;
			if(temp2.available()>0){
				next = temp2.readInt();
				processor.getRegisterFile().setProgramCounter(next);
			} 
			for(int address=0;temp2.available()>0;address++){
				next=temp2.readInt();
				processor.getMainMemory().setWord(address, next);
			}
			processor.getRegisterFile().setValue(0, 0);
			processor.getRegisterFile().setValue(1,65535);
			processor.getRegisterFile().setValue(2, 65535);
			temp2.close();
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
			processor.getIFUnit().performIF();
			Clock.incrementClock();
			//System.out.println("he1llo");
			processor.getOFUnit().performOF();
			Clock.incrementClock();
			//System.out.println("hell2o");
			processor.getEXUnit().performEX();
			Clock.incrementClock();
			//System.out.println("hell3o");
			processor.getMAUnit().performMA();
			Clock.incrementClock();
			//System.out.println("he4llo");
			processor.getRWUnit().performRW();
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

    public static Object getEventQueue() {
        return null;
    }
}

