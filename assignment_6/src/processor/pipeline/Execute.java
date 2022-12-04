package processor.pipeline;

import generic.Instruction;
import generic.Operand;
import generic.Statistics;
import processor.Processor;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch, IF_OF_LatchType iF_OF_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	private static String rqdBinarylength(int num, int lenOfTargetString) {
		String binary = String.format("%" + lenOfTargetString + "s", Integer.toBinaryString(num)).replace(' ', '0');
		return binary;
	}
	
	
	private void looping(int num) {
		for (int i = 0; i < num; i += 1)
			toSignedInteger(rqdBinarylength(i, 20));
	}

	
	private static int toSignedInteger(String binary) {
		int n = 32 - binary.length();
        char[] sign_ext = new char[n];
        Arrays.fill(sign_ext, binary.charAt(0));
        int signedInteger = (int) Long.parseLong(new String(sign_ext) + binary, 2);
        return signedInteger;
	}

	
	public static int add(int rs1, int rs2){
		return (rs1 + rs2);
	}

	public static int sub(int rs1, int rs2){
		return(rs1 - rs2);
	}

	public static int mul(int rs1, int rs2){
		return(rs1*rs2);
	}

	public static int div(int rs1, int rs2){
		return(rs1/rs2);
	}
 
	public static int rem(int rs1, int rs2){
		return(rs1%rs2);
	}

	public static int and(int rs1, int rs2){
		return(rs1&rs2);
	}

	public static int or(int rs1, int rs2){
		return(rs1|rs2);
	}

	public static int xor(int rs1, int rs2){
		return(rs1^rs2);
	}

	public static int slt(int rs1, int rs2){
		return(rs1<rs2?1:0);
	}
	
	public static int sll(int rs1, int rs2){
		return(rs1<<rs2);
	}
	
	public static int srl(int rs1, int rs2){
		return(rs1>>>rs2);
	}
	
	public static int sra(int rs1, int rs2){
		return(rs1>>rs2);
	}
	

	public void performEX()
	{
		if(OF_EX_Latch.isEX_Busy()){
			IF_OF_Latch.setOF_Busy(true);
		}
		else {
			IF_OF_Latch.setOF_Busy(false);

			if(OF_EX_Latch.isEX_Locked()){
				EX_MA_Latch.setMA_Lock(true);
				OF_EX_Latch.setEX_Lock(false);
				EX_MA_Latch.setInstruction(null);
				OF_EX_Latch.setEX_enable(false);
			}
			else if(OF_EX_Latch.isEX_enable()) {

				OF_EX_Latch.setEX_enable(false);
				Instruction currentInstruction = OF_EX_Latch.getInstruction();
				System.out.println("\nEX: " + currentInstruction);
				int currentPC = currentInstruction.getProgramCounter();
				looping(50);
				OperationType currentOperation = currentInstruction.getOperationType();
				int sourceOperand1 = -1, sourceOperand2 = -1, immediate, remainder;
				int aluResult = -1;

				//Creating set of branch instructions and end instruction
				Set<String> BranchInstructions = new HashSet<String>();
				//Adding the branch instructions
				BranchInstructions.add("jmp");
				BranchInstructions.add("beq");
				BranchInstructions.add("bne");
				BranchInstructions.add("blt");
				BranchInstructions.add("bgt");
				BranchInstructions.add("end");

				if (BranchInstructions.contains(currentOperation.name())) {
					if (currentOperation != OperationType.end) {
						Statistics.setNumberOfBranchesTaken(Statistics.getNumberOfBranchesTaken() + 1);
					}
					IF_EnableLatch.setIF_enable(false);
					IF_OF_Latch.setOF_enable(false);
					OF_EX_Latch.setEX_enable(false);
				}

				switch (currentOperation) {
					case add:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						sourceOperand2 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand2().getValue());
						aluResult = add(sourceOperand1,sourceOperand2);
						break;
					case addi:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						immediate = currentInstruction.getSourceOperand2().getValue();
						aluResult = add(sourceOperand1,immediate);
						break;
					case sub:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						sourceOperand2 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand2().getValue());
						aluResult = sub(sourceOperand1,sourceOperand2);
						break;
					case subi:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						immediate = currentInstruction.getSourceOperand2().getValue();
						aluResult = sub(sourceOperand1,immediate);
						break;
					case mul:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						sourceOperand2 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand2().getValue());
						aluResult = mul(sourceOperand1,sourceOperand2);
						break;
					case muli:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						immediate = currentInstruction.getSourceOperand2().getValue();
						aluResult = mul(sourceOperand1,immediate);
						break;
					case div:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						sourceOperand2 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand2().getValue());
						aluResult = div(sourceOperand1,sourceOperand2);
						remainder = rem(sourceOperand1,sourceOperand2);
						containingProcessor.getRegisterFile().setValue(31, remainder);
						break;
					case divi:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						immediate = currentInstruction.getSourceOperand2().getValue();
						aluResult = div(sourceOperand1,immediate);
						remainder = rem(sourceOperand1,immediate);
						containingProcessor.getRegisterFile().setValue(31, remainder);
						break;
					case and:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						sourceOperand2 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand2().getValue());
						aluResult = and(sourceOperand1,sourceOperand2);
						break;
					case andi:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						immediate = currentInstruction.getSourceOperand2().getValue();
						aluResult = and(sourceOperand1,immediate);
						break;
					case or:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						sourceOperand2 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand2().getValue());
						aluResult = or(sourceOperand1,sourceOperand2);
						break;
					case ori:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						immediate = currentInstruction.getSourceOperand2().getValue();
						aluResult = or(sourceOperand1,immediate);
						break;
					case xor:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						sourceOperand2 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand2().getValue());
						aluResult = xor(sourceOperand1,sourceOperand2);
						break;
					case xori:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						immediate = currentInstruction.getSourceOperand2().getValue();
						aluResult = xor(sourceOperand1,immediate);
						break;
					case slt:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						sourceOperand2 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand2().getValue());
						aluResult = slt(sourceOperand1,sourceOperand2);
						break;
					case slti:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						immediate = currentInstruction.getSourceOperand2().getValue();
						aluResult = slt(sourceOperand1, immediate);
						break;
					case sll:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						sourceOperand2 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand2().getValue());
						aluResult = sll(sourceOperand1,sourceOperand2);
						break;
					case slli:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						immediate = currentInstruction.getSourceOperand2().getValue();
						aluResult = sll(sourceOperand1,immediate);
						break;
					case srl:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						sourceOperand2 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand2().getValue());
						aluResult = srl(sourceOperand1,sourceOperand2);
						break;
					case srli:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						immediate = currentInstruction.getSourceOperand2().getValue();
						aluResult = srl(sourceOperand1,immediate);
						break;
					case sra:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						sourceOperand2 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand2().getValue());
						aluResult = sra(sourceOperand1,sourceOperand2);
						break;
					case srai:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						immediate = currentInstruction.getSourceOperand2().getValue();
						aluResult = sra(sourceOperand1,immediate);
						break;
					case load:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						immediate = currentInstruction.getSourceOperand2().getValue();
						aluResult = add(sourceOperand1,immediate);
						break;
					case store:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getDestinationOperand().getValue());
						immediate = currentInstruction.getSourceOperand2().getValue();
						aluResult = add(sourceOperand1,immediate);
						break;
					case jmp:
						OperandType jump = currentInstruction.getDestinationOperand().getOperandType();
						if (jump == OperandType.Register) {
							immediate = containingProcessor.getRegisterFile().getValue(currentInstruction.getDestinationOperand().getValue());
						} else {
							immediate = currentInstruction.getDestinationOperand().getValue();
						}
						aluResult = add(currentPC,immediate);
						EX_IF_Latch.setEX_IF_enable(true, aluResult);
						break;
					case beq:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						sourceOperand2 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand2().getValue());
						immediate = currentInstruction.getDestinationOperand().getValue();
						if (sourceOperand1 == sourceOperand2) {
							aluResult = add(currentPC,immediate);
							EX_IF_Latch.setEX_IF_enable(true, aluResult);
						}
						break;
					case bne:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						sourceOperand2 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand2().getValue());
						immediate = currentInstruction.getDestinationOperand().getValue();
						if (sourceOperand1 != sourceOperand2) {
							aluResult = add(currentPC,immediate);
							EX_IF_Latch.setEX_IF_enable(true, aluResult);
						}
						break;
					case blt:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						sourceOperand2 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand2().getValue());
						immediate = currentInstruction.getDestinationOperand().getValue();
						System.out.println(sourceOperand1 + " " + sourceOperand2);
						if (sourceOperand1 < sourceOperand2) {
							aluResult = add(currentPC,immediate);
							looping(50);
							EX_IF_Latch.setEX_IF_enable(true, aluResult);
							System.out.println("aluresult = " + aluResult);
						}
						break;
					case bgt:
						sourceOperand1 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand1().getValue());
						sourceOperand2 = containingProcessor.getRegisterFile().getValue(currentInstruction.getSourceOperand2().getValue());
						immediate = currentInstruction.getDestinationOperand().getValue();
						if (sourceOperand1 > sourceOperand2) {
							aluResult = add(currentPC,immediate);
							EX_IF_Latch.setEX_IF_enable(true, aluResult);
						}
						break;
					case end:
						break;
					default:
						break;
				}
				EX_MA_Latch.setAluResult(aluResult);
				EX_MA_Latch.setInstruction(currentInstruction);
				EX_MA_Latch.setMA_enable(true);

				/*if(aluResult != -1)
					System.out.println("\nEX Stage: " + "Current PC:" + currentPC + " rs1:" + sourceOperand1 + " rs2:" + sourceOperand2 + " Alu:" + aluResult);
				else
					System.out.println("\nEX Stage: " + "Current PC:" + currentPC + " rs1:" + sourceOperand1 + " rs2:" + sourceOperand2);
				*/
			}
		}
	}
}
