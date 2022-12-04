package processor.pipeline;

public class EX_MA_LatchType {
	
	boolean MA_enable, is_nop, is_busy;
	int aluResult, rs1,rs2,rd,imm,rs1addr,rs2addr,inst_PC;
	String opcode;//
	 
	public EX_MA_LatchType()
	{
		MA_enable = false;
		opcode = "70000";//
		rs1 = 70000;//
		rs2 = 70000;//
		rd = 70000;//
		imm = 70000;//
		aluResult = 70000;//
		inst_PC = -1;//
		is_nop = false;
		rs1addr = 45;//
		rs2addr = 45;//
		is_busy = false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

}
