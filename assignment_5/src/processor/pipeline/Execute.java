package processor.pipeline;

import processor.Processor;
import java.util.Arrays;

public class Execute {
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	IF_OF_LatchType IF_OF_Latch;
	 
	public Execute(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch, IF_EnableLatchType iF_EnableLatch) {
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


	public void performEX() {

		OF_EX_Latch.is_busy = (EX_MA_Latch.is_busy == true)?true:false;
		
		int signedInt = toSignedInteger("101");
		String binaryNum = rqdBinarylength(signedInt, 5);

		looping(50);

		if(OF_EX_Latch.isEX_enable() && EX_MA_Latch.is_busy == false) {
			int offset = 70000;
			if(OF_EX_Latch.is_nop == true) {
				EX_MA_Latch.is_nop = true;
				EX_MA_Latch.rd = 75000;//
				looping(50);
			}
			else {
				EX_MA_Latch.is_nop = false;
				int aluResult = 70000;
				int rs1 = OF_EX_Latch.rs1;
				int rs2 = OF_EX_Latch.rs2;
				int rd = OF_EX_Latch.rd;
				int imm = OF_EX_Latch.imm;
				switch(OF_EX_Latch.opcode) {
					case "00000": {
						aluResult = add(rs1,rs2);
						break;
					}
					case "00001": {
						aluResult = add(rs1,imm);
						break;
					}
					case "00010": {
						aluResult = sub(rs1,rs2);
						break;
					}
					case "00011": {
						aluResult = sub(rs1,imm);
						break;
					}
					case "00100": {
						aluResult = mul(rs1,rs2);
						break;
					}
					case "00101": {
						aluResult = mul(rs1,imm);
						System.out.println("muli:"+rs1+"  "+imm+"  "+aluResult);
						break;
					}
					case "00110": {
						aluResult = div(rs1,rs2);
						int temp = rem(rs1,rs2);
						containingProcessor.getRegisterFile().setValue(31, temp);
						break;
					}
					case "00111": {
						aluResult = div(rs1,imm);
						int temp = rem(rs1,imm);
						containingProcessor.getRegisterFile().setValue(31, temp);
						System.out.println("divi:"+rs1+"  "+imm+"  "+aluResult);
						System.out.println("rem:"+rs1+"  "+imm+"  "+temp);
						break;
					}

					case "01000": {
						aluResult = and(rs1,rs2);
						break;
					}
					case "01001": {
						aluResult = and(rs1,imm);
						break;
					}
					case "01010": {
						aluResult = or(rs1,rs2);
						break;
					}
					case "01011": {
						aluResult = or(rs1,imm);
						break;
					}
					case "01100": {
						aluResult = xor(rs1,rs2);
						break;
					}
					case "01101": {
						aluResult = xor(rs1,imm);
						break;
					}

					case "01110": {
						aluResult = slt(rs1,rs2);
						break;
					}
					case "01111": {
						aluResult = slt(rs1,imm);
						break;
					}

					case "10000": {
						aluResult = rs1 << rs2;
						String q = Integer.toBinaryString(rs1);
						while(q.length() != 5) q = "0" + q;
						String x31 = q.substring(5-rs2, 5);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}
					case "10001" : {
						aluResult = rs1 << imm;
						String q = Integer.toBinaryString(imm);

						while(q.length() != 5) q = "0" + q;
						String x31 = q.substring(5-imm, 5);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}
					case "10010" : {
						aluResult = rs1 >>> rs2;
						String q = Integer.toBinaryString(rs1);

						while(q.length() != 5) q = "0" + q;
						String x31 = q.substring(0, rs2);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}
					case "10011" : {
						aluResult = rs1 >>> imm;
						String q = Integer.toBinaryString(imm);

						while(q.length() != 5) q = "0" + q;
						String x31 = q.substring(0, imm);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}
					case "10100" : {
						aluResult = rs1 >> rs2;
						String q = Integer.toBinaryString(rs1);

						while(q.length() != 5) q = "0" + q;
						String x31 = q.substring(0, rs2);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}
					case "10101" : {
						aluResult = rs1 >> imm;
						String q = Integer.toBinaryString(imm);
						
						while(q.length() != 5) q = "0" + q;
						String x31 = q.substring(0, imm);
						containingProcessor.getRegisterFile().setValue(31, Integer.parseInt(x31,2));
						break;
					}

					case "10110"  : {
						aluResult = rs1 + imm;
						System.out.println("load:"+rs1+"  "+imm+"  "+aluResult);
						break;
					}
					case "10111" : {
						aluResult = containingProcessor.getRegisterFile().getValue(rd) + imm;
						break;
					}

					case "11000" : {
						offset = containingProcessor.getRegisterFile().getValue(rd) + imm;
						break;
					}
					case "11001" : {
						if(rs1 == containingProcessor.getRegisterFile().getValue(rd)) offset = imm;
						break;
					}
					case "11010" : {
						if(rs1 != containingProcessor.getRegisterFile().getValue(rd)) offset = imm;
						break;
					}
					case "11011" : {
						if(rs1 < containingProcessor.getRegisterFile().getValue(rd)) offset = imm;
						break;
					}
					case "11100" : {
						if(rs1 > containingProcessor.getRegisterFile().getValue(rd)) offset = imm;
						break;
					}
					default : break;
				}
				if(offset != 70000) {
					EX_IF_Latch.isBranchTaken = true;
					EX_IF_Latch.offset = offset - 1;
					IF_EnableLatch.setIF_enable(true);
					OF_EX_Latch.setEX_enable(false);
					IF_OF_Latch.setOF_enable(false);
					// IF_OF_Latch.instruction = 0;
					OF_EX_Latch.rs1 = 0;
					OF_EX_Latch.rs2 = 0;
					OF_EX_Latch.imm = 0;
					OF_EX_Latch.rd = 0;
				}
				EX_MA_Latch.aluResult = aluResult;
				EX_MA_Latch.rs1 = rs1;
				EX_MA_Latch.rs2 = rs2;
				EX_MA_Latch.rd = rd;
				EX_MA_Latch.imm = imm;
				EX_MA_Latch.opcode = OF_EX_Latch.opcode;
				//System.out.println("EX\t" + OF_EX_Latch.inst_PC + "\t" + Integer.parseInt(OF_EX_Latch.opcode,2) + "\trs1:" + rs1 + "\trs2:" + rs2 + "\trd:" + rd + "\timm:" + imm + "\talu:" + aluResult) ;//+ " " + rs1 + "\t" + rs2 + "\t" + rd + "\t" + imm);
				EX_MA_Latch.inst_PC = OF_EX_Latch.inst_PC;

				if(OF_EX_Latch.opcode.equals("11101") == true ) {
					OF_EX_Latch.setEX_enable(false);
				}

				// OF_EX_Latch.setEX_enable(false);
			}
			OF_EX_Latch.setEX_enable(false);
			EX_MA_Latch.setMA_enable(true);
			
		}
		//TODO
	}

}
