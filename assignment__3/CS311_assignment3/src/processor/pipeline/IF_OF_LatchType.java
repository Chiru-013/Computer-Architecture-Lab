package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	int instruction;
    public boolean is_busy;
    public int inst_PC;
	
	//constructor to intialize enable signal
	public IF_OF_LatchType()
	{
		OF_enable = false;
	} 

	//constructor to take enable signal and instruction as input 
	public IF_OF_LatchType(boolean oF_enable, int instruction) {

		OF_enable = oF_enable;
		this.instruction = instruction;
	}

	//returns the current state of OF_enable signal
	public boolean isOF_enable() {
		return OF_enable;
	}

	//sets enable signal
	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	//returns the instruction
	public int getInstruction() {
		return instruction;
	}

	//sets instruction from the previous stage
	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

}
