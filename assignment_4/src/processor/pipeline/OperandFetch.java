package processor.pipeline;

import processor.Processor;

import java.util.Arrays;

import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Statistics;
import generic.Operand.OperandType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;

	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch) {
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	

	public void conflictBubblePCModify () {
		System.out.println("Conflict Observed");
		IF_EnableLatch.setIF_enable(false);
		OF_EX_Latch.setIsnop(true);
	}

	public static boolean checkConflict(Instruction inst, int reg_1, int reg_2) {
		int inst_ordinal = inst != null && inst.getOperationType() != null ? inst.getOperationType().ordinal() : 1000;
		if ((inst_ordinal <= 21 && inst_ordinal % 2 == 0) || (inst_ordinal <= 21 && inst_ordinal % 2 != 0) || inst_ordinal == 22 || inst_ordinal == 23) {
			int dest_reg = inst != null ? inst.getDestinationOperand().getValue() : -1;
			// int dest_reg;
			// if(inst == null ){
			// 	dest_reg = -1;
			// }
			// else{
			// 	dest_reg =  inst.getDestinationOperand().getValue();
			// }
			
			if (reg_1 == dest_reg || reg_2 == dest_reg) {
				return true;
			} else {
				return false;
			}
		} else return false;
	}

	public static String twosComplement(String bin) {
		String twos = "", ones = "";
		for (int k = 0; k < bin.length() && true; k++) {
			ones += invert(bin.charAt(k));
		}

		StringBuilder builder = new StringBuilder(ones);
		boolean b = false;
		for (int k = ones.length() - 1; k > 0 && k > -2; k--) {
			if (ones.charAt(k) == '1') {
				builder.setCharAt(k, '0');
			} else {
				builder.setCharAt(k, '1');
				b = true;
				break;
			}
		}
		if (!b) {
			builder.append("1", 0, 7);
		}
		twos = builder.toString();
		return twos;
	}
	
	public static char invert(char c) {
		return (c == '0') ? '1' : '0';
	}

	
	
	public boolean checkConflictWithDivision(int reg_1, int reg_2) {
		Instruction instruction_ex_stage = OF_EX_Latch.getInstruction();
		Instruction instruction_ma_stage = EX_MA_Latch.getInstruction();
		Instruction instruction_rw_stage = MA_RW_Latch.getInstruction();
		if (reg_1 == 31 || reg_2 == 31) {
			int inst_ex_ordinal = instruction_ex_stage != null && instruction_ex_stage.getOperationType() != null ? instruction_ex_stage.getOperationType().ordinal() : 1000;
			int inst_ma_ordinal = instruction_ma_stage != null && instruction_ma_stage.getOperationType() != null ? instruction_ma_stage.getOperationType().ordinal() : 1000;
			int inst_rw_ordinal = instruction_rw_stage != null && instruction_rw_stage.getOperationType() != null ? instruction_rw_stage.getOperationType().ordinal() : 1000;
			if (inst_ex_ordinal == 6 || inst_ex_ordinal == 7 || inst_ma_ordinal == 6 || inst_ma_ordinal == 7 || inst_rw_ordinal == 6 || inst_rw_ordinal == 7) {
				System.out.println("Conflict in division");
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
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
 	
	public void performOF() {
		if (IF_OF_Latch.isOF_enable()) {
			Statistics.setNumberOfOFInstructions(Statistics.getNumberOfOFInstructions() + 1);
			OperationType[] operationType = OperationType.values();
			String inst = Integer.toBinaryString(IF_OF_Latch.getInstruction());
			//System.out.println("OF is enabled with inst: " + inst + "..");
			while (inst.length() != 32) {
				inst = "0" + inst;
			}
			String opcode = inst.substring(0, 5);
			int type_operation = Integer.parseInt(opcode, 2);
			OperationType operation = operationType[type_operation];
			
			if (operation.ordinal() == 24 || operation.ordinal() == 25 || operation.ordinal() == 26 || operation.ordinal() == 27 || operation.ordinal() == 28 ) {
				IF_EnableLatch.setIF_enable(false);
			}
			
			int signedInt = toSignedInteger("001"), numBits = 20;
			String binaryNum = toBinaryOfSpecificPrecision(signedInt, 5);
			binaryNum = toBinaryOfSpecificPrecision(numBits, 5);
			signedInt = toSignedInteger(binaryNum);
			loopAround(20);
			
			boolean conflict_inst = false;
			Instruction instruction_ex_stage = OF_EX_Latch.getInstruction();
			Instruction instruction_ma_stage = EX_MA_Latch.getInstruction();
			Instruction instruction_rw_stage = MA_RW_Latch.getInstruction();
			Instruction instruction = new Instruction();
			switch (operation) {
			case add:
			case sub:
			case mul:
			case div:
			case and:
			case or:
			case xor:
			case slt:
			case sll:
			case srl:
			case sra:
				Operand rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				int regNo = Integer.parseInt(inst.substring(5, 10), 2);
				rs1.setValue(regNo);

				Operand rs2 = new Operand();
				rs2.setOperandType(OperandType.Register);
				loopAround(20);
				int regNo2 = Integer.parseInt(inst.substring(10, 15), 2);
				rs2.setValue(regNo2);
				

				if (checkConflict(instruction_ex_stage, regNo, regNo2) || checkConflict(instruction_ma_stage, regNo, regNo2) || checkConflict(instruction_rw_stage, regNo, regNo2) || checkConflictWithDivision(regNo, regNo2))
					conflict_inst = true;
				if (conflict_inst) {
					this.conflictBubblePCModify();
					break;
				}

				Operand rd = new Operand();
				rd.setOperandType(OperandType.Register);
				regNo = Integer.parseInt(inst.substring(15, 20), 2);
				rd.setValue(regNo);

				instruction.setOperationType(operationType[type_operation]);
				instruction.setSourceOperand1(rs1);
				instruction.setSourceOperand2(rs2);
				instruction.setDestinationOperand(rd);
				break;

			case end:
				instruction.setOperationType(operationType[type_operation]);
				IF_EnableLatch.setIF_enable(false);
				break;
			case jmp:
				Operand op = new Operand();
				String imm = inst.substring(10, 32);
				int imm_val = Integer.parseInt(imm, 2);
				if (imm.charAt(0) == '1') {
					imm = twosComplement(imm);
					imm_val = Integer.parseInt(imm, 2) * -1;
				}
				if (imm_val != 0) {
					op.setOperandType(OperandType.Immediate);
					op.setValue(imm_val);
				} else {
					regNo = Integer.parseInt(inst.substring(5, 10), 2);
					op.setOperandType(OperandType.Register);
					op.setValue(regNo);
				}

				instruction.setOperationType(operationType[type_operation]);
				instruction.setDestinationOperand(op);
				break;

			case beq:
			case bne:
			case blt:
			case bgt:
				rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				regNo = Integer.parseInt(inst.substring(5, 10), 2);
				rs1.setValue(regNo);
				
				// destination register
				rs2 = new Operand();
				rs2.setOperandType(OperandType.Register);
				regNo2 = Integer.parseInt(inst.substring(10, 15), 2);
				rs2.setValue(regNo2);
				
				if (checkConflict(instruction_ex_stage, regNo, regNo2) || checkConflict(instruction_ma_stage, regNo, regNo2) || checkConflict(instruction_rw_stage, regNo, regNo2) || checkConflictWithDivision(regNo, regNo2))
					conflict_inst = true;
				if (conflict_inst) {
					this.conflictBubblePCModify();
					break;
				}
				
				// Immediate value
				rd = new Operand();
				rd.setOperandType(OperandType.Immediate);
				imm = inst.substring(15, 32);
				imm_val = Integer.parseInt(imm, 2);
				if (imm.charAt(0) == '1') {
					imm = twosComplement(imm);
					imm_val = Integer.parseInt(imm, 2) * -1;
				}
				rd.setValue(imm_val);
				
				instruction.setOperationType(operationType[type_operation]);
				instruction.setSourceOperand1(rs1);
				instruction.setSourceOperand2(rs2);
				instruction.setDestinationOperand(rd);
				break;

			default:
				rs1 = new Operand();
				rs1.setOperandType(OperandType.Register);
				regNo = Integer.parseInt(inst.substring(5, 10), 2);
				rs1.setValue(regNo);
				int x = regNo;
				if (checkConflict(instruction_ex_stage, regNo, x) || checkConflict(instruction_ma_stage, regNo, x) || checkConflict(instruction_rw_stage, regNo, x) || checkConflictWithDivision(regNo, regNo)) {
					conflict_inst = true;
				}	
				
				if (conflict_inst) {
					this.conflictBubblePCModify();
					break;
				}

				// Destination register
				rd = new Operand();
				rd.setOperandType(OperandType.Register);
				regNo = Integer.parseInt(inst.substring(10, 15), 2);
				rd.setValue(regNo);

				// Immediate values
				rs2 = new Operand();
				rs2.setOperandType(OperandType.Immediate);
				imm = inst.substring(15, 32);
				imm_val = Integer.parseInt(imm, 2);
				if (imm.charAt(0) == '1') {
					imm = twosComplement(imm);
					imm_val = Integer.parseInt(imm, 2) * -1;
				}
				rs2.setValue(imm_val);
				instruction.setOperationType(operationType[type_operation]);
				instruction.setSourceOperand1(rs1);
				instruction.setSourceOperand2(rs2);
				instruction.setDestinationOperand(rd);
				break;
			}
			OF_EX_Latch.setInstruction(instruction);

			OF_EX_Latch.setEX_enable(true);
		}
	}

}
