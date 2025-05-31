package mnemonic;

import components.VirtualMachine;
import operands.Operand;

public class MOV extends Mnemonic2 {

	public MOV(VirtualMachine vm) {
		super(vm, "MOV");
	}

	@Override
	public void operate(Operand A, Operand B) throws Exception{

		A.setValue(B.getValue());
		
	}

	
}


