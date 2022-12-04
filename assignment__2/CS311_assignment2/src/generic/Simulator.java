package generic;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;

import generic.Instruction.OperationType;
import generic.Operand.OperandType;


public class Simulator {
	// Mapping the intructions to their respective op-Code
	public static HashMap<Instruction.OperationType, String> mapping = new HashMap<Instruction.OperationType, String>() {{
		put(OperationType.add, "00000");
		put(OperationType.addi, "00001");
		put(OperationType.sub, "00010");
		put(OperationType.subi, "00011");
		put(OperationType.mul, "00100");
		put(OperationType.muli, "00101");
		put(OperationType.div, "00110");
		put(OperationType.divi, "00111");
		put(OperationType.and, "01000");
		put(OperationType.andi, "01001");
		put(OperationType.or, "01010");
		put(OperationType.ori, "01011");
		put(OperationType.xor, "01100");
		put(OperationType.xori, "01101");
		put(OperationType.slt, "01110");
		put(OperationType.slti, "01111");
		put(OperationType.sll, "10000");
		put(OperationType.slli, "10001");
		put(OperationType.srl, "10010");
		put(OperationType.srli, "10011");
		put(OperationType.sra, "10100");
		put(OperationType.srai, "10101");
		put(OperationType.load, "10110");
		put(OperationType.end, "11101");
		put(OperationType.beq, "11001");
		put(OperationType.jmp, "11000");
		put(OperationType.bne, "11010");
		put(OperationType.blt, "11011");
		put(OperationType.bgt, "11100");
	}};

	static FileInputStream inputcodeStream = null;

    public static int ins_count;
	public static void setupSimulation(String assemblyProgramFile)
	{	
		int first_CodeAddress = ParsedProgram.parseDataSection(assemblyProgramFile);
		ParsedProgram.parseCodeSection(assemblyProgramFile, first_CodeAddress);
		ParsedProgram.printState();
	}

	//Performs sign_extension of the given Binary_string
	public static String sgn_ext(Operand inst, int fill){
		String binary="";
			if(inst.getValue() >= 0){
			binary = String.format("%" + fill + "s", Integer.toBinaryString(inst.getValue())).replace(' ', '0');	
		}
			else {
			//32-bit representaion of negative nos. reduced to required no. of bits
			char[] buffer_bin = Integer.toBinaryString(inst.getValue()).toCharArray();	
			for (int i = 32-fill; i < 32; i++) {
				binary = binary + Array.getChar(buffer_bin, i);
			}			
		}
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
	
	//Encodes 3-address type_of instructions
	public static String R3I(String x, Operand rs1, Operand rs2, Operand rd){
	   x += sgn_ext(rs1, 5);
	   x += sgn_ext(rs2, 5);
	   x += sgn_ext(rd, 5);
       x += bin_padding(0,12);
	   return x;
	}

	//Encodes 2-address type_of instructions
	public static String R2I(String x, Operand rs1, Operand rd, Operand I){
		x += sgn_ext(rs1, 5);
		x += sgn_ext(rd, 5);
		x += sgn_ext(I, 17);
		return x; 
	}

	//Encodes Load and Store instructions
	public static String Load_store(String x, Operand rs1, Operand rd, Operand I){
		x += sgn_ext(rs1, 5);
		x += sgn_ext(rd, 5);
		x += sgn_ext(I, 17);
		return x; 
	}

	//Encodes jmp instruction
	public static String jump(String x, Operand rd, int PC){
		if (rd.getOperandType() == Operand.OperandType.Immediate) {
			x += bin_padding(0, 5);
		    x += bin_padding(rd.getValue(),22);
		}
		else if(rd.getOperandType() == Operand.OperandType.Label){						
			x += bin_padding(0,5);
			int offset = Label_process(rd) - PC;
			x += bin_padding(offset, 22);
		}
		return x;
	}

	//Encodes all the branching instructions
	public static String branch(String x, int offset, Operand rs1, Operand rs2){
		x += sgn_ext(rs1, 5);
		x += sgn_ext(rs2, 5);
		x += bin_padding(offset, 17);
		return x;
	}

	//Encodes exit instruction
	public static String exit(String x){
		x += bin_padding(0,27);
		return x;
	}

	//Returns the Valueof(Label)
	public static int Label_process(Operand inst){
		return (ParsedProgram.symtab.get(inst.getLabelValue()));
	}

	//Main function of assembler
	public static void assemble(String object_File)
	{	
		FileOutputStream obj_file;//obj_file created
		
		try {

			//1. open declared obj_file in binary mode
			obj_file = new FileOutputStream(object_File);
			BufferedOutputStream buffer_file = new BufferedOutputStream(obj_file);//buffer file created

			//2. write the firstCodeAddress to the obj_file
			byte[] address_firstCode_line = ByteBuffer.allocate(4).putInt(ParsedProgram.firstCodeAddress).array();//allocation of 4 bytes of memory to int_type
			buffer_file.write(address_firstCode_line);//writing firstCodeaddress to the buffer file

			//3. write the data to the obj_file
			for (int ip_data: ParsedProgram.data) {
				byte[] Data = ByteBuffer.allocate(4).putInt(ip_data).array();
				buffer_file.write(Data);
			}	
			for(Instruction inst: ParsedProgram.code){
				String binary_Rep="";
				binary_Rep += mapping.get(inst.getOperationType());
				int opCode = Integer.parseInt(binary_Rep,2);
				int PC = inst.getProgramCounter();
                
				if(opCode <= 20 && opCode%2 == 0){
					//Segragating 3-address format instructions
					binary_Rep = R3I(binary_Rep, inst.getSourceOperand1(), inst.getSourceOperand2(), inst.getDestinationOperand());					
				}
				else if(opCode <= 21 && opCode%2 != 0){
					//Segragating 2-address format instructions
					binary_Rep = R2I(binary_Rep, inst.getSourceOperand1(), inst.getDestinationOperand(), inst.getSourceOperand2());
				}
				else if(opCode == 22 || opCode == 23){
					//Segragating Load store instructions
					binary_Rep = Load_store(binary_Rep, inst.getSourceOperand1(), inst.getDestinationOperand(), inst.getSourceOperand2());
				}
				else if(opCode > 24 && opCode < 29){
					//Segragating branching instructions
					int offset = 0;
					if(inst.getDestinationOperand().getOperandType() == Operand.OperandType.Immediate){
						offset = (Integer.parseInt(sgn_ext(inst.getDestinationOperand(), 5), 2))-PC;
						for (int i = 0; i < binary_Rep.length(); i++) {
							
								if(opCode == 5){
									//System.out.println(offset);
									//System.out.println(PC);
									//System.out.println(binary_Rep);
									break;
								}
							
						}
					  
					}
					else if(inst.getDestinationOperand().getOperandType() == Operand.OperandType.Label){
						int a = Label_process(inst.getDestinationOperand());
						offset = a-PC;//saving the destination address as offset with current_PC
					}
					binary_Rep = branch(binary_Rep, offset, inst.getSourceOperand1(), inst.getSourceOperand2());
				}
				else if(opCode == 24){
					//Segragating of jmp instruction
					binary_Rep = jump(binary_Rep, inst.getDestinationOperand(), PC);
				}
				else if(opCode == 29){
					//Segragating exit instructions
					binary_Rep = exit(binary_Rep);
				}

				//Converting binary string to integer
				int Integer_encode = (int) Long.parseLong(binary_Rep, 2);

				//Allocating 4 bytes of memory to the integer
				byte[] Binary_encode = ByteBuffer.allocate(4).putInt(Integer_encode).array();

				//Writing the encoded integers to the buffer file 
				buffer_file.write(Binary_encode);
			}

			//close obj_file
			buffer_file.close();
			
		}catch (IOException e) {
			e.printStackTrace();
			
		} 

		
	}
	
}
