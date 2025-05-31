package mnemonic;

import components.*;
import operands.Operand;

public class JP extends Mnemonic1 {

    public JP(VirtualMachine vm) {
        super(vm, "JP");
    }

    @Override
    public void operate(Operand A) throws Exception {

        int cc = this.vm.getRegisters().getRegister(8);
        if ((cc & 0xC0000000) == 0) { 
            //int address = A.getValue(); 
            //this.vm.getRegisters().setRegister(5, address); 

            int address = this.vm.getRegisters().getRegister(0);
            address += A.getValue();
            this.vm.getRegisters().setRegister(5, address);

        } 
    }

}
