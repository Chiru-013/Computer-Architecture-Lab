package processor.pipeline;

import generic.Simulator;
import generic.Statistics;
import processor.Processor;

import java.util.Arrays;

import generic.Instruction;
import generic.Instruction.OperationType;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;

	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch,
			IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}


	private static String toBinaryOfSpecificPrecision(int num, int lenOfTargetString) {
		String binary = String.format("%" + lenOfTargetString + "s", Integer.toBinaryString(num)).replace(' ', '0');
		return binary;
	}
	
	private static int toSignedInteger(String binary) {
		int n = 32 - binary.length();
        char[] sign_ext = new char[n];
        Arrays.fill(sign_ext, binary.charAt(0));
        int signedInteger = (int) Long.parseLong(new String(sign_ext) + binary, 2);
        return signedInteger;
	}

	private void loopAround(int num) {
		for (int k = 0; k < num; k += 1)
			toSignedInteger(toBinaryOfSpecificPrecision(k, 20));
	}

	public void performRW() {
		if (MA_RW_Latch.getIsnop()) {
			MA_RW_Latch.setIsnop(false);
//			IF_EnableLatch.setIF_enable(true);
		} else if (MA_RW_Latch.isRW_enable()) {
			Statistics.setnumberOfRegisterWriteInstructions(Statistics.getNumberOfRegisterWriteInstructions() + 1);
			Instruction instruction = MA_RW_Latch.getInstruction();
			int alu_result = MA_RW_Latch.getALU_result();
			loopAround(20);
			OperationType op_type = instruction.getOperationType();
			System.out.println("RW is enabled with aluResult: " + alu_result + "..");
			switch (op_type) {
			case store:
			case jmp:
			case beq:
			case bne:
			case blt:
			case bgt:
				break;
			case load:
				int load_result = MA_RW_Latch.getLoad_result();
				int rd = instruction.getDestinationOperand().getValue();
				containingProcessor.getRegisterFile().setValue(rd, load_result);
				break;
			case end:
				Simulator.setSimulationComplete(true);
				break;
			default:
				rd = instruction.getDestinationOperand().getValue();
				containingProcessor.getRegisterFile().setValue(rd, alu_result);
				break;
			}

			
			if (op_type.ordinal() != 29) {
				// MA_RW_Latch.setRW_enable(false);
				IF_EnableLatch.setIF_enable(true);
			}
		}
	}

}
