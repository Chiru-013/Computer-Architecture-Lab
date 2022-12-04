package processor.pipeline;

public class EX_IF_LatchType {

		//intialization of enable and PC
		boolean branch_enable;
		int PC;
        public boolean isBranchTaken;
        public int offset;
		
		//contructor to intialize value of enable signal
		public EX_IF_LatchType() {
			branch_enable = false;
		}
	
		public EX_IF_LatchType(boolean b_enable, int pc) {
			branch_enable = b_enable;
			PC = pc;
		} 

		public EX_IF_LatchType(boolean b_enable) {
			branch_enable = b_enable;
		}


		//returns branch_enable signal
		public boolean get_branch_enable() {
			return branch_enable;
		}
	
		//sets only branch enable signal
		public void set_branch_enable(boolean b_enable) {
			branch_enable = b_enable;
		}

		//setts branch enable and PC aftr a branching instruction
		public void set_branch_enable(boolean b_enable, int pc) {
			branch_enable = b_enable;
			PC = pc;
		}
	
		//returns PC
		public int getPC() {
			return PC;
		}

		//sets PC value
		public void setPC(int pc){
			PC = pc;
		}
	
	

}
