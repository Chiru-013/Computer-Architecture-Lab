package processor.pipeline;

public class MA_RW_LatchType {
	
	boolean RW_enable,nop,isLoad,is_nop;
	int aluResult, alu_result, rs1,rs2,rd,imm,rs1addr,rs2addr,inst_PC,load_result;
	String opcode;
	
	public MA_RW_LatchType() {
		RW_enable = false;
		rs1addr = 45;
		rs2addr = 45;
		opcode = "70000";
		rs1 = 70000;
		rs2 = 70000;
		aluResult = 70000;
		inst_PC = -1;
		isLoad =  false;
		rd = 70000;
		imm = 70000;
		is_nop = false;
		
	}
 
	public String toString() {
		return "MA_RW_LatchType";
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

	public void setLoad_result(int result) {
		load_result = result;
	}

	public int getLoad_result() {
		return load_result;
	}

	public int getALU_result() {
		return alu_result;
	}

	public void setALU_result(int result) {
		alu_result = result;
	}
	
	public boolean getIsNOP() {
		return nop;
	}
	
	public void setIsNOP(boolean is_NOP) {
		nop = is_NOP;
	}
}
