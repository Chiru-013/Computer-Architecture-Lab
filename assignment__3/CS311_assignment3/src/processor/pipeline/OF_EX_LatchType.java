package processor.pipeline;

import generic.Instruction;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	Instruction instruction;
    public boolean isBusy;
    public boolean nop;
    public boolean is_busy;
    public int rd;
    public boolean is_nop;
    public String opcode;
    public int rs1;
    public int rs2;
    public int imm;
    public int inst_PC;
	
	//contructor to intialize the value of EX_enable
	public OF_EX_LatchType()
	{
		EX_enable = false;
	}

	//returns the execute stage enable signal
	public boolean isEX_enable() {
		return EX_enable;
	}

	//saves the execute stage enable signal 
	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}

	//saves the instruction for the next stages
	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}

	//returns the instruction 
	public Instruction getInstruction() {
		return this.instruction;
	}

}
