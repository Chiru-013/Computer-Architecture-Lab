package processor.pipeline;

import generic.Instruction;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	Instruction instruction;
	int ld_result;
	int alu_result;
    public boolean is_nop;
    public int aluResult;
    public int rs1;
    public int rs2;
    public int rd;
    public int imm;
    public String opcode;
    public String inst_PC;
    public boolean isLoad;
	
	//constructor to initialize set read write enable signal
	public MA_RW_LatchType()
	{
		RW_enable = false;
	}

	//contructor to take read write enable, intruction, load result and alu result as input
	public MA_RW_LatchType(boolean rw_enable, Instruction instruction, int ld_result, int alu_result)
	{
		RW_enable = rw_enable;
		this.instruction = instruction;
		this.ld_result = ld_result;
		this.alu_result = alu_result;
	}	

	//returns the value of RW-enable for multiplexer
	public boolean isRW_enable() {
		return RW_enable;
	}

	//sets RW enable 
	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

	//access to instruction
	public Instruction getInstruction() {
		return instruction;
	}

	//sets instruction
	public void setInstruction(Instruction inst) {
		instruction = inst;
	}

	//sets load result
	public void setLoad_result(int result) {
		ld_result = result;
	}

	//returns load result
	public int getLoad_result() {
		return ld_result;
	}

	//returns aluresult which is offset calculation
	public int getALU_result() {
		return alu_result;
	}

	//saves offset calculation from alu as alu result
	public void setALU_result(int result) {
		alu_result = result;
	}
}
