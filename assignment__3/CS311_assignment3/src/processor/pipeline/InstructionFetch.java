package processor.pipeline;

import processor.Processor;

public class InstructionFetch {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
		if(IF_EnableLatch.isIF_enable() == true)
		{
			if(EX_IF_Latch.get_branch_enable() == true){
				//updates PC according to the branch instruction
				int branch_PC = EX_IF_Latch.getPC();
				containingProcessor.getRegisterFile().setProgramCounter(branch_PC);
				EX_IF_Latch.set_branch_enable(false);
			}

			//sends instruction(32 bit) binary string to next stage and then increements PC
			int curr_PC = containingProcessor.getRegisterFile().getProgramCounter();
			int newInstruction = containingProcessor.getMainMemory().getWord(curr_PC);
			IF_OF_Latch.setInstruction(newInstruction);
			containingProcessor.getRegisterFile().setProgramCounter(curr_PC + 1);
			
			//disables IF_latch and enables IF-OF Latch
			IF_EnableLatch.setIF_enable(false);
			IF_OF_Latch.setOF_enable(true);
		}
	}

}
