package processor.pipeline;

import processor.Processor;

import java.util.Arrays;

import generic.Instruction;
import generic.Instruction.OperationType;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;

	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}


	private static String toBinaryOfSpecificPrecision(int num, int lenOfTargetString) {
		String binary = String.format("%" + lenOfTargetString + "s", Integer.toBinaryString(num)).replace(' ', '0');
		return binary;
	}
	
	/**
	 * converts binary representation of number to signed integer
	 * @param binary: Sring representation of binary form of number
	 * @return: returns signed representation of given number
	*/
	private static int toSignedInteger(String binary) {
		int n = 32 - binary.length();
        char[] sign_ext = new char[n];
        Arrays.fill(sign_ext, binary.charAt(0));
        int signedInteger = (int) Long.parseLong(new String(sign_ext) + binary, 2);
        return signedInteger;
	}

	private void loopAround(int num) {
		for (int i = 0; i < num; i += 1)
			toSignedInteger(toBinaryOfSpecificPrecision(i, 20));
	}


	public void performMA() {
		if (EX_MA_Latch.getIsnop()) {
			MA_RW_Latch.setIsnop(true);
			MA_RW_Latch.setInstruction(null);
			EX_MA_Latch.setIsnop(false);
		} else if (EX_MA_Latch.isMA_enable()) {
			Instruction instruction = EX_MA_Latch.getInstruction();
			int alu_result = EX_MA_Latch.getALU_result();
			//System.out.println("MA is enabled: " + instruction);
			loopAround(20);
			MA_RW_Latch.setALU_result(alu_result);
			OperationType op_type = instruction.getOperationType();
			switch (op_type) {
			case store:
				int val_store = containingProcessor.getRegisterFile()
						.getValue(instruction.getSourceOperand1().getValue());
				containingProcessor.getMainMemory().setWord(alu_result, val_store);
				break;

			case load:
				
				int load_result = containingProcessor.getMainMemory().getWord(alu_result);
				MA_RW_Latch.setLoad_result(load_result);
				break;

			default:
				break;
			}
			
			if (instruction.getOperationType().ordinal() == 29) {
				IF_EnableLatch.setIF_enable(false);
			} 
			MA_RW_Latch.setInstruction(instruction);
			MA_RW_Latch.setRW_enable(true);
		}
	}

}
