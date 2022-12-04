package processor.pipeline;

import java.util.Arrays;

import generic.Instruction;
import generic.Simulator;
import generic.Instruction.OperationType;
import generic.Operand.OperandType;
import processor.Processor;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	



	public static char invert(char c){
        if(c == '0'){
			return '1';
		}
		else{
			return '0';
		}
    }

	public static String comp_2s(String binary){
        String twos = "", ones = "";
        for (int i = 0; i < binary.length(); i++){
            ones += invert(binary.charAt(i));
        }

        StringBuilder builder = new StringBuilder(ones);
        int extra = 1;
        for (int i = ones.length() - 1; i > 0; i--){
            if (ones.charAt(i) == '1'){
                builder.setCharAt(i, '0');
            }
            else{
                builder.setCharAt(i, '1');
                extra = 0;
                break;
            }
        }
		// loop(twos.length());
        if (extra == 1){
            builder.append("1", 0, 7);
        }

        twos = builder.toString();
		
        return twos;
    }

	public static void loop(int x){
		for(int i=0; i<5*x; i++ ){
			loop(x-20);
		}
	}
	public void performEX()
	{
		//TODO 
		boolean jumpRes = false;
		if(OF_EX_Latch.isEX_enable()){ 
			
			
			Instruction inst = OF_EX_Latch.getInstruction();
			EX_MA_Latch.setInstruction(inst);
			OperationType op_type = inst.getOperationType(); 
			
			
			// int opcode = Arrays.asList(OperationType.values()).indexOf(op_type);
			String optype = inst.getOperationType().toString();
			int curr_PC = containingProcessor.getRegisterFile().getProgramCounter() - 1;
			// loop(opcode);
			int alu_result = 0;
			//System.out.println(optype);
			if(optype.equals("add") || optype.equals("sub") || optype.equals("mul") || optype.equals("div") || optype.equals("and") || optype.equals("or") || optype.equals("xor") || optype.equals("slt") || optype.equals("sll") || optype.equals("srl") || optype.equals("sra") ){
				int operand_1 = containingProcessor.getRegisterFile().getValue(
					inst.getSourceOperand1().getValue());
				int operand_2 = containingProcessor.getRegisterFile().getValue(
					inst.getSourceOperand2().getValue());
				//usual R3 type format address intruction operand fetch
				
				switch(op_type){
					
					case add:
						alu_result = operand_1 + operand_2;
						break;
					case sub:
						
						alu_result = operand_1 - operand_2;
						break;
					case mul:
						alu_result = operand_1 * operand_2;
						break;
					case div:
						alu_result = operand_1 / operand_2;
						int rem = operand_1 % operand_2;
						containingProcessor.getRegisterFile().setValue(31, rem);
						break;
					case and:
						alu_result = operand_1 & operand_2;
						break;
					case or:
						alu_result = operand_1 | operand_2;
						break;
					case xor:
						alu_result = operand_1 ^ operand_2;
						break;
					case slt:
						if(operand_1 >= operand_2)
							alu_result = 0;
						else
							alu_result = 1;
						break;
					case sll:
						alu_result = operand_1 << operand_2;
						break;
					case srl:
						alu_result = operand_1 >>> operand_2;
						break;
					case sra:
						alu_result = operand_1 >> operand_2;
						break;
					default:
						break;
				}
			}
			else if(optype.equals("addi") || optype.equals("subi") || optype.equals("muli") || optype.equals("divi") || optype.equals("andi") || optype.equals("ori") || optype.equals("xori") || optype.equals("slti") || optype.equals("slli") || optype.equals("srli") || optype.equals("srai") || optype.equals("load") ){
				//int i = inst.getSourceOperand1().getValue();
				int operand_1 = containingProcessor.getRegisterFile().getValue(inst.getSourceOperand1().getValue());
				int operand_2 = inst.getSourceOperand2().getValue();
				//R2I type format address intruction operand fetch
				switch(op_type){
					case addi:
						alu_result = operand_1 + operand_2;
						break;
					case subi:
						alu_result = operand_1 - operand_2;
						break;
					case muli:
						alu_result = operand_1 * operand_2;
						break;
					case divi:
						alu_result = operand_1 / operand_2;
						int remainder = operand_1 % operand_2;
						containingProcessor.getRegisterFile().setValue(31, remainder);
						break;
					case andi:
						alu_result = operand_1 & operand_2;
						break;
					case ori:
						alu_result = operand_1 | operand_2;
						break;
					case xori:
						alu_result = operand_1 ^ operand_2;
						break;
					case slti:
						if(operand_1 >= operand_2)
							alu_result = 0;
						else
							alu_result = 1;
						break;
					case slli:
						alu_result = operand_1 << operand_2;
						break;
					case srli:
						alu_result = operand_1 >>> operand_2;
						break;
					case srai:
						alu_result = operand_1 >> operand_2;
						break;
					case load:
						alu_result = operand_1 + operand_2;
						break;
					default:
						break;
				}
			}
			else if(optype.equals("store")){
				//operand fetch for store intruction
				int operand_1 = containingProcessor.getRegisterFile().getValue(inst.getDestinationOperand().getValue());
				int operand_2 = inst.getSourceOperand2().getValue();
				alu_result = operand_1 + operand_2;
			}
			else if(optype.equals("jmp")){
				//operand fetch for jump intruction
				jumpRes = true;
				OperandType operand_type = inst.getDestinationOperand().getOperandType();
				int imm = 0;
				if (operand_type == OperandType.Register){
					imm = containingProcessor.getRegisterFile().getValue(inst.getDestinationOperand().getValue());
				}
				else{
					imm = inst.getDestinationOperand().getValue();
				}
				alu_result = imm + curr_PC;
				EX_IF_Latch.set_branch_enable(true, alu_result);
			}
			else if(optype.equals("beq") || optype.equals("bne") || optype.equals("blt") || optype.equals("bgt")){
				//operand fetch for branching intructions
				int operand_1 = containingProcessor.getRegisterFile().getValue(
					inst.getSourceOperand1().getValue());
				int operand_2 = containingProcessor.getRegisterFile().getValue(
					inst.getSourceOperand2().getValue());
				int imm = inst.getDestinationOperand().getValue();
				// loop(opcode);
				// System.out.println(op1);
				// System.out.println(op2);
				// System.out.println(instruction);
				switch(op_type){
					case beq:
						if(operand_1 == operand_2){
							alu_result = imm + curr_PC;
							EX_IF_Latch.set_branch_enable(true, alu_result);
						}
						break;
					case bne:
						if(operand_1 != operand_2){
							alu_result = imm + curr_PC;
							EX_IF_Latch.set_branch_enable(true, alu_result);
						}

						break;
					case blt:
						if(operand_1 < operand_2){
							alu_result = imm + curr_PC;
							EX_IF_Latch.set_branch_enable(true, alu_result);
						}
						break;
					case bgt:
						if(operand_1 > operand_2){
							alu_result = imm + curr_PC;
							EX_IF_Latch.set_branch_enable(true, alu_result);
						}
						break;
					default:
						break;
				}
			
			}
			if (optype.equals("end")){
				/*
				 * 
				 * 
				 */
				Simulator.setSimulationComplete(true);
			}
			System.out.println("alu_result: " + Integer.toString(alu_result));
			EX_MA_Latch.setALU_result(alu_result);
		}

		//disabling OF_EX atch adn enabling EX_MA_latch

		OF_EX_Latch.setEX_enable(false);
		if (!jumpRes){
		EX_MA_Latch.setMA_enable(true);
		}
	}

}
