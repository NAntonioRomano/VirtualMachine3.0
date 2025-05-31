package mnemonic;

import components.VirtualMachine;
import operands.Operand;
import exceptions.StackOverflowException;

public class PUSH extends Mnemonic1{

    public PUSH (VirtualMachine vm) {
        super (vm,"PUSH");
    }

	@Override
	public void operate(Operand A) throws Exception {
        int SP = this.vm.getRegisters().getRegister(6);

        if (((SP & 0xFFFF) < 3) && (SP & 0xFFFF) >= 0) {
            throw new StackOverflowException("Stack Overflow Exception: Stack is full"); 
        }

        if(SP == -1) {
            SP = this.vm.getRegisters().getRegister(3);
            SP += (this.vm.getSegTable().getSize(SP >> 16));
        }
        
         SP -= 4;
        this.vm.getVirtualMemory().write4bytes(this.vm.getSegTable().LogicToPhysic(SP),A.getValue());


        this.vm.getRegisters().setRegister(6, SP);

        

	}

    

}



