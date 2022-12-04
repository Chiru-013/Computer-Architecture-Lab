package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int alu_result;
	Instruction instruction;
    public boolean isBusy;
    public boolean nop;
    public int rd;
    public boolean is_busy;
    public boolean is_nop;
    public int aluResult;
    public int rs1;
    public int rs2;
    public int imm;
    public String opcode;
    public int inst_PC;
	
	//constructor
	public EX_MA_LatchType()
	{
		MA_enable = false;
	}

	//contructor to set memory acces enable
	public EX_MA_LatchType(boolean mA_enable)
	{
		MA_enable = mA_enable;
	}

	//constructor to set memory access enable and to save alu result in the latch for load operations
	public EX_MA_LatchType(boolean mA_enable, int alu_Result) 
	{
		MA_enable = mA_enable;
		this.alu_result = alu_Result;
	}

	//contructor to set meory access enable and to save result in alu_result  
	public EX_MA_LatchType(boolean mA_enable, int alu_Result, Instruction instruction) 
	{
		MA_enable = mA_enable;
		this.alu_result = alu_Result;
		this.instruction = instruction;
	}


	//function to set memory access enable signal
	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	//function to get memory access enable signal
	public boolean isMA_enable() {
		return MA_enable;
	}
	
	//function to set instruction
	public void setInstruction(Instruction inst) {
		instruction = inst;
	}

	//function to get the instruction
	public Instruction getInstruction() {
		return instruction;
	}

	//functiom to set the alu result
	public void setALU_result(int result) {
		alu_result = result;
	}

	//funtion to get the alu result
	public int getALU_result() {
		return alu_result;
	}

	
}
