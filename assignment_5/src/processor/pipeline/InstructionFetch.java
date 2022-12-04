package processor.pipeline;

import configuration.Configuration;
import generic.*;
import processor.Clock;
import processor.Processor;

public class InstructionFetch implements Element{
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	int currPC;
	 
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
		if(IF_EnableLatch.isIF_enable()) {
			if(IF_EnableLatch.is_busy == true) return;

			currPC = containingProcessor.getRegisterFile().getProgramCounter();
			if(EX_IF_Latch.isBranchTaken == true) {
				currPC = currPC + EX_IF_Latch.offset - 1;
				EX_IF_Latch.isBranchTaken = false;
			}
			
			// System.out.println("IF is enabled with instruction: " + Integer.toBinaryString(newInstruction) + "..");
			//System.out.println("currPC " + Integer.toString(currPC));
			Simulator.ninsts++;
			Simulator.getEventQueue().addEvent(
				new MemoryReadEvent(
					Clock.getCurrentTime() + Configuration.mainMemoryLatency, 
					this, 
					containingProcessor.getMainMemory(), 
					currPC)
			);

			IF_EnableLatch.is_busy = true;
		}
	}
 
	@Override

	public void handleEvent(Event e) {
		if(IF_OF_Latch.is_busy == true) {
			//System.out.println("IFOF is busy");
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		}
		else {
			MemoryResponseEvent event = (MemoryResponseEvent) e ; 
			//System.out.println("Memory is responding");
			if(EX_IF_Latch.isBranchTaken == false)	{
				IF_OF_Latch.setInstruction(event.getValue());
				// Simulator.ninsts++;
			}
			else IF_OF_Latch.setInstruction(0);
			IF_OF_Latch.inst_PC = this.currPC;
			containingProcessor.getRegisterFile().setProgramCounter(this.currPC + 1);
			//System.out.println("event value " + event.getValue());
			IF_OF_Latch.setOF_enable(true);
			IF_EnableLatch.is_busy = false;

		}
	}

}
