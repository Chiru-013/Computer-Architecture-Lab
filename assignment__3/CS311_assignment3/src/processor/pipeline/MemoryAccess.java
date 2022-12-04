package processor.pipeline;

import java.util.Arrays;

import generic.Instruction;
import generic.Instruction.OperationType;
import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		//TODO
		if(EX_MA_Latch.isMA_enable() == true){
			//fetching alu output result
			Instruction inst = EX_MA_Latch.getInstruction();
			int alu_result = EX_MA_Latch.getALU_result();
			MA_RW_Latch.setALU_result(alu_result);

			//fetchign type of operation and opcode
			OperationType op_type = inst.getOperationType();
			int opcode = Arrays.asList(OperationType.values()).indexOf(op_type);
			String optype = inst.getOperationType().toString();
			
			//Accessing memory while loading and storing
			if(opcode == 22 && optype == "load"){
				int load_result = containingProcessor.getMainMemory().getWord(alu_result);
				MA_RW_Latch.setLoad_result(load_result);
			}else if(opcode == 23 && optype == "store"){
				int i = inst.getSourceOperand1().getValue();
				int store_value = containingProcessor.getRegisterFile().getValue(i);
				containingProcessor.getMainMemory().setWord(alu_result, store_value);
			}
			
			MA_RW_Latch.setInstruction(inst);
			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);
		}
	}
	
}

	


