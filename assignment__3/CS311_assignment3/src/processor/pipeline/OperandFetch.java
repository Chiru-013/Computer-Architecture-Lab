package processor.pipeline;

import processor.Processor;

import java.util.Arrays;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Operand;
import generic.Operand.OperandType;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
		
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch) {
		
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}

	public static char invert(char c) {
		
        if(c == '0'){
			return '1';
		}
		else{
			return '0';
		}
	}

	
    static int onesComplement(int num) { 
          
        int number_of_bits =  (int)(Math.floor(Math.log(num) / Math.log(2))) + 1; 
        return ((1 << number_of_bits) - 1) ^ num; 
    } 
	
	public static String comp_2s(String binary) {
		
        String twos = "", ones = "";
        for (int i = 0; i < binary.length(); i++) 
            ones += invert(binary.charAt(i));

        StringBuilder builder = new StringBuilder(ones);
        boolean extra = true;
		
        for (int i = ones.length() - 1; i > 0; i--) {
		
            if (ones.charAt(i) == '1') 
                builder.setCharAt(i, '0');
            else {
		
                builder.setCharAt(i, '1');
                extra = false;
                break;
            }
        }
		
        if (extra == true) 
            builder.append("1", 0, 7);
		
        twos = builder.toString();
        return twos;
    }
	
	

	private static String toBinaryOfSpecificPrecision(int num, int lenOfTargetString) {
		String binary = String.format("%" + lenOfTargetString + "s", Integer.toBinaryString(num)).replace(' ', '0');
		return binary;
	}
	
	//Padding of unused bits
	public static String bin_padding(int value, int length){	
		String binary_pad = "";
		if(value >= 0){
			binary_pad = String.format("%" + length + "s", Integer.toBinaryString(value)).replace(' ', '0');		   
		}
		else{
			//32-bit representaion of negative nos. reduced to required no. of bits
			char[] buffer_bin = Integer.toBinaryString(value).toCharArray();	
			for (int i = 32-length; i < 32; i++) {
				binary_pad = binary_pad + Array.getChar(buffer_bin, i);
			}
			
		}
		return binary_pad;
		
	}
	
	private static int toSignedInteger(String binary) {
		int n = 32 - binary.length();
        char[] sign_ext = new char[n];
        Arrays.fill(sign_ext, binary.charAt(0));
        int signedInteger = (int) Long.parseLong(new String(sign_ext) + binary, 2);
        return signedInteger;
	}


	private void loop(int num) {
		for (int i = 0; i < num; i += 1)
			toSignedInteger(toBinaryOfSpecificPrecision(i, 20));
	}
	
	public void performOF() {
		
		if(IF_OF_Latch.isOF_enable()) {
		
			OperationType[] operationType = OperationType.values();
			String instruction = Integer.toBinaryString(IF_OF_Latch.getInstruction());

			//int numBits = 32;
			// int signedInt = toSignedInteger("001");
			// String binaryNum = toBinaryOfSpecificPrecision(signedInt, 5);
			// binaryNum = toBinaryOfSpecificPrecision(numBits, 5);
			// signedInt = toSignedInteger(binaryNum);
			
			// padding to make it 32 bit
			while(instruction.length() != 32) 
				instruction = "0" + instruction;
		
			//System.out.println(instruction);
			/*do {
				
			} while (
				

			); */
			String opcode = instruction.substring(0, 5);
			int type_operation = Integer.parseInt(opcode, 2);
			OperationType operation = operationType[type_operation];

			Instruction inst = new Instruction();
			loop(50);
			switch(operation) {
		
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
					int registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
					rs1.setValue(registerNo);

					Operand rs2 = new Operand();
					rs2.setOperandType(OperandType.Register);
					registerNo = Integer.parseInt(instruction.substring(10, 15), 2);
					rs2.setValue(registerNo);

					Operand rd = new Operand();
					rd.setOperandType(OperandType.Register);
					registerNo = Integer.parseInt(instruction.substring(15, 20), 2);
					rd.setValue(registerNo);

					inst.setOperationType(operationType[type_operation]);
					inst.setSourceOperand1(rs1);
					inst.setSourceOperand2(rs2);
					inst.setDestinationOperand(rd);
					break;

				case end:
					inst.setOperationType(operationType[type_operation]);
					break;
				case jmp:
					Operand op = new Operand();
					String imm = instruction.substring(10, 32);
					int imm_val = Integer.parseInt(imm, 2);
					if (imm.charAt(0) == '1') {
		
						imm = comp_2s(imm);
						imm_val = Integer.parseInt(imm, 2) * -1;
					}
		
					if (imm_val != 0) {
		
						op.setOperandType(OperandType.Immediate);
						op.setValue(imm_val);
					}
					else {
		
						registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
						op.setOperandType(OperandType.Register);
						op.setValue(registerNo);
					}

					inst.setOperationType(operationType[type_operation]);
					inst.setDestinationOperand(op);
					break;
			
				case beq:
				case bne:
				case blt:
				case bgt:
					rs1 = new Operand();
					rs1.setOperandType(OperandType.Register);
					registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
					rs1.setValue(registerNo);

					// destination register
					rs2 = new Operand();
					rs2.setOperandType(OperandType.Register);
					registerNo = Integer.parseInt(instruction.substring(10, 15), 2);
					rs2.setValue(registerNo);

					// Immediate value
					rd = new Operand();
					rd.setOperandType(OperandType.Immediate);
					imm = instruction.substring(15, 32);
					imm_val = Integer.parseInt(imm, 2);
		
					if (imm.charAt(0) == '1') {
		
						imm = comp_2s(imm);
						imm_val = Integer.parseInt(imm, 2) * -1;
					}
		
					rd.setValue(imm_val);

					inst.setOperationType(operationType[type_operation]);
					inst.setSourceOperand1(rs1);
					inst.setSourceOperand2(rs2);
					inst.setDestinationOperand(rd);
					break;

				default:
					// Source register 1
					rs1 = new Operand();
					rs1.setOperandType(OperandType.Register);
					registerNo = Integer.parseInt(instruction.substring(5, 10), 2);
					System.out.println(registerNo);
					rs1.setValue(registerNo);

					// Destination register
					rd = new Operand();
					rd.setOperandType(OperandType.Register);
					registerNo = Integer.parseInt(instruction.substring(10, 15), 2);
					System.out.println(registerNo);
					rd.setValue(registerNo);

					// Immediate values
					rs2 = new Operand();
					rs2.setOperandType(OperandType.Immediate);
					imm = instruction.substring(15, 32);
					System.out.println(imm);
					imm_val = Integer.parseInt(imm, 2);
		
					if (imm.charAt(0) == '1') {
		
						imm = comp_2s(imm);
						imm_val = Integer.parseInt(imm, 2) * -1;
					}
					rs2.setValue(imm_val);

					inst.setOperationType(operationType[type_operation]);
					inst.setSourceOperand1(rs1);
					inst.setSourceOperand2(rs2);
					inst.setDestinationOperand(rd);
					break;
			}

			OF_EX_Latch.setInstruction(inst);
			IF_OF_Latch.setOF_enable(false);
			OF_EX_Latch.setEX_enable(true);
		}
	}

}
