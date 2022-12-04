package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable,nop,is_busy,is_nop;
	String opcode;
	int rs1,rs2,rd,imm,inst_PC,rs1addr,rs2addr;
	
	public OF_EX_LatchType() {
		EX_enable = false;
		opcode = "70000";
		rs1 = 70000;
		rs2 = 70000;
		rd = 70000;
		imm = 70000;
		inst_PC = -1;
		is_nop = false;
		rs1addr = 45;
		rs2addr = 45;
		is_busy = false;
	}
 
	public String toString() {
		return "OF_EX_LatchType";
	}

	public boolean comparePC (int pc) {
		return inst_PC == pc;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}
	
	public boolean getIsNOP() {
		return nop;
	}
	
	public void setIsNOP(boolean is_NOP) {
		nop = is_NOP;
	}
}
