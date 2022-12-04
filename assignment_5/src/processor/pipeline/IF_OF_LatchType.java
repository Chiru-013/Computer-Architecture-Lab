package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable,is_busy;
	int instruction, inst_PC;
	
	public IF_OF_LatchType() {
		OF_enable = false;
		inst_PC = -1;
		instruction = -1999;
		is_busy = false;
	}
	 
	public IF_OF_LatchType(boolean oF_enable, boolean is_busy) {
		OF_enable = oF_enable;
		inst_PC = -1;
		instruction = -1999;
		this.is_busy = is_busy;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public boolean checkInstruction(int instruction) {
		return this.instruction == instruction;
	}

	public boolean checkPC(int pc) {
		return inst_PC == pc;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

}
