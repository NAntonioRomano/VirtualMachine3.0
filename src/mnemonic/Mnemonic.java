package mnemonic;

import components.VirtualMachine;

abstract public class Mnemonic{

	protected VirtualMachine vm;
	protected String mnemonic;

	public Mnemonic(VirtualMachine vm, String mnemonic) {
		this.mnemonic = mnemonic;
		this.vm = vm;
	}



	public void ModifyCC(int result){
		int cc = vm.getRegisters().getRegister(8);

		cc = cc & 0x0FFFFFFF;

		if (result == 0) {
			cc = cc | 0x40000000; 
		} else if (result < 0) {
			cc = cc | 0x80000000; 
		} else {
			cc = 0x0; 
		}

		vm.getRegisters().setRegister(8, cc);

	}

	
	

}