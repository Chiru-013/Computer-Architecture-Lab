package processor.pipeline;

import generic.Instruction;
import generic.Simulator;
import generic.Instruction.OperationType;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable() == true)
		{
			//TODO
			Instruction inst = MA_RW_Latch.getInstruction();
			OperationType op_type = inst.getOperationType();
			String optype = inst.getOperationType().toString();
			
			if(optype.equals("store")|| optype.equals("jmp")|| optype.equals("beq")|| optype.equals("bne")|| optype.equals("blt") || optype.equals("bgt")){
				//just get out nub
				/*
				 * 
				 * 
				 * 
				 * 
				 */
			}
			else if(optype.equals("load")){
				int load_result = MA_RW_Latch.getLoad_result();
				int rd = inst.getDestinationOperand().getValue();
				containingProcessor.getRegisterFile().setValue(rd, load_result);
			}
			else if(optype.equals("end")){
				Simulator.setSimulationComplete(true);
			}
			else {
				int alu_result = MA_RW_Latch.getALU_result();
				int rd = inst.getDestinationOperand().getValue();
				containingProcessor.getRegisterFile().setValue(rd, alu_result);
			}
			
		}
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
	}
	

}
