package processor.pipeline;

public class EX_IF_LatchType {

	boolean IS_enable;
	int PC;
	
	//constructor: Enables the EX_IF latch
	public EX_IF_LatchType() {
		IS_enable = false;
	}
	
	public EX_IF_LatchType(boolean iS_enable) {
		IS_enable = iS_enable;
	}

	//returns the branch enable value
	public boolean get_branch_enable() {
		return IS_enable;
	}

	//set branch_enable value
	public void set_branch_enable(boolean iS_enable) {
		IS_enable = iS_enable;
	}

	//sets branch_enable value and also sets the PC
	public void set_branch_enable(boolean iS_enable, int newPC) {
		IS_enable = iS_enable;
		PC = newPC;
	}

	//returns the current PC value
	public int getPC() {
		return PC;
	}


	
}
